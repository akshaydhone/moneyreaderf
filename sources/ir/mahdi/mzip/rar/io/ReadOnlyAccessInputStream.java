package ir.mahdi.mzip.rar.io;

import java.io.IOException;
import java.io.InputStream;

public class ReadOnlyAccessInputStream extends InputStream {
    private long curPos;
    private final long endPos;
    private IReadOnlyAccess file;
    private final long startPos;

    public ReadOnlyAccessInputStream(IReadOnlyAccess file, long startPos, long endPos) throws IOException {
        this.file = file;
        this.startPos = startPos;
        this.curPos = startPos;
        this.endPos = endPos;
        file.setPosition(this.curPos);
    }

    public int read() throws IOException {
        if (this.curPos == this.endPos) {
            return -1;
        }
        int b = this.file.read();
        this.curPos++;
        return b;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (this.curPos == this.endPos) {
            return -1;
        }
        int bytesRead = this.file.read(b, off, (int) Math.min((long) len, this.endPos - this.curPos));
        this.curPos += (long) bytesRead;
        return bytesRead;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
}
