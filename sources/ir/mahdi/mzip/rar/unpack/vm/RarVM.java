package ir.mahdi.mzip.rar.unpack.vm;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import ir.mahdi.mzip.rar.crc.RarCRC;
import ir.mahdi.mzip.rar.io.Raw;
import java.util.List;
import java.util.Vector;

public class RarVM extends BitInput {
    private static final long UINT_MASK = -1;
    public static final int VM_FIXEDGLOBALSIZE = 64;
    public static final int VM_GLOBALMEMADDR = 245760;
    public static final int VM_GLOBALMEMSIZE = 8192;
    public static final int VM_MEMMASK = 262143;
    public static final int VM_MEMSIZE = 262144;
    private static final int regCount = 8;
    private int IP;
    /* renamed from: R */
    private int[] f16R = new int[8];
    private int codeSize;
    private int flags;
    private int maxOpCount = 25000000;
    private byte[] mem = null;

    public static int ReadData(BitInput rarVM) {
        int data = rarVM.fgetbits();
        switch (49152 & data) {
            case 0:
                rarVM.faddbits(6);
                return (data >> 10) & 15;
            case 16384:
                if ((data & 15360) == 0) {
                    data = ((data >> 2) & 255) | InputDeviceCompat.SOURCE_ANY;
                    rarVM.faddbits(14);
                } else {
                    data = (data >> 6) & 255;
                    rarVM.faddbits(10);
                }
                return data;
            case 32768:
                rarVM.faddbits(2);
                data = rarVM.fgetbits();
                rarVM.faddbits(16);
                return data;
            default:
                rarVM.faddbits(2);
                data = rarVM.fgetbits() << 16;
                rarVM.faddbits(16);
                data |= rarVM.fgetbits();
                rarVM.faddbits(16);
                return data;
        }
    }

    public void init() {
        if (this.mem == null) {
            this.mem = new byte[262148];
        }
    }

    private boolean isVMMem(byte[] mem) {
        return this.mem == mem;
    }

    private int getValue(boolean byteMode, byte[] mem, int offset) {
        if (byteMode) {
            if (isVMMem(mem)) {
                return mem[offset];
            }
            return mem[offset] & 255;
        } else if (isVMMem(mem)) {
            return Raw.readIntLittleEndian(mem, offset);
        } else {
            return Raw.readIntBigEndian(mem, offset);
        }
    }

    private void setValue(boolean byteMode, byte[] mem, int offset, int value) {
        if (byteMode) {
            if (isVMMem(mem)) {
                mem[offset] = (byte) value;
            } else {
                mem[offset] = (byte) ((mem[offset] & 0) | ((byte) (value & 255)));
            }
        } else if (isVMMem(mem)) {
            Raw.writeIntLittleEndian(mem, offset, value);
        } else {
            Raw.writeIntBigEndian(mem, offset, value);
        }
    }

    public void setLowEndianValue(byte[] mem, int offset, int value) {
        Raw.writeIntLittleEndian(mem, offset, value);
    }

    public void setLowEndianValue(Vector<Byte> mem, int offset, int value) {
        mem.set(offset + 0, Byte.valueOf((byte) (value & 255)));
        mem.set(offset + 1, Byte.valueOf((byte) ((value >>> 8) & 255)));
        mem.set(offset + 2, Byte.valueOf((byte) ((value >>> 16) & 255)));
        mem.set(offset + 3, Byte.valueOf((byte) ((value >>> 24) & 255)));
    }

    private int getOperand(VMPreparedOperand cmdOp) {
        if (cmdOp.getType() == VMOpType.VM_OPREGMEM) {
            return Raw.readIntLittleEndian(this.mem, (cmdOp.getOffset() + cmdOp.getBase()) & VM_MEMMASK);
        }
        return Raw.readIntLittleEndian(this.mem, cmdOp.getOffset());
    }

    public void execute(VMPreparedProgram prg) {
        int i;
        List<VMPreparedCommand> preparedCode;
        for (i = 0; i < prg.getInitR().length; i++) {
            this.f16R[i] = prg.getInitR()[i];
        }
        long globalSize = (long) (Math.min(prg.getGlobalData().size(), 8192) & -1);
        if (globalSize != 0) {
            for (i = 0; ((long) i) < globalSize; i++) {
                this.mem[VM_GLOBALMEMADDR + i] = ((Byte) prg.getGlobalData().get(i)).byteValue();
            }
        }
        long staticSize = Math.min((long) prg.getStaticData().size(), PlaybackStateCompat.ACTION_PLAY_FROM_URI - globalSize) & -1;
        if (staticSize != 0) {
            for (i = 0; ((long) i) < staticSize; i++) {
                this.mem[(VM_GLOBALMEMADDR + ((int) globalSize)) + i] = ((Byte) prg.getStaticData().get(i)).byteValue();
            }
        }
        this.f16R[7] = 262144;
        this.flags = 0;
        if (prg.getAltCmd().size() != 0) {
            preparedCode = prg.getAltCmd();
        } else {
            preparedCode = prg.getCmd();
        }
        if (!ExecuteCode(preparedCode, prg.getCmdCount())) {
            ((VMPreparedCommand) preparedCode.get(0)).setOpCode(VMCommands.VM_RET);
        }
        int newBlockPos = getValue(false, this.mem, 245792) & VM_MEMMASK;
        int newBlockSize = getValue(false, this.mem, 245788) & VM_MEMMASK;
        if (newBlockPos + newBlockSize >= 262144) {
            newBlockPos = 0;
            newBlockSize = 0;
        }
        prg.setFilteredDataOffset(newBlockPos);
        prg.setFilteredDataSize(newBlockSize);
        prg.getGlobalData().clear();
        int dataSize = Math.min(getValue(false, this.mem, 245808), 8128);
        if (dataSize != 0) {
            prg.getGlobalData().setSize(dataSize + 64);
            for (i = 0; i < dataSize + 64; i++) {
                prg.getGlobalData().set(i, Byte.valueOf(this.mem[VM_GLOBALMEMADDR + i]));
            }
        }
    }

    public byte[] getMem() {
        return this.mem;
    }

    private boolean setIP(int ip) {
        if (ip >= this.codeSize) {
            return true;
        }
        int i = this.maxOpCount - 1;
        this.maxOpCount = i;
        if (i <= 0) {
            return false;
        }
        this.IP = ip;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean ExecuteCode(java.util.List<ir.mahdi.mzip.rar.unpack.vm.VMPreparedCommand> r23, int r24) {
        /*
        r22 = this;
        r14 = 25000000; // 0x17d7840 float:4.6555036E-38 double:1.2351641E-316;
        r0 = r22;
        r0.maxOpCount = r14;
        r0 = r24;
        r1 = r22;
        r1.codeSize = r0;
        r14 = 0;
        r0 = r22;
        r0.IP = r14;
    L_0x0012:
        r0 = r22;
        r14 = r0.IP;
        r0 = r23;
        r6 = r0.get(r14);
        r6 = (ir.mahdi.mzip.rar.unpack.vm.VMPreparedCommand) r6;
        r14 = r6.getOp1();
        r0 = r22;
        r9 = r0.getOperand(r14);
        r14 = r6.getOp2();
        r0 = r22;
        r10 = r0.getOperand(r14);
        r14 = ir.mahdi.mzip.rar.unpack.vm.RarVM.C03061.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands;
        r15 = r6.getOpCode();
        r15 = r15.ordinal();
        r14 = r14[r15];
        switch(r14) {
            case 1: goto L_0x0056;
            case 2: goto L_0x007a;
            case 3: goto L_0x0099;
            case 4: goto L_0x00b8;
            case 5: goto L_0x00f7;
            case 6: goto L_0x0130;
            case 7: goto L_0x0169;
            case 8: goto L_0x01e9;
            case 9: goto L_0x0234;
            case 10: goto L_0x027f;
            case 11: goto L_0x02db;
            case 12: goto L_0x0326;
            case 13: goto L_0x0371;
            case 14: goto L_0x0390;
            case 15: goto L_0x03af;
            case 16: goto L_0x03ee;
            case 17: goto L_0x041c;
            case 18: goto L_0x044a;
            case 19: goto L_0x0481;
            case 20: goto L_0x04af;
            case 21: goto L_0x04dd;
            case 22: goto L_0x04ef;
            case 23: goto L_0x0534;
            case 24: goto L_0x0579;
            case 25: goto L_0x05be;
            case 26: goto L_0x05f6;
            case 27: goto L_0x0615;
            case 28: goto L_0x0634;
            case 29: goto L_0x0653;
            case 30: goto L_0x067a;
            case 31: goto L_0x06a1;
            case 32: goto L_0x06c0;
            case 33: goto L_0x06fc;
            case 34: goto L_0x0738;
            case 35: goto L_0x077a;
            case 36: goto L_0x07a1;
            case 37: goto L_0x07f7;
            case 38: goto L_0x0845;
            case 39: goto L_0x0893;
            case 40: goto L_0x08cc;
            case 41: goto L_0x08f1;
            case 42: goto L_0x0916;
            case 43: goto L_0x0951;
            case 44: goto L_0x0983;
            case 45: goto L_0x09b3;
            case 46: goto L_0x09e0;
            case 47: goto L_0x0a00;
            case 48: goto L_0x0a25;
            case 49: goto L_0x0a65;
            case 50: goto L_0x0aab;
            case 51: goto L_0x0ada;
            case 52: goto L_0x0b57;
            case 53: goto L_0x0bd4;
            case 54: goto L_0x0c0f;
            default: goto L_0x0041;
        };
    L_0x0041:
        r0 = r22;
        r14 = r0.IP;
        r14 = r14 + 1;
        r0 = r22;
        r0.IP = r14;
        r0 = r22;
        r14 = r0.maxOpCount;
        r14 = r14 + -1;
        r0 = r22;
        r0.maxOpCount = r14;
        goto L_0x0012;
    L_0x0056:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r16 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x007a:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0099:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x00b8:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r10);
        r11 = r12 - r14;
        if (r11 != 0) goto L_0x00e4;
    L_0x00d8:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x00e4:
        if (r11 <= r12) goto L_0x00ed;
    L_0x00e6:
        r14 = 1;
    L_0x00e7:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x00ed:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        r14 = r14 | 0;
        goto L_0x00e7;
    L_0x00f7:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r10);
        r11 = r12 - r14;
        if (r11 != 0) goto L_0x011d;
    L_0x0111:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x011d:
        if (r11 <= r12) goto L_0x0126;
    L_0x011f:
        r14 = 1;
    L_0x0120:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x0126:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        r14 = r14 | 0;
        goto L_0x0120;
    L_0x0130:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r10);
        r11 = r12 - r14;
        if (r11 != 0) goto L_0x0156;
    L_0x014a:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x0156:
        if (r11 <= r12) goto L_0x015f;
    L_0x0158:
        r14 = 1;
    L_0x0159:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x015f:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        r14 = r14 | 0;
        goto L_0x0159;
    L_0x0169:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = (long) r12;
        r16 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r14 = r14 + r16;
        r16 = -1;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        if (r14 == 0) goto L_0x01ce;
    L_0x019e:
        r11 = r11 & 255;
        if (r11 >= r12) goto L_0x01b6;
    L_0x01a2:
        r14 = 1;
    L_0x01a3:
        r0 = r22;
        r0.flags = r14;
    L_0x01a7:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x01b6:
        if (r11 != 0) goto L_0x01c1;
    L_0x01b8:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x01be:
        r14 = r14 | 0;
        goto L_0x01a3;
    L_0x01c1:
        r14 = r11 & 128;
        if (r14 == 0) goto L_0x01cc;
    L_0x01c5:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        goto L_0x01be;
    L_0x01cc:
        r14 = 0;
        goto L_0x01be;
    L_0x01ce:
        if (r11 >= r12) goto L_0x01d6;
    L_0x01d0:
        r14 = 1;
    L_0x01d1:
        r0 = r22;
        r0.flags = r14;
        goto L_0x01a7;
    L_0x01d6:
        if (r11 != 0) goto L_0x01e1;
    L_0x01d8:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x01de:
        r14 = r14 | 0;
        goto L_0x01d1;
    L_0x01e1:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x01de;
    L_0x01e9:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -1;
        r20 = 1;
        r0 = r22;
        r0 = r0.mem;
        r21 = r0;
        r0 = r22;
        r1 = r20;
        r2 = r21;
        r20 = r0.getValue(r1, r2, r10);
        r0 = r20;
        r0 = (long) r0;
        r20 = r0;
        r18 = r18 + r20;
        r16 = r16 & r18;
        r18 = -1;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0234:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -1;
        r20 = 0;
        r0 = r22;
        r0 = r0.mem;
        r21 = r0;
        r0 = r22;
        r1 = r20;
        r2 = r21;
        r20 = r0.getValue(r1, r2, r10);
        r0 = r20;
        r0 = (long) r0;
        r20 = r0;
        r18 = r18 + r20;
        r16 = r16 & r18;
        r18 = -1;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x027f:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = (long) r12;
        r16 = -1;
        r18 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r19 = r0;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r18 = r0.getValue(r1, r2, r10);
        r0 = r18;
        r0 = (long) r0;
        r18 = r0;
        r16 = r16 - r18;
        r14 = r14 & r16;
        r16 = -1;
        r14 = r14 & r16;
        r11 = (int) r14;
        if (r11 != 0) goto L_0x02cd;
    L_0x02b4:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x02ba:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x02cd:
        if (r11 <= r12) goto L_0x02d1;
    L_0x02cf:
        r14 = 1;
        goto L_0x02ba;
    L_0x02d1:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        r14 = r14 | 0;
        goto L_0x02ba;
    L_0x02db:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -1;
        r20 = 1;
        r0 = r22;
        r0 = r0.mem;
        r21 = r0;
        r0 = r22;
        r1 = r20;
        r2 = r21;
        r20 = r0.getValue(r1, r2, r10);
        r0 = r20;
        r0 = (long) r0;
        r20 = r0;
        r18 = r18 - r20;
        r16 = r16 & r18;
        r18 = -1;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0326:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -1;
        r20 = 0;
        r0 = r22;
        r0 = r0.mem;
        r21 = r0;
        r0 = r22;
        r1 = r20;
        r2 = r21;
        r20 = r0.getValue(r1, r2, r10);
        r0 = r20;
        r0 = (long) r0;
        r20 = r0;
        r18 = r18 - r20;
        r16 = r16 & r18;
        r18 = -1;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0371:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x0041;
    L_0x037e:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x0390:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 != 0) goto L_0x0041;
    L_0x039d:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x03af:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r14 = (long) r14;
        r16 = 0;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        if (r14 == 0) goto L_0x03cb;
    L_0x03c9:
        r11 = r11 & 255;
    L_0x03cb:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        if (r11 != 0) goto L_0x03e6;
    L_0x03da:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x03e0:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x03e6:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x03e0;
    L_0x03ee:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = 0;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x041c:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = 0;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x044a:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r14 = (long) r14;
        r16 = -2;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        if (r11 != 0) goto L_0x0479;
    L_0x046d:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x0473:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x0479:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x0473;
    L_0x0481:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -2;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x04af:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = (long) r0;
        r16 = r0;
        r18 = -2;
        r16 = r16 & r18;
        r0 = r16;
        r0 = (int) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x04dd:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x04ef:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r15 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r15 = r0.getValue(r15, r1, r10);
        r11 = r14 ^ r15;
        if (r11 != 0) goto L_0x052c;
    L_0x0513:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x0519:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x052c:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x0519;
    L_0x0534:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r15 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r15 = r0.getValue(r15, r1, r10);
        r11 = r14 & r15;
        if (r11 != 0) goto L_0x0571;
    L_0x0558:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x055e:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x0571:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x055e;
    L_0x0579:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r15 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r15 = r0.getValue(r15, r1, r10);
        r11 = r14 | r15;
        if (r11 != 0) goto L_0x05b6;
    L_0x059d:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x05a3:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x05b6:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x05a3;
    L_0x05be:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r15 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r15 = r0.getValue(r15, r1, r10);
        r11 = r14 & r15;
        if (r11 != 0) goto L_0x05ee;
    L_0x05e2:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x05e8:
        r0 = r22;
        r0.flags = r14;
        goto L_0x0041;
    L_0x05ee:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x05e8;
    L_0x05f6:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x0041;
    L_0x0603:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x0615:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 != 0) goto L_0x0041;
    L_0x0622:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x0634:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x0041;
    L_0x0641:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x0653:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r16 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r16 = r16.getFlag();
        r15 = r15 | r16;
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x0041;
    L_0x0668:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x067a:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r16 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r16 = r16.getFlag();
        r15 = r15 | r16;
        r14 = r14 & r15;
        if (r14 != 0) goto L_0x0041;
    L_0x068f:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x06a1:
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r14 = r14 & r15;
        if (r14 != 0) goto L_0x0041;
    L_0x06ae:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x06c0:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + -4;
        r14[r15] = r16;
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0 = r0.f16R;
        r16 = r0;
        r17 = 7;
        r16 = r16[r17];
        r17 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r17;
        r17 = 0;
        r0 = r22;
        r0 = r0.mem;
        r18 = r0;
        r0 = r22;
        r1 = r17;
        r2 = r18;
        r17 = r0.getValue(r1, r2, r9);
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r0.setValue(r14, r15, r1, r2);
        goto L_0x0041;
    L_0x06fc:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r0 = r0.f16R;
        r18 = r0;
        r19 = 7;
        r18 = r18[r19];
        r19 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r18 = r18 & r19;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r3 = r18;
        r16 = r0.getValue(r1, r2, r3);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + 4;
        r14[r15] = r16;
        goto L_0x0041;
    L_0x0738:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + -4;
        r14[r15] = r16;
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0 = r0.f16R;
        r16 = r0;
        r17 = 7;
        r16 = r16[r17];
        r17 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r17;
        r0 = r22;
        r0 = r0.IP;
        r17 = r0;
        r17 = r17 + 1;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r0.setValue(r14, r15, r1, r2);
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r0.setIP(r14);
        goto L_0x0012;
    L_0x077a:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r16 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r16 = r16 ^ -1;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x07a1:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r13 = r0.getValue(r14, r15, r10);
        r11 = r12 << r13;
        if (r11 != 0) goto L_0x07ec;
    L_0x07c1:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
        r15 = r14;
    L_0x07c8:
        r14 = r13 + -1;
        r14 = r12 << r14;
        r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r14 = r14 & r16;
        if (r14 == 0) goto L_0x07f5;
    L_0x07d2:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r14 = r14.getFlag();
    L_0x07d8:
        r14 = r14 | r15;
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x07ec:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        r15 = r14;
        goto L_0x07c8;
    L_0x07f5:
        r14 = 0;
        goto L_0x07d8;
    L_0x07f7:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r13 = r0.getValue(r14, r15, r10);
        r11 = r12 >>> r13;
        if (r11 != 0) goto L_0x083d;
    L_0x0817:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x081d:
        r15 = r13 + -1;
        r15 = r12 >>> r15;
        r16 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r16 = r16.getFlag();
        r15 = r15 & r16;
        r14 = r14 | r15;
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x083d:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x081d;
    L_0x0845:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r13 = r0.getValue(r14, r15, r10);
        r11 = r12 >> r13;
        if (r11 != 0) goto L_0x088b;
    L_0x0865:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x086b:
        r15 = r13 + -1;
        r15 = r12 >> r15;
        r16 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r16 = r16.getFlag();
        r15 = r15 & r16;
        r14 = r14 | r15;
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x088b:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x086b;
    L_0x0893:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r11 = -r14;
        if (r11 != 0) goto L_0x08bd;
    L_0x08a4:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x08aa:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x08bd:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r14 = r14.getFlag();
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r15 = r15.getFlag();
        r15 = r15 & r11;
        r14 = r14 | r15;
        goto L_0x08aa;
    L_0x08cc:
        r14 = 1;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = -r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x08f1:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r9);
        r0 = r16;
        r0 = -r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0916:
        r8 = 0;
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r14 = r14[r15];
        r5 = r14 + -4;
    L_0x0920:
        r14 = 8;
        if (r8 >= r14) goto L_0x0944;
    L_0x0924:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r5;
        r0 = r22;
        r0 = r0.f16R;
        r17 = r0;
        r17 = r17[r8];
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r0.setValue(r14, r15, r1, r2);
        r8 = r8 + 1;
        r5 = r5 + -4;
        goto L_0x0920;
    L_0x0944:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + -32;
        r14[r15] = r16;
        goto L_0x0041;
    L_0x0951:
        r8 = 0;
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r5 = r14[r15];
    L_0x0959:
        r14 = 8;
        if (r8 >= r14) goto L_0x0041;
    L_0x095d:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7 - r8;
        r16 = 0;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r18 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r18 = r18 & r5;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r3 = r18;
        r16 = r0.getValue(r1, r2, r3);
        r14[r15] = r16;
        r8 = r8 + 1;
        r5 = r5 + 4;
        goto L_0x0959;
    L_0x0983:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + -4;
        r14[r15] = r16;
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0 = r0.f16R;
        r16 = r0;
        r17 = 7;
        r16 = r16[r17];
        r17 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r17;
        r0 = r22;
        r0 = r0.flags;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r0.setValue(r14, r15, r1, r2);
        goto L_0x0041;
    L_0x09b3:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0 = r0.f16R;
        r16 = r0;
        r17 = 7;
        r16 = r16[r17];
        r17 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r17;
        r0 = r22;
        r1 = r16;
        r14 = r0.getValue(r14, r15, r1);
        r0 = r22;
        r0.flags = r14;
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + 4;
        r14[r15] = r16;
        goto L_0x0041;
    L_0x09e0:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0a00:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r16 = 1;
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r16;
        r0 = (byte) r0;
        r16 = r0;
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        goto L_0x0041;
    L_0x0a25:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r16 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r17 = r0;
        r0 = r22;
        r1 = r16;
        r2 = r17;
        r16 = r0.getValue(r1, r2, r10);
        r0 = r22;
        r1 = r16;
        r0.setValue(r14, r15, r9, r1);
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r10, r12);
        goto L_0x0041;
    L_0x0a65:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r14 = (long) r14;
        r16 = -1;
        r18 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r19 = r0;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r18 = r0.getValue(r1, r2, r10);
        r0 = r18;
        r0 = (long) r0;
        r18 = r0;
        r16 = r16 * r18;
        r14 = r14 & r16;
        r16 = -1;
        r14 = r14 & r16;
        r16 = -1;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x0aab:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r7 = r0.getValue(r14, r15, r10);
        if (r7 == 0) goto L_0x0041;
    L_0x0abb:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r14 = r0.getValue(r14, r15, r9);
        r11 = r14 / r7;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x0ada:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r4 = r14 & r15;
        r14 = (long) r12;
        r16 = -1;
        r18 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r19 = r0;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r18 = r0.getValue(r1, r2, r10);
        r0 = r18;
        r0 = (long) r0;
        r18 = r0;
        r16 = r16 + r18;
        r14 = r14 & r16;
        r16 = -1;
        r0 = (long) r4;
        r18 = r0;
        r16 = r16 + r18;
        r14 = r14 & r16;
        r16 = -1;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        if (r14 == 0) goto L_0x0b2a;
    L_0x0b28:
        r11 = r11 & 255;
    L_0x0b2a:
        if (r11 < r12) goto L_0x0b30;
    L_0x0b2c:
        if (r11 != r12) goto L_0x0b44;
    L_0x0b2e:
        if (r4 == 0) goto L_0x0b44;
    L_0x0b30:
        r14 = 1;
    L_0x0b31:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x0b44:
        if (r11 != 0) goto L_0x0b4f;
    L_0x0b46:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x0b4c:
        r14 = r14 | 0;
        goto L_0x0b31;
    L_0x0b4f:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x0b4c;
    L_0x0b57:
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r12 = r0.getValue(r14, r15, r9);
        r0 = r22;
        r14 = r0.flags;
        r15 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r15 = r15.getFlag();
        r4 = r14 & r15;
        r14 = (long) r12;
        r16 = -1;
        r18 = r6.isByteMode();
        r0 = r22;
        r0 = r0.mem;
        r19 = r0;
        r0 = r22;
        r1 = r18;
        r2 = r19;
        r18 = r0.getValue(r1, r2, r10);
        r0 = r18;
        r0 = (long) r0;
        r18 = r0;
        r16 = r16 - r18;
        r14 = r14 & r16;
        r16 = -1;
        r0 = (long) r4;
        r18 = r0;
        r16 = r16 - r18;
        r14 = r14 & r16;
        r16 = -1;
        r14 = r14 & r16;
        r11 = (int) r14;
        r14 = r6.isByteMode();
        if (r14 == 0) goto L_0x0ba7;
    L_0x0ba5:
        r11 = r11 & 255;
    L_0x0ba7:
        if (r11 > r12) goto L_0x0bad;
    L_0x0ba9:
        if (r11 != r12) goto L_0x0bc1;
    L_0x0bab:
        if (r4 == 0) goto L_0x0bc1;
    L_0x0bad:
        r14 = 1;
    L_0x0bae:
        r0 = r22;
        r0.flags = r14;
        r14 = r6.isByteMode();
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0.setValue(r14, r15, r9, r11);
        goto L_0x0041;
    L_0x0bc1:
        if (r11 != 0) goto L_0x0bcc;
    L_0x0bc3:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r14 = r14.getFlag();
    L_0x0bc9:
        r14 = r14 | 0;
        goto L_0x0bae;
    L_0x0bcc:
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r14 = r14.getFlag();
        r14 = r14 & r11;
        goto L_0x0bc9;
    L_0x0bd4:
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r14 = r14[r15];
        r15 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r14 < r15) goto L_0x0be1;
    L_0x0bdf:
        r14 = 1;
        return r14;
    L_0x0be1:
        r14 = 0;
        r0 = r22;
        r15 = r0.mem;
        r0 = r22;
        r0 = r0.f16R;
        r16 = r0;
        r17 = 7;
        r16 = r16[r17];
        r17 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r16 = r16 & r17;
        r0 = r22;
        r1 = r16;
        r14 = r0.getValue(r14, r15, r1);
        r0 = r22;
        r0.setIP(r14);
        r0 = r22;
        r14 = r0.f16R;
        r15 = 7;
        r16 = r14[r15];
        r16 = r16 + 4;
        r14[r15] = r16;
        goto L_0x0012;
    L_0x0c0f:
        r14 = r6.getOp1();
        r14 = r14.getData();
        r14 = ir.mahdi.mzip.rar.unpack.vm.VMStandardFilters.findFilter(r14);
        r0 = r22;
        r0.ExecuteStandardFilter(r14);
        goto L_0x0041;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.vm.RarVM.ExecuteCode(java.util.List, int):boolean");
    }

    public void prepare(byte[] code, int codeSize, VMPreparedProgram prg) {
        int i;
        VMPreparedCommand curCmd;
        InitBitInput();
        int cpLength = Math.min(32768, codeSize);
        for (i = 0; i < cpLength; i++) {
            byte[] bArr = this.inBuf;
            bArr[i] = (byte) (bArr[i] | code[i]);
        }
        byte xorSum = (byte) 0;
        for (i = 1; i < codeSize; i++) {
            xorSum = (byte) (code[i] ^ xorSum);
        }
        faddbits(8);
        prg.setCmdCount(0);
        if (xorSum == code[0]) {
            VMStandardFilters filterType = IsStandardFilter(code, codeSize);
            if (filterType != VMStandardFilters.VMSF_NONE) {
                curCmd = new VMPreparedCommand();
                curCmd.setOpCode(VMCommands.VM_STANDARD);
                curCmd.getOp1().setData(filterType.getFilter());
                curCmd.getOp1().setType(VMOpType.VM_OPNONE);
                curCmd.getOp2().setType(VMOpType.VM_OPNONE);
                codeSize = 0;
                prg.getCmd().add(curCmd);
                prg.setCmdCount(prg.getCmdCount() + 1);
            }
            int dataFlag = fgetbits();
            faddbits(1);
            if ((32768 & dataFlag) != 0) {
                long dataSize = ((long) ReadData(this)) & 0;
                i = 0;
                while (this.inAddr < codeSize && ((long) i) < dataSize) {
                    prg.getStaticData().add(Byte.valueOf((byte) (fgetbits() >> 8)));
                    faddbits(8);
                    i++;
                }
            }
            while (this.inAddr < codeSize) {
                curCmd = new VMPreparedCommand();
                int data = fgetbits();
                if ((32768 & data) == 0) {
                    curCmd.setOpCode(VMCommands.findVMCommand(data >> 12));
                    faddbits(4);
                } else {
                    curCmd.setOpCode(VMCommands.findVMCommand((data >> 10) - 24));
                    faddbits(6);
                }
                if ((VMCmdFlags.VM_CmdFlags[curCmd.getOpCode().getVMCommand()] & 4) != 0) {
                    curCmd.setByteMode((fgetbits() >> 15) == 1);
                    faddbits(1);
                } else {
                    curCmd.setByteMode(false);
                }
                curCmd.getOp1().setType(VMOpType.VM_OPNONE);
                curCmd.getOp2().setType(VMOpType.VM_OPNONE);
                int opNum = VMCmdFlags.VM_CmdFlags[curCmd.getOpCode().getVMCommand()] & 3;
                if (opNum > 0) {
                    decodeArg(curCmd.getOp1(), curCmd.isByteMode());
                    if (opNum == 2) {
                        decodeArg(curCmd.getOp2(), curCmd.isByteMode());
                    } else if (curCmd.getOp1().getType() == VMOpType.VM_OPINT && (VMCmdFlags.VM_CmdFlags[curCmd.getOpCode().getVMCommand()] & 24) != 0) {
                        int distance = curCmd.getOp1().getData();
                        if (distance >= 256) {
                            distance += InputDeviceCompat.SOURCE_ANY;
                        } else {
                            if (distance >= 136) {
                                distance -= 264;
                            } else if (distance >= 16) {
                                distance -= 8;
                            } else if (distance >= 8) {
                                distance -= 16;
                            }
                            distance += prg.getCmdCount();
                        }
                        curCmd.getOp1().setData(distance);
                    }
                }
                prg.setCmdCount(prg.getCmdCount() + 1);
                prg.getCmd().add(curCmd);
            }
        }
        curCmd = new VMPreparedCommand();
        curCmd.setOpCode(VMCommands.VM_RET);
        curCmd.getOp1().setType(VMOpType.VM_OPNONE);
        curCmd.getOp2().setType(VMOpType.VM_OPNONE);
        prg.getCmd().add(curCmd);
        prg.setCmdCount(prg.getCmdCount() + 1);
        if (codeSize != 0) {
            optimize(prg);
        }
    }

    private void decodeArg(VMPreparedOperand op, boolean byteMode) {
        int data = fgetbits();
        if ((32768 & data) != 0) {
            op.setType(VMOpType.VM_OPREG);
            op.setData((data >> 12) & 7);
            op.setOffset(op.getData());
            faddbits(4);
        } else if ((49152 & data) == 0) {
            op.setType(VMOpType.VM_OPINT);
            if (byteMode) {
                op.setData((data >> 6) & 255);
                faddbits(10);
                return;
            }
            faddbits(2);
            op.setData(ReadData(this));
        } else {
            op.setType(VMOpType.VM_OPREGMEM);
            if ((data & 8192) == 0) {
                op.setData((data >> 10) & 7);
                op.setOffset(op.getData());
                op.setBase(0);
                faddbits(6);
                return;
            }
            if ((data & 4096) == 0) {
                op.setData((data >> 9) & 7);
                op.setOffset(op.getData());
                faddbits(7);
            } else {
                op.setData(0);
                faddbits(4);
            }
            op.setBase(ReadData(this));
        }
    }

    private void optimize(VMPreparedProgram prg) {
        List<VMPreparedCommand> commands = prg.getCmd();
        for (VMPreparedCommand cmd : commands) {
            switch (cmd.getOpCode()) {
                case VM_MOV:
                    cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_MOVB : VMCommands.VM_MOVD);
                    break;
                case VM_CMP:
                    cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_CMPB : VMCommands.VM_CMPD);
                    break;
                default:
                    if ((VMCmdFlags.VM_CmdFlags[cmd.getOpCode().getVMCommand()] & 64) == 0) {
                        break;
                    }
                    VMCommands vMCommands;
                    boolean flagsRequired = false;
                    int i = commands.indexOf(cmd) + 1;
                    while (i < commands.size()) {
                        int flags = VMCmdFlags.VM_CmdFlags[((VMPreparedCommand) commands.get(i)).getOpCode().getVMCommand()];
                        if ((flags & 56) != 0) {
                            flagsRequired = true;
                        } else if ((flags & 64) == 0) {
                            i++;
                        }
                        if (flagsRequired) {
                            break;
                        }
                        switch (cmd.getOpCode()) {
                            case VM_ADD:
                                if (cmd.isByteMode()) {
                                    vMCommands = VMCommands.VM_ADDD;
                                } else {
                                    vMCommands = VMCommands.VM_ADDB;
                                }
                                cmd.setOpCode(vMCommands);
                                break;
                            case VM_SUB:
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_SUBB : VMCommands.VM_SUBD);
                                break;
                            case VM_INC:
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_INCB : VMCommands.VM_INCD);
                                break;
                            case VM_DEC:
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_DECB : VMCommands.VM_DECD);
                                break;
                            case VM_NEG:
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_NEGB : VMCommands.VM_NEGD);
                                break;
                            default:
                                break;
                        }
                    }
                    if (flagsRequired) {
                        switch (cmd.getOpCode()) {
                            case VM_ADD:
                                if (cmd.isByteMode()) {
                                    vMCommands = VMCommands.VM_ADDD;
                                } else {
                                    vMCommands = VMCommands.VM_ADDB;
                                }
                                cmd.setOpCode(vMCommands);
                                break;
                            case VM_SUB:
                                if (cmd.isByteMode()) {
                                }
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_SUBB : VMCommands.VM_SUBD);
                                break;
                            case VM_INC:
                                if (cmd.isByteMode()) {
                                }
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_INCB : VMCommands.VM_INCD);
                                break;
                            case VM_DEC:
                                if (cmd.isByteMode()) {
                                }
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_DECB : VMCommands.VM_DECD);
                                break;
                            case VM_NEG:
                                if (cmd.isByteMode()) {
                                }
                                cmd.setOpCode(cmd.isByteMode() ? VMCommands.VM_NEGB : VMCommands.VM_NEGD);
                                break;
                            default:
                                break;
                        }
                    }
            }
        }
    }

    private VMStandardFilters IsStandardFilter(byte[] code, int codeSize) {
        VMStandardFilterSignature[] stdList = new VMStandardFilterSignature[]{new VMStandardFilterSignature(53, -1386780537, VMStandardFilters.VMSF_E8), new VMStandardFilterSignature(57, 1020781950, VMStandardFilters.VMSF_E8E9), new VMStandardFilterSignature(120, 929663295, VMStandardFilters.VMSF_ITANIUM), new VMStandardFilterSignature(29, 235276157, VMStandardFilters.VMSF_DELTA), new VMStandardFilterSignature(149, 472669640, VMStandardFilters.VMSF_RGB), new VMStandardFilterSignature(216, -1132075263, VMStandardFilters.VMSF_AUDIO), new VMStandardFilterSignature(40, 1186579808, VMStandardFilters.VMSF_UPCASE)};
        int CodeCRC = RarCRC.checkCrc(-1, code, 0, code.length) ^ -1;
        int i = 0;
        while (i < stdList.length) {
            if (stdList[i].getCRC() == CodeCRC && stdList[i].getLength() == code.length) {
                return stdList[i].getType();
            }
            i++;
        }
        return VMStandardFilters.VMSF_NONE;
    }

    private void ExecuteStandardFilter(VMStandardFilters filterType) {
        int dataSize;
        long fileOffset;
        int curPos;
        byte curByte;
        int i;
        int channels;
        int srcPos;
        int border;
        int curChannel;
        int destPos;
        int srcPos2;
        int destDataPos;
        long prevByte;
        long predicted;
        switch (filterType) {
            case VMSF_E8:
            case VMSF_E8E9:
                dataSize = this.f16R[4];
                fileOffset = (long) (this.f16R[6] & -1);
                if (dataSize < 245760) {
                    byte cmpByte2 = (byte) (filterType == VMStandardFilters.VMSF_E8E9 ? 233 : 232);
                    int curPos2 = 0;
                    while (curPos2 < dataSize - 4) {
                        curPos = curPos2 + 1;
                        curByte = this.mem[curPos2];
                        if (curByte == (byte) -24 || curByte == cmpByte2) {
                            long offset = ((long) curPos) + fileOffset;
                            long Addr = (long) getValue(false, this.mem, curPos);
                            if ((-2147483648L & Addr) != 0) {
                                if (((Addr + offset) & -2147483648L) == 0) {
                                    setValue(false, this.mem, curPos, ((int) Addr) + 16777216);
                                }
                            } else if (((Addr - ((long) 16777216)) & -2147483648L) != 0) {
                                setValue(false, this.mem, curPos, (int) (Addr - offset));
                            }
                            curPos += 4;
                        }
                        curPos2 = curPos;
                    }
                    return;
                }
                return;
            case VMSF_ITANIUM:
                dataSize = this.f16R[4];
                fileOffset = (long) (this.f16R[6] & -1);
                if (dataSize < 245760) {
                    curPos = 0;
                    byte[] bArr = new byte[16];
                    bArr = new byte[]{(byte) 4, (byte) 4, (byte) 6, (byte) 6, (byte) 0, (byte) 0, (byte) 7, (byte) 7, (byte) 4, (byte) 4, (byte) 0, (byte) 0, (byte) 4, (byte) 4, (byte) 0, (byte) 0};
                    fileOffset >>>= 4;
                    while (curPos < dataSize - 21) {
                        int Byte = (this.mem[curPos] & 31) - 16;
                        if (Byte >= 0) {
                            byte cmdMask = bArr[Byte];
                            if (cmdMask != (byte) 0) {
                                for (i = 0; i <= 2; i++) {
                                    if (((1 << i) & cmdMask) != 0) {
                                        int startPos = (i * 41) + 5;
                                        if (filterItanium_GetBits(curPos, startPos + 37, 4) == 5) {
                                            filterItanium_SetBits(curPos, ((int) (((long) filterItanium_GetBits(curPos, startPos + 13, 20)) - fileOffset)) & 1048575, startPos + 13, 20);
                                        }
                                    }
                                }
                            }
                        }
                        curPos += 16;
                        fileOffset++;
                    }
                    return;
                }
                return;
            case VMSF_DELTA:
                dataSize = this.f16R[4] & -1;
                channels = this.f16R[0] & -1;
                srcPos = 0;
                border = (dataSize * 2) & -1;
                setValue(false, this.mem, 245792, dataSize);
                if (dataSize < 122880) {
                    curChannel = 0;
                    while (curChannel < channels) {
                        byte PrevByte = (byte) 0;
                        destPos = dataSize + curChannel;
                        srcPos2 = srcPos;
                        while (destPos < border) {
                            srcPos = srcPos2 + 1;
                            PrevByte = (byte) (PrevByte - this.mem[srcPos2]);
                            this.mem[destPos] = PrevByte;
                            destPos += channels;
                            srcPos2 = srcPos;
                        }
                        curChannel++;
                        srcPos = srcPos2;
                    }
                    return;
                }
                return;
            case VMSF_RGB:
                dataSize = this.f16R[4];
                int width = this.f16R[0] - 3;
                int posR = this.f16R[1];
                srcPos = 0;
                destDataPos = dataSize;
                setValue(false, this.mem, 245792, dataSize);
                if (dataSize < 122880 && posR >= 0) {
                    curChannel = 0;
                    while (curChannel < 3) {
                        prevByte = 0;
                        i = curChannel;
                        srcPos2 = srcPos;
                        while (i < dataSize) {
                            int upperPos = i - width;
                            if (upperPos >= 3) {
                                int upperDataPos = destDataPos + upperPos;
                                int upperByte = this.mem[upperDataPos] & 255;
                                int upperLeftByte = this.mem[upperDataPos - 3] & 255;
                                predicted = (((long) upperByte) + prevByte) - ((long) upperLeftByte);
                                int pa = Math.abs((int) (predicted - prevByte));
                                int pb = Math.abs((int) (predicted - ((long) upperByte)));
                                int pc = Math.abs((int) (predicted - ((long) upperLeftByte)));
                                if (pa <= pb && pa <= pc) {
                                    predicted = prevByte;
                                } else if (pb <= pc) {
                                    predicted = (long) upperByte;
                                } else {
                                    predicted = (long) upperLeftByte;
                                }
                            } else {
                                predicted = prevByte;
                            }
                            srcPos = srcPos2 + 1;
                            prevByte = ((predicted - ((long) this.mem[srcPos2])) & 255) & 255;
                            this.mem[destDataPos + i] = (byte) ((int) (255 & prevByte));
                            i += 3;
                            srcPos2 = srcPos;
                        }
                        curChannel++;
                        srcPos = srcPos2;
                    }
                    border = dataSize - 2;
                    for (i = posR; i < border; i += 3) {
                        byte G = this.mem[(destDataPos + i) + 1];
                        byte[] bArr2 = this.mem;
                        int i2 = destDataPos + i;
                        bArr2[i2] = (byte) (bArr2[i2] + G);
                        bArr2 = this.mem;
                        i2 = (destDataPos + i) + 2;
                        bArr2[i2] = (byte) (bArr2[i2] + G);
                    }
                    return;
                }
                return;
            case VMSF_AUDIO:
                dataSize = this.f16R[4];
                channels = this.f16R[0];
                srcPos = 0;
                destDataPos = dataSize;
                setValue(false, this.mem, 245792, dataSize);
                if (dataSize < 122880) {
                    curChannel = 0;
                    while (curChannel < channels) {
                        prevByte = 0;
                        long prevDelta = 0;
                        long[] Dif = new long[7];
                        int D1 = 0;
                        int D2 = 0;
                        int K1 = 0;
                        int K2 = 0;
                        int K3 = 0;
                        i = curChannel;
                        int byteCount = 0;
                        srcPos2 = srcPos;
                        while (i < dataSize) {
                            int D3 = D2;
                            D2 = ((int) prevDelta) - D1;
                            D1 = (int) prevDelta;
                            srcPos = srcPos2 + 1;
                            long curByte2 = (long) (this.mem[srcPos2] & 255);
                            predicted = (((((((8 * prevByte) + ((long) (K1 * D1))) + ((long) (K2 * D2))) + ((long) (K3 * D3))) >>> 3) & 255) - curByte2) & -1;
                            this.mem[destDataPos + i] = (byte) ((int) predicted);
                            prevDelta = (long) ((byte) ((int) (predicted - prevByte)));
                            prevByte = predicted;
                            int D = ((byte) ((int) curByte2)) << 3;
                            Dif[0] = Dif[0] + ((long) Math.abs(D));
                            Dif[1] = Dif[1] + ((long) Math.abs(D - D1));
                            Dif[2] = Dif[2] + ((long) Math.abs(D + D1));
                            Dif[3] = Dif[3] + ((long) Math.abs(D - D2));
                            Dif[4] = Dif[4] + ((long) Math.abs(D + D2));
                            Dif[5] = Dif[5] + ((long) Math.abs(D - D3));
                            Dif[6] = Dif[6] + ((long) Math.abs(D + D3));
                            if ((byteCount & 31) == 0) {
                                long minDif = Dif[0];
                                long numMinDif = 0;
                                Dif[0] = 0;
                                for (int j = 1; j < Dif.length; j++) {
                                    if (Dif[j] < minDif) {
                                        minDif = Dif[j];
                                        numMinDif = (long) j;
                                    }
                                    Dif[j] = 0;
                                }
                                switch ((int) numMinDif) {
                                    case 1:
                                        if (K1 < -16) {
                                            break;
                                        }
                                        K1--;
                                        break;
                                    case 2:
                                        if (K1 >= 16) {
                                            break;
                                        }
                                        K1++;
                                        break;
                                    case 3:
                                        if (K2 < -16) {
                                            break;
                                        }
                                        K2--;
                                        break;
                                    case 4:
                                        if (K2 >= 16) {
                                            break;
                                        }
                                        K2++;
                                        break;
                                    case 5:
                                        if (K3 < -16) {
                                            break;
                                        }
                                        K3--;
                                        break;
                                    case 6:
                                        if (K3 >= 16) {
                                            break;
                                        }
                                        K3++;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            i += channels;
                            byteCount++;
                            srcPos2 = srcPos;
                        }
                        curChannel++;
                        srcPos = srcPos2;
                    }
                    return;
                }
                return;
            case VMSF_UPCASE:
                dataSize = this.f16R[4];
                destPos = dataSize;
                if (dataSize < 122880) {
                    int destPos2 = destPos;
                    srcPos2 = 0;
                    while (srcPos2 < dataSize) {
                        srcPos = srcPos2 + 1;
                        curByte = this.mem[srcPos2];
                        if (curByte == (byte) 2) {
                            srcPos2 = srcPos + 1;
                            curByte = this.mem[srcPos];
                            if (curByte != (byte) 2) {
                                curByte = (byte) (curByte - 32);
                                srcPos = srcPos2;
                            } else {
                                srcPos = srcPos2;
                            }
                        }
                        destPos = destPos2 + 1;
                        this.mem[destPos2] = curByte;
                        destPos2 = destPos;
                        srcPos2 = srcPos;
                    }
                    setValue(false, this.mem, 245788, destPos2 - dataSize);
                    setValue(false, this.mem, 245792, dataSize);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void filterItanium_SetBits(int curPos, int bitField, int bitPos, int bitCount) {
        int inAddr = bitPos / 8;
        int inBit = bitPos & 7;
        int andMask = ((-1 >>> (32 - bitCount)) << inBit) ^ -1;
        bitField <<= inBit;
        for (int i = 0; i < 4; i++) {
            byte[] bArr = this.mem;
            int i2 = (curPos + inAddr) + i;
            bArr[i2] = (byte) (bArr[i2] & andMask);
            bArr = this.mem;
            i2 = (curPos + inAddr) + i;
            bArr[i2] = (byte) (bArr[i2] | bitField);
            andMask = (andMask >>> 8) | ViewCompat.MEASURED_STATE_MASK;
            bitField >>>= 8;
        }
    }

    private int filterItanium_GetBits(int curPos, int bitPos, int bitCount) {
        int inAddr = bitPos / 8;
        int inAddr2 = inAddr + 1;
        inAddr = inAddr2 + 1;
        return (-1 >>> (32 - bitCount)) & (((((this.mem[curPos + inAddr] & 255) | ((this.mem[curPos + inAddr2] & 255) << 8)) | ((this.mem[curPos + inAddr] & 255) << 16)) | ((this.mem[curPos + (inAddr + 1)] & 255) << 24)) >>> (bitPos & 7));
    }

    public void setMemory(int pos, byte[] data, int offset, int dataSize) {
        if (pos < 262144) {
            int i = 0;
            while (i < Math.min(data.length - offset, dataSize) && 262144 - pos >= i) {
                this.mem[pos + i] = data[offset + i];
                i++;
            }
        }
    }
}
