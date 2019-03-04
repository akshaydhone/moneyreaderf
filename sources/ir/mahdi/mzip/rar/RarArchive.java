package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RarArchive {
    public static void extractArchive(String archive, String destination) {
        if (archive == null || destination == null) {
            throw new RuntimeException("archive and destination must me set");
        }
        File arch = new File(archive);
        if (arch.exists()) {
            File dest = new File(destination);
            if (dest.exists() && dest.isDirectory()) {
                extractArchive(arch, dest);
                return;
            }
            throw new RuntimeException("the destination must exist and point to a directory: " + destination);
        }
        throw new RuntimeException("the archive does not exit: " + archive);
    }

    public static void extractArchive(File archive, File destination) {
        Archive arch = null;
        try {
            arch = new Archive(archive);
        } catch (RarException e) {
        } catch (IOException e2) {
        }
        if (arch != null && !arch.isEncrypted()) {
            while (true) {
                FileHeader fh = arch.nextFileHeader();
                if (fh == null) {
                    return;
                }
                if (!fh.isEncrypted()) {
                    try {
                        if (fh.isDirectory()) {
                            createDirectory(fh, destination);
                        } else {
                            OutputStream stream = new FileOutputStream(createFile(fh, destination));
                            arch.extractFile(fh, stream);
                            stream.close();
                        }
                    } catch (IOException e3) {
                    } catch (RarException e4) {
                    }
                }
            }
        }
    }

    private static File createFile(FileHeader fh, File destination) {
        String name;
        if (fh.isFileHeader() && fh.isUnicode()) {
            name = fh.getFileNameW();
        } else {
            name = fh.getFileNameString();
        }
        File f = new File(destination, name);
        if (!f.exists()) {
            try {
                f = makeFile(destination, name);
            } catch (IOException e) {
            }
        }
        return f;
    }

    private static File makeFile(File destination, String name) throws IOException {
        String[] dirs = name.split("\\\\");
        if (dirs == null) {
            return null;
        }
        String path = "";
        int size = dirs.length;
        if (size == 1) {
            return new File(destination, name);
        }
        if (size <= 1) {
            return null;
        }
        for (int i = 0; i < dirs.length - 1; i++) {
            path = path + File.separator + dirs[i];
            new File(destination, path).mkdir();
        }
        File f = new File(destination, path + File.separator + dirs[dirs.length - 1]);
        f.createNewFile();
        return f;
    }

    private static void createDirectory(FileHeader fh, File destination) {
        if (fh.isDirectory() && fh.isUnicode()) {
            if (!new File(destination, fh.getFileNameW()).exists()) {
                makeDirectory(destination, fh.getFileNameW());
            }
        } else if (fh.isDirectory() && !fh.isUnicode() && !new File(destination, fh.getFileNameString()).exists()) {
            makeDirectory(destination, fh.getFileNameString());
        }
    }

    private static void makeDirectory(File destination, String fileName) {
        String[] dirs = fileName.split("\\\\");
        if (dirs != null) {
            String path = "";
            for (String dir : dirs) {
                path = path + File.separator + dir;
                new File(destination, path).mkdir();
            }
        }
    }
}
