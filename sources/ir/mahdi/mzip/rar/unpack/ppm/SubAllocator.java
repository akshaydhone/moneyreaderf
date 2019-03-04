package ir.mahdi.mzip.rar.unpack.ppm;

import java.util.Arrays;

public class SubAllocator {
    static final /* synthetic */ boolean $assertionsDisabled = (!SubAllocator.class.desiredAssertionStatus());
    public static final int FIXED_UNIT_SIZE = 12;
    public static final int N1 = 4;
    public static final int N2 = 4;
    public static final int N3 = 4;
    public static final int N4 = 26;
    public static final int N_INDEXES = 38;
    public static final int UNIT_SIZE = Math.max(PPMContext.size, 12);
    private int fakeUnitsStart;
    private final RarNode[] freeList = new RarNode[38];
    private int freeListPos;
    private int glueCount;
    private byte[] heap;
    private int heapEnd;
    private int heapStart;
    private int hiUnit;
    private int[] indx2Units = new int[38];
    private int loUnit;
    private int pText;
    private int subAllocatorSize;
    private int tempMemBlockPos;
    private RarMemBlock tempRarMemBlock1 = null;
    private RarMemBlock tempRarMemBlock2 = null;
    private RarMemBlock tempRarMemBlock3 = null;
    private RarNode tempRarNode = null;
    private int[] units2Indx = new int[128];
    private int unitsStart;

    public SubAllocator() {
        clean();
    }

    public void clean() {
        this.subAllocatorSize = 0;
    }

    private void insertNode(int p, int indx) {
        RarNode temp = this.tempRarNode;
        temp.setAddress(p);
        temp.setNext(this.freeList[indx].getNext());
        this.freeList[indx].setNext(temp);
    }

    public void incPText() {
        this.pText++;
    }

    private int removeNode(int indx) {
        int retVal = this.freeList[indx].getNext();
        RarNode temp = this.tempRarNode;
        temp.setAddress(retVal);
        this.freeList[indx].setNext(temp.getNext());
        return retVal;
    }

    private int U2B(int NU) {
        return UNIT_SIZE * NU;
    }

    private int MBPtr(int BasePtr, int Items) {
        return U2B(Items) + BasePtr;
    }

    private void splitBlock(int pv, int oldIndx, int newIndx) {
        int uDiff = this.indx2Units[oldIndx] - this.indx2Units[newIndx];
        int p = pv + U2B(this.indx2Units[newIndx]);
        int[] iArr = this.indx2Units;
        int i = this.units2Indx[uDiff - 1];
        if (iArr[i] != uDiff) {
            i--;
            insertNode(p, i);
            i = this.indx2Units[i];
            p += U2B(i);
            uDiff -= i;
        }
        insertNode(p, this.units2Indx[uDiff - 1]);
    }

    public void stopSubAllocator() {
        if (this.subAllocatorSize != 0) {
            this.subAllocatorSize = 0;
            this.heap = null;
            this.heapStart = 1;
            this.tempRarNode = null;
            this.tempRarMemBlock1 = null;
            this.tempRarMemBlock2 = null;
            this.tempRarMemBlock3 = null;
        }
    }

    public int GetAllocatedMemory() {
        return this.subAllocatorSize;
    }

    public boolean startSubAllocator(int SASize) {
        int t = SASize << 20;
        if (this.subAllocatorSize != t) {
            stopSubAllocator();
            int allocSize = ((t / 12) * UNIT_SIZE) + UNIT_SIZE;
            int realAllocSize = (allocSize + 1) + 152;
            this.tempMemBlockPos = realAllocSize;
            realAllocSize += 12;
            this.heap = new byte[realAllocSize];
            this.heapStart = 1;
            this.heapEnd = (this.heapStart + allocSize) - UNIT_SIZE;
            this.subAllocatorSize = t;
            this.freeListPos = this.heapStart + allocSize;
            if ($assertionsDisabled || realAllocSize - this.tempMemBlockPos == 12) {
                int i = 0;
                int pos = this.freeListPos;
                while (i < this.freeList.length) {
                    this.freeList[i] = new RarNode(this.heap);
                    this.freeList[i].setAddress(pos);
                    i++;
                    pos += 4;
                }
                this.tempRarNode = new RarNode(this.heap);
                this.tempRarMemBlock1 = new RarMemBlock(this.heap);
                this.tempRarMemBlock2 = new RarMemBlock(this.heap);
                this.tempRarMemBlock3 = new RarMemBlock(this.heap);
            } else {
                throw new AssertionError(realAllocSize + " " + this.tempMemBlockPos + " " + 12);
            }
        }
        return true;
    }

    private void glueFreeBlocks() {
        int i;
        RarMemBlock s0 = this.tempRarMemBlock1;
        s0.setAddress(this.tempMemBlockPos);
        RarMemBlock p = this.tempRarMemBlock2;
        RarMemBlock p1 = this.tempRarMemBlock3;
        if (this.loUnit != this.hiUnit) {
            this.heap[this.loUnit] = (byte) 0;
        }
        s0.setPrev(s0);
        s0.setNext(s0);
        for (i = 0; i < 38; i++) {
            while (this.freeList[i].getNext() != 0) {
                p.setAddress(removeNode(i));
                p.insertAt(s0);
                p.setStamp(65535);
                p.setNU(this.indx2Units[i]);
            }
        }
        p.setAddress(s0.getNext());
        while (p.getAddress() != s0.getAddress()) {
            p1.setAddress(MBPtr(p.getAddress(), p.getNU()));
            while (p1.getStamp() == 65535 && p.getNU() + p1.getNU() < 65536) {
                p1.remove();
                p.setNU(p.getNU() + p1.getNU());
                p1.setAddress(MBPtr(p.getAddress(), p.getNU()));
            }
            p.setAddress(p.getNext());
        }
        p.setAddress(s0.getNext());
        while (p.getAddress() != s0.getAddress()) {
            p.remove();
            int sz = p.getNU();
            while (sz > 128) {
                insertNode(p.getAddress(), 37);
                sz -= 128;
                p.setAddress(MBPtr(p.getAddress(), 128));
            }
            int[] iArr = this.indx2Units;
            i = this.units2Indx[sz - 1];
            if (iArr[i] != sz) {
                i--;
                int k = sz - this.indx2Units[i];
                insertNode(MBPtr(p.getAddress(), sz - k), k - 1);
            }
            insertNode(p.getAddress(), i);
            p.setAddress(s0.getNext());
        }
    }

    private int allocUnitsRare(int indx) {
        if (this.glueCount == 0) {
            this.glueCount = 255;
            glueFreeBlocks();
            if (this.freeList[indx].getNext() != 0) {
                return removeNode(indx);
            }
        }
        int i = indx;
        do {
            i++;
            if (i == 38) {
                this.glueCount--;
                i = U2B(this.indx2Units[indx]);
                int j = this.indx2Units[indx] * 12;
                if (this.fakeUnitsStart - this.pText <= j) {
                    return 0;
                }
                this.fakeUnitsStart -= j;
                this.unitsStart -= i;
                return this.unitsStart;
            }
        } while (this.freeList[i].getNext() == 0);
        int retVal = removeNode(i);
        splitBlock(retVal, i, indx);
        return retVal;
    }

    public int allocUnits(int NU) {
        int indx = this.units2Indx[NU - 1];
        if (this.freeList[indx].getNext() != 0) {
            return removeNode(indx);
        }
        int retVal = this.loUnit;
        this.loUnit += U2B(this.indx2Units[indx]);
        if (this.loUnit <= this.hiUnit) {
            return retVal;
        }
        this.loUnit -= U2B(this.indx2Units[indx]);
        return allocUnitsRare(indx);
    }

    public int allocContext() {
        if (this.hiUnit != this.loUnit) {
            int i = this.hiUnit - UNIT_SIZE;
            this.hiUnit = i;
            return i;
        } else if (this.freeList[0].getNext() != 0) {
            return removeNode(0);
        } else {
            return allocUnitsRare(0);
        }
    }

    public int expandUnits(int oldPtr, int OldNU) {
        int i0 = this.units2Indx[OldNU - 1];
        if (i0 == this.units2Indx[(OldNU - 1) + 1]) {
            return oldPtr;
        }
        int ptr = allocUnits(OldNU + 1);
        if (ptr != 0) {
            System.arraycopy(this.heap, oldPtr, this.heap, ptr, U2B(OldNU));
            insertNode(oldPtr, i0);
        }
        return ptr;
    }

    public int shrinkUnits(int oldPtr, int oldNU, int newNU) {
        int i0 = this.units2Indx[oldNU - 1];
        int i1 = this.units2Indx[newNU - 1];
        if (i0 == i1) {
            return oldPtr;
        }
        if (this.freeList[i1].getNext() != 0) {
            int ptr = removeNode(i1);
            System.arraycopy(this.heap, oldPtr, this.heap, ptr, U2B(newNU));
            insertNode(oldPtr, i0);
            return ptr;
        }
        splitBlock(oldPtr, i0, i1);
        return oldPtr;
    }

    public void freeUnits(int ptr, int OldNU) {
        insertNode(ptr, this.units2Indx[OldNU - 1]);
    }

    public int getFakeUnitsStart() {
        return this.fakeUnitsStart;
    }

    public void setFakeUnitsStart(int fakeUnitsStart) {
        this.fakeUnitsStart = fakeUnitsStart;
    }

    public int getHeapEnd() {
        return this.heapEnd;
    }

    public int getPText() {
        return this.pText;
    }

    public void setPText(int text) {
        this.pText = text;
    }

    public void decPText(int dPText) {
        setPText(getPText() - dPText);
    }

    public int getUnitsStart() {
        return this.unitsStart;
    }

    public void setUnitsStart(int unitsStart) {
        this.unitsStart = unitsStart;
    }

    public void initSubAllocator() {
        Arrays.fill(this.heap, this.freeListPos, this.freeListPos + sizeOfFreeList(), (byte) 0);
        this.pText = this.heapStart;
        int size2 = (((this.subAllocatorSize / 8) / 12) * 7) * 12;
        int realSize2 = (size2 / 12) * UNIT_SIZE;
        int size1 = this.subAllocatorSize - size2;
        int realSize1 = ((size1 / 12) * UNIT_SIZE) + (size1 % 12);
        this.hiUnit = this.heapStart + this.subAllocatorSize;
        int i = this.heapStart + realSize1;
        this.unitsStart = i;
        this.loUnit = i;
        this.fakeUnitsStart = this.heapStart + size1;
        this.hiUnit = this.loUnit + realSize2;
        int i2 = 0;
        int k = 1;
        while (i2 < 4) {
            this.indx2Units[i2] = k & 255;
            i2++;
            k++;
        }
        k++;
        while (i2 < 8) {
            this.indx2Units[i2] = k & 255;
            i2++;
            k += 2;
        }
        k++;
        while (i2 < 12) {
            this.indx2Units[i2] = k & 255;
            i2++;
            k += 3;
        }
        k++;
        while (i2 < 38) {
            this.indx2Units[i2] = k & 255;
            i2++;
            k += 4;
        }
        this.glueCount = 0;
        i2 = 0;
        for (k = 0; k < 128; k++) {
            if (this.indx2Units[i2] < k + 1) {
                i = 1;
            } else {
                i = 0;
            }
            i2 += i;
            this.units2Indx[k] = i2 & 255;
        }
    }

    private int sizeOfFreeList() {
        return this.freeList.length * 4;
    }

    public byte[] getHeap() {
        return this.heap;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SubAllocator[");
        buffer.append("\n  subAllocatorSize=");
        buffer.append(this.subAllocatorSize);
        buffer.append("\n  glueCount=");
        buffer.append(this.glueCount);
        buffer.append("\n  heapStart=");
        buffer.append(this.heapStart);
        buffer.append("\n  loUnit=");
        buffer.append(this.loUnit);
        buffer.append("\n  hiUnit=");
        buffer.append(this.hiUnit);
        buffer.append("\n  pText=");
        buffer.append(this.pText);
        buffer.append("\n  unitsStart=");
        buffer.append(this.unitsStart);
        buffer.append("\n]");
        return buffer.toString();
    }
}
