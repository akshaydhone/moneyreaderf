package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ModelPPM {
    public static final int BIN_SCALE = 16384;
    public static final int INTERVAL = 128;
    public static final int INT_BITS = 7;
    private static int[] InitBinEsc = new int[]{15581, 7999, 22975, 18675, 25761, 23228, 26162, 24657};
    public static final int MAX_FREQ = 124;
    public static final int MAX_O = 64;
    public static final int PERIOD_BITS = 7;
    public static final int TOT_BITS = 14;
    private int[] HB2Flag = new int[256];
    private int[] NS2BSIndx = new int[256];
    private int[] NS2Indx = new int[256];
    private SEE2Context[][] SEE2Cont = ((SEE2Context[][]) Array.newInstance(SEE2Context.class, new int[]{25, 16}));
    private int[][] binSumm = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{128, 64}));
    private int[] charMask = new int[256];
    private RangeCoder coder = new RangeCoder();
    private SEE2Context dummySEE2Cont;
    private int escCount;
    private State foundState;
    private int hiBitsFlag;
    private int initEsc;
    private int initRL;
    private PPMContext maxContext = null;
    private int maxOrder;
    private PPMContext medContext = null;
    private PPMContext minContext = null;
    private int numMasked;
    private int orderFall;
    private int prevSuccess;
    private final int[] ps = new int[64];
    private int runLength;
    private SubAllocator subAlloc = new SubAllocator();
    private final PPMContext tempPPMContext1 = new PPMContext(null);
    private final PPMContext tempPPMContext2 = new PPMContext(null);
    private final PPMContext tempPPMContext3 = new PPMContext(null);
    private final PPMContext tempPPMContext4 = new PPMContext(null);
    private final State tempState1 = new State(null);
    private final State tempState2 = new State(null);
    private final State tempState3 = new State(null);
    private final State tempState4 = new State(null);
    private final StateRef tempStateRef1 = new StateRef();
    private final StateRef tempStateRef2 = new StateRef();

    public SubAllocator getSubAlloc() {
        return this.subAlloc;
    }

    private void restartModelRare() {
        int i;
        int k;
        int i2 = 12;
        Arrays.fill(this.charMask, 0);
        this.subAlloc.initSubAllocator();
        if (this.maxOrder < 12) {
            i2 = this.maxOrder;
        }
        this.initRL = (-i2) - 1;
        int addr = this.subAlloc.allocContext();
        this.minContext.setAddress(addr);
        this.maxContext.setAddress(addr);
        this.minContext.setSuffix(0);
        this.orderFall = this.maxOrder;
        this.minContext.setNumStats(256);
        this.minContext.getFreqData().setSummFreq(this.minContext.getNumStats() + 1);
        addr = this.subAlloc.allocUnits(128);
        this.foundState.setAddress(addr);
        this.minContext.getFreqData().setStats(addr);
        State state = new State(this.subAlloc.getHeap());
        addr = this.minContext.getFreqData().getStats();
        this.runLength = this.initRL;
        this.prevSuccess = 0;
        for (i = 0; i < 256; i++) {
            state.setAddress((i * 6) + addr);
            state.setSymbol(i);
            state.setFreq(1);
            state.setSuccessor(0);
        }
        for (i = 0; i < 128; i++) {
            for (k = 0; k < 8; k++) {
                for (int m = 0; m < 64; m += 8) {
                    this.binSumm[i][k + m] = 16384 - (InitBinEsc[k] / (i + 2));
                }
            }
        }
        for (i = 0; i < 25; i++) {
            for (k = 0; k < 16; k++) {
                this.SEE2Cont[i][k].init((i * 5) + 10);
            }
        }
    }

    private void startModelRare(int MaxOrder) {
        int j;
        this.escCount = 1;
        this.maxOrder = MaxOrder;
        restartModelRare();
        this.NS2BSIndx[0] = 0;
        this.NS2BSIndx[1] = 2;
        for (j = 0; j < 9; j++) {
            this.NS2BSIndx[j + 2] = 4;
        }
        for (j = 0; j < 245; j++) {
            this.NS2BSIndx[j + 11] = 6;
        }
        int i = 0;
        while (i < 3) {
            this.NS2Indx[i] = i;
            i++;
        }
        int m = i;
        int k = 1;
        int Step = 1;
        while (i < 256) {
            this.NS2Indx[i] = m;
            k--;
            if (k == 0) {
                Step++;
                k = Step;
                m++;
            }
            i++;
        }
        for (j = 0; j < 64; j++) {
            this.HB2Flag[j] = 0;
        }
        for (j = 0; j < 192; j++) {
            this.HB2Flag[j + 64] = 8;
        }
        this.dummySEE2Cont.setShift(7);
    }

    private void clearMask() {
        this.escCount = 1;
        Arrays.fill(this.charMask, 0);
    }

    public boolean decodeInit(Unpack unpackRead, int escChar) throws IOException, RarException {
        boolean reset;
        boolean z = true;
        int MaxOrder = unpackRead.getChar() & 255;
        if ((MaxOrder & 32) != 0) {
            reset = true;
        } else {
            reset = false;
        }
        int MaxMB = 0;
        if (reset) {
            MaxMB = unpackRead.getChar();
        } else if (this.subAlloc.GetAllocatedMemory() == 0) {
            return false;
        }
        if ((MaxOrder & 64) != 0) {
            unpackRead.setPpmEscChar(unpackRead.getChar());
        }
        this.coder.initDecoder(unpackRead);
        if (reset) {
            MaxOrder = (MaxOrder & 31) + 1;
            if (MaxOrder > 16) {
                MaxOrder = ((MaxOrder - 16) * 3) + 16;
            }
            if (MaxOrder == 1) {
                this.subAlloc.stopSubAllocator();
                return false;
            }
            this.subAlloc.startSubAllocator(MaxMB + 1);
            this.minContext = new PPMContext(getHeap());
            this.medContext = new PPMContext(getHeap());
            this.maxContext = new PPMContext(getHeap());
            this.foundState = new State(getHeap());
            this.dummySEE2Cont = new SEE2Context();
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 16; j++) {
                    this.SEE2Cont[i][j] = new SEE2Context();
                }
            }
            startModelRare(MaxOrder);
        }
        if (this.minContext.getAddress() == 0) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int decodeChar() throws java.io.IOException, ir.mahdi.mzip.rar.exception.RarException {
        /*
        r4 = this;
        r0 = -1;
        r2 = r4.minContext;
        r2 = r2.getAddress();
        r3 = r4.subAlloc;
        r3 = r3.getPText();
        if (r2 <= r3) goto L_0x001d;
    L_0x000f:
        r2 = r4.minContext;
        r2 = r2.getAddress();
        r3 = r4.subAlloc;
        r3 = r3.getHeapEnd();
        if (r2 <= r3) goto L_0x001e;
    L_0x001d:
        return r0;
    L_0x001e:
        r2 = r4.minContext;
        r2 = r2.getNumStats();
        r3 = 1;
        if (r2 == r3) goto L_0x00aa;
    L_0x0027:
        r2 = r4.minContext;
        r2 = r2.getFreqData();
        r2 = r2.getStats();
        r3 = r4.subAlloc;
        r3 = r3.getPText();
        if (r2 <= r3) goto L_0x001d;
    L_0x0039:
        r2 = r4.minContext;
        r2 = r2.getFreqData();
        r2 = r2.getStats();
        r3 = r4.subAlloc;
        r3 = r3.getHeapEnd();
        if (r2 > r3) goto L_0x001d;
    L_0x004b:
        r2 = r4.minContext;
        r2 = r2.decodeSymbol1(r4);
        if (r2 == 0) goto L_0x001d;
    L_0x0053:
        r2 = r4.coder;
        r2.decode();
    L_0x0058:
        r2 = r4.foundState;
        r2 = r2.getAddress();
        if (r2 != 0) goto L_0x00b0;
    L_0x0060:
        r2 = r4.coder;
        r2.ariDecNormalize();
    L_0x0065:
        r2 = r4.orderFall;
        r2 = r2 + 1;
        r4.orderFall = r2;
        r2 = r4.minContext;
        r3 = r4.minContext;
        r3 = r3.getSuffix();
        r2.setAddress(r3);
        r2 = r4.minContext;
        r2 = r2.getAddress();
        r3 = r4.subAlloc;
        r3 = r3.getPText();
        if (r2 <= r3) goto L_0x001d;
    L_0x0084:
        r2 = r4.minContext;
        r2 = r2.getAddress();
        r3 = r4.subAlloc;
        r3 = r3.getHeapEnd();
        if (r2 > r3) goto L_0x001d;
    L_0x0092:
        r2 = r4.minContext;
        r2 = r2.getNumStats();
        r3 = r4.numMasked;
        if (r2 == r3) goto L_0x0065;
    L_0x009c:
        r2 = r4.minContext;
        r2 = r2.decodeSymbol2(r4);
        if (r2 == 0) goto L_0x001d;
    L_0x00a4:
        r2 = r4.coder;
        r2.decode();
        goto L_0x0058;
    L_0x00aa:
        r2 = r4.minContext;
        r2.decodeBinSymbol(r4);
        goto L_0x0053;
    L_0x00b0:
        r2 = r4.foundState;
        r0 = r2.getSymbol();
        r2 = r4.orderFall;
        if (r2 != 0) goto L_0x00df;
    L_0x00ba:
        r2 = r4.foundState;
        r2 = r2.getSuccessor();
        r3 = r4.subAlloc;
        r3 = r3.getPText();
        if (r2 <= r3) goto L_0x00df;
    L_0x00c8:
        r2 = r4.foundState;
        r1 = r2.getSuccessor();
        r2 = r4.minContext;
        r2.setAddress(r1);
        r2 = r4.maxContext;
        r2.setAddress(r1);
    L_0x00d8:
        r2 = r4.coder;
        r2.ariDecNormalize();
        goto L_0x001d;
    L_0x00df:
        r4.updateModel();
        r2 = r4.escCount;
        if (r2 != 0) goto L_0x00d8;
    L_0x00e6:
        r4.clearMask();
        goto L_0x00d8;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.ppm.ModelPPM.decodeChar():int");
    }

    public SEE2Context[][] getSEE2Cont() {
        return this.SEE2Cont;
    }

    public SEE2Context getDummySEE2Cont() {
        return this.dummySEE2Cont;
    }

    public int getInitRL() {
        return this.initRL;
    }

    public int getEscCount() {
        return this.escCount;
    }

    public void setEscCount(int escCount) {
        this.escCount = escCount & 255;
    }

    public void incEscCount(int dEscCount) {
        setEscCount(getEscCount() + dEscCount);
    }

    public int[] getCharMask() {
        return this.charMask;
    }

    public int getNumMasked() {
        return this.numMasked;
    }

    public void setNumMasked(int numMasked) {
        this.numMasked = numMasked;
    }

    public int getInitEsc() {
        return this.initEsc;
    }

    public void setInitEsc(int initEsc) {
        this.initEsc = initEsc;
    }

    public int getRunLength() {
        return this.runLength;
    }

    public void setRunLength(int runLength) {
        this.runLength = runLength;
    }

    public void incRunLength(int dRunLength) {
        setRunLength(getRunLength() + dRunLength);
    }

    public int getPrevSuccess() {
        return this.prevSuccess;
    }

    public void setPrevSuccess(int prevSuccess) {
        this.prevSuccess = prevSuccess & 255;
    }

    public int getHiBitsFlag() {
        return this.hiBitsFlag;
    }

    public void setHiBitsFlag(int hiBitsFlag) {
        this.hiBitsFlag = hiBitsFlag & 255;
    }

    public int[][] getBinSumm() {
        return this.binSumm;
    }

    public RangeCoder getCoder() {
        return this.coder;
    }

    public int[] getHB2Flag() {
        return this.HB2Flag;
    }

    public int[] getNS2BSIndx() {
        return this.NS2BSIndx;
    }

    public int[] getNS2Indx() {
        return this.NS2Indx;
    }

    public State getFoundState() {
        return this.foundState;
    }

    public byte[] getHeap() {
        return this.subAlloc.getHeap();
    }

    public int getOrderFall() {
        return this.orderFall;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int createSuccessors(boolean r14, ir.mahdi.mzip.rar.unpack.ppm.State r15) {
        /*
        r13 = this;
        r10 = r13.tempStateRef2;
        r11 = r13.tempState1;
        r12 = r13.getHeap();
        r8 = r11.init(r12);
        r11 = r13.tempPPMContext1;
        r12 = r13.getHeap();
        r4 = r11.init(r12);
        r11 = r13.minContext;
        r11 = r11.getAddress();
        r4.setAddress(r11);
        r11 = r13.tempPPMContext2;
        r12 = r13.getHeap();
        r9 = r11.init(r12);
        r11 = r13.foundState;
        r11 = r11.getSuccessor();
        r9.setAddress(r11);
        r11 = r13.tempState2;
        r12 = r13.getHeap();
        r3 = r11.init(r12);
        r5 = 0;
        r2 = 0;
        if (r14 != 0) goto L_0x0054;
    L_0x0040:
        r11 = r13.ps;
        r6 = r5 + 1;
        r12 = r13.foundState;
        r12 = r12.getAddress();
        r11[r5] = r12;
        r11 = r4.getSuffix();
        if (r11 != 0) goto L_0x0185;
    L_0x0052:
        r2 = 1;
        r5 = r6;
    L_0x0054:
        if (r2 != 0) goto L_0x00b6;
    L_0x0056:
        r1 = 0;
        r11 = r15.getAddress();
        if (r11 == 0) goto L_0x00d9;
    L_0x005d:
        r11 = r15.getAddress();
        r3.setAddress(r11);
        r11 = r4.getSuffix();
        r4.setAddress(r11);
        r1 = 1;
        r6 = r5;
    L_0x006d:
        if (r1 != 0) goto L_0x00a3;
    L_0x006f:
        r11 = r4.getSuffix();
        r4.setAddress(r11);
        r11 = r4.getNumStats();
        r12 = 1;
        if (r11 == r12) goto L_0x00bd;
    L_0x007d:
        r11 = r4.getFreqData();
        r11 = r11.getStats();
        r3.setAddress(r11);
        r11 = r3.getSymbol();
        r12 = r13.foundState;
        r12 = r12.getSymbol();
        if (r11 == r12) goto L_0x00a3;
    L_0x0094:
        r3.incAddress();
        r11 = r3.getSymbol();
        r12 = r13.foundState;
        r12 = r12.getSymbol();
        if (r11 != r12) goto L_0x0094;
    L_0x00a3:
        r1 = 0;
        r11 = r3.getSuccessor();
        r12 = r9.getAddress();
        if (r11 == r12) goto L_0x00c9;
    L_0x00ae:
        r11 = r3.getSuccessor();
        r4.setAddress(r11);
        r5 = r6;
    L_0x00b6:
        if (r5 != 0) goto L_0x00db;
    L_0x00b8:
        r11 = r4.getAddress();
    L_0x00bc:
        return r11;
    L_0x00bd:
        r11 = r4.getOneState();
        r11 = r11.getAddress();
        r3.setAddress(r11);
        goto L_0x00a3;
    L_0x00c9:
        r11 = r13.ps;
        r5 = r6 + 1;
        r12 = r3.getAddress();
        r11[r6] = r12;
        r11 = r4.getSuffix();
        if (r11 == 0) goto L_0x00b6;
    L_0x00d9:
        r6 = r5;
        goto L_0x006d;
    L_0x00db:
        r11 = r13.getHeap();
        r12 = r9.getAddress();
        r11 = r11[r12];
        r10.setSymbol(r11);
        r11 = r9.getAddress();
        r11 = r11 + 1;
        r10.setSuccessor(r11);
        r11 = r4.getNumStats();
        r12 = 1;
        if (r11 == r12) goto L_0x0171;
    L_0x00f8:
        r11 = r4.getAddress();
        r12 = r13.subAlloc;
        r12 = r12.getPText();
        if (r11 > r12) goto L_0x0106;
    L_0x0104:
        r11 = 0;
        goto L_0x00bc;
    L_0x0106:
        r11 = r4.getFreqData();
        r11 = r11.getStats();
        r3.setAddress(r11);
        r11 = r3.getSymbol();
        r12 = r10.getSymbol();
        if (r11 == r12) goto L_0x0128;
    L_0x011b:
        r3.incAddress();
        r11 = r3.getSymbol();
        r12 = r10.getSymbol();
        if (r11 != r12) goto L_0x011b;
    L_0x0128:
        r11 = r3.getFreq();
        r0 = r11 + -1;
        r11 = r4.getFreqData();
        r11 = r11.getSummFreq();
        r12 = r4.getNumStats();
        r11 = r11 - r12;
        r7 = r11 - r0;
        r11 = r0 * 2;
        if (r11 > r7) goto L_0x0166;
    L_0x0141:
        r11 = r0 * 5;
        if (r11 <= r7) goto L_0x0164;
    L_0x0145:
        r11 = 1;
    L_0x0146:
        r11 = r11 + 1;
        r10.setFreq(r11);
    L_0x014b:
        r11 = r13.ps;
        r5 = r5 + -1;
        r11 = r11[r5];
        r8.setAddress(r11);
        r11 = r4.createChild(r13, r8, r10);
        r4.setAddress(r11);
        r11 = r4.getAddress();
        if (r11 != 0) goto L_0x017d;
    L_0x0161:
        r11 = 0;
        goto L_0x00bc;
    L_0x0164:
        r11 = 0;
        goto L_0x0146;
    L_0x0166:
        r11 = r0 * 2;
        r12 = r7 * 3;
        r11 = r11 + r12;
        r11 = r11 + -1;
        r12 = r7 * 2;
        r11 = r11 / r12;
        goto L_0x0146;
    L_0x0171:
        r11 = r4.getOneState();
        r11 = r11.getFreq();
        r10.setFreq(r11);
        goto L_0x014b;
    L_0x017d:
        if (r5 != 0) goto L_0x014b;
    L_0x017f:
        r11 = r4.getAddress();
        goto L_0x00bc;
    L_0x0185:
        r5 = r6;
        goto L_0x0054;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.ppm.ModelPPM.createSuccessors(boolean, ir.mahdi.mzip.rar.unpack.ppm.State):int");
    }

    private void updateModelRestart() {
        restartModelRare();
        this.escCount = 0;
    }

    private void updateModel() {
        StateRef fs = this.tempStateRef1;
        fs.setValues(this.foundState);
        State p = this.tempState3.init(getHeap());
        State tempState = this.tempState4.init(getHeap());
        PPMContext pc = this.tempPPMContext3.init(getHeap());
        PPMContext successor = this.tempPPMContext4.init(getHeap());
        pc.setAddress(this.minContext.getSuffix());
        if (fs.getFreq() < 31 && pc.getAddress() != 0) {
            if (pc.getNumStats() != 1) {
                p.setAddress(pc.getFreqData().getStats());
                if (p.getSymbol() != fs.getSymbol()) {
                    do {
                        p.incAddress();
                    } while (p.getSymbol() != fs.getSymbol());
                    tempState.setAddress(p.getAddress() - 6);
                    if (p.getFreq() >= tempState.getFreq()) {
                        State.ppmdSwap(p, tempState);
                        p.decAddress();
                    }
                }
                if (p.getFreq() < 115) {
                    p.incFreq(2);
                    pc.getFreqData().incSummFreq(2);
                }
            } else {
                p.setAddress(pc.getOneState().getAddress());
                if (p.getFreq() < 32) {
                    p.incFreq(1);
                }
            }
        }
        if (this.orderFall == 0) {
            this.foundState.setSuccessor(createSuccessors(true, p));
            this.minContext.setAddress(this.foundState.getSuccessor());
            this.maxContext.setAddress(this.foundState.getSuccessor());
            if (this.minContext.getAddress() == 0) {
                updateModelRestart();
                return;
            }
            return;
        }
        this.subAlloc.getHeap()[this.subAlloc.getPText()] = (byte) fs.getSymbol();
        this.subAlloc.incPText();
        successor.setAddress(this.subAlloc.getPText());
        if (this.subAlloc.getPText() >= this.subAlloc.getFakeUnitsStart()) {
            updateModelRestart();
            return;
        }
        if (fs.getSuccessor() != 0) {
            int i;
            if (fs.getSuccessor() <= this.subAlloc.getPText()) {
                fs.setSuccessor(createSuccessors(false, p));
                if (fs.getSuccessor() == 0) {
                    updateModelRestart();
                    return;
                }
            }
            i = this.orderFall - 1;
            this.orderFall = i;
            if (i == 0) {
                successor.setAddress(fs.getSuccessor());
                if (this.maxContext.getAddress() != this.minContext.getAddress()) {
                    this.subAlloc.decPText(1);
                }
            }
        } else {
            this.foundState.setSuccessor(successor.getAddress());
            fs.setSuccessor(this.minContext);
        }
        int ns = this.minContext.getNumStats();
        int s0 = (this.minContext.getFreqData().getSummFreq() - ns) - (fs.getFreq() - 1);
        pc.setAddress(this.maxContext.getAddress());
        while (pc.getAddress() != this.minContext.getAddress()) {
            int ns1 = pc.getNumStats();
            if (ns1 != 1) {
                if ((ns1 & 1) == 0) {
                    pc.getFreqData().setStats(this.subAlloc.expandUnits(pc.getFreqData().getStats(), ns1 >>> 1));
                    if (pc.getFreqData().getStats() == 0) {
                        updateModelRestart();
                        return;
                    }
                }
                pc.getFreqData().incSummFreq((ns1 * 2 < ns ? 1 : 0) + (((ns1 * 4 <= ns ? 1 : 0) & (pc.getFreqData().getSummFreq() <= ns1 * 8 ? 1 : 0)) * 2));
            } else {
                p.setAddress(this.subAlloc.allocUnits(1));
                if (p.getAddress() == 0) {
                    updateModelRestart();
                    return;
                }
                p.setValues(pc.getOneState());
                pc.getFreqData().setStats(p);
                if (p.getFreq() < 30) {
                    p.incFreq(p.getFreq());
                } else {
                    p.setFreq(120);
                }
                pc.getFreqData().setSummFreq((ns > 3 ? 1 : 0) + (this.initEsc + p.getFreq()));
            }
            int cf = (fs.getFreq() * 2) * (pc.getFreqData().getSummFreq() + 6);
            int sf = s0 + pc.getFreqData().getSummFreq();
            if (cf < sf * 6) {
                cf = ((cf > sf ? 1 : 0) + 1) + (cf >= sf * 4 ? 1 : 0);
                pc.getFreqData().incSummFreq(3);
            } else {
                int i2 = (cf >= sf * 9 ? 1 : 0) + 4;
                if (cf >= sf * 12) {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 += i;
                if (cf >= sf * 15) {
                    i = 1;
                } else {
                    i = 0;
                }
                cf = i2 + i;
                pc.getFreqData().incSummFreq(cf);
            }
            p.setAddress(pc.getFreqData().getStats() + (ns1 * 6));
            p.setSuccessor(successor);
            p.setSymbol(fs.getSymbol());
            p.setFreq(cf);
            pc.setNumStats(ns1 + 1);
            pc.setAddress(pc.getSuffix());
        }
        int address = fs.getSuccessor();
        this.maxContext.setAddress(address);
        this.minContext.setAddress(address);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("ModelPPM[");
        buffer.append("\n  numMasked=");
        buffer.append(this.numMasked);
        buffer.append("\n  initEsc=");
        buffer.append(this.initEsc);
        buffer.append("\n  orderFall=");
        buffer.append(this.orderFall);
        buffer.append("\n  maxOrder=");
        buffer.append(this.maxOrder);
        buffer.append("\n  runLength=");
        buffer.append(this.runLength);
        buffer.append("\n  initRL=");
        buffer.append(this.initRL);
        buffer.append("\n  escCount=");
        buffer.append(this.escCount);
        buffer.append("\n  prevSuccess=");
        buffer.append(this.prevSuccess);
        buffer.append("\n  foundState=");
        buffer.append(this.foundState);
        buffer.append("\n  coder=");
        buffer.append(this.coder);
        buffer.append("\n  subAlloc=");
        buffer.append(this.subAlloc);
        buffer.append("\n]");
        return buffer.toString();
    }
}
