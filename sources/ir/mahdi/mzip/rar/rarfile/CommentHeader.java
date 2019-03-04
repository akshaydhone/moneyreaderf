package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class CommentHeader extends BaseBlock {
    public static final short commentHeaderSize = (short) 6;
    private short commCRC;
    private byte unpMethod;
    private short unpSize;
    private byte unpVersion;

    public CommentHeader(BaseBlock bb, byte[] commentHeader) {
        super(bb);
        this.unpSize = Raw.readShortLittleEndian(commentHeader, 0);
        int pos = 0 + 2;
        this.unpVersion = (byte) (this.unpVersion | (commentHeader[pos] & 255));
        pos++;
        this.unpMethod = (byte) (this.unpMethod | (commentHeader[pos] & 255));
        this.commCRC = Raw.readShortLittleEndian(commentHeader, pos + 1);
    }

    public short getCommCRC() {
        return this.commCRC;
    }

    public byte getUnpMethod() {
        return this.unpMethod;
    }

    public short getUnpSize() {
        return this.unpSize;
    }

    public byte getUnpVersion() {
        return this.unpVersion;
    }
}
