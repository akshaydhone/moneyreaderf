package ir.mahdi.mzip.rar.unpack;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.InputDeviceCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.AudioVariables;
import ir.mahdi.mzip.rar.unpack.decode.BitDecode;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.decode.Decode;
import ir.mahdi.mzip.rar.unpack.decode.DistDecode;
import ir.mahdi.mzip.rar.unpack.decode.LitDecode;
import ir.mahdi.mzip.rar.unpack.decode.LowDistDecode;
import ir.mahdi.mzip.rar.unpack.decode.MultDecode;
import ir.mahdi.mzip.rar.unpack.decode.RepDecode;
import java.io.IOException;
import java.util.Arrays;

public abstract class Unpack20 extends Unpack15 {
    public static final int[] DBits = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};
    public static final int[] DDecode = new int[]{0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576, 32768, 49152, 65536, 98304, 131072, 196608, 262144, 327680, 393216, 458752, 524288, 589824, 655360, 720896, 786432, 851968, 917504, 983040};
    public static final byte[] LBits = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 5};
    public static final int[] LDecode = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224};
    public static final int[] SDBits = new int[]{2, 2, 3, 4, 5, 6, 6, 6};
    public static final int[] SDDecode = new int[]{0, 4, 8, 16, 32, 64, 128, 192};
    protected AudioVariables[] AudV = new AudioVariables[4];
    protected BitDecode BD = new BitDecode();
    protected DistDecode DD = new DistDecode();
    protected LitDecode LD = new LitDecode();
    protected LowDistDecode LDD = new LowDistDecode();
    protected MultDecode[] MD = new MultDecode[4];
    protected RepDecode RD = new RepDecode();
    protected int UnpAudioBlock;
    protected int UnpChannelDelta;
    protected int UnpChannels;
    protected int UnpCurChannel;
    protected byte[] UnpOldTable20 = new byte[1028];

    protected void unpack20(boolean solid) throws IOException, RarException {
        if (this.suspended) {
            this.unpPtr = this.wrPtr;
        } else {
            unpInitData(solid);
            if (!unpReadBuf()) {
                return;
            }
            if (solid || ReadTables20()) {
                this.destUnpSize--;
            } else {
                return;
            }
        }
        while (this.destUnpSize >= 0) {
            this.unpPtr &= Compress.MAXWINMASK;
            if (this.inAddr > this.readTop - 30 && !unpReadBuf()) {
                break;
            }
            if (((this.wrPtr - this.unpPtr) & Compress.MAXWINMASK) < 270 && this.wrPtr != this.unpPtr) {
                oldUnpWriteBuf();
                if (this.suspended) {
                    return;
                }
            }
            byte[] bArr;
            int i;
            if (this.UnpAudioBlock != 0) {
                int AudioNumber = decodeNumber(this.MD[this.UnpCurChannel]);
                if (AudioNumber != 256) {
                    bArr = this.window;
                    i = this.unpPtr;
                    this.unpPtr = i + 1;
                    bArr[i] = DecodeAudio(AudioNumber);
                    int i2 = this.UnpCurChannel + 1;
                    this.UnpCurChannel = i2;
                    if (i2 == this.UnpChannels) {
                        this.UnpCurChannel = 0;
                    }
                    this.destUnpSize--;
                } else if (!ReadTables20()) {
                    break;
                }
            } else {
                int Number = decodeNumber(this.LD);
                if (Number < 256) {
                    bArr = this.window;
                    i = this.unpPtr;
                    this.unpPtr = i + 1;
                    bArr[i] = (byte) Number;
                    this.destUnpSize--;
                } else if (Number > 269) {
                    Number -= 270;
                    Length = LDecode[Number] + 3;
                    Bits = LBits[Number];
                    if (Bits > 0) {
                        Length += getbits() >>> (16 - Bits);
                        addbits(Bits);
                    }
                    int DistNumber = decodeNumber(this.DD);
                    Distance = DDecode[DistNumber] + 1;
                    Bits = DBits[DistNumber];
                    if (Bits > 0) {
                        Distance += getbits() >>> (16 - Bits);
                        addbits(Bits);
                    }
                    if (Distance >= 8192) {
                        Length++;
                        if (((long) Distance) >= PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                            Length++;
                        }
                    }
                    CopyString20(Length, Distance);
                } else if (Number == 269) {
                    if (!ReadTables20()) {
                        break;
                    }
                } else if (Number == 256) {
                    CopyString20(this.lastLength, this.lastDist);
                } else if (Number < 261) {
                    Distance = this.oldDist[(this.oldDistPtr - (Number + InputDeviceCompat.SOURCE_ANY)) & 3];
                    int LengthNumber = decodeNumber(this.RD);
                    Length = LDecode[LengthNumber] + 2;
                    Bits = LBits[LengthNumber];
                    if (Bits > 0) {
                        Length += getbits() >>> (16 - Bits);
                        addbits(Bits);
                    }
                    if (Distance >= 257) {
                        Length++;
                        if (Distance >= 8192) {
                            Length++;
                            if (Distance >= 262144) {
                                Length++;
                            }
                        }
                    }
                    CopyString20(Length, Distance);
                } else if (Number < 270) {
                    Number -= 261;
                    Distance = SDDecode[Number] + 1;
                    Bits = SDBits[Number];
                    if (Bits > 0) {
                        Distance += getbits() >>> (16 - Bits);
                        addbits(Bits);
                    }
                    CopyString20(2, Distance);
                }
            }
        }
        ReadLastTables();
        oldUnpWriteBuf();
    }

    protected void CopyString20(int Length, int Distance) {
        int DestPtr;
        int[] iArr = this.oldDist;
        int i = this.oldDistPtr;
        this.oldDistPtr = i + 1;
        iArr[i & 3] = Distance;
        this.lastDist = Distance;
        this.lastLength = Length;
        this.destUnpSize -= (long) Length;
        int DestPtr2 = this.unpPtr - Distance;
        if (DestPtr2 >= 4194004 || this.unpPtr >= 4194004) {
            DestPtr = DestPtr2;
            int Length2 = Length;
            while (true) {
                Length = Length2 - 1;
                if (Length2 == 0) {
                    break;
                }
                DestPtr2 = DestPtr + 1;
                this.window[this.unpPtr] = this.window[DestPtr & Compress.MAXWINMASK];
                this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                DestPtr = DestPtr2;
                Length2 = Length;
            }
        } else {
            byte[] bArr = this.window;
            i = this.unpPtr;
            this.unpPtr = i + 1;
            DestPtr = DestPtr2 + 1;
            bArr[i] = this.window[DestPtr2];
            bArr = this.window;
            i = this.unpPtr;
            this.unpPtr = i + 1;
            DestPtr2 = DestPtr + 1;
            bArr[i] = this.window[DestPtr];
            DestPtr = DestPtr2;
            while (Length > 2) {
                Length--;
                bArr = this.window;
                i = this.unpPtr;
                this.unpPtr = i + 1;
                DestPtr2 = DestPtr + 1;
                bArr[i] = this.window[DestPtr];
                DestPtr = DestPtr2;
            }
        }
        DestPtr2 = DestPtr;
    }

    protected void makeDecodeTables(byte[] lenTab, int offset, Decode dec, int size) {
        int i;
        int[] lenCount = new int[16];
        int[] tmpPos = new int[16];
        Arrays.fill(lenCount, 0);
        Arrays.fill(dec.getDecodeNum(), 0);
        for (i = 0; i < size; i++) {
            int i2 = lenTab[offset + i] & 15;
            lenCount[i2] = lenCount[i2] + 1;
        }
        lenCount[0] = 0;
        tmpPos[0] = 0;
        dec.getDecodePos()[0] = 0;
        dec.getDecodeLen()[0] = 0;
        long N = 0;
        for (i = 1; i < 16; i++) {
            N = 2 * (((long) lenCount[i]) + N);
            long M = N << (15 - i);
            if (M > 65535) {
                M = 65535;
            }
            dec.getDecodeLen()[i] = (int) M;
            int i3 = dec.getDecodePos()[i - 1] + lenCount[i - 1];
            dec.getDecodePos()[i] = i3;
            tmpPos[i] = i3;
        }
        for (i = 0; i < size; i++) {
            if (lenTab[offset + i] != (byte) 0) {
                int[] decodeNum = dec.getDecodeNum();
                i3 = lenTab[offset + i] & 15;
                int i4 = tmpPos[i3];
                tmpPos[i3] = i4 + 1;
                decodeNum[i4] = i;
            }
        }
        dec.setMaxNum(size);
    }

    protected int decodeNumber(Decode dec) {
        int bits;
        long bitField = (long) (getbits() & 65534);
        int[] decodeLen = dec.getDecodeLen();
        if (bitField < ((long) decodeLen[8])) {
            if (bitField < ((long) decodeLen[4])) {
                if (bitField < ((long) decodeLen[2])) {
                    if (bitField < ((long) decodeLen[1])) {
                        bits = 1;
                    } else {
                        bits = 2;
                    }
                } else if (bitField < ((long) decodeLen[3])) {
                    bits = 3;
                } else {
                    bits = 4;
                }
            } else if (bitField < ((long) decodeLen[6])) {
                if (bitField < ((long) decodeLen[5])) {
                    bits = 5;
                } else {
                    bits = 6;
                }
            } else if (bitField < ((long) decodeLen[7])) {
                bits = 7;
            } else {
                bits = 8;
            }
        } else if (bitField < ((long) decodeLen[12])) {
            if (bitField < ((long) decodeLen[10])) {
                if (bitField < ((long) decodeLen[9])) {
                    bits = 9;
                } else {
                    bits = 10;
                }
            } else if (bitField < ((long) decodeLen[11])) {
                bits = 11;
            } else {
                bits = 12;
            }
        } else if (bitField >= ((long) decodeLen[14])) {
            bits = 15;
        } else if (bitField < ((long) decodeLen[13])) {
            bits = 13;
        } else {
            bits = 14;
        }
        addbits(bits);
        int N = dec.getDecodePos()[bits] + ((((int) bitField) - decodeLen[bits - 1]) >>> (16 - bits));
        if (N >= dec.getMaxNum()) {
            N = 0;
        }
        return dec.getDecodeNum()[N];
    }

    protected boolean ReadTables20() throws IOException, RarException {
        byte[] BitLength = new byte[19];
        byte[] Table = new byte[1028];
        if (this.inAddr > this.readTop - 25 && !unpReadBuf()) {
            return false;
        }
        int TableSize;
        int I;
        int BitField = getbits();
        this.UnpAudioBlock = 32768 & BitField;
        if ((BitField & 16384) == 0) {
            Arrays.fill(this.UnpOldTable20, (byte) 0);
        }
        addbits(2);
        if (this.UnpAudioBlock != 0) {
            this.UnpChannels = ((BitField >>> 12) & 3) + 1;
            if (this.UnpCurChannel >= this.UnpChannels) {
                this.UnpCurChannel = 0;
            }
            addbits(2);
            TableSize = this.UnpChannels * 257;
        } else {
            TableSize = 374;
        }
        for (I = 0; I < 19; I++) {
            BitLength[I] = (byte) (getbits() >>> 12);
            addbits(4);
        }
        makeDecodeTables(BitLength, 0, this.BD, 19);
        I = 0;
        while (I < TableSize) {
            if (this.inAddr > this.readTop - 5 && !unpReadBuf()) {
                return false;
            }
            int Number = decodeNumber(this.BD);
            if (Number < 16) {
                Table[I] = (byte) ((this.UnpOldTable20[I] + Number) & 15);
                I++;
            } else if (Number == 16) {
                N = (getbits() >>> 14) + 3;
                addbits(2);
                N = N;
                while (true) {
                    N = N - 1;
                    if (N <= 0 || I >= TableSize) {
                        break;
                    }
                    Table[I] = Table[I - 1];
                    I++;
                    N = N;
                }
            } else {
                int I2;
                if (Number == 17) {
                    N = (getbits() >>> 13) + 3;
                    addbits(3);
                    I2 = I;
                    N = N;
                } else {
                    N = (getbits() >>> 9) + 11;
                    addbits(7);
                    I2 = I;
                    N = N;
                }
                while (true) {
                    N = N - 1;
                    if (N <= 0 || I2 >= TableSize) {
                        I = I2;
                    } else {
                        I = I2 + 1;
                        Table[I2] = (byte) 0;
                        I2 = I;
                        N = N;
                    }
                }
                I = I2;
            }
        }
        if (this.inAddr > this.readTop) {
            return true;
        }
        if (this.UnpAudioBlock != 0) {
            for (I = 0; I < this.UnpChannels; I++) {
                makeDecodeTables(Table, I * 257, this.MD[I], 257);
            }
        } else {
            makeDecodeTables(Table, 0, this.LD, Compress.NC20);
            makeDecodeTables(Table, Compress.NC20, this.DD, 48);
            makeDecodeTables(Table, 346, this.RD, 28);
        }
        for (int i = 0; i < this.UnpOldTable20.length; i++) {
            this.UnpOldTable20[i] = Table[i];
        }
        return true;
    }

    protected void unpInitData20(boolean Solid) {
        if (!Solid) {
            this.UnpCurChannel = 0;
            this.UnpChannelDelta = 0;
            this.UnpChannels = 1;
            Arrays.fill(this.AudV, new AudioVariables());
            Arrays.fill(this.UnpOldTable20, (byte) 0);
        }
    }

    protected void ReadLastTables() throws IOException, RarException {
        if (this.readTop < this.inAddr + 5) {
            return;
        }
        if (this.UnpAudioBlock != 0) {
            if (decodeNumber(this.MD[this.UnpCurChannel]) == 256) {
                ReadTables20();
            }
        } else if (decodeNumber(this.LD) == 269) {
            ReadTables20();
        }
    }

    protected byte DecodeAudio(int Delta) {
        AudioVariables v = this.AudV[this.UnpCurChannel];
        v.setByteCount(v.getByteCount() + 1);
        v.setD4(v.getD3());
        v.setD3(v.getD2());
        v.setD2(v.getLastDelta() - v.getD1());
        v.setD1(v.getLastDelta());
        int Ch = ((((((v.getLastChar() * 8) + (v.getK1() * v.getD1())) + ((v.getK2() * v.getD2()) + (v.getK3() * v.getD3()))) + ((v.getK4() * v.getD4()) + (v.getK5() * this.UnpChannelDelta))) >>> 3) & 255) - Delta;
        int D = ((byte) Delta) << 3;
        int[] dif = v.getDif();
        dif[0] = dif[0] + Math.abs(D);
        dif = v.getDif();
        dif[1] = dif[1] + Math.abs(D - v.getD1());
        dif = v.getDif();
        dif[2] = dif[2] + Math.abs(v.getD1() + D);
        dif = v.getDif();
        dif[3] = dif[3] + Math.abs(D - v.getD2());
        dif = v.getDif();
        dif[4] = dif[4] + Math.abs(v.getD2() + D);
        dif = v.getDif();
        dif[5] = dif[5] + Math.abs(D - v.getD3());
        dif = v.getDif();
        dif[6] = dif[6] + Math.abs(v.getD3() + D);
        dif = v.getDif();
        dif[7] = dif[7] + Math.abs(D - v.getD4());
        dif = v.getDif();
        dif[8] = dif[8] + Math.abs(v.getD4() + D);
        dif = v.getDif();
        dif[9] = dif[9] + Math.abs(D - this.UnpChannelDelta);
        dif = v.getDif();
        dif[10] = dif[10] + Math.abs(this.UnpChannelDelta + D);
        v.setLastDelta((byte) (Ch - v.getLastChar()));
        this.UnpChannelDelta = v.getLastDelta();
        v.setLastChar(Ch);
        if ((v.getByteCount() & 31) == 0) {
            int MinDif = v.getDif()[0];
            int NumMinDif = 0;
            v.getDif()[0] = 0;
            for (int I = 1; I < v.getDif().length; I++) {
                if (v.getDif()[I] < MinDif) {
                    MinDif = v.getDif()[I];
                    NumMinDif = I;
                }
                v.getDif()[I] = 0;
            }
            switch (NumMinDif) {
                case 1:
                    if (v.getK1() >= -16) {
                        v.setK1(v.getK1() - 1);
                        break;
                    }
                    break;
                case 2:
                    if (v.getK1() < 16) {
                        v.setK1(v.getK1() + 1);
                        break;
                    }
                    break;
                case 3:
                    if (v.getK2() >= -16) {
                        v.setK2(v.getK2() - 1);
                        break;
                    }
                    break;
                case 4:
                    if (v.getK2() < 16) {
                        v.setK2(v.getK2() + 1);
                        break;
                    }
                    break;
                case 5:
                    if (v.getK3() >= -16) {
                        v.setK3(v.getK3() - 1);
                        break;
                    }
                    break;
                case 6:
                    if (v.getK3() < 16) {
                        v.setK3(v.getK3() + 1);
                        break;
                    }
                    break;
                case 7:
                    if (v.getK4() >= -16) {
                        v.setK4(v.getK4() - 1);
                        break;
                    }
                    break;
                case 8:
                    if (v.getK4() < 16) {
                        v.setK4(v.getK4() + 1);
                        break;
                    }
                    break;
                case 9:
                    if (v.getK5() >= -16) {
                        v.setK5(v.getK5() - 1);
                        break;
                    }
                    break;
                case 10:
                    if (v.getK5() < 16) {
                        v.setK5(v.getK5() + 1);
                        break;
                    }
                    break;
            }
        }
        return (byte) Ch;
    }
}
