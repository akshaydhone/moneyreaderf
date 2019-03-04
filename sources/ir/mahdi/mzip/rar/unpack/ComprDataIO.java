package ir.mahdi.mzip.rar.unpack;

import ir.mahdi.mzip.rar.Archive;
import ir.mahdi.mzip.rar.UnrarCallback;
import ir.mahdi.mzip.rar.Volume;
import ir.mahdi.mzip.rar.crc.RarCRC;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.exception.RarException.RarExceptionType;
import ir.mahdi.mzip.rar.io.ReadOnlyAccessInputStream;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComprDataIO {
    private final Archive archive;
    private long curPackRead;
    private long curPackWrite;
    private long curUnpRead;
    private long curUnpWrite;
    private char currentCommand;
    private int decryption;
    private int encryption;
    private InputStream inputStream;
    private int lastPercent;
    private boolean nextVolumeMissing;
    private OutputStream outputStream;
    private long packFileCRC;
    private boolean packVolume;
    private long packedCRC;
    private long processedArcSize;
    private boolean skipUnpCRC;
    private FileHeader subHead;
    private boolean testMode;
    private long totalArcSize;
    private long totalPackRead;
    private long unpArcSize;
    private long unpFileCRC;
    private long unpPackedSize;
    private boolean unpVolume;

    public ComprDataIO(Archive arc) {
        this.archive = arc;
    }

    public void init(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.unpPackedSize = 0;
        this.testMode = false;
        this.skipUnpCRC = false;
        this.packVolume = false;
        this.unpVolume = false;
        this.nextVolumeMissing = false;
        this.encryption = 0;
        this.decryption = 0;
        this.totalPackRead = 0;
        this.curUnpWrite = 0;
        this.curUnpRead = 0;
        this.curPackWrite = 0;
        this.curPackRead = 0;
        this.packedCRC = -1;
        this.unpFileCRC = -1;
        this.packFileCRC = -1;
        this.lastPercent = -1;
        this.subHead = null;
        this.currentCommand = '\u0000';
        this.totalArcSize = 0;
        this.processedArcSize = 0;
    }

    public void init(FileHeader hd) throws IOException {
        long startPos = hd.getPositionInFile() + ((long) hd.getHeaderSize());
        this.unpPackedSize = hd.getFullPackSize();
        this.inputStream = new ReadOnlyAccessInputStream(this.archive.getRof(), startPos, this.unpPackedSize + startPos);
        this.subHead = hd;
        this.curUnpRead = 0;
        this.curPackWrite = 0;
        this.packedCRC = -1;
    }

    public int unpRead(byte[] addr, int offset, int count) throws IOException, RarException {
        int retCode = 0;
        int totalRead = 0;
        while (count > 0) {
            int readSize;
            if (((long) count) > this.unpPackedSize) {
                readSize = (int) this.unpPackedSize;
            } else {
                readSize = count;
            }
            retCode = this.inputStream.read(addr, offset, readSize);
            if (retCode >= 0) {
                if (this.subHead.isSplitAfter()) {
                    this.packedCRC = (long) RarCRC.checkCrc((int) this.packedCRC, addr, offset, retCode);
                }
                this.curUnpRead += (long) retCode;
                totalRead += retCode;
                offset += retCode;
                count -= retCode;
                this.unpPackedSize -= (long) retCode;
                this.archive.bytesReadRead(retCode);
                if (this.unpPackedSize != 0 || !this.subHead.isSplitAfter()) {
                    break;
                }
                Volume nextVolume = this.archive.getVolumeManager().nextArchive(this.archive, this.archive.getVolume());
                if (nextVolume == null) {
                    this.nextVolumeMissing = true;
                    return -1;
                }
                FileHeader hd = getSubHeader();
                if (hd.getUnpVersion() < (byte) 20 || hd.getFileCRC() == -1 || getPackedCRC() == ((long) (hd.getFileCRC() ^ -1))) {
                    UnrarCallback callback = this.archive.getUnrarCallback();
                    if (callback != null && !callback.isNextVolumeReady(nextVolume)) {
                        return -1;
                    }
                    this.archive.setVolume(nextVolume);
                    hd = this.archive.nextFileHeader();
                    if (hd == null) {
                        return -1;
                    }
                    init(hd);
                } else {
                    throw new RarException(RarExceptionType.crcError);
                }
            }
            throw new EOFException();
        }
        if (retCode != -1) {
            retCode = totalRead;
        }
        return retCode;
    }

    public void unpWrite(byte[] addr, int offset, int count) throws IOException {
        if (!this.testMode) {
            this.outputStream.write(addr, offset, count);
        }
        this.curUnpWrite += (long) count;
        if (!this.skipUnpCRC) {
            if (this.archive.isOldFormat()) {
                this.unpFileCRC = (long) RarCRC.checkOldCrc((short) ((int) this.unpFileCRC), addr, count);
            } else {
                this.unpFileCRC = (long) RarCRC.checkCrc((int) this.unpFileCRC, addr, offset, count);
            }
        }
    }

    public void setPackedSizeToRead(long size) {
        this.unpPackedSize = size;
    }

    public void setTestMode(boolean mode) {
        this.testMode = mode;
    }

    public void setSkipUnpCRC(boolean skip) {
        this.skipUnpCRC = skip;
    }

    public long getCurPackRead() {
        return this.curPackRead;
    }

    public void setCurPackRead(long curPackRead) {
        this.curPackRead = curPackRead;
    }

    public long getCurPackWrite() {
        return this.curPackWrite;
    }

    public void setCurPackWrite(long curPackWrite) {
        this.curPackWrite = curPackWrite;
    }

    public long getCurUnpRead() {
        return this.curUnpRead;
    }

    public void setCurUnpRead(long curUnpRead) {
        this.curUnpRead = curUnpRead;
    }

    public long getCurUnpWrite() {
        return this.curUnpWrite;
    }

    public void setCurUnpWrite(long curUnpWrite) {
        this.curUnpWrite = curUnpWrite;
    }

    public int getDecryption() {
        return this.decryption;
    }

    public void setDecryption(int decryption) {
        this.decryption = decryption;
    }

    public int getEncryption() {
        return this.encryption;
    }

    public void setEncryption(int encryption) {
        this.encryption = encryption;
    }

    public boolean isNextVolumeMissing() {
        return this.nextVolumeMissing;
    }

    public void setNextVolumeMissing(boolean nextVolumeMissing) {
        this.nextVolumeMissing = nextVolumeMissing;
    }

    public long getPackedCRC() {
        return this.packedCRC;
    }

    public void setPackedCRC(long packedCRC) {
        this.packedCRC = packedCRC;
    }

    public long getPackFileCRC() {
        return this.packFileCRC;
    }

    public void setPackFileCRC(long packFileCRC) {
        this.packFileCRC = packFileCRC;
    }

    public boolean isPackVolume() {
        return this.packVolume;
    }

    public void setPackVolume(boolean packVolume) {
        this.packVolume = packVolume;
    }

    public long getProcessedArcSize() {
        return this.processedArcSize;
    }

    public void setProcessedArcSize(long processedArcSize) {
        this.processedArcSize = processedArcSize;
    }

    public long getTotalArcSize() {
        return this.totalArcSize;
    }

    public void setTotalArcSize(long totalArcSize) {
        this.totalArcSize = totalArcSize;
    }

    public long getTotalPackRead() {
        return this.totalPackRead;
    }

    public void setTotalPackRead(long totalPackRead) {
        this.totalPackRead = totalPackRead;
    }

    public long getUnpArcSize() {
        return this.unpArcSize;
    }

    public void setUnpArcSize(long unpArcSize) {
        this.unpArcSize = unpArcSize;
    }

    public long getUnpFileCRC() {
        return this.unpFileCRC;
    }

    public void setUnpFileCRC(long unpFileCRC) {
        this.unpFileCRC = unpFileCRC;
    }

    public boolean isUnpVolume() {
        return this.unpVolume;
    }

    public void setUnpVolume(boolean unpVolume) {
        this.unpVolume = unpVolume;
    }

    public FileHeader getSubHeader() {
        return this.subHead;
    }

    public void setSubHeader(FileHeader hd) {
        this.subHead = hd;
    }
}
