package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BlockHeader extends BaseBlock {
    public static final short blockHeaderSize = (short) 4;
    private int dataSize;
    private int packSize;

    public BlockHeader(BlockHeader bh) {
        super((BaseBlock) bh);
        this.packSize = bh.getDataSize();
        this.dataSize = this.packSize;
        this.positionInFile = bh.getPositionInFile();
    }

    public BlockHeader(BaseBlock bb, byte[] blockHeader) {
        super(bb);
        this.packSize = Raw.readIntLittleEndian(blockHeader, 0);
        this.dataSize = this.packSize;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public int getPackSize() {
        return this.packSize;
    }

    public void print() {
        super.print();
        String s = "DataSize: " + getDataSize() + " packSize: " + getPackSize();
    }
}
