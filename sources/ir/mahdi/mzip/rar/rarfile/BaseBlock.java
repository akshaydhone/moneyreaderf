package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BaseBlock {
    public static final short BaseBlockSize = (short) 7;
    public static final short EARC_DATACRC = (short) 2;
    public static final short EARC_NEXT_VOLUME = (short) 1;
    public static final short EARC_REVSPACE = (short) 4;
    public static final short EARC_VOLNUMBER = (short) 8;
    public static final short LHD_COMMENT = (short) 8;
    public static final short LHD_DIRECTORY = (short) 224;
    public static final short LHD_EXTFLAGS = (short) 8192;
    public static final short LHD_EXTTIME = (short) 4096;
    public static final short LHD_LARGE = (short) 256;
    public static final short LHD_PASSWORD = (short) 4;
    public static final short LHD_SALT = (short) 1024;
    public static final short LHD_SOLID = (short) 16;
    public static final short LHD_SPLIT_AFTER = (short) 2;
    public static final short LHD_SPLIT_BEFORE = (short) 1;
    public static final short LHD_UNICODE = (short) 512;
    public static final short LHD_VERSION = (short) 2048;
    public static final short LHD_WINDOW1024 = (short) 128;
    public static final short LHD_WINDOW128 = (short) 32;
    public static final short LHD_WINDOW2048 = (short) 160;
    public static final short LHD_WINDOW256 = (short) 64;
    public static final short LHD_WINDOW4096 = (short) 192;
    public static final short LHD_WINDOW512 = (short) 96;
    public static final short LHD_WINDOW64 = (short) 0;
    public static final short LHD_WINDOWMASK = (short) 224;
    public static final short LONG_BLOCK = Short.MIN_VALUE;
    public static final short MHD_AV = (short) 32;
    public static final short MHD_COMMENT = (short) 2;
    public static final short MHD_ENCRYPTVER = (short) 512;
    public static final short MHD_FIRSTVOLUME = (short) 256;
    public static final short MHD_LOCK = (short) 4;
    public static final short MHD_NEWNUMBERING = (short) 16;
    public static final short MHD_PACK_COMMENT = (short) 16;
    public static final short MHD_PASSWORD = (short) 128;
    public static final short MHD_PROTECT = (short) 64;
    public static final short MHD_SOLID = (short) 8;
    public static final short MHD_VOLUME = (short) 1;
    public static final short SKIP_IF_UNKNOWN = (short) 16384;
    protected short flags = (short) 0;
    protected short headCRC = (short) 0;
    protected short headerSize = (short) 0;
    protected byte headerType = (byte) 0;
    protected long positionInFile;

    public BaseBlock(BaseBlock bb) {
        this.flags = bb.getFlags();
        this.headCRC = bb.getHeadCRC();
        this.headerType = bb.getHeaderType().getHeaderByte();
        this.headerSize = bb.getHeaderSize();
        this.positionInFile = bb.getPositionInFile();
    }

    public BaseBlock(byte[] baseBlockHeader) {
        this.headCRC = Raw.readShortLittleEndian(baseBlockHeader, 0);
        int pos = 0 + 2;
        this.headerType = (byte) (this.headerType | (baseBlockHeader[pos] & 255));
        pos++;
        this.flags = Raw.readShortLittleEndian(baseBlockHeader, pos);
        this.headerSize = Raw.readShortLittleEndian(baseBlockHeader, pos + 2);
    }

    public boolean hasArchiveDataCRC() {
        return (this.flags & 2) != 0;
    }

    public boolean hasVolumeNumber() {
        return (this.flags & 8) != 0;
    }

    public boolean hasEncryptVersion() {
        return (this.flags & 512) != 0;
    }

    public boolean isSubBlock() {
        if (UnrarHeadertype.SubHeader.equals(this.headerType)) {
            return true;
        }
        if (!UnrarHeadertype.NewSubHeader.equals(this.headerType) || (this.flags & 16) == 0) {
            return false;
        }
        return true;
    }

    public long getPositionInFile() {
        return this.positionInFile;
    }

    public void setPositionInFile(long positionInFile) {
        this.positionInFile = positionInFile;
    }

    public short getFlags() {
        return this.flags;
    }

    public short getHeadCRC() {
        return this.headCRC;
    }

    public short getHeaderSize() {
        return this.headerSize;
    }

    public UnrarHeadertype getHeaderType() {
        return UnrarHeadertype.findType(this.headerType);
    }

    public void print() {
        StringBuilder str = new StringBuilder();
        str.append("HeaderType: " + getHeaderType());
        str.append("\nHeadCRC: " + Integer.toHexString(getHeadCRC()));
        str.append("\nFlags: " + Integer.toHexString(getFlags()));
        str.append("\nHeaderSize: " + getHeaderSize());
        str.append("\nPosition in file: " + getPositionInFile());
    }
}
