package ir.mahdi.mzip.rar.io;

import java.io.EOFException;
import java.io.IOException;

public class ReadOnlyAccessByteArray implements IReadOnlyAccess {
    private byte[] file;
    private int positionInFile;

    public ReadOnlyAccessByteArray(byte[] file) {
        if (file == null) {
            throw new NullPointerException("file must not be null!!");
        }
        this.file = file;
        this.positionInFile = 0;
    }

    public long getPosition() throws IOException {
        return (long) this.positionInFile;
    }

    public void setPosition(long pos) throws IOException {
        if (pos >= ((long) this.file.length) || pos < 0) {
            throw new EOFException();
        }
        this.positionInFile = (int) pos;
    }

    public int read() throws IOException {
        byte[] bArr = this.file;
        int i = this.positionInFile;
        this.positionInFile = i + 1;
        return bArr[i];
    }

    public int read(byte[] buffer, int off, int count) throws IOException {
        int read = Math.min(count, this.file.length - this.positionInFile);
        System.arraycopy(this.file, this.positionInFile, buffer, off, read);
        this.positionInFile += read;
        return read;
    }

    public int readFully(byte[] buffer, int count) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("buffer must not be null");
        } else if (count == 0) {
            throw new IllegalArgumentException("cannot read 0 bytes ;-)");
        } else {
            int read = Math.min(count, (this.file.length - this.positionInFile) - 1);
            System.arraycopy(this.file, this.positionInFile, buffer, 0, read);
            this.positionInFile += read;
            return read;
        }
    }

    public void close() throws IOException {
    }
}
