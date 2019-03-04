package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MainHeader extends BaseBlock {
    public static final short mainHeaderSize = (short) 6;
    public static final short mainHeaderSizeWithEnc = (short) 7;
    private byte encryptVersion;
    private short highPosAv;
    private int posAv;

    public MainHeader(BaseBlock bb, byte[] mainHeader) {
        super(bb);
        this.highPosAv = Raw.readShortLittleEndian(mainHeader, 0);
        int pos = 0 + 2;
        this.posAv = Raw.readIntLittleEndian(mainHeader, pos);
        pos += 4;
        if (hasEncryptVersion()) {
            this.encryptVersion = (byte) (this.encryptVersion | (mainHeader[pos] & 255));
        }
    }

    public boolean hasArchCmt() {
        return (this.flags & 2) != 0;
    }

    public byte getEncryptVersion() {
        return this.encryptVersion;
    }

    public short getHighPosAv() {
        return this.highPosAv;
    }

    public int getPosAv() {
        return this.posAv;
    }

    public boolean isEncrypted() {
        return (this.flags & 128) != 0;
    }

    public boolean isMultiVolume() {
        return (this.flags & 1) != 0;
    }

    public boolean isFirstVolume() {
        return (this.flags & 256) != 0;
    }

    public void print() {
        super.print();
        StringBuilder str = new StringBuilder();
        str.append("posav: " + getPosAv());
        str.append("\nhighposav: " + getHighPosAv());
        str.append("\nhasencversion: " + hasEncryptVersion() + (hasEncryptVersion() ? Byte.valueOf(getEncryptVersion()) : ""));
        str.append("\nhasarchcmt: " + hasArchCmt());
        str.append("\nisEncrypted: " + isEncrypted());
        str.append("\nisMultivolume: " + isMultiVolume());
        str.append("\nisFirstvolume: " + isFirstVolume());
        str.append("\nisSolid: " + isSolid());
        str.append("\nisLocked: " + isLocked());
        str.append("\nisProtected: " + isProtected());
        str.append("\nisAV: " + isAV());
    }

    public boolean isSolid() {
        return (this.flags & 8) != 0;
    }

    public boolean isLocked() {
        return (this.flags & 4) != 0;
    }

    public boolean isProtected() {
        return (this.flags & 64) != 0;
    }

    public boolean isAV() {
        return (this.flags & 32) != 0;
    }

    public boolean isNewNumbering() {
        return (this.flags & 16) != 0;
    }
}
