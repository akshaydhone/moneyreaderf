package ir.mahdi.mzip.rar.unpack.ppm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AnalyzeHeapDump {
    public static void main(String[] argv) {
        IOException e;
        Throwable th;
        File cfile = new File("P:\\test\\heapdumpc");
        File jfile = new File("P:\\test\\heapdumpj");
        if (!cfile.exists()) {
            System.err.println("File not found: " + cfile.getAbsolutePath());
        } else if (jfile.exists()) {
            long clen = cfile.length();
            long jlen = jfile.length();
            if (clen != jlen) {
                System.out.println("File size mismatch");
                System.out.println("clen = " + clen);
                System.out.println("jlen = " + jlen);
            }
            long len = Math.min(clen, jlen);
            InputStream cin = null;
            InputStream jin = null;
            try {
                InputStream cin2 = new BufferedInputStream(new FileInputStream(cfile), 262144);
                try {
                    InputStream jin2 = new BufferedInputStream(new FileInputStream(jfile), 262144);
                    boolean matching = true;
                    boolean mismatchFound = false;
                    long startOff = 0;
                    long off = 0;
                    while (off < len) {
                        try {
                            if (cin2.read() != jin2.read()) {
                                if (matching) {
                                    startOff = off;
                                    matching = false;
                                    mismatchFound = true;
                                }
                            } else if (!matching) {
                                printMismatch(startOff, off);
                                matching = true;
                            }
                            off++;
                        } catch (IOException e2) {
                            e = e2;
                            jin = jin2;
                            cin = cin2;
                        } catch (Throwable th2) {
                            th = th2;
                            jin = jin2;
                            cin = cin2;
                        }
                    }
                    if (!matching) {
                        printMismatch(startOff, off);
                    }
                    if (!mismatchFound) {
                        System.out.println("Files are identical");
                    }
                    System.out.println("Done");
                    try {
                        cin2.close();
                        jin2.close();
                        jin = jin2;
                        cin = cin2;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        jin = jin2;
                        cin = cin2;
                    }
                } catch (IOException e4) {
                    e3 = e4;
                    cin = cin2;
                    try {
                        e3.printStackTrace();
                        try {
                            cin.close();
                            jin.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        try {
                            cin.close();
                            jin.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    cin = cin2;
                    cin.close();
                    jin.close();
                    throw th;
                }
            } catch (IOException e5) {
                e322 = e5;
                e322.printStackTrace();
                cin.close();
                jin.close();
            }
        } else {
            System.err.println("File not found: " + jfile.getAbsolutePath());
        }
    }

    private static void printMismatch(long startOff, long bytesRead) {
        System.out.println("Mismatch: off=" + startOff + "(0x" + Long.toHexString(startOff) + "), len=" + (bytesRead - startOff));
    }
}
