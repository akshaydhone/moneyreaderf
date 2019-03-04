package ir.mahdi.mzip.rar.unpack.ppm;

import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.io.Raw;

public class PPMContext extends Pointer {
    public static final int[] ExpEscape = new int[]{25, 14, 9, 7, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2};
    public static final int size = ((unionSize + 2) + 4);
    private static final int unionSize = Math.max(6, 6);
    private final FreqData freqData;
    private int numStats;
    private final State oneState;
    private final int[] ps = new int[256];
    private int suffix;
    private PPMContext tempPPMContext = null;
    private final State tempState1 = new State(null);
    private final State tempState2 = new State(null);
    private final State tempState3 = new State(null);
    private final State tempState4 = new State(null);
    private final State tempState5 = new State(null);

    public PPMContext(byte[] mem) {
        super(mem);
        this.oneState = new State(mem);
        this.freqData = new FreqData(mem);
    }

    public PPMContext init(byte[] mem) {
        this.mem = mem;
        this.pos = 0;
        this.oneState.init(mem);
        this.freqData.init(mem);
        return this;
    }

    public FreqData getFreqData() {
        return this.freqData;
    }

    public void setFreqData(FreqData freqData) {
        this.freqData.setSummFreq(freqData.getSummFreq());
        this.freqData.setStats(freqData.getStats());
    }

    public final int getNumStats() {
        if (this.mem != null) {
            this.numStats = Raw.readShortLittleEndian(this.mem, this.pos) & 65535;
        }
        return this.numStats;
    }

    public final void setNumStats(int numStats) {
        this.numStats = 65535 & numStats;
        if (this.mem != null) {
            Raw.writeShortLittleEndian(this.mem, this.pos, (short) numStats);
        }
    }

    public State getOneState() {
        return this.oneState;
    }

    public void setOneState(StateRef oneState) {
        this.oneState.setValues(oneState);
    }

    public int getSuffix() {
        if (this.mem != null) {
            this.suffix = Raw.readIntLittleEndian(this.mem, this.pos + 8);
        }
        return this.suffix;
    }

    public void setSuffix(int suffix) {
        this.suffix = suffix;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos + 8, suffix);
        }
    }

    public void setSuffix(PPMContext suffix) {
        setSuffix(suffix.getAddress());
    }

    public void setAddress(int pos) {
        super.setAddress(pos);
        this.oneState.setAddress(pos + 2);
        this.freqData.setAddress(pos + 2);
    }

    private PPMContext getTempPPMContext(byte[] mem) {
        if (this.tempPPMContext == null) {
            this.tempPPMContext = new PPMContext(null);
        }
        return this.tempPPMContext.init(mem);
    }

    public int createChild(ModelPPM model, State pStats, StateRef firstState) {
        PPMContext pc = getTempPPMContext(model.getSubAlloc().getHeap());
        pc.setAddress(model.getSubAlloc().allocContext());
        if (pc != null) {
            pc.setNumStats(1);
            pc.setOneState(firstState);
            pc.setSuffix(this);
            pStats.setSuccessor(pc);
        }
        return pc.getAddress();
    }

    public void rescale(ModelPPM model) {
        int OldNS = getNumStats();
        int i = getNumStats() - 1;
        State p1 = new State(model.getHeap());
        State p = new State(model.getHeap());
        State temp = new State(model.getHeap());
        p.setAddress(model.getFoundState().getAddress());
        while (p.getAddress() != this.freqData.getStats()) {
            temp.setAddress(p.getAddress() - 6);
            State.ppmdSwap(p, temp);
            p.decAddress();
        }
        temp.setAddress(this.freqData.getStats());
        temp.incFreq(4);
        this.freqData.incSummFreq(4);
        int EscFreq = this.freqData.getSummFreq() - p.getFreq();
        int Adder = model.getOrderFall() != 0 ? 1 : 0;
        p.setFreq((p.getFreq() + Adder) >>> 1);
        this.freqData.setSummFreq(p.getFreq());
        do {
            p.incAddress();
            EscFreq -= p.getFreq();
            p.setFreq((p.getFreq() + Adder) >>> 1);
            this.freqData.incSummFreq(p.getFreq());
            temp.setAddress(p.getAddress() - 6);
            if (p.getFreq() > temp.getFreq()) {
                StateRef tmp;
                p1.setAddress(p.getAddress());
                tmp = new StateRef();
                tmp.setValues(p1);
                State temp2 = new State(model.getHeap());
                State temp3 = new State(model.getHeap());
                do {
                    temp2.setAddress(p1.getAddress() - 6);
                    p1.setValues(temp2);
                    p1.decAddress();
                    temp3.setAddress(p1.getAddress() - 6);
                    if (p1.getAddress() == this.freqData.getStats()) {
                        break;
                    }
                } while (tmp.getFreq() > temp3.getFreq());
                p1.setValues(tmp);
            }
            i--;
        } while (i != 0);
        if (p.getFreq() == 0) {
            do {
                i++;
                p.decAddress();
            } while (p.getFreq() == 0);
            EscFreq += i;
            setNumStats(getNumStats() - i);
            if (getNumStats() == 1) {
                tmp = new StateRef();
                temp.setAddress(this.freqData.getStats());
                tmp.setValues(temp);
                do {
                    tmp.decFreq(tmp.getFreq() >>> 1);
                    EscFreq >>>= 1;
                } while (EscFreq > 1);
                model.getSubAlloc().freeUnits(this.freqData.getStats(), (OldNS + 1) >>> 1);
                this.oneState.setValues(tmp);
                model.getFoundState().setAddress(this.oneState.getAddress());
                return;
            }
        }
        this.freqData.incSummFreq(EscFreq - (EscFreq >>> 1));
        int n0 = (OldNS + 1) >>> 1;
        int n1 = (getNumStats() + 1) >>> 1;
        if (n0 != n1) {
            this.freqData.setStats(model.getSubAlloc().shrinkUnits(this.freqData.getStats(), n0, n1));
        }
        model.getFoundState().setAddress(this.freqData.getStats());
    }

    private int getArrayIndex(ModelPPM Model, State rs) {
        PPMContext tempSuffix = getTempPPMContext(Model.getSubAlloc().getHeap());
        tempSuffix.setAddress(getSuffix());
        return (((0 + Model.getPrevSuccess()) + Model.getNS2BSIndx()[tempSuffix.getNumStats() - 1]) + (Model.getHiBitsFlag() + (Model.getHB2Flag()[rs.getSymbol()] * 2))) + ((Model.getRunLength() >>> 26) & 32);
    }

    public int getMean(int summ, int shift, int round) {
        return ((1 << (shift - round)) + summ) >>> shift;
    }

    public void decodeBinSymbol(ModelPPM model) {
        int i = 0;
        State rs = this.tempState1.init(model.getHeap());
        rs.setAddress(this.oneState.getAddress());
        model.setHiBitsFlag(model.getHB2Flag()[model.getFoundState().getSymbol()]);
        int off1 = rs.getFreq() - 1;
        int off2 = getArrayIndex(model, rs);
        int bs = model.getBinSumm()[off1][off2];
        if (model.getCoder().getCurrentShiftCount(14) < ((long) bs)) {
            model.getFoundState().setAddress(rs.getAddress());
            if (rs.getFreq() < 128) {
                i = 1;
            }
            rs.incFreq(i);
            model.getCoder().getSubRange().setLowCount(0);
            model.getCoder().getSubRange().setHighCount((long) bs);
            model.getBinSumm()[off1][off2] = ((bs + 128) - getMean(bs, 7, 2)) & 65535;
            model.setPrevSuccess(1);
            model.incRunLength(1);
            return;
        }
        model.getCoder().getSubRange().setLowCount((long) bs);
        bs = (bs - getMean(bs, 7, 2)) & 65535;
        model.getBinSumm()[off1][off2] = bs;
        model.getCoder().getSubRange().setHighCount(PlaybackStateCompat.ACTION_PREPARE);
        model.setInitEsc(ExpEscape[bs >>> 10]);
        model.setNumMasked(1);
        model.getCharMask()[rs.getSymbol()] = model.getEscCount();
        model.setPrevSuccess(0);
        model.getFoundState().setAddress(0);
    }

    public void update1(ModelPPM model, int p) {
        model.getFoundState().setAddress(p);
        model.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        State p0 = this.tempState3.init(model.getHeap());
        State p1 = this.tempState4.init(model.getHeap());
        p0.setAddress(p);
        p1.setAddress(p - 6);
        if (p0.getFreq() > p1.getFreq()) {
            State.ppmdSwap(p0, p1);
            model.getFoundState().setAddress(p1.getAddress());
            if (p1.getFreq() > ModelPPM.MAX_FREQ) {
                rescale(model);
            }
        }
    }

    public boolean decodeSymbol2(ModelPPM model) {
        int i = getNumStats() - model.getNumMasked();
        SEE2Context psee2c = makeEscFreq2(model, i);
        RangeCoder coder = model.getCoder();
        State p = this.tempState1.init(model.getHeap());
        State temp = this.tempState2.init(model.getHeap());
        p.setAddress(this.freqData.getStats() - 6);
        int pps = 0;
        int hiCnt = 0;
        while (true) {
            int pps2;
            p.incAddress();
            if (model.getCharMask()[p.getSymbol()] != model.getEscCount()) {
                hiCnt += p.getFreq();
                pps2 = pps + 1;
                this.ps[pps] = p.getAddress();
                i--;
                if (i == 0) {
                    break;
                }
                pps = pps2;
            }
        }
        coder.getSubRange().incScale(hiCnt);
        long count = (long) coder.getCurrentCount();
        if (count >= coder.getSubRange().getScale()) {
            pps = pps2;
            return false;
        }
        pps = 0;
        p.setAddress(this.ps[0]);
        if (count < ((long) hiCnt)) {
            hiCnt = 0;
            while (true) {
                hiCnt += p.getFreq();
                if (((long) hiCnt) > count) {
                    break;
                }
                pps++;
                p.setAddress(this.ps[pps]);
            }
            coder.getSubRange().setHighCount((long) hiCnt);
            coder.getSubRange().setLowCount((long) (hiCnt - p.getFreq()));
            psee2c.update();
            update2(model, p.getAddress());
        } else {
            coder.getSubRange().setLowCount((long) hiCnt);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            i = getNumStats() - model.getNumMasked();
            pps = 0 - 1;
            do {
                pps++;
                temp.setAddress(this.ps[pps]);
                model.getCharMask()[temp.getSymbol()] = model.getEscCount();
                i--;
            } while (i != 0);
            psee2c.incSumm((int) coder.getSubRange().getScale());
            model.setNumMasked(getNumStats());
        }
        return true;
    }

    public void update2(ModelPPM model, int p) {
        State temp = this.tempState5.init(model.getHeap());
        temp.setAddress(p);
        model.getFoundState().setAddress(p);
        model.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        if (temp.getFreq() > ModelPPM.MAX_FREQ) {
            rescale(model);
        }
        model.incEscCount(1);
        model.setRunLength(model.getInitRL());
    }

    private SEE2Context makeEscFreq2(ModelPPM model, int Diff) {
        int i = 1;
        int numStats = getNumStats();
        if (numStats != 256) {
            int i2;
            PPMContext suff = getTempPPMContext(model.getHeap());
            suff.setAddress(getSuffix());
            int idx1 = model.getNS2Indx()[Diff - 1];
            if (Diff < suff.getNumStats() - numStats) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            int idx2 = 0 + i2;
            if (this.freqData.getSummFreq() < numStats * 11) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            idx2 += i2 * 2;
            if (model.getNumMasked() <= Diff) {
                i = 0;
            }
            SEE2Context psee2c = model.getSEE2Cont()[idx1][(idx2 + (i * 4)) + model.getHiBitsFlag()];
            model.getCoder().getSubRange().setScale((long) psee2c.getMean());
            return psee2c;
        }
        psee2c = model.getDummySEE2Cont();
        model.getCoder().getSubRange().setScale(1);
        return psee2c;
    }

    public boolean decodeSymbol1(ModelPPM model) {
        int i = 0;
        RangeCoder coder = model.getCoder();
        coder.getSubRange().setScale((long) this.freqData.getSummFreq());
        State p = new State(model.getHeap());
        p.setAddress(this.freqData.getStats());
        long count = (long) coder.getCurrentCount();
        if (count >= coder.getSubRange().getScale()) {
            return false;
        }
        int HiCnt = p.getFreq();
        if (count < ((long) HiCnt)) {
            coder.getSubRange().setHighCount((long) HiCnt);
            if (((long) (HiCnt * 2)) > coder.getSubRange().getScale()) {
                i = 1;
            }
            model.setPrevSuccess(i);
            model.incRunLength(model.getPrevSuccess());
            HiCnt += 4;
            model.getFoundState().setAddress(p.getAddress());
            model.getFoundState().setFreq(HiCnt);
            this.freqData.incSummFreq(4);
            if (HiCnt > ModelPPM.MAX_FREQ) {
                rescale(model);
            }
            coder.getSubRange().setLowCount(0);
            return true;
        } else if (model.getFoundState().getAddress() == 0) {
            return false;
        } else {
            model.setPrevSuccess(0);
            int numStats = getNumStats();
            int i2 = numStats - 1;
            do {
                HiCnt += p.incAddress().getFreq();
                if (((long) HiCnt) <= count) {
                    i2--;
                } else {
                    coder.getSubRange().setLowCount((long) (HiCnt - p.getFreq()));
                    coder.getSubRange().setHighCount((long) HiCnt);
                    update1(model, p.getAddress());
                    return true;
                }
            } while (i2 != 0);
            model.setHiBitsFlag(model.getHB2Flag()[model.getFoundState().getSymbol()]);
            coder.getSubRange().setLowCount((long) HiCnt);
            model.getCharMask()[p.getSymbol()] = model.getEscCount();
            model.setNumMasked(numStats);
            i2 = numStats - 1;
            model.getFoundState().setAddress(0);
            do {
                model.getCharMask()[p.decAddress().getSymbol()] = model.getEscCount();
                i2--;
            } while (i2 != 0);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            return true;
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("PPMContext[");
        buffer.append("\n  pos=");
        buffer.append(this.pos);
        buffer.append("\n  size=");
        buffer.append(size);
        buffer.append("\n  numStats=");
        buffer.append(getNumStats());
        buffer.append("\n  Suffix=");
        buffer.append(getSuffix());
        buffer.append("\n  freqData=");
        buffer.append(this.freqData);
        buffer.append("\n  oneState=");
        buffer.append(this.oneState);
        buffer.append("\n]");
        return buffer.toString();
    }
}
