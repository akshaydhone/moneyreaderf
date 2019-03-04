package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class AVHeader extends BaseBlock {
    public static final int avHeaderSize = 7;
    private int avInfoCRC;
    private byte avVersion;
    private byte method;
    private byte unpackVersion;

    public AVHeader(BaseBlock bb, byte[] avHeader) {
        super(bb);
        this.unpackVersion = (byte) (this.unpackVersion | (avHeader[0] & 255));
        int pos = 0 + 1;
        this.method = (byte) (this.method | (avHeader[pos] & 255));
        pos++;
        this.avVersion = (byte) (this.avVersion | (avHeader[pos] & 255));
        this.avInfoCRC = Raw.readIntLittleEndian(avHeader, pos + 1);
    }

    public int getAvInfoCRC() {
        return this.avInfoCRC;
    }

    public byte getAvVersion() {
        return this.avVersion;
    }

    public byte getMethod() {
        return this.method;
    }

    public byte getUnpackVersion() {
        return this.unpackVersion;
    }
}
