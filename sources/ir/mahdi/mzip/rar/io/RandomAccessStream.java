package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;

public final class RandomAccessStream extends InputStream {
    private static final int BLOCK_MASK = 511;
    private static final int BLOCK_SHIFT = 9;
    private static final int BLOCK_SIZE = 512;
    private Vector data;
    private boolean foundEOS;
    private int length;
    private long pointer;
    private RandomAccessFile ras;
    private InputStream src;

    public RandomAccessStream(InputStream inputstream) {
        this.pointer = 0;
        this.data = new Vector();
        this.length = 0;
        this.foundEOS = false;
        this.src = inputstream;
    }

    public RandomAccessStream(RandomAccessFile ras) {
        this.ras = ras;
    }

    public int getFilePointer() throws IOException {
        if (this.ras != null) {
            return (int) this.ras.getFilePointer();
        }
        return (int) this.pointer;
    }

    public long getLongFilePointer() throws IOException {
        if (this.ras != null) {
            return this.ras.getFilePointer();
        }
        return this.pointer;
    }

    public int read() throws IOException {
        if (this.ras != null) {
            return this.ras.read();
        }
        long l = this.pointer + 1;
        if (readUntil(l) < l) {
            return -1;
        }
        byte[] abyte0 = (byte[]) this.data.elementAt((int) (this.pointer >> 9));
        long j = this.pointer;
        this.pointer = j + 1;
        return abyte0[(int) (j & 511)] & 255;
    }

    public int read(byte[] bytes, int off, int len) throws IOException {
        if (bytes == null) {
            throw new NullPointerException();
        } else if (this.ras != null) {
            return this.ras.read(bytes, off, len);
        } else {
            if (off < 0 || len < 0 || off + len > bytes.length) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            } else {
                if (readUntil(this.pointer + ((long) len)) <= this.pointer) {
                    return -1;
                }
                byte[] abyte1 = (byte[]) this.data.elementAt((int) (this.pointer >> 9));
                int k = Math.min(len, 512 - ((int) (this.pointer & 511)));
                System.arraycopy(abyte1, (int) (this.pointer & 511), bytes, off, k);
                this.pointer += (long) k;
                return k;
            }
        }
    }

    public final void readFully(byte[] bytes) throws IOException {
        readFully(bytes, bytes.length);
    }

    public final void readFully(byte[] bytes, int len) throws IOException {
        int read = 0;
        do {
            int l = read(bytes, read, len - read);
            if (l >= 0) {
                read += l;
            } else {
                return;
            }
        } while (read < len);
    }

    private long readUntil(long l) throws IOException {
        if (l < ((long) this.length)) {
            return l;
        }
        if (this.foundEOS) {
            return (long) this.length;
        }
        int i = (int) (l >> 9);
        for (int k = this.length >> 9; k <= i; k++) {
            byte[] abyte0 = new byte[512];
            this.data.addElement(abyte0);
            int i1 = 512;
            int j1 = 0;
            while (i1 > 0) {
                int k1 = this.src.read(abyte0, j1, i1);
                if (k1 == -1) {
                    this.foundEOS = true;
                    return (long) this.length;
                }
                j1 += k1;
                i1 -= k1;
                this.length += k1;
            }
        }
        return (long) this.length;
    }

    public void seek(long loc) throws IOException {
        if (this.ras != null) {
            this.ras.seek(loc);
        } else if (loc < 0) {
            this.pointer = 0;
        } else {
            this.pointer = loc;
        }
    }

    public void seek(int loc) throws IOException {
        long lloc = ((long) loc) & InternalZipConstants.ZIP_64_LIMIT;
        if (this.ras != null) {
            this.ras.seek(lloc);
        } else if (lloc < 0) {
            this.pointer = 0;
        } else {
            this.pointer = lloc;
        }
    }

    public final int readInt() throws IOException {
        int i = read();
        int j = read();
        int k = read();
        int l = read();
        if ((((i | j) | k) | l) >= 0) {
            return (((i << 24) + (j << 16)) + (k << 8)) + l;
        }
        throw new EOFException();
    }

    public final long readLong() throws IOException {
        return (((long) readInt()) << 32) + (((long) readInt()) & InternalZipConstants.ZIP_64_LIMIT);
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final short readShort() throws IOException {
        int i = read();
        int j = read();
        if ((i | j) >= 0) {
            return (short) ((i << 8) + j);
        }
        throw new EOFException();
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void close() throws IOException {
        if (this.ras != null) {
            this.ras.close();
            return;
        }
        this.data.removeAllElements();
        this.src.close();
    }
}
