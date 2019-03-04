package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class RarNode extends Pointer {
    public static final int size = 4;
    private int next;

    public RarNode(byte[] mem) {
        super(mem);
    }

    public int getNext() {
        if (this.mem != null) {
            this.next = Raw.readIntLittleEndian(this.mem, this.pos);
        }
        return this.next;
    }

    public void setNext(int next) {
        this.next = next;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos, next);
        }
    }

    public void setNext(RarNode next) {
        setNext(next.getAddress());
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("State[");
        buffer.append("\n  pos=");
        buffer.append(this.pos);
        buffer.append("\n  size=");
        buffer.append(4);
        buffer.append("\n  next=");
        buffer.append(getNext());
        buffer.append("\n]");
        return buffer.toString();
    }
}
