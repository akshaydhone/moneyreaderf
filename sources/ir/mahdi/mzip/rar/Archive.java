package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.exception.RarException.RarExceptionType;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.io.IReadOnlyAccess;
import ir.mahdi.mzip.rar.rarfile.AVHeader;
import ir.mahdi.mzip.rar.rarfile.BaseBlock;
import ir.mahdi.mzip.rar.rarfile.BlockHeader;
import ir.mahdi.mzip.rar.rarfile.CommentHeader;
import ir.mahdi.mzip.rar.rarfile.EAHeader;
import ir.mahdi.mzip.rar.rarfile.EndArcHeader;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import ir.mahdi.mzip.rar.rarfile.MacInfoHeader;
import ir.mahdi.mzip.rar.rarfile.MainHeader;
import ir.mahdi.mzip.rar.rarfile.MarkHeader;
import ir.mahdi.mzip.rar.rarfile.ProtectHeader;
import ir.mahdi.mzip.rar.rarfile.SignHeader;
import ir.mahdi.mzip.rar.rarfile.SubBlockHeader;
import ir.mahdi.mzip.rar.rarfile.UnixOwnersHeader;
import ir.mahdi.mzip.rar.rarfile.UnrarHeadertype;
import ir.mahdi.mzip.rar.unpack.ComprDataIO;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Archive implements Closeable {
    private static Logger logger = Logger.getLogger(Archive.class.getName());
    private int currentHeaderIndex;
    private final ComprDataIO dataIO;
    private final List<BaseBlock> headers;
    private MarkHeader markHead;
    private MainHeader newMhd;
    private IReadOnlyAccess rof;
    private long totalPackedRead;
    private long totalPackedSize;
    private Unpack unpack;
    private final UnrarCallback unrarCallback;
    private Volume volume;
    private VolumeManager volumeManager;

    public Archive(VolumeManager volumeManager) throws RarException, IOException {
        this(volumeManager, null);
    }

    public Archive(VolumeManager volumeManager, UnrarCallback unrarCallback) throws RarException, IOException {
        this.headers = new ArrayList();
        this.markHead = null;
        this.newMhd = null;
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        this.volumeManager = volumeManager;
        this.unrarCallback = unrarCallback;
        setVolume(this.volumeManager.nextArchive(this, null));
        this.dataIO = new ComprDataIO(this);
    }

    public Archive(File firstVolume) throws RarException, IOException {
        this(new FileVolumeManager(firstVolume), null);
    }

    public Archive(File firstVolume, UnrarCallback unrarCallback) throws RarException, IOException {
        this(new FileVolumeManager(firstVolume), unrarCallback);
    }

    private void setFile(IReadOnlyAccess file, long length) throws IOException {
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        close();
        this.rof = file;
        try {
            readHeaders(length);
        } catch (Exception e) {
            logger.log(Level.WARNING, "exception in archive constructor maybe file is encrypted or currupt", e);
        }
        for (BaseBlock block : this.headers) {
            if (block.getHeaderType() == UnrarHeadertype.FileHeader) {
                this.totalPackedSize += ((FileHeader) block).getFullPackSize();
            }
        }
        if (this.unrarCallback != null) {
            this.unrarCallback.volumeProgressChanged(this.totalPackedRead, this.totalPackedSize);
        }
    }

    public void bytesReadRead(int count) {
        if (count > 0) {
            this.totalPackedRead += (long) count;
            if (this.unrarCallback != null) {
                this.unrarCallback.volumeProgressChanged(this.totalPackedRead, this.totalPackedSize);
            }
        }
    }

    public IReadOnlyAccess getRof() {
        return this.rof;
    }

    public List<FileHeader> getFileHeaders() {
        List<FileHeader> list = new ArrayList();
        for (BaseBlock block : this.headers) {
            if (block.getHeaderType().equals(UnrarHeadertype.FileHeader)) {
                list.add((FileHeader) block);
            }
        }
        return list;
    }

    public FileHeader nextFileHeader() {
        int n = this.headers.size();
        while (this.currentHeaderIndex < n) {
            List list = this.headers;
            int i = this.currentHeaderIndex;
            this.currentHeaderIndex = i + 1;
            BaseBlock block = (BaseBlock) list.get(i);
            if (block.getHeaderType() == UnrarHeadertype.FileHeader) {
                return (FileHeader) block;
            }
        }
        return null;
    }

    public UnrarCallback getUnrarCallback() {
        return this.unrarCallback;
    }

    public boolean isEncrypted() {
        if (this.newMhd != null) {
            return this.newMhd.isEncrypted();
        }
        throw new NullPointerException("mainheader is null");
    }

    private void readHeaders(long fileLength) throws IOException, RarException {
        this.markHead = null;
        this.newMhd = null;
        this.headers.clear();
        this.currentHeaderIndex = 0;
        while (true) {
            byte[] baseBlockBuffer = new byte[7];
            long position = this.rof.getPosition();
            if (position < fileLength && this.rof.readFully(baseBlockBuffer, 7) != 0) {
                BaseBlock block = new BaseBlock(baseBlockBuffer);
                block.setPositionInFile(position);
                int toRead;
                switch (block.getHeaderType()) {
                    case MarkHeader:
                        this.markHead = new MarkHeader(block);
                        if (this.markHead.isSignature()) {
                            this.headers.add(this.markHead);
                            break;
                        }
                        throw new RarException(RarExceptionType.badRarArchive);
                    case MainHeader:
                        toRead = block.hasEncryptVersion() ? 7 : 6;
                        byte[] mainbuff = new byte[toRead];
                        this.rof.readFully(mainbuff, toRead);
                        MainHeader mainHeader = new MainHeader(block, mainbuff);
                        this.headers.add(mainHeader);
                        this.newMhd = mainHeader;
                        if (!this.newMhd.isEncrypted()) {
                            break;
                        }
                        throw new RarException(RarExceptionType.rarEncryptedException);
                    case SignHeader:
                        byte[] signBuff = new byte[8];
                        this.rof.readFully(signBuff, 8);
                        this.headers.add(new SignHeader(block, signBuff));
                        break;
                    case AvHeader:
                        byte[] avBuff = new byte[7];
                        this.rof.readFully(avBuff, 7);
                        this.headers.add(new AVHeader(block, avBuff));
                        break;
                    case CommHeader:
                        byte[] commBuff = new byte[6];
                        this.rof.readFully(commBuff, 6);
                        CommentHeader commHead = new CommentHeader(block, commBuff);
                        this.headers.add(commHead);
                        this.rof.setPosition(commHead.getPositionInFile() + ((long) commHead.getHeaderSize()));
                        break;
                    case EndArcHeader:
                        EndArcHeader endArcHead;
                        toRead = 0;
                        if (block.hasArchiveDataCRC()) {
                            toRead = 0 + 4;
                        }
                        if (block.hasVolumeNumber()) {
                            toRead += 2;
                        }
                        if (toRead > 0) {
                            byte[] endArchBuff = new byte[toRead];
                            this.rof.readFully(endArchBuff, toRead);
                            endArcHead = new EndArcHeader(block, endArchBuff);
                        } else {
                            endArcHead = new EndArcHeader(block, null);
                        }
                        this.headers.add(endArcHead);
                        return;
                    default:
                        byte[] blockHeaderBuffer = new byte[4];
                        this.rof.readFully(blockHeaderBuffer, 4);
                        BlockHeader blockHead = new BlockHeader(block, blockHeaderBuffer);
                        switch (blockHead.getHeaderType()) {
                            case NewSubHeader:
                            case FileHeader:
                                toRead = (blockHead.getHeaderSize() - 7) - 4;
                                byte[] fileHeaderBuffer = new byte[toRead];
                                this.rof.readFully(fileHeaderBuffer, toRead);
                                FileHeader fileHeader = new FileHeader(blockHead, fileHeaderBuffer);
                                this.headers.add(fileHeader);
                                this.rof.setPosition((fileHeader.getPositionInFile() + ((long) fileHeader.getHeaderSize())) + fileHeader.getFullPackSize());
                                break;
                            case ProtectHeader:
                                toRead = (blockHead.getHeaderSize() - 7) - 4;
                                byte[] protectHeaderBuffer = new byte[toRead];
                                this.rof.readFully(protectHeaderBuffer, toRead);
                                ProtectHeader protectHeader = new ProtectHeader(blockHead, protectHeaderBuffer);
                                this.rof.setPosition((protectHeader.getPositionInFile() + ((long) protectHeader.getHeaderSize())) + ((long) protectHeader.getDataSize()));
                                break;
                            case SubHeader:
                                byte[] subHeadbuffer = new byte[3];
                                this.rof.readFully(subHeadbuffer, 3);
                                SubBlockHeader subBlockHeader = new SubBlockHeader(blockHead, subHeadbuffer);
                                subBlockHeader.print();
                                switch (subBlockHeader.getSubType()) {
                                    case MAC_HEAD:
                                        byte[] macHeaderbuffer = new byte[8];
                                        this.rof.readFully(macHeaderbuffer, 8);
                                        MacInfoHeader macInfoHeader = new MacInfoHeader(subBlockHeader, macHeaderbuffer);
                                        macInfoHeader.print();
                                        this.headers.add(macInfoHeader);
                                        break;
                                    case BEEA_HEAD:
                                    case NTACL_HEAD:
                                    case STREAM_HEAD:
                                        break;
                                    case EA_HEAD:
                                        byte[] eaHeaderBuffer = new byte[10];
                                        this.rof.readFully(eaHeaderBuffer, 10);
                                        EAHeader eaHeader = new EAHeader(subBlockHeader, eaHeaderBuffer);
                                        eaHeader.print();
                                        this.headers.add(eaHeader);
                                        break;
                                    case UO_HEAD:
                                        toRead = ((subBlockHeader.getHeaderSize() - 7) - 4) - 3;
                                        byte[] uoHeaderBuffer = new byte[toRead];
                                        this.rof.readFully(uoHeaderBuffer, toRead);
                                        UnixOwnersHeader unixOwnersHeader = new UnixOwnersHeader(subBlockHeader, uoHeaderBuffer);
                                        unixOwnersHeader.print();
                                        this.headers.add(unixOwnersHeader);
                                        break;
                                    default:
                                        break;
                                }
                            default:
                                logger.warning("Unknown Header");
                                throw new RarException(RarExceptionType.notRarArchive);
                        }
                }
            }
            return;
        }
    }

    public void extractFile(FileHeader hd, OutputStream os) throws RarException {
        if (this.headers.contains(hd)) {
            try {
                doExtractFile(hd, os);
                return;
            } catch (Exception e) {
                if (e instanceof RarException) {
                    throw ((RarException) e);
                }
                throw new RarException(e);
            }
        }
        throw new RarException(RarExceptionType.headerNotInArchive);
    }

    public InputStream getInputStream(final FileHeader hd) throws RarException, IOException {
        PipedInputStream in = new PipedInputStream(32768);
        final PipedOutputStream out = new PipedOutputStream(in);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Archive.this.extractFile(hd, out);
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                } catch (RarException e2) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                    }
                } catch (Throwable th) {
                    try {
                        out.close();
                    } catch (IOException e4) {
                    }
                    throw th;
                }
            }
        }).start();
        return in;
    }

    private void doExtractFile(FileHeader hd, OutputStream os) throws RarException, IOException {
        this.dataIO.init(os);
        this.dataIO.init(hd);
        this.dataIO.setUnpFileCRC(isOldFormat() ? 0 : -1);
        if (this.unpack == null) {
            this.unpack = new Unpack(this.dataIO);
        }
        if (!hd.isSolid()) {
            this.unpack.init(null);
        }
        this.unpack.setDestSize(hd.getFullUnpackSize());
        try {
            long actualCRC;
            this.unpack.doUnpack(hd.getUnpVersion(), hd.isSolid());
            hd = this.dataIO.getSubHeader();
            if (hd.isSplitAfter()) {
                actualCRC = this.dataIO.getPackedCRC() ^ -1;
            } else {
                actualCRC = this.dataIO.getUnpFileCRC() ^ -1;
            }
            if (actualCRC != ((long) hd.getFileCRC())) {
                throw new RarException(RarExceptionType.crcError);
            }
        } catch (Exception e) {
            this.unpack.cleanUp();
            if (e instanceof RarException) {
                throw ((RarException) e);
            }
            throw new RarException(e);
        }
    }

    public MainHeader getMainHeader() {
        return this.newMhd;
    }

    public boolean isOldFormat() {
        return this.markHead.isOldFormat();
    }

    public void close() throws IOException {
        if (this.rof != null) {
            this.rof.close();
            this.rof = null;
        }
        if (this.unpack != null) {
            this.unpack.cleanUp();
        }
    }

    public VolumeManager getVolumeManager() {
        return this.volumeManager;
    }

    public void setVolumeManager(VolumeManager volumeManager) {
        this.volumeManager = volumeManager;
    }

    public Volume getVolume() {
        return this.volume;
    }

    public void setVolume(Volume volume) throws IOException {
        this.volume = volume;
        setFile(volume.getReadOnlyAccess(), volume.getLength());
    }
}
