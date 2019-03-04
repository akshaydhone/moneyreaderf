package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;
import java.util.Calendar;
import java.util.Date;

public class FileHeader extends BlockHeader {
    private static final byte NEWLHD_SIZE = (byte) 32;
    private static final byte SALT_SIZE = (byte) 8;
    private Date aTime;
    private Date arcTime;
    private Date cTime;
    private int fileAttr;
    private final int fileCRC;
    private String fileName;
    private final byte[] fileNameBytes;
    private String fileNameW;
    private final int fileTime;
    private long fullPackSize;
    private long fullUnpackSize;
    private int highPackSize;
    private int highUnpackSize;
    private final HostSystem hostOS;
    private Date mTime;
    private short nameSize;
    private int recoverySectors = -1;
    private final byte[] salt = new byte[8];
    private byte[] subData;
    private int subFlags;
    private byte unpMethod;
    private long unpSize;
    private byte unpVersion;

    public FileHeader(BlockHeader bh, byte[] fileHeader) {
        int i;
        short s = BaseBlock.LHD_EXTTIME;
        super(bh);
        this.unpSize = Raw.readIntLittleEndianAsLong(fileHeader, 0);
        int position = 0 + 4;
        this.hostOS = HostSystem.findHostSystem(fileHeader[4]);
        position++;
        this.fileCRC = Raw.readIntLittleEndian(fileHeader, position);
        position += 4;
        this.fileTime = Raw.readIntLittleEndian(fileHeader, position);
        position += 4;
        this.unpVersion = (byte) (this.unpVersion | (fileHeader[13] & 255));
        position++;
        this.unpMethod = (byte) (this.unpMethod | (fileHeader[14] & 255));
        position++;
        this.nameSize = Raw.readShortLittleEndian(fileHeader, position);
        position += 2;
        this.fileAttr = Raw.readIntLittleEndian(fileHeader, position);
        position += 4;
        if (isLargeBlock()) {
            this.highPackSize = Raw.readIntLittleEndian(fileHeader, position);
            position += 4;
            this.highUnpackSize = Raw.readIntLittleEndian(fileHeader, position);
            position += 4;
        } else {
            this.highPackSize = 0;
            this.highUnpackSize = 0;
            if (this.unpSize == -1) {
                this.unpSize = -1;
                this.highUnpackSize = Integer.MAX_VALUE;
            }
        }
        this.fullPackSize |= (long) this.highPackSize;
        this.fullPackSize <<= 32;
        this.fullPackSize |= (long) getPackSize();
        this.fullUnpackSize |= (long) this.highUnpackSize;
        this.fullUnpackSize <<= 32;
        this.fullUnpackSize += this.unpSize;
        if (this.nameSize <= BaseBlock.LHD_EXTTIME) {
            s = this.nameSize;
        }
        this.nameSize = s;
        this.fileNameBytes = new byte[this.nameSize];
        for (short i2 = (short) 0; i2 < this.nameSize; i2++) {
            this.fileNameBytes[i2] = fileHeader[position];
            position++;
        }
        if (isFileHeader()) {
            if (isUnicode()) {
                short length = (short) 0;
                this.fileName = "";
                this.fileNameW = "";
                while (length < this.fileNameBytes.length && this.fileNameBytes[length] != (byte) 0) {
                    length++;
                }
                byte[] name = new byte[length];
                System.arraycopy(this.fileNameBytes, 0, name, 0, name.length);
                this.fileName = new String(name);
                if (length != this.nameSize) {
                    this.fileNameW = FileNameDecoder.decode(this.fileNameBytes, length + 1);
                }
            } else {
                this.fileName = new String(this.fileNameBytes);
                this.fileNameW = "";
            }
        }
        if (UnrarHeadertype.NewSubHeader.equals(this.headerType)) {
            int datasize = (this.headerSize - 32) - this.nameSize;
            if (hasSalt()) {
                datasize -= 8;
            }
            if (datasize > 0) {
                this.subData = new byte[datasize];
                for (i = 0; i < datasize; i++) {
                    this.subData[i] = fileHeader[position];
                    position++;
                }
            }
            if (NewSubHeaderType.SUBHEAD_TYPE_RR.byteEquals(this.fileNameBytes)) {
                this.recoverySectors = ((this.subData[8] + (this.subData[9] << 8)) + (this.subData[10] << 16)) + (this.subData[11] << 24);
            }
        }
        if (hasSalt()) {
            for (i = 0; i < 8; i++) {
                this.salt[i] = fileHeader[position];
                position++;
            }
        }
        this.mTime = getDateDos(this.fileTime);
    }

    public void print() {
        super.print();
        StringBuilder str = new StringBuilder();
        str.append("unpSize: " + getUnpSize());
        str.append("\nHostOS: " + this.hostOS.name());
        str.append("\nMDate: " + this.mTime);
        str.append("\nFileName: " + getFileNameString());
        str.append("\nunpMethod: " + Integer.toHexString(getUnpMethod()));
        str.append("\nunpVersion: " + Integer.toHexString(getUnpVersion()));
        str.append("\nfullpackedsize: " + getFullPackSize());
        str.append("\nfullunpackedsize: " + getFullUnpackSize());
        str.append("\nisEncrypted: " + isEncrypted());
        str.append("\nisfileHeader: " + isFileHeader());
        str.append("\nisSolid: " + isSolid());
        str.append("\nisSplitafter: " + isSplitAfter());
        str.append("\nisSplitBefore:" + isSplitBefore());
        str.append("\nunpSize: " + getUnpSize());
        str.append("\ndataSize: " + getDataSize());
        str.append("\nisUnicode: " + isUnicode());
        str.append("\nhasVolumeNumber: " + hasVolumeNumber());
        str.append("\nhasArchiveDataCRC: " + hasArchiveDataCRC());
        str.append("\nhasSalt: " + hasSalt());
        str.append("\nhasEncryptVersions: " + hasEncryptVersion());
        str.append("\nisSubBlock: " + isSubBlock());
    }

    private Date getDateDos(int time) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, (time >>> 25) + 1980);
        cal.set(2, ((time >>> 21) & 15) - 1);
        cal.set(5, (time >>> 16) & 31);
        cal.set(11, (time >>> 11) & 31);
        cal.set(12, (time >>> 5) & 63);
        cal.set(13, (time & 31) * 2);
        return cal.getTime();
    }

    public Date getArcTime() {
        return this.arcTime;
    }

    public void setArcTime(Date arcTime) {
        this.arcTime = arcTime;
    }

    public Date getATime() {
        return this.aTime;
    }

    public void setATime(Date time) {
        this.aTime = time;
    }

    public Date getCTime() {
        return this.cTime;
    }

    public void setCTime(Date time) {
        this.cTime = time;
    }

    public int getFileAttr() {
        return this.fileAttr;
    }

    public void setFileAttr(int fileAttr) {
        this.fileAttr = fileAttr;
    }

    public int getFileCRC() {
        return this.fileCRC;
    }

    public byte[] getFileNameByteArray() {
        return this.fileNameBytes;
    }

    public String getFileNameString() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameW() {
        return this.fileNameW;
    }

    public void setFileNameW(String fileNameW) {
        this.fileNameW = fileNameW;
    }

    public int getHighPackSize() {
        return this.highPackSize;
    }

    public int getHighUnpackSize() {
        return this.highUnpackSize;
    }

    public HostSystem getHostOS() {
        return this.hostOS;
    }

    public Date getMTime() {
        return this.mTime;
    }

    public void setMTime(Date time) {
        this.mTime = time;
    }

    public short getNameSize() {
        return this.nameSize;
    }

    public int getRecoverySectors() {
        return this.recoverySectors;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getSubData() {
        return this.subData;
    }

    public int getSubFlags() {
        return this.subFlags;
    }

    public byte getUnpMethod() {
        return this.unpMethod;
    }

    public long getUnpSize() {
        return this.unpSize;
    }

    public byte getUnpVersion() {
        return this.unpVersion;
    }

    public long getFullPackSize() {
        return this.fullPackSize;
    }

    public long getFullUnpackSize() {
        return this.fullUnpackSize;
    }

    public String toString() {
        return super.toString();
    }

    public boolean isSplitAfter() {
        return (this.flags & 2) != 0;
    }

    public boolean isSplitBefore() {
        return (this.flags & 1) != 0;
    }

    public boolean isSolid() {
        return (this.flags & 16) != 0;
    }

    public boolean isEncrypted() {
        return (this.flags & 4) != 0;
    }

    public boolean isUnicode() {
        return (this.flags & 512) != 0;
    }

    public boolean isFileHeader() {
        return UnrarHeadertype.FileHeader.equals(this.headerType);
    }

    public boolean hasSalt() {
        return (this.flags & 1024) != 0;
    }

    public boolean isLargeBlock() {
        return (this.flags & 256) != 0;
    }

    public boolean isDirectory() {
        return (this.flags & 224) == 224;
    }
}
