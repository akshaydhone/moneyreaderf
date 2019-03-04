package ir.mahdi.mzip.rar.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.util.InternalZipConstants;

public class ReadOnlyAccessFile extends RandomAccessFile implements IReadOnlyAccess {
    static final /* synthetic */ boolean $assertionsDisabled = (!ReadOnlyAccessFile.class.desiredAssertionStatus());

    public ReadOnlyAccessFile(File file) throws FileNotFoundException {
        super(file, InternalZipConstants.READ_MODE);
    }

    public int readFully(byte[] buffer, int count) throws IOException {
        if ($assertionsDisabled || count > 0) {
            readFully(buffer, 0, count);
            return count;
        }
        throw new AssertionError(count);
    }

    public long getPosition() throws IOException {
        return getFilePointer();
    }

    public void setPosition(long pos) throws IOException {
        seek(pos);
    }
}
