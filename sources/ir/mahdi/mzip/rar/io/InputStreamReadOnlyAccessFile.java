package ir.mahdi.mzip.rar.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamReadOnlyAccessFile implements IReadOnlyAccess {
    private RandomAccessStream is;

    public InputStreamReadOnlyAccessFile(InputStream is) {
        this.is = new RandomAccessStream(new BufferedInputStream(is));
    }

    public long getPosition() throws IOException {
        return this.is.getLongFilePointer();
    }

    public void setPosition(long pos) throws IOException {
        this.is.seek(pos);
    }

    public int read() throws IOException {
        return this.is.read();
    }

    public int read(byte[] buffer, int off, int count) throws IOException {
        return this.is.read(buffer, off, count);
    }

    public int readFully(byte[] buffer, int count) throws IOException {
        this.is.readFully(buffer, count);
        return count;
    }

    public void close() throws IOException {
        this.is.close();
    }
}
