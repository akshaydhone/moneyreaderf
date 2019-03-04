package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class EAHeader extends SubBlockHeader {
    public static final short EAHeaderSize = (short) 10;
    private int EACRC;
    private byte method;
    private int unpSize;
    private byte unpVer;

    public EAHeader(SubBlockHeader sb, byte[] eahead) {
        super(sb);
        this.unpSize = Raw.readIntLittleEndian(eahead, 0);
        int pos = 0 + 4;
        this.unpVer = (byte) (this.unpVer | (eahead[pos] & 255));
        pos++;
        this.method = (byte) (this.method | (eahead[pos] & 255));
        this.EACRC = Raw.readIntLittleEndian(eahead, pos + 1);
    }

    public int getEACRC() {
        return this.EACRC;
    }

    public byte getMethod() {
        return this.method;
    }

    public int getUnpSize() {
        return this.unpSize;
    }

    public byte getUnpVer() {
        return this.unpVer;
    }

    public void print() {
        super.print();
    }
}
