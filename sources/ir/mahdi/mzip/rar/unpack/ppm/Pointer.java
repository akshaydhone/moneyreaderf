package ir.mahdi.mzip.rar.unpack.ppm;

public abstract class Pointer {
    static final /* synthetic */ boolean $assertionsDisabled = (!Pointer.class.desiredAssertionStatus());
    protected byte[] mem;
    protected int pos;

    public Pointer(byte[] mem) {
        this.mem = mem;
    }

    public int getAddress() {
        if ($assertionsDisabled || this.mem != null) {
            return this.pos;
        }
        throw new AssertionError();
    }

    public void setAddress(int pos) {
        if (!$assertionsDisabled && this.mem == null) {
            throw new AssertionError();
        } else if ($assertionsDisabled || (pos >= 0 && pos < this.mem.length)) {
            this.pos = pos;
        } else {
            throw new AssertionError(pos);
        }
    }
}
