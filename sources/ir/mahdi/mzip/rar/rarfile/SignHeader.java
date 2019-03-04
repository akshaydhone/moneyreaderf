package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class SignHeader extends BaseBlock {
    public static final short signHeaderSize = (short) 8;
    private short arcNameSize = (short) 0;
    private int creationTime = 0;
    private short userNameSize = (short) 0;

    public SignHeader(BaseBlock bb, byte[] signHeader) {
        super(bb);
        this.creationTime = Raw.readIntLittleEndian(signHeader, 0);
        int pos = 0 + 4;
        this.arcNameSize = Raw.readShortLittleEndian(signHeader, pos);
        this.userNameSize = Raw.readShortLittleEndian(signHeader, pos + 2);
    }

    public short getArcNameSize() {
        return this.arcNameSize;
    }

    public int getCreationTime() {
        return this.creationTime;
    }

    public short getUserNameSize() {
        return this.userNameSize;
    }
}
