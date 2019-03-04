package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MacInfoHeader extends SubBlockHeader {
    public static final short MacInfoHeaderSize = (short) 8;
    private int fileCreator;
    private int fileType;

    public MacInfoHeader(SubBlockHeader sb, byte[] macHeader) {
        super(sb);
        this.fileType = Raw.readIntLittleEndian(macHeader, 0);
        this.fileCreator = Raw.readIntLittleEndian(macHeader, 0 + 4);
    }

    public int getFileCreator() {
        return this.fileCreator;
    }

    public void setFileCreator(int fileCreator) {
        this.fileCreator = fileCreator;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public void print() {
        super.print();
    }
}
