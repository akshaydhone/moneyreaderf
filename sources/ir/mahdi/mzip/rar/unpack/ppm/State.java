package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class State extends Pointer {
    public static final int size = 6;

    public State(byte[] mem) {
        super(mem);
    }

    public static void ppmdSwap(State ptr1, State ptr2) {
        byte[] mem1 = ptr1.mem;
        byte[] mem2 = ptr2.mem;
        int i = 0;
        int pos1 = ptr1.pos;
        int pos2 = ptr2.pos;
        while (i < 6) {
            byte temp = mem1[pos1];
            mem1[pos1] = mem2[pos2];
            mem2[pos2] = temp;
            i++;
            pos1++;
            pos2++;
        }
    }

    public State init(byte[] mem) {
        this.mem = mem;
        this.pos = 0;
        return this;
    }

    public int getSymbol() {
        return this.mem[this.pos] & 255;
    }

    public void setSymbol(int symbol) {
        this.mem[this.pos] = (byte) symbol;
    }

    public int getFreq() {
        return this.mem[this.pos + 1] & 255;
    }

    public void setFreq(int freq) {
        this.mem[this.pos + 1] = (byte) freq;
    }

    public void incFreq(int dFreq) {
        byte[] bArr = this.mem;
        int i = this.pos + 1;
        bArr[i] = (byte) (bArr[i] + dFreq);
    }

    public int getSuccessor() {
        return Raw.readIntLittleEndian(this.mem, this.pos + 2);
    }

    public void setSuccessor(int successor) {
        Raw.writeIntLittleEndian(this.mem, this.pos + 2, successor);
    }

    public void setSuccessor(PPMContext successor) {
        setSuccessor(successor.getAddress());
    }

    public void setValues(StateRef state) {
        setSymbol(state.getSymbol());
        setFreq(state.getFreq());
        setSuccessor(state.getSuccessor());
    }

    public void setValues(State ptr) {
        System.arraycopy(ptr.mem, ptr.pos, this.mem, this.pos, 6);
    }

    public State decAddress() {
        setAddress(this.pos - 6);
        return this;
    }

    public State incAddress() {
        setAddress(this.pos + 6);
        return this;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("State[");
        buffer.append("\n  pos=");
        buffer.append(this.pos);
        buffer.append("\n  size=");
        buffer.append(6);
        buffer.append("\n  symbol=");
        buffer.append(getSymbol());
        buffer.append("\n  freq=");
        buffer.append(getFreq());
        buffer.append("\n  successor=");
        buffer.append(getSuccessor());
        buffer.append("\n]");
        return buffer.toString();
    }
}
