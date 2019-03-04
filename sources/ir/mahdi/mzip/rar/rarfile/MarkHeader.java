package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MarkHeader extends BaseBlock {
    private boolean oldFormat = false;

    public MarkHeader(BaseBlock bb) {
        super(bb);
    }

    public boolean isValid() {
        if (getHeadCRC() == (short) 24914 && getHeaderType() == UnrarHeadertype.MarkHeader && getFlags() == (short) 6689 && getHeaderSize() == (short) 7) {
            return true;
        }
        return false;
    }

    public boolean isSignature() {
        byte[] d = new byte[7];
        Raw.writeShortLittleEndian(d, 0, this.headCRC);
        d[2] = this.headerType;
        Raw.writeShortLittleEndian(d, 3, this.flags);
        Raw.writeShortLittleEndian(d, 5, this.headerSize);
        if (d[0] != (byte) 82) {
            return false;
        }
        if (d[1] == (byte) 69 && d[2] == (byte) 126 && d[3] == (byte) 94) {
            this.oldFormat = true;
            return true;
        } else if (d[1] != (byte) 97 || d[2] != (byte) 114 || d[3] != (byte) 33 || d[4] != (byte) 26 || d[5] != (byte) 7 || d[6] != (byte) 0) {
            return false;
        } else {
            this.oldFormat = false;
            return true;
        }
    }

    public boolean isOldFormat() {
        return this.oldFormat;
    }

    public void print() {
        super.print();
    }
}
