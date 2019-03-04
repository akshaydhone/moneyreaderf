package ir.mahdi.mzip.rar.impl;

import ir.mahdi.mzip.rar.Archive;
import ir.mahdi.mzip.rar.Volume;
import ir.mahdi.mzip.rar.VolumeManager;
import ir.mahdi.mzip.rar.util.VolumeHelper;
import java.io.File;
import java.io.IOException;

public class FileVolumeManager implements VolumeManager {
    private final File firstVolume;

    public FileVolumeManager(File firstVolume) {
        this.firstVolume = firstVolume;
    }

    public Volume nextArchive(Archive archive, Volume last) throws IOException {
        if (last == null) {
            return new FileVolume(archive, this.firstVolume);
        }
        FileVolume lastFileVolume = (FileVolume) last;
        boolean oldNumbering = !archive.getMainHeader().isNewNumbering() || archive.isOldFormat();
        return new FileVolume(archive, new File(VolumeHelper.nextVolumeName(lastFileVolume.getFile().getAbsolutePath(), oldNumbering)));
    }
}
