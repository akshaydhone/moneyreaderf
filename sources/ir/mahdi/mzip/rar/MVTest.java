package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MVTest {
    public static void main(String[] args) {
        Archive a = null;
        try {
            a = new Archive(new FileVolumeManager(new File("/home/rogiel/fs/home/ae721273-eade-45e7-8112-d14115ebae56/Village People - Y.M.C.A.mp3.part1.rar")));
        } catch (RarException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (a != null) {
            a.getMainHeader().print();
            for (FileHeader fh = a.nextFileHeader(); fh != null; fh = a.nextFileHeader()) {
                try {
                    File out = new File("/home/rogiel/fs/test/" + fh.getFileNameString().trim());
                    System.out.println(out.getAbsolutePath());
                    FileOutputStream os = new FileOutputStream(out);
                    a.extractFile(fh, os);
                    os.close();
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                } catch (RarException e4) {
                    e4.printStackTrace();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
        }
    }
}
