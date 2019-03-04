package ir.mahdi.mzip.rar.unpack;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.vm.BitInput;
import java.io.IOException;
import java.util.Arrays;

public abstract class Unpack15 extends BitInput {
    private static int[] DecHf0 = new int[]{32768, 49152, 57344, 61952, 61952, 61952, 61952, 61952, 65535};
    private static int[] DecHf1 = new int[]{8192, 49152, 57344, 61440, 61952, 61952, 63456, 65535};
    private static int[] DecHf2 = new int[]{4096, 9216, 32768, 49152, 64000, 65535, 65535, 65535};
    private static int[] DecHf3 = new int[]{2048, 9216, 60928, 65152, 65535, 65535, 65535};
    private static int[] DecHf4 = new int[]{MotionEventCompat.ACTION_POINTER_INDEX_MASK, 65535, 65535, 65535, 65535, 65535};
    private static int[] DecL1 = new int[]{32768, 40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 61952, 65535};
    private static int[] DecL2 = new int[]{40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 62016, 65535};
    private static int[] PosHf0 = new int[]{0, 0, 0, 0, 0, 8, 16, 24, 33, 33, 33, 33, 33};
    private static int[] PosHf1 = new int[]{0, 0, 0, 0, 0, 0, 4, 44, 60, 76, 80, 80, 127};
    private static int[] PosHf2 = new int[]{0, 0, 0, 0, 0, 0, 2, 7, 53, 117, 233, 0, 0};
    private static int[] PosHf3 = new int[]{0, 0, 0, 0, 0, 0, 0, 2, 16, 218, 251, 0, 0};
    private static int[] PosHf4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0};
    private static int[] PosL1 = new int[]{0, 0, 0, 2, 3, 5, 7, 11, 16, 20, 24, 32, 32};
    private static int[] PosL2 = new int[]{0, 0, 0, 0, 5, 7, 9, 13, 18, 22, 26, 34, 36};
    private static final int STARTHF0 = 4;
    private static final int STARTHF1 = 5;
    private static final int STARTHF2 = 5;
    private static final int STARTHF3 = 6;
    private static final int STARTHF4 = 8;
    private static final int STARTL1 = 2;
    private static final int STARTL2 = 3;
    static int[] ShortLen1 = new int[]{1, 3, 4, 4, 5, 6, 7, 8, 8, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortLen2 = new int[]{2, 3, 3, 3, 4, 4, 5, 6, 6, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortXor1 = new int[]{0, 160, 208, 224, 240, 248, 252, 254, 255, 192, 128, 144, 152, 156, 176};
    static int[] ShortXor2 = new int[]{0, 64, 96, 160, 208, 224, 240, 248, 252, 192, 128, 144, 152, 156, 176};
    protected int AvrLn1;
    protected int AvrLn2;
    protected int AvrLn3;
    protected int AvrPlc;
    protected int AvrPlcB;
    protected int Buf60;
    protected int[] ChSet = new int[256];
    protected int[] ChSetA = new int[256];
    protected int[] ChSetB = new int[256];
    protected int[] ChSetC = new int[256];
    protected int FlagBuf;
    protected int FlagsCnt;
    protected int LCount;
    protected int MaxDist3;
    protected int[] NToPl = new int[256];
    protected int[] NToPlB = new int[256];
    protected int[] NToPlC = new int[256];
    protected int Nhfb;
    protected int Nlzb;
    protected int NumHuf;
    protected int[] Place = new int[256];
    protected int[] PlaceA = new int[256];
    protected int[] PlaceB = new int[256];
    protected int[] PlaceC = new int[256];
    protected int StMode;
    protected long destUnpSize;
    protected int lastDist;
    protected int lastLength;
    protected int[] oldDist = new int[4];
    protected int oldDistPtr;
    protected int readBorder;
    protected int readTop;
    protected boolean suspended;
    protected boolean unpAllBuf;
    protected ComprDataIO unpIO;
    protected int unpPtr;
    protected boolean unpSomeRead;
    protected byte[] window;
    protected int wrPtr;

    protected abstract void unpInitData(boolean z);

    protected void unpack15(boolean solid) throws IOException, RarException {
        if (this.suspended) {
            this.unpPtr = this.wrPtr;
        } else {
            unpInitData(solid);
            oldUnpInitData(solid);
            unpReadBuf();
            if (solid) {
                this.unpPtr = this.wrPtr;
            } else {
                initHuff();
                this.unpPtr = 0;
            }
            this.destUnpSize--;
        }
        if (this.destUnpSize >= 0) {
            getFlagsBuf();
            this.FlagsCnt = 8;
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
            if (this.StMode != 0) {
                huffDecode();
            } else {
                int i = this.FlagsCnt - 1;
                this.FlagsCnt = i;
                if (i < 0) {
                    getFlagsBuf();
                    this.FlagsCnt = 7;
                }
                if ((this.FlagBuf & 128) != 0) {
                    this.FlagBuf <<= 1;
                    if (this.Nlzb > this.Nhfb) {
                        longLZ();
                    } else {
                        huffDecode();
                    }
                } else {
                    this.FlagBuf <<= 1;
                    i = this.FlagsCnt - 1;
                    this.FlagsCnt = i;
                    if (i < 0) {
                        getFlagsBuf();
                        this.FlagsCnt = 7;
                    }
                    if ((this.FlagBuf & 128) != 0) {
                        this.FlagBuf <<= 1;
                        if (this.Nlzb > this.Nhfb) {
                            huffDecode();
                        } else {
                            longLZ();
                        }
                    } else {
                        this.FlagBuf <<= 1;
                        shortLZ();
                    }
                }
            }
        }
        oldUnpWriteBuf();
    }

    protected boolean unpReadBuf() throws IOException, RarException {
        int dataSize = this.readTop - this.inAddr;
        if (dataSize < 0) {
            return false;
        }
        if (this.inAddr > 16384) {
            if (dataSize > 0) {
                System.arraycopy(this.inBuf, this.inAddr, this.inBuf, 0, dataSize);
            }
            this.inAddr = 0;
            this.readTop = dataSize;
        } else {
            dataSize = this.readTop;
        }
        int readCode = this.unpIO.unpRead(this.inBuf, dataSize, (32768 - dataSize) & -16);
        if (readCode > 0) {
            this.readTop += readCode;
        }
        this.readBorder = this.readTop - 30;
        if (readCode != -1) {
            return true;
        }
        return false;
    }

    private int getShortLen1(int pos) {
        return pos == 1 ? this.Buf60 + 3 : ShortLen1[pos];
    }

    private int getShortLen2(int pos) {
        return pos == 3 ? this.Buf60 + 3 : ShortLen2[pos];
    }

    protected void shortLZ() {
        int Length;
        this.NumHuf = 0;
        int BitField = fgetbits();
        if (this.LCount == 2) {
            faddbits(1);
            if (BitField >= 32768) {
                oldCopyString(this.lastDist, this.lastLength);
                return;
            } else {
                BitField <<= 1;
                this.LCount = 0;
            }
        }
        BitField >>>= 8;
        if (this.AvrLn1 < 37) {
            Length = 0;
            while (((ShortXor1[Length] ^ BitField) & ((255 >>> getShortLen1(Length)) ^ -1)) != 0) {
                Length++;
            }
            faddbits(getShortLen1(Length));
        } else {
            Length = 0;
            while (((ShortXor2[Length] ^ BitField) & ((255 >> getShortLen2(Length)) ^ -1)) != 0) {
                Length++;
            }
            faddbits(getShortLen2(Length));
        }
        int Distance;
        int[] iArr;
        int i;
        if (Length < 9) {
            this.LCount = 0;
            this.AvrLn1 += Length;
            this.AvrLn1 -= this.AvrLn1 >> 4;
            int DistancePlace = decodeNum(fgetbits(), 5, DecHf2, PosHf2) & 255;
            Distance = this.ChSetA[DistancePlace];
            DistancePlace--;
            if (DistancePlace != -1) {
                iArr = this.PlaceA;
                iArr[Distance] = iArr[Distance] - 1;
                int LastDistance = this.ChSetA[DistancePlace];
                iArr = this.PlaceA;
                iArr[LastDistance] = iArr[LastDistance] + 1;
                this.ChSetA[DistancePlace + 1] = LastDistance;
                this.ChSetA[DistancePlace] = Distance;
            }
            Length += 2;
            iArr = this.oldDist;
            i = this.oldDistPtr;
            this.oldDistPtr = i + 1;
            Distance++;
            iArr[i] = Distance;
            this.oldDistPtr &= 3;
            this.lastLength = Length;
            this.lastDist = Distance;
            oldCopyString(Distance, Length);
        } else if (Length == 9) {
            this.LCount++;
            oldCopyString(this.lastDist, this.lastLength);
        } else if (Length == 14) {
            this.LCount = 0;
            Length = decodeNum(fgetbits(), 3, DecL2, PosL2) + 5;
            Distance = (fgetbits() >> 1) | 32768;
            faddbits(15);
            this.lastLength = Length;
            this.lastDist = Distance;
            oldCopyString(Distance, Length);
        } else {
            this.LCount = 0;
            int SaveLength = Length;
            Distance = this.oldDist[(this.oldDistPtr - (Length - 9)) & 3];
            Length = decodeNum(fgetbits(), 2, DecL1, PosL1) + 2;
            if (Length == 257 && SaveLength == 10) {
                this.Buf60 ^= 1;
                return;
            }
            if (Distance > 256) {
                Length++;
            }
            if (Distance >= this.MaxDist3) {
                Length++;
            }
            iArr = this.oldDist;
            i = this.oldDistPtr;
            this.oldDistPtr = i + 1;
            iArr[i] = Distance;
            this.oldDistPtr &= 3;
            this.lastLength = Length;
            this.lastDist = Distance;
            oldCopyString(Distance, Length);
        }
    }

    protected void longLZ() {
        int Length;
        int DistancePlace;
        int Distance;
        int[] iArr;
        int Distance2;
        int i;
        int NewDistancePlace;
        this.NumHuf = 0;
        this.Nlzb += 16;
        if (this.Nlzb > 255) {
            this.Nlzb = 144;
            this.Nhfb >>>= 1;
        }
        int OldAvr2 = this.AvrLn2;
        int BitField = fgetbits();
        if (this.AvrLn2 >= 122) {
            Length = decodeNum(BitField, 3, DecL2, PosL2);
        } else if (this.AvrLn2 >= 64) {
            Length = decodeNum(BitField, 2, DecL1, PosL1);
        } else if (BitField < 256) {
            Length = BitField;
            faddbits(16);
        } else {
            Length = 0;
            while (((BitField << Length) & 32768) == 0) {
                Length++;
            }
            faddbits(Length + 1);
        }
        this.AvrLn2 += Length;
        this.AvrLn2 -= this.AvrLn2 >>> 5;
        BitField = fgetbits();
        if (this.AvrPlcB > 10495) {
            DistancePlace = decodeNum(BitField, 5, DecHf2, PosHf2);
        } else if (this.AvrPlcB > 1791) {
            DistancePlace = decodeNum(BitField, 5, DecHf1, PosHf1);
        } else {
            DistancePlace = decodeNum(BitField, 4, DecHf0, PosHf0);
        }
        this.AvrPlcB += DistancePlace;
        this.AvrPlcB -= this.AvrPlcB >> 8;
        while (true) {
            Distance = this.ChSetB[DistancePlace & 255];
            iArr = this.NToPlB;
            Distance2 = Distance + 1;
            i = Distance & 255;
            NewDistancePlace = iArr[i];
            iArr[i] = NewDistancePlace + 1;
            if ((Distance2 & 255) != 0) {
                break;
            }
            corrHuff(this.ChSetB, this.NToPlB);
        }
        this.ChSetB[DistancePlace] = this.ChSetB[NewDistancePlace];
        this.ChSetB[NewDistancePlace] = Distance2;
        Distance = ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & Distance2) | (fgetbits() >>> 8)) >>> 1;
        faddbits(7);
        int OldAvr3 = this.AvrLn3;
        if (!(Length == 1 || Length == 4)) {
            if (Length == 0 && Distance <= this.MaxDist3) {
                this.AvrLn3++;
                this.AvrLn3 -= this.AvrLn3 >> 8;
            } else if (this.AvrLn3 > 0) {
                this.AvrLn3--;
            }
        }
        Length += 3;
        if (Distance >= this.MaxDist3) {
            Length++;
        }
        if (Distance <= 256) {
            Length += 8;
        }
        if (OldAvr3 > 176 || (this.AvrPlc >= 10752 && OldAvr2 < 64)) {
            this.MaxDist3 = 32512;
        } else {
            this.MaxDist3 = 8193;
        }
        iArr = this.oldDist;
        i = this.oldDistPtr;
        this.oldDistPtr = i + 1;
        iArr[i] = Distance;
        this.oldDistPtr &= 3;
        this.lastLength = Length;
        this.lastDist = Distance;
        oldCopyString(Distance, Length);
    }

    protected void huffDecode() {
        int BytePlace;
        int Length = 4;
        int BitField = fgetbits();
        if (this.AvrPlc > 30207) {
            BytePlace = decodeNum(BitField, 8, DecHf4, PosHf4);
        } else if (this.AvrPlc > 24063) {
            BytePlace = decodeNum(BitField, 6, DecHf3, PosHf3);
        } else if (this.AvrPlc > 13823) {
            BytePlace = decodeNum(BitField, 5, DecHf2, PosHf2);
        } else if (this.AvrPlc > 3583) {
            BytePlace = decodeNum(BitField, 5, DecHf1, PosHf1);
        } else {
            BytePlace = decodeNum(BitField, 4, DecHf0, PosHf0);
        }
        BytePlace &= 255;
        if (this.StMode != 0) {
            if (BytePlace == 0 && BitField > 4095) {
                BytePlace = 256;
            }
            BytePlace--;
            if (BytePlace == -1) {
                BitField = fgetbits();
                faddbits(1);
                if ((32768 & BitField) != 0) {
                    this.StMode = 0;
                    this.NumHuf = 0;
                    return;
                }
                if ((BitField & 16384) == 0) {
                    Length = 3;
                }
                faddbits(1);
                int Distance = (decodeNum(fgetbits(), 5, DecHf2, PosHf2) << 5) | (fgetbits() >>> 11);
                faddbits(5);
                oldCopyString(Distance, Length);
                return;
            }
        }
        int i = this.NumHuf;
        this.NumHuf = i + 1;
        if (i >= 16 && this.FlagsCnt == 0) {
            this.StMode = 1;
        }
        this.AvrPlc += BytePlace;
        this.AvrPlc -= this.AvrPlc >>> 8;
        this.Nhfb += 16;
        if (this.Nhfb > 255) {
            this.Nhfb = 144;
            this.Nlzb >>>= 1;
        }
        byte[] bArr = this.window;
        int i2 = this.unpPtr;
        this.unpPtr = i2 + 1;
        bArr[i2] = (byte) (this.ChSet[BytePlace] >>> 8);
        this.destUnpSize--;
        while (true) {
            int CurByte = this.ChSet[BytePlace];
            int[] iArr = this.NToPl;
            int CurByte2 = CurByte + 1;
            i2 = CurByte & 255;
            int NewBytePlace = iArr[i2];
            iArr[i2] = NewBytePlace + 1;
            if ((CurByte2 & 255) > 161) {
                corrHuff(this.ChSet, this.NToPl);
            } else {
                this.ChSet[BytePlace] = this.ChSet[NewBytePlace];
                this.ChSet[NewBytePlace] = CurByte2;
                return;
            }
        }
    }

    protected void getFlagsBuf() {
        int FlagsPlace = decodeNum(fgetbits(), 5, DecHf2, PosHf2);
        while (true) {
            int Flags = this.ChSetC[FlagsPlace];
            this.FlagBuf = Flags >>> 8;
            int[] iArr = this.NToPlC;
            int Flags2 = Flags + 1;
            int i = Flags & 255;
            int NewFlagsPlace = iArr[i];
            iArr[i] = NewFlagsPlace + 1;
            if ((Flags2 & 255) != 0) {
                this.ChSetC[FlagsPlace] = this.ChSetC[NewFlagsPlace];
                this.ChSetC[NewFlagsPlace] = Flags2;
                return;
            }
            corrHuff(this.ChSetC, this.NToPlC);
        }
    }

    protected void oldUnpInitData(boolean Solid) {
        if (!Solid) {
            this.Buf60 = 0;
            this.NumHuf = 0;
            this.AvrLn3 = 0;
            this.AvrLn2 = 0;
            this.AvrLn1 = 0;
            this.AvrPlcB = 0;
            this.AvrPlc = 13568;
            this.MaxDist3 = 8193;
            this.Nlzb = 128;
            this.Nhfb = 128;
        }
        this.FlagsCnt = 0;
        this.FlagBuf = 0;
        this.StMode = 0;
        this.LCount = 0;
        this.readTop = 0;
    }

    protected void initHuff() {
        for (int I = 0; I < 256; I++) {
            int[] iArr = this.Place;
            int[] iArr2 = this.PlaceA;
            this.PlaceB[I] = I;
            iArr2[I] = I;
            iArr[I] = I;
            this.PlaceC[I] = ((I ^ -1) + 1) & 255;
            iArr = this.ChSet;
            int i = I << 8;
            this.ChSetB[I] = i;
            iArr[I] = i;
            this.ChSetA[I] = I;
            this.ChSetC[I] = (((I ^ -1) + 1) & 255) << 8;
        }
        Arrays.fill(this.NToPl, 0);
        Arrays.fill(this.NToPlB, 0);
        Arrays.fill(this.NToPlC, 0);
        corrHuff(this.ChSetB, this.NToPlB);
    }

    protected void corrHuff(int[] CharSet, int[] NumToPlace) {
        int I;
        int pos = 0;
        for (I = 7; I >= 0; I--) {
            int J = 0;
            while (J < 32) {
                CharSet[pos] = (CharSet[pos] & InputDeviceCompat.SOURCE_ANY) | I;
                J++;
                pos++;
            }
        }
        Arrays.fill(NumToPlace, 0);
        for (I = 6; I >= 0; I--) {
            NumToPlace[I] = (7 - I) * 32;
        }
    }

    protected void oldCopyString(int Distance, int Length) {
        this.destUnpSize -= (long) Length;
        int Length2 = Length;
        while (true) {
            Length = Length2 - 1;
            if (Length2 != 0) {
                this.window[this.unpPtr] = this.window[(this.unpPtr - Distance) & Compress.MAXWINMASK];
                this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                Length2 = Length;
            } else {
                return;
            }
        }
    }

    protected int decodeNum(int Num, int StartPos, int[] DecTab, int[] PosTab) {
        Num &= 65520;
        int I = 0;
        while (DecTab[I] <= Num) {
            StartPos++;
            I++;
        }
        faddbits(StartPos);
        return ((Num - (I != 0 ? DecTab[I - 1] : 0)) >>> (16 - StartPos)) + PosTab[StartPos];
    }

    protected void oldUnpWriteBuf() throws IOException {
        if (this.unpPtr != this.wrPtr) {
            this.unpSomeRead = true;
        }
        if (this.unpPtr < this.wrPtr) {
            this.unpIO.unpWrite(this.window, this.wrPtr, (-this.wrPtr) & Compress.MAXWINMASK);
            this.unpIO.unpWrite(this.window, 0, this.unpPtr);
            this.unpAllBuf = true;
        } else {
            this.unpIO.unpWrite(this.window, this.wrPtr, this.unpPtr - this.wrPtr);
        }
        this.wrPtr = this.unpPtr;
    }
}
