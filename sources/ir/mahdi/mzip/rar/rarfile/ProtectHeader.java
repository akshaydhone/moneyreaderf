package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class ProtectHeader extends BlockHeader {
    public static final int protectHeaderSize = 8;
    private byte mark;
    private short recSectors;
    private int totalBlocks;
    private byte version;

    public ProtectHeader(BlockHeader bh, byte[] protectHeader) {
        super(bh);
        this.version = (byte) (this.version | (protectHeader[0] & 255));
        this.recSectors = Raw.readShortLittleEndian(protectHeader, 0);
        int pos = 0 + 2;
        this.totalBlocks = Raw.readIntLittleEndian(protectHeader, pos);
        this.mark = (byte) (this.mark | (protectHeader[pos + 4] & 255));
    }

    public byte getMark() {
        return this.mark;
    }

    public short getRecSectors() {
        return this.recSectors;
    }

    public int getTotalBlocks() {
        return this.totalBlocks;
    }

    public byte getVersion() {
        return this.version;
    }
}
