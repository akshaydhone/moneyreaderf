package ir.mahdi.mzip.rar.unpack;

import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.ppm.BlockTypes;
import ir.mahdi.mzip.rar.unpack.ppm.ModelPPM;
import ir.mahdi.mzip.rar.unpack.ppm.SubAllocator;
import ir.mahdi.mzip.rar.unpack.vm.BitInput;
import ir.mahdi.mzip.rar.unpack.vm.RarVM;
import ir.mahdi.mzip.rar.unpack.vm.VMPreparedProgram;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class Unpack extends Unpack20 {
    public static int[] DBitLengthCounts = new int[]{4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 14, 0, 12};
    private boolean externalWindow;
    private boolean fileExtracted;
    private List<UnpackFilter> filters = new ArrayList();
    private int lastFilter;
    private int lowDistRepCount;
    private List<Integer> oldFilterLengths = new ArrayList();
    private final ModelPPM ppm = new ModelPPM();
    private boolean ppmError;
    private int ppmEscChar;
    private int prevLowDist;
    private List<UnpackFilter> prgStack = new ArrayList();
    private RarVM rarVM = new RarVM();
    private boolean tablesRead;
    private BlockTypes unpBlockType;
    private byte[] unpOldTable = new byte[Compress.HUFF_TABLE_SIZE];
    private long writtenFileSize;

    public Unpack(ComprDataIO DataIO) {
        this.unpIO = DataIO;
        this.window = null;
        this.externalWindow = false;
        this.suspended = false;
        this.unpAllBuf = false;
        this.unpSomeRead = false;
    }

    public void init(byte[] window) {
        if (window == null) {
            this.window = new byte[4194304];
        } else {
            this.window = window;
            this.externalWindow = true;
        }
        this.inAddr = 0;
        unpInitData(false);
    }

    public void doUnpack(int method, boolean solid) throws IOException, RarException {
        if (this.unpIO.getSubHeader().getUnpMethod() == (byte) 48) {
            unstoreFile();
        }
        switch (method) {
            case 15:
                unpack15(solid);
                return;
            case 20:
            case 26:
                unpack20(solid);
                return;
            case 29:
            case 36:
                unpack29(solid);
                return;
            default:
                return;
        }
    }

    private void unstoreFile() throws IOException, RarException {
        byte[] buffer = new byte[65536];
        while (true) {
            int code = this.unpIO.unpRead(buffer, 0, (int) Math.min((long) buffer.length, this.destUnpSize));
            if (code != 0 && code != -1) {
                if (((long) code) >= this.destUnpSize) {
                    code = (int) this.destUnpSize;
                }
                this.unpIO.unpWrite(buffer, 0, code);
                if (this.destUnpSize >= 0) {
                    this.destUnpSize -= (long) code;
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unpack29(boolean r29) throws java.io.IOException, ir.mahdi.mzip.rar.exception.RarException {
        /*
        r28 = this;
        r24 = 60;
        r0 = r24;
        r8 = new int[r0];
        r24 = 60;
        r0 = r24;
        r7 = new byte[r0];
        r24 = 1;
        r24 = r8[r24];
        if (r24 != 0) goto L_0x0042;
    L_0x0012:
        r9 = 0;
        r4 = 0;
        r20 = 0;
        r13 = 0;
    L_0x0017:
        r24 = DBitLengthCounts;
        r0 = r24;
        r0 = r0.length;
        r24 = r0;
        r0 = r24;
        if (r13 >= r0) goto L_0x0042;
    L_0x0022:
        r24 = DBitLengthCounts;
        r22 = r24[r13];
        r14 = 0;
    L_0x0027:
        r0 = r22;
        if (r14 >= r0) goto L_0x003d;
    L_0x002b:
        r8[r20] = r9;
        r0 = (byte) r4;
        r24 = r0;
        r7[r20] = r24;
        r14 = r14 + 1;
        r20 = r20 + 1;
        r24 = 1;
        r24 = r24 << r4;
        r9 = r9 + r24;
        goto L_0x0027;
    L_0x003d:
        r13 = r13 + 1;
        r4 = r4 + 1;
        goto L_0x0017;
    L_0x0042:
        r24 = 1;
        r0 = r24;
        r1 = r28;
        r1.fileExtracted = r0;
        r0 = r28;
        r0 = r0.suspended;
        r24 = r0;
        if (r24 != 0) goto L_0x006c;
    L_0x0052:
        r28.unpInitData(r29);
        r24 = r28.unpReadBuf();
        if (r24 != 0) goto L_0x005c;
    L_0x005b:
        return;
    L_0x005c:
        if (r29 == 0) goto L_0x0066;
    L_0x005e:
        r0 = r28;
        r0 = r0.tablesRead;
        r24 = r0;
        if (r24 != 0) goto L_0x006c;
    L_0x0066:
        r24 = r28.readTables();
        if (r24 == 0) goto L_0x005b;
    L_0x006c:
        r0 = r28;
        r0 = r0.ppmError;
        r24 = r0;
        if (r24 != 0) goto L_0x005b;
    L_0x0074:
        r0 = r28;
        r0 = r0.unpPtr;
        r24 = r0;
        r25 = 4194303; // 0x3fffff float:5.87747E-39 double:2.072261E-317;
        r24 = r24 & r25;
        r0 = r24;
        r1 = r28;
        r1.unpPtr = r0;
        r0 = r28;
        r0 = r0.inAddr;
        r24 = r0;
        r0 = r28;
        r0 = r0.readBorder;
        r25 = r0;
        r0 = r24;
        r1 = r25;
        if (r0 <= r1) goto L_0x00a1;
    L_0x0097:
        r24 = r28.unpReadBuf();
        if (r24 != 0) goto L_0x00a1;
    L_0x009d:
        r28.UnpWriteBuf();
        goto L_0x005b;
    L_0x00a1:
        r0 = r28;
        r0 = r0.wrPtr;
        r24 = r0;
        r0 = r28;
        r0 = r0.unpPtr;
        r25 = r0;
        r24 = r24 - r25;
        r25 = 4194303; // 0x3fffff float:5.87747E-39 double:2.072261E-317;
        r24 = r24 & r25;
        r25 = 260; // 0x104 float:3.64E-43 double:1.285E-321;
        r0 = r24;
        r1 = r25;
        if (r0 >= r1) goto L_0x00f3;
    L_0x00bc:
        r0 = r28;
        r0 = r0.wrPtr;
        r24 = r0;
        r0 = r28;
        r0 = r0.unpPtr;
        r25 = r0;
        r0 = r24;
        r1 = r25;
        if (r0 == r1) goto L_0x00f3;
    L_0x00ce:
        r28.UnpWriteBuf();
        r0 = r28;
        r0 = r0.writtenFileSize;
        r24 = r0;
        r0 = r28;
        r0 = r0.destUnpSize;
        r26 = r0;
        r24 = (r24 > r26 ? 1 : (r24 == r26 ? 0 : -1));
        if (r24 > 0) goto L_0x005b;
    L_0x00e1:
        r0 = r28;
        r0 = r0.suspended;
        r24 = r0;
        if (r24 == 0) goto L_0x00f3;
    L_0x00e9:
        r24 = 0;
        r0 = r24;
        r1 = r28;
        r1.fileExtracted = r0;
        goto L_0x005b;
    L_0x00f3:
        r0 = r28;
        r0 = r0.unpBlockType;
        r24 = r0;
        r25 = ir.mahdi.mzip.rar.unpack.ppm.BlockTypes.BLOCK_PPM;
        r0 = r24;
        r1 = r25;
        if (r0 != r1) goto L_0x01ed;
    L_0x0101:
        r0 = r28;
        r0 = r0.ppm;
        r24 = r0;
        r6 = r24.decodeChar();
        r24 = -1;
        r0 = r24;
        if (r6 != r0) goto L_0x011a;
    L_0x0111:
        r24 = 1;
        r0 = r24;
        r1 = r28;
        r1.ppmError = r0;
        goto L_0x009d;
    L_0x011a:
        r0 = r28;
        r0 = r0.ppmEscChar;
        r24 = r0;
        r0 = r24;
        if (r6 != r0) goto L_0x01d2;
    L_0x0124:
        r0 = r28;
        r0 = r0.ppm;
        r24 = r0;
        r18 = r24.decodeChar();
        if (r18 != 0) goto L_0x0138;
    L_0x0130:
        r24 = r28.readTables();
        if (r24 != 0) goto L_0x0074;
    L_0x0136:
        goto L_0x009d;
    L_0x0138:
        r24 = 2;
        r0 = r18;
        r1 = r24;
        if (r0 == r1) goto L_0x009d;
    L_0x0140:
        r24 = -1;
        r0 = r18;
        r1 = r24;
        if (r0 == r1) goto L_0x009d;
    L_0x0148:
        r24 = 3;
        r0 = r18;
        r1 = r24;
        if (r0 != r1) goto L_0x0158;
    L_0x0150:
        r24 = r28.readVMCodePPM();
        if (r24 != 0) goto L_0x0074;
    L_0x0156:
        goto L_0x009d;
    L_0x0158:
        r24 = 4;
        r0 = r18;
        r1 = r24;
        if (r0 != r1) goto L_0x01ab;
    L_0x0160:
        r12 = 0;
        r15 = 0;
        r23 = 0;
        r13 = 0;
    L_0x0165:
        r24 = 4;
        r0 = r24;
        if (r13 >= r0) goto L_0x019a;
    L_0x016b:
        if (r23 != 0) goto L_0x019a;
    L_0x016d:
        r0 = r28;
        r0 = r0.ppm;
        r24 = r0;
        r21 = r24.decodeChar();
        r24 = -1;
        r0 = r21;
        r1 = r24;
        if (r0 != r1) goto L_0x0184;
    L_0x017f:
        r23 = 1;
    L_0x0181:
        r13 = r13 + 1;
        goto L_0x0165;
    L_0x0184:
        r24 = 3;
        r0 = r24;
        if (r13 != r0) goto L_0x018f;
    L_0x018a:
        r0 = r21;
        r15 = r0 & 255;
        goto L_0x0181;
    L_0x018f:
        r24 = r12 << 8;
        r0 = r21;
        r0 = r0 & 255;
        r25 = r0;
        r12 = r24 + r25;
        goto L_0x0181;
    L_0x019a:
        if (r23 != 0) goto L_0x009d;
    L_0x019c:
        r24 = r15 + 32;
        r25 = r12 + 2;
        r0 = r28;
        r1 = r24;
        r2 = r25;
        r0.copyString(r1, r2);
        goto L_0x0074;
    L_0x01ab:
        r24 = 5;
        r0 = r18;
        r1 = r24;
        if (r0 != r1) goto L_0x01d2;
    L_0x01b3:
        r0 = r28;
        r0 = r0.ppm;
        r24 = r0;
        r15 = r24.decodeChar();
        r24 = -1;
        r0 = r24;
        if (r15 == r0) goto L_0x009d;
    L_0x01c3:
        r24 = r15 + 4;
        r25 = 1;
        r0 = r28;
        r1 = r24;
        r2 = r25;
        r0.copyString(r1, r2);
        goto L_0x0074;
    L_0x01d2:
        r0 = r28;
        r0 = r0.window;
        r24 = r0;
        r0 = r28;
        r0 = r0.unpPtr;
        r25 = r0;
        r26 = r25 + 1;
        r0 = r26;
        r1 = r28;
        r1.unpPtr = r0;
        r0 = (byte) r6;
        r26 = r0;
        r24[r25] = r26;
        goto L_0x0074;
    L_0x01ed:
        r0 = r28;
        r0 = r0.LD;
        r24 = r0;
        r0 = r28;
        r1 = r24;
        r19 = r0.decodeNumber(r1);
        r24 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0 = r19;
        r1 = r24;
        if (r0 >= r1) goto L_0x0220;
    L_0x0203:
        r0 = r28;
        r0 = r0.window;
        r24 = r0;
        r0 = r28;
        r0 = r0.unpPtr;
        r25 = r0;
        r26 = r25 + 1;
        r0 = r26;
        r1 = r28;
        r1.unpPtr = r0;
        r0 = r19;
        r0 = (byte) r0;
        r26 = r0;
        r24[r25] = r26;
        goto L_0x0074;
    L_0x0220:
        r24 = 271; // 0x10f float:3.8E-43 double:1.34E-321;
        r0 = r19;
        r1 = r24;
        if (r0 < r1) goto L_0x0303;
    L_0x0228:
        r24 = LDecode;
        r0 = r19;
        r0 = r0 + -271;
        r19 = r0;
        r24 = r24[r19];
        r15 = r24 + 3;
        r24 = LBits;
        r5 = r24[r19];
        if (r5 <= 0) goto L_0x0249;
    L_0x023a:
        r24 = r28.getbits();
        r25 = 16 - r5;
        r24 = r24 >>> r25;
        r15 = r15 + r24;
        r0 = r28;
        r0.addbits(r5);
    L_0x0249:
        r0 = r28;
        r0 = r0.DD;
        r24 = r0;
        r0 = r28;
        r1 = r24;
        r11 = r0.decodeNumber(r1);
        r24 = r8[r11];
        r12 = r24 + 1;
        r5 = r7[r11];
        if (r5 <= 0) goto L_0x029e;
    L_0x025f:
        r24 = 9;
        r0 = r24;
        if (r11 <= r0) goto L_0x02f3;
    L_0x0265:
        r24 = 4;
        r0 = r24;
        if (r5 <= r0) goto L_0x0280;
    L_0x026b:
        r24 = r28.getbits();
        r25 = 20 - r5;
        r24 = r24 >>> r25;
        r24 = r24 << 4;
        r12 = r12 + r24;
        r24 = r5 + -4;
        r0 = r28;
        r1 = r24;
        r0.addbits(r1);
    L_0x0280:
        r0 = r28;
        r0 = r0.lowDistRepCount;
        r24 = r0;
        if (r24 <= 0) goto L_0x02c3;
    L_0x0288:
        r0 = r28;
        r0 = r0.lowDistRepCount;
        r24 = r0;
        r24 = r24 + -1;
        r0 = r24;
        r1 = r28;
        r1.lowDistRepCount = r0;
        r0 = r28;
        r0 = r0.prevLowDist;
        r24 = r0;
        r12 = r12 + r24;
    L_0x029e:
        r24 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0 = r24;
        if (r12 < r0) goto L_0x02b2;
    L_0x02a4:
        r15 = r15 + 1;
        r0 = (long) r12;
        r24 = r0;
        r26 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r24 = (r24 > r26 ? 1 : (r24 == r26 ? 0 : -1));
        if (r24 < 0) goto L_0x02b2;
    L_0x02b0:
        r15 = r15 + 1;
    L_0x02b2:
        r0 = r28;
        r0.insertOldDist(r12);
        r0 = r28;
        r0.insertLastMatch(r15, r12);
        r0 = r28;
        r0.copyString(r15, r12);
        goto L_0x0074;
    L_0x02c3:
        r0 = r28;
        r0 = r0.LDD;
        r24 = r0;
        r0 = r28;
        r1 = r24;
        r17 = r0.decodeNumber(r1);
        r24 = 16;
        r0 = r17;
        r1 = r24;
        if (r0 != r1) goto L_0x02ea;
    L_0x02d9:
        r24 = 15;
        r0 = r24;
        r1 = r28;
        r1.lowDistRepCount = r0;
        r0 = r28;
        r0 = r0.prevLowDist;
        r24 = r0;
        r12 = r12 + r24;
        goto L_0x029e;
    L_0x02ea:
        r12 = r12 + r17;
        r0 = r17;
        r1 = r28;
        r1.prevLowDist = r0;
        goto L_0x029e;
    L_0x02f3:
        r24 = r28.getbits();
        r25 = 16 - r5;
        r24 = r24 >>> r25;
        r12 = r12 + r24;
        r0 = r28;
        r0.addbits(r5);
        goto L_0x029e;
    L_0x0303:
        r24 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0 = r19;
        r1 = r24;
        if (r0 != r1) goto L_0x0313;
    L_0x030b:
        r24 = r28.readEndOfBlock();
        if (r24 != 0) goto L_0x0074;
    L_0x0311:
        goto L_0x009d;
    L_0x0313:
        r24 = 257; // 0x101 float:3.6E-43 double:1.27E-321;
        r0 = r19;
        r1 = r24;
        if (r0 != r1) goto L_0x0323;
    L_0x031b:
        r24 = r28.readVMCode();
        if (r24 != 0) goto L_0x0074;
    L_0x0321:
        goto L_0x009d;
    L_0x0323:
        r24 = 258; // 0x102 float:3.62E-43 double:1.275E-321;
        r0 = r19;
        r1 = r24;
        if (r0 != r1) goto L_0x034a;
    L_0x032b:
        r0 = r28;
        r0 = r0.lastLength;
        r24 = r0;
        if (r24 == 0) goto L_0x0074;
    L_0x0333:
        r0 = r28;
        r0 = r0.lastLength;
        r24 = r0;
        r0 = r28;
        r0 = r0.lastDist;
        r25 = r0;
        r0 = r28;
        r1 = r24;
        r2 = r25;
        r0.copyString(r1, r2);
        goto L_0x0074;
    L_0x034a:
        r24 = 263; // 0x107 float:3.69E-43 double:1.3E-321;
        r0 = r19;
        r1 = r24;
        if (r0 >= r1) goto L_0x03b5;
    L_0x0352:
        r0 = r19;
        r10 = r0 + -259;
        r0 = r28;
        r0 = r0.oldDist;
        r24 = r0;
        r12 = r24[r10];
        r13 = r10;
    L_0x035f:
        if (r13 <= 0) goto L_0x0376;
    L_0x0361:
        r0 = r28;
        r0 = r0.oldDist;
        r24 = r0;
        r0 = r28;
        r0 = r0.oldDist;
        r25 = r0;
        r26 = r13 + -1;
        r25 = r25[r26];
        r24[r13] = r25;
        r13 = r13 + -1;
        goto L_0x035f;
    L_0x0376:
        r0 = r28;
        r0 = r0.oldDist;
        r24 = r0;
        r25 = 0;
        r24[r25] = r12;
        r0 = r28;
        r0 = r0.RD;
        r24 = r0;
        r0 = r28;
        r1 = r24;
        r16 = r0.decodeNumber(r1);
        r24 = LDecode;
        r24 = r24[r16];
        r15 = r24 + 2;
        r24 = LBits;
        r5 = r24[r16];
        if (r5 <= 0) goto L_0x03a9;
    L_0x039a:
        r24 = r28.getbits();
        r25 = 16 - r5;
        r24 = r24 >>> r25;
        r15 = r15 + r24;
        r0 = r28;
        r0.addbits(r5);
    L_0x03a9:
        r0 = r28;
        r0.insertLastMatch(r15, r12);
        r0 = r28;
        r0.copyString(r15, r12);
        goto L_0x0074;
    L_0x03b5:
        r24 = 272; // 0x110 float:3.81E-43 double:1.344E-321;
        r0 = r19;
        r1 = r24;
        if (r0 >= r1) goto L_0x0074;
    L_0x03bd:
        r24 = SDDecode;
        r0 = r19;
        r0 = r0 + -263;
        r19 = r0;
        r24 = r24[r19];
        r12 = r24 + 1;
        r24 = SDBits;
        r5 = r24[r19];
        if (r5 <= 0) goto L_0x03de;
    L_0x03cf:
        r24 = r28.getbits();
        r25 = 16 - r5;
        r24 = r24 >>> r25;
        r12 = r12 + r24;
        r0 = r28;
        r0.addbits(r5);
    L_0x03de:
        r0 = r28;
        r0.insertOldDist(r12);
        r24 = 2;
        r0 = r28;
        r1 = r24;
        r0.insertLastMatch(r1, r12);
        r24 = 2;
        r0 = r28;
        r1 = r24;
        r0.copyString(r1, r12);
        goto L_0x0074;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.Unpack.unpack29(boolean):void");
    }

    private void UnpWriteBuf() throws IOException {
        int WrittenBorder = this.wrPtr;
        int WriteSize = (this.unpPtr - WrittenBorder) & Compress.MAXWINMASK;
        int I = 0;
        while (I < this.prgStack.size()) {
            UnpackFilter flt = (UnpackFilter) this.prgStack.get(I);
            if (flt != null) {
                if (flt.isNextWindow()) {
                    flt.setNextWindow(false);
                } else {
                    int BlockStart = flt.getBlockStart();
                    int BlockLength = flt.getBlockLength();
                    if (((BlockStart - WrittenBorder) & Compress.MAXWINMASK) >= WriteSize) {
                        continue;
                    } else {
                        if (WrittenBorder != BlockStart) {
                            UnpWriteArea(WrittenBorder, BlockStart);
                            WrittenBorder = BlockStart;
                            WriteSize = (this.unpPtr - WrittenBorder) & Compress.MAXWINMASK;
                        }
                        if (BlockLength <= WriteSize) {
                            int i;
                            int BlockEnd = (BlockStart + BlockLength) & Compress.MAXWINMASK;
                            if (BlockStart < BlockEnd || BlockEnd == 0) {
                                this.rarVM.setMemory(0, this.window, BlockStart, BlockLength);
                            } else {
                                int FirstPartLength = 4194304 - BlockStart;
                                this.rarVM.setMemory(0, this.window, BlockStart, FirstPartLength);
                                this.rarVM.setMemory(FirstPartLength, this.window, 0, BlockEnd);
                            }
                            VMPreparedProgram ParentPrg = ((UnpackFilter) this.filters.get(flt.getParentFilter())).getPrg();
                            VMPreparedProgram Prg = flt.getPrg();
                            if (ParentPrg.getGlobalData().size() > 64) {
                                Prg.getGlobalData().setSize(ParentPrg.getGlobalData().size());
                                for (i = 0; i < ParentPrg.getGlobalData().size() - 64; i++) {
                                    Prg.getGlobalData().set(i + 64, ParentPrg.getGlobalData().get(i + 64));
                                }
                            }
                            ExecuteCode(Prg);
                            if (Prg.getGlobalData().size() > 64) {
                                if (ParentPrg.getGlobalData().size() < Prg.getGlobalData().size()) {
                                    ParentPrg.getGlobalData().setSize(Prg.getGlobalData().size());
                                }
                                for (i = 0; i < Prg.getGlobalData().size() - 64; i++) {
                                    ParentPrg.getGlobalData().set(i + 64, Prg.getGlobalData().get(i + 64));
                                }
                            } else {
                                ParentPrg.getGlobalData().clear();
                            }
                            int FilteredDataOffset = Prg.getFilteredDataOffset();
                            int FilteredDataSize = Prg.getFilteredDataSize();
                            byte[] FilteredData = new byte[FilteredDataSize];
                            for (i = 0; i < FilteredDataSize; i++) {
                                FilteredData[i] = this.rarVM.getMem()[FilteredDataOffset + i];
                            }
                            this.prgStack.set(I, null);
                            while (I + 1 < this.prgStack.size()) {
                                UnpackFilter NextFilter = (UnpackFilter) this.prgStack.get(I + 1);
                                if (NextFilter == null || NextFilter.getBlockStart() != BlockStart || NextFilter.getBlockLength() != FilteredDataSize || NextFilter.isNextWindow()) {
                                    break;
                                }
                                this.rarVM.setMemory(0, FilteredData, 0, FilteredDataSize);
                                VMPreparedProgram pPrg = ((UnpackFilter) this.filters.get(NextFilter.getParentFilter())).getPrg();
                                VMPreparedProgram NextPrg = NextFilter.getPrg();
                                if (pPrg.getGlobalData().size() > 64) {
                                    NextPrg.getGlobalData().setSize(pPrg.getGlobalData().size());
                                    for (i = 0; i < pPrg.getGlobalData().size() - 64; i++) {
                                        NextPrg.getGlobalData().set(i + 64, pPrg.getGlobalData().get(i + 64));
                                    }
                                }
                                ExecuteCode(NextPrg);
                                if (NextPrg.getGlobalData().size() > 64) {
                                    if (pPrg.getGlobalData().size() < NextPrg.getGlobalData().size()) {
                                        pPrg.getGlobalData().setSize(NextPrg.getGlobalData().size());
                                    }
                                    for (i = 0; i < NextPrg.getGlobalData().size() - 64; i++) {
                                        pPrg.getGlobalData().set(i + 64, NextPrg.getGlobalData().get(i + 64));
                                    }
                                } else {
                                    pPrg.getGlobalData().clear();
                                }
                                FilteredDataOffset = NextPrg.getFilteredDataOffset();
                                FilteredDataSize = NextPrg.getFilteredDataSize();
                                FilteredData = new byte[FilteredDataSize];
                                for (i = 0; i < FilteredDataSize; i++) {
                                    FilteredData[i] = ((Byte) NextPrg.getGlobalData().get(FilteredDataOffset + i)).byteValue();
                                }
                                I++;
                                this.prgStack.set(I, null);
                            }
                            this.unpIO.unpWrite(FilteredData, 0, FilteredDataSize);
                            this.unpSomeRead = true;
                            this.writtenFileSize += (long) FilteredDataSize;
                            WrittenBorder = BlockEnd;
                            WriteSize = (this.unpPtr - WrittenBorder) & Compress.MAXWINMASK;
                        } else {
                            for (int J = I; J < this.prgStack.size(); J++) {
                                UnpackFilter filt = (UnpackFilter) this.prgStack.get(J);
                                if (filt != null && filt.isNextWindow()) {
                                    filt.setNextWindow(false);
                                }
                            }
                            this.wrPtr = WrittenBorder;
                            return;
                        }
                    }
                }
            }
            I++;
        }
        UnpWriteArea(WrittenBorder, this.unpPtr);
        this.wrPtr = this.unpPtr;
    }

    private void UnpWriteArea(int startPtr, int endPtr) throws IOException {
        if (endPtr != startPtr) {
            this.unpSomeRead = true;
        }
        if (endPtr < startPtr) {
            UnpWriteData(this.window, startPtr, (-startPtr) & Compress.MAXWINMASK);
            UnpWriteData(this.window, 0, endPtr);
            this.unpAllBuf = true;
            return;
        }
        UnpWriteData(this.window, startPtr, endPtr - startPtr);
    }

    private void UnpWriteData(byte[] data, int offset, int size) throws IOException {
        if (this.writtenFileSize < this.destUnpSize) {
            int writeSize = size;
            long leftToWrite = this.destUnpSize - this.writtenFileSize;
            if (((long) writeSize) > leftToWrite) {
                writeSize = (int) leftToWrite;
            }
            this.unpIO.unpWrite(data, offset, writeSize);
            this.writtenFileSize += (long) size;
        }
    }

    private void insertOldDist(int distance) {
        this.oldDist[3] = this.oldDist[2];
        this.oldDist[2] = this.oldDist[1];
        this.oldDist[1] = this.oldDist[0];
        this.oldDist[0] = distance;
    }

    private void insertLastMatch(int length, int distance) {
        this.lastDist = distance;
        this.lastLength = length;
    }

    private void copyString(int length, int distance) {
        int destPtr;
        int destPtr2 = this.unpPtr - distance;
        if (destPtr2 >= 0 && destPtr2 < 4194044 && this.unpPtr < 4194044) {
            byte[] bArr = this.window;
            int i = this.unpPtr;
            this.unpPtr = i + 1;
            destPtr = destPtr2 + 1;
            bArr[i] = this.window[destPtr2];
            while (true) {
                length--;
                if (length <= 0) {
                    break;
                }
                bArr = this.window;
                i = this.unpPtr;
                this.unpPtr = i + 1;
                destPtr2 = destPtr + 1;
                bArr[i] = this.window[destPtr];
                destPtr = destPtr2;
            }
        } else {
            destPtr = destPtr2;
            int length2 = length;
            while (true) {
                length = length2 - 1;
                if (length2 == 0) {
                    break;
                }
                destPtr2 = destPtr + 1;
                this.window[this.unpPtr] = this.window[destPtr & Compress.MAXWINMASK];
                this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                destPtr = destPtr2;
                length2 = length;
            }
        }
        destPtr2 = destPtr;
    }

    protected void unpInitData(boolean solid) {
        if (!solid) {
            this.tablesRead = false;
            Arrays.fill(this.oldDist, 0);
            this.oldDistPtr = 0;
            this.lastDist = 0;
            this.lastLength = 0;
            Arrays.fill(this.unpOldTable, (byte) 0);
            this.unpPtr = 0;
            this.wrPtr = 0;
            this.ppmEscChar = 2;
            initFilters();
        }
        InitBitInput();
        this.ppmError = false;
        this.writtenFileSize = 0;
        this.readTop = 0;
        this.readBorder = 0;
        unpInitData20(solid);
    }

    private void initFilters() {
        this.oldFilterLengths.clear();
        this.lastFilter = 0;
        this.filters.clear();
        this.prgStack.clear();
    }

    private boolean readEndOfBlock() throws IOException, RarException {
        boolean NewTable;
        boolean z;
        int BitField = getbits();
        boolean NewFile = false;
        if ((32768 & BitField) != 0) {
            NewTable = true;
            addbits(1);
        } else {
            NewFile = true;
            if ((BitField & 16384) != 0) {
                NewTable = true;
            } else {
                NewTable = false;
            }
            addbits(2);
        }
        if (NewTable) {
            z = false;
        } else {
            z = true;
        }
        this.tablesRead = z;
        if (NewFile || (NewTable && !readTables())) {
            return false;
        }
        return true;
    }

    private boolean readTables() throws IOException, RarException {
        byte[] bitLength = new byte[20];
        byte[] table = new byte[Compress.HUFF_TABLE_SIZE];
        if (this.inAddr > this.readTop - 25 && !unpReadBuf()) {
            return false;
        }
        faddbits((8 - this.inBit) & 7);
        long bitField = (long) (fgetbits() & -1);
        if ((PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID & bitField) != 0) {
            this.unpBlockType = BlockTypes.BLOCK_PPM;
            return this.ppm.decodeInit(this, this.ppmEscChar);
        }
        this.unpBlockType = BlockTypes.BLOCK_LZ;
        this.prevLowDist = 0;
        this.lowDistRepCount = 0;
        if ((PlaybackStateCompat.ACTION_PREPARE & bitField) == 0) {
            Arrays.fill(this.unpOldTable, (byte) 0);
        }
        faddbits(2);
        int i = 0;
        while (i < 20) {
            int i2;
            int length = (fgetbits() >>> 12) & 255;
            faddbits(4);
            if (length == 15) {
                int zeroCount = (fgetbits() >>> 12) & 255;
                faddbits(4);
                if (zeroCount == 0) {
                    bitLength[i] = (byte) 15;
                } else {
                    int zeroCount2 = zeroCount + 2;
                    i2 = i;
                    while (true) {
                        zeroCount = zeroCount2 - 1;
                        if (zeroCount2 <= 0 || i2 >= bitLength.length) {
                            i = i2 - 1;
                        } else {
                            i = i2 + 1;
                            bitLength[i2] = (byte) 0;
                            zeroCount2 = zeroCount;
                            i2 = i;
                        }
                    }
                    i = i2 - 1;
                }
            } else {
                bitLength[i] = (byte) length;
            }
            i++;
        }
        makeDecodeTables(bitLength, 0, this.BD, 20);
        i = 0;
        while (i < Compress.HUFF_TABLE_SIZE) {
            if (this.inAddr > this.readTop - 5 && !unpReadBuf()) {
                return false;
            }
            int Number = decodeNumber(this.BD);
            if (Number < 16) {
                table[i] = (byte) ((this.unpOldTable[i] + Number) & 15);
                i++;
            } else if (Number < 18) {
                if (Number == 16) {
                    N = (fgetbits() >>> 13) + 3;
                    faddbits(3);
                    N = N;
                } else {
                    N = (fgetbits() >>> 9) + 11;
                    faddbits(7);
                    N = N;
                }
                while (true) {
                    N = N - 1;
                    if (N <= 0 || i >= Compress.HUFF_TABLE_SIZE) {
                        break;
                    }
                    table[i] = table[i - 1];
                    i++;
                    N = N;
                }
            } else {
                if (Number == 18) {
                    N = (fgetbits() >>> 13) + 3;
                    faddbits(3);
                    N = N;
                    i2 = i;
                } else {
                    N = (fgetbits() >>> 9) + 11;
                    faddbits(7);
                    N = N;
                    i2 = i;
                }
                while (true) {
                    N = N - 1;
                    if (N <= 0 || i2 >= Compress.HUFF_TABLE_SIZE) {
                        i = i2;
                    } else {
                        i = i2 + 1;
                        table[i2] = (byte) 0;
                        N = N;
                        i2 = i;
                    }
                }
                i = i2;
            }
        }
        this.tablesRead = true;
        if (this.inAddr > this.readTop) {
            return false;
        }
        makeDecodeTables(table, 0, this.LD, Compress.NC);
        makeDecodeTables(table, Compress.NC, this.DD, 60);
        makeDecodeTables(table, 359, this.LDD, 17);
        makeDecodeTables(table, 376, this.RD, 28);
        for (i = 0; i < this.unpOldTable.length; i++) {
            this.unpOldTable[i] = table[i];
        }
        return true;
    }

    private boolean readVMCode() throws IOException, RarException {
        int FirstByte = getbits() >> 8;
        addbits(8);
        int Length = (FirstByte & 7) + 1;
        if (Length == 7) {
            Length = (getbits() >> 8) + 7;
            addbits(8);
        } else if (Length == 8) {
            Length = getbits();
            addbits(16);
        }
        List<Byte> vmCode = new ArrayList();
        int I = 0;
        while (I < Length) {
            if (this.inAddr >= this.readTop - 1 && !unpReadBuf() && I < Length - 1) {
                return false;
            }
            vmCode.add(Byte.valueOf((byte) (getbits() >> 8)));
            addbits(8);
            I++;
        }
        return addVMCode(FirstByte, vmCode, Length);
    }

    private boolean readVMCodePPM() throws IOException, RarException {
        int FirstByte = this.ppm.decodeChar();
        if (FirstByte == -1) {
            return false;
        }
        int Length = (FirstByte & 7) + 1;
        int B1;
        if (Length == 7) {
            B1 = this.ppm.decodeChar();
            if (B1 == -1) {
                return false;
            }
            Length = B1 + 7;
        } else if (Length == 8) {
            B1 = this.ppm.decodeChar();
            if (B1 == -1) {
                return false;
            }
            int B2 = this.ppm.decodeChar();
            if (B2 == -1) {
                return false;
            }
            Length = (B1 * 256) + B2;
        }
        List<Byte> vmCode = new ArrayList();
        for (int I = 0; I < Length; I++) {
            int Ch = this.ppm.decodeChar();
            if (Ch == -1) {
                return false;
            }
            vmCode.add(Byte.valueOf((byte) Ch));
        }
        return addVMCode(FirstByte, vmCode, Length);
    }

    private boolean addVMCode(int firstByte, List<Byte> vmCode, int length) {
        int i;
        int FiltPos;
        BitInput Inp = new BitInput();
        Inp.InitBitInput();
        for (i = 0; i < Math.min(32768, vmCode.size()); i++) {
            Inp.getInBuf()[i] = ((Byte) vmCode.get(i)).byteValue();
        }
        this.rarVM.init();
        if ((firstByte & 128) != 0) {
            FiltPos = RarVM.ReadData(Inp);
            if (FiltPos == 0) {
                initFilters();
            } else {
                FiltPos--;
            }
        } else {
            FiltPos = this.lastFilter;
        }
        if (FiltPos > this.filters.size() || FiltPos > this.oldFilterLengths.size()) {
            return false;
        }
        UnpackFilter Filter;
        int I;
        this.lastFilter = FiltPos;
        boolean NewFilter = FiltPos == this.filters.size();
        UnpackFilter StackFilter = new UnpackFilter();
        if (!NewFilter) {
            Filter = (UnpackFilter) this.filters.get(FiltPos);
            StackFilter.setParentFilter(FiltPos);
            Filter.setExecCount(Filter.getExecCount() + 1);
        } else if (FiltPos > 1024) {
            return false;
        } else {
            Filter = new UnpackFilter();
            this.filters.add(Filter);
            StackFilter.setParentFilter(this.filters.size() - 1);
            this.oldFilterLengths.add(Integer.valueOf(0));
            Filter.setExecCount(0);
        }
        this.prgStack.add(StackFilter);
        StackFilter.setExecCount(Filter.getExecCount());
        int BlockStart = RarVM.ReadData(Inp);
        if ((firstByte & 64) != 0) {
            BlockStart += 258;
        }
        StackFilter.setBlockStart((this.unpPtr + BlockStart) & Compress.MAXWINMASK);
        if ((firstByte & 32) != 0) {
            StackFilter.setBlockLength(RarVM.ReadData(Inp));
        } else {
            StackFilter.setBlockLength(FiltPos < this.oldFilterLengths.size() ? ((Integer) this.oldFilterLengths.get(FiltPos)).intValue() : 0);
        }
        boolean z = this.wrPtr != this.unpPtr && ((this.wrPtr - this.unpPtr) & Compress.MAXWINMASK) <= BlockStart;
        StackFilter.setNextWindow(z);
        this.oldFilterLengths.set(FiltPos, Integer.valueOf(StackFilter.getBlockLength()));
        Arrays.fill(StackFilter.getPrg().getInitR(), 0);
        StackFilter.getPrg().getInitR()[3] = RarVM.VM_GLOBALMEMADDR;
        StackFilter.getPrg().getInitR()[4] = StackFilter.getBlockLength();
        StackFilter.getPrg().getInitR()[5] = StackFilter.getExecCount();
        if ((firstByte & 16) != 0) {
            int InitMask = Inp.fgetbits() >>> 9;
            Inp.faddbits(7);
            for (I = 0; I < 7; I++) {
                if (((1 << I) & InitMask) != 0) {
                    StackFilter.getPrg().getInitR()[I] = RarVM.ReadData(Inp);
                }
            }
        }
        if (NewFilter) {
            int VMCodeSize = RarVM.ReadData(Inp);
            if (VMCodeSize >= 65536 || VMCodeSize == 0) {
                return false;
            }
            byte[] VMCode = new byte[VMCodeSize];
            for (I = 0; I < VMCodeSize; I++) {
                if (Inp.Overflow(3)) {
                    return false;
                }
                VMCode[I] = (byte) (Inp.fgetbits() >> 8);
                Inp.faddbits(8);
            }
            this.rarVM.prepare(VMCode, VMCodeSize, Filter.getPrg());
        }
        StackFilter.getPrg().setAltCmd(Filter.getPrg().getCmd());
        StackFilter.getPrg().setCmdCount(Filter.getPrg().getCmdCount());
        int StaticDataSize = Filter.getPrg().getStaticData().size();
        if (StaticDataSize > 0 && StaticDataSize < 8192) {
            StackFilter.getPrg().setStaticData(Filter.getPrg().getStaticData());
        }
        if (StackFilter.getPrg().getGlobalData().size() < 64) {
            StackFilter.getPrg().getGlobalData().clear();
            StackFilter.getPrg().getGlobalData().setSize(64);
        }
        Vector<Byte> globalData = StackFilter.getPrg().getGlobalData();
        for (I = 0; I < 7; I++) {
            this.rarVM.setLowEndianValue((Vector) globalData, I * 4, StackFilter.getPrg().getInitR()[I]);
        }
        this.rarVM.setLowEndianValue((Vector) globalData, 28, StackFilter.getBlockLength());
        this.rarVM.setLowEndianValue((Vector) globalData, 32, 0);
        this.rarVM.setLowEndianValue((Vector) globalData, 36, 0);
        this.rarVM.setLowEndianValue((Vector) globalData, 40, 0);
        this.rarVM.setLowEndianValue((Vector) globalData, 44, StackFilter.getExecCount());
        for (i = 0; i < 16; i++) {
            globalData.set(i + 48, Byte.valueOf((byte) 0));
        }
        if ((firstByte & 8) != 0) {
            if (Inp.Overflow(3)) {
                return false;
            }
            int DataSize = RarVM.ReadData(Inp);
            if (DataSize > 8128) {
                return false;
            }
            int CurSize = StackFilter.getPrg().getGlobalData().size();
            if (CurSize < DataSize + 64) {
                StackFilter.getPrg().getGlobalData().setSize((DataSize + 64) - CurSize);
            }
            globalData = StackFilter.getPrg().getGlobalData();
            for (I = 0; I < DataSize; I++) {
                if (Inp.Overflow(3)) {
                    return false;
                }
                globalData.set(64 + I, Byte.valueOf((byte) (Inp.fgetbits() >>> 8)));
                Inp.faddbits(8);
            }
        }
        return true;
    }

    private void ExecuteCode(VMPreparedProgram Prg) {
        if (Prg.getGlobalData().size() > 0) {
            Prg.getInitR()[6] = (int) this.writtenFileSize;
            this.rarVM.setLowEndianValue(Prg.getGlobalData(), 36, (int) this.writtenFileSize);
            this.rarVM.setLowEndianValue(Prg.getGlobalData(), 40, (int) (this.writtenFileSize >>> 32));
            this.rarVM.execute(Prg);
        }
    }

    public boolean isFileExtracted() {
        return this.fileExtracted;
    }

    public void setDestSize(long destSize) {
        this.destUnpSize = destSize;
        this.fileExtracted = false;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public int getChar() throws IOException, RarException {
        if (this.inAddr > 32738) {
            unpReadBuf();
        }
        byte[] bArr = this.inBuf;
        int i = this.inAddr;
        this.inAddr = i + 1;
        return bArr[i] & 255;
    }

    public int getPpmEscChar() {
        return this.ppmEscChar;
    }

    public void setPpmEscChar(int ppmEscChar) {
        this.ppmEscChar = ppmEscChar;
    }

    public void cleanUp() {
        if (this.ppm != null) {
            SubAllocator allocator = this.ppm.getSubAlloc();
            if (allocator != null) {
                allocator.stopSubAllocator();
            }
        }
    }
}
