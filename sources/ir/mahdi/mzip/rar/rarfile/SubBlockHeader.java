package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class SubBlockHeader extends BlockHeader {
    public static final short SubBlockHeaderSize = (short) 3;
    private byte level;
    private short subType;

    public SubBlockHeader(SubBlockHeader sb) {
        super(sb);
        this.subType = sb.getSubType().getSubblocktype();
        this.level = sb.getLevel();
    }

    public SubBlockHeader(BlockHeader bh, byte[] subblock) {
        super(bh);
        this.subType = Raw.readShortLittleEndian(subblock, 0);
        this.level = (byte) (this.level | (subblock[0 + 2] & 255));
    }

    public byte getLevel() {
        return this.level;
    }

    public SubBlockHeaderType getSubType() {
        return SubBlockHeaderType.findSubblockHeaderType(this.subType);
    }

    public void print() {
        super.print();
    }
}
