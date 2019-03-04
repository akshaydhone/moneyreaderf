package net.lingala.zip4j.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipEngine {
    private ZipModel zipModel;

    /* renamed from: net.lingala.zip4j.zip.ZipEngine$1 */
    class C03181 extends Thread {
        final ZipEngine this$0;
        private final ArrayList val$fileList;
        private final ZipParameters val$parameters;
        private final ProgressMonitor val$progressMonitor;

        C03181(ZipEngine zipEngine, String $anonymous0, ArrayList arrayList, ZipParameters zipParameters, ProgressMonitor progressMonitor) {
            super($anonymous0);
            this.this$0 = zipEngine;
            this.val$fileList = arrayList;
            this.val$parameters = zipParameters;
            this.val$progressMonitor = progressMonitor;
        }

        public void run() {
            try {
                ZipEngine.access$0(this.this$0, this.val$fileList, this.val$parameters, this.val$progressMonitor);
            } catch (ZipException e) {
            }
        }
    }

    public ZipEngine(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null in ZipEngine constructor");
        }
        this.zipModel = zipModel;
    }

    public void addFiles(ArrayList fileList, ZipParameters parameters, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (fileList == null || parameters == null) {
            throw new ZipException("one of the input parameters is null when adding files");
        } else if (fileList.size() <= 0) {
            throw new ZipException("no files to add");
        } else {
            progressMonitor.setCurrentOperation(0);
            progressMonitor.setState(1);
            progressMonitor.setResult(1);
            if (runInThread) {
                progressMonitor.setTotalWork(calculateTotalWork(fileList, parameters));
                progressMonitor.setFileName(((File) fileList.get(0)).getAbsolutePath());
                new C03181(this, InternalZipConstants.THREAD_NAME, fileList, parameters, progressMonitor).start();
                return;
            }
            initAddFiles(fileList, parameters, progressMonitor);
        }
    }

    static void access$0(ZipEngine zipEngine, ArrayList arrayList, ZipParameters zipParameters, ProgressMonitor progressMonitor) throws ZipException {
        zipEngine.initAddFiles(arrayList, zipParameters, progressMonitor);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initAddFiles(java.util.ArrayList r19, net.lingala.zip4j.model.ZipParameters r20, net.lingala.zip4j.progress.ProgressMonitor r21) throws net.lingala.zip4j.exception.ZipException {
        /*
        r18 = this;
        if (r19 == 0) goto L_0x0004;
    L_0x0002:
        if (r20 != 0) goto L_0x000c;
    L_0x0004:
        r13 = new net.lingala.zip4j.exception.ZipException;
        r14 = "one of the input parameters is null when adding files";
        r13.<init>(r14);
        throw r13;
    L_0x000c:
        r13 = r19.size();
        if (r13 > 0) goto L_0x001a;
    L_0x0012:
        r13 = new net.lingala.zip4j.exception.ZipException;
        r14 = "no files to add";
        r13.<init>(r14);
        throw r13;
    L_0x001a:
        r0 = r18;
        r13 = r0.zipModel;
        r13 = r13.getEndCentralDirRecord();
        if (r13 != 0) goto L_0x002f;
    L_0x0024:
        r0 = r18;
        r13 = r0.zipModel;
        r14 = r18.createEndOfCentralDirectoryRecord();
        r13.setEndCentralDirRecord(r14);
    L_0x002f:
        r8 = 0;
        r5 = 0;
        r0 = r18;
        r1 = r20;
        r0.checkParameters(r1);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r18.removeFilesIfExists(r19, r20, r21);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r0 = r18;
        r13 = r0.zipModel;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r13 = r13.getZipFile();	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r7 = net.lingala.zip4j.util.Zip4jUtil.checkFileExists(r13);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r12 = new net.lingala.zip4j.io.SplitOutputStream;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r13 = new java.io.File;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r0 = r18;
        r14 = r0.zipModel;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r14 = r14.getZipFile();	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r13.<init>(r14);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r0 = r18;
        r14 = r0.zipModel;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r14 = r14.getSplitLength();	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r12.<init>(r13, r14);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r9 = new net.lingala.zip4j.io.ZipOutputStream;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r0 = r18;
        r13 = r0.zipModel;	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        r9.<init>(r12, r13);	 Catch:{ ZipException -> 0x0207, Exception -> 0x0201 }
        if (r7 == 0) goto L_0x00a1;
    L_0x006c:
        r0 = r18;
        r13 = r0.zipModel;	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r13 = r13.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        if (r13 != 0) goto L_0x0092;
    L_0x0076:
        r13 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r14 = "invalid end of central directory record";
        r13.<init>(r14);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        throw r13;	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
    L_0x007e:
        r2 = move-exception;
        r8 = r9;
    L_0x0080:
        r0 = r21;
        r0.endProgressMonitorError(r2);	 Catch:{ all -> 0x0086 }
        throw r2;	 Catch:{ all -> 0x0086 }
    L_0x0086:
        r13 = move-exception;
    L_0x0087:
        if (r5 == 0) goto L_0x008c;
    L_0x0089:
        r5.close();	 Catch:{ IOException -> 0x01f0 }
    L_0x008c:
        if (r8 == 0) goto L_0x0091;
    L_0x008e:
        r8.close();	 Catch:{ IOException -> 0x01f3 }
    L_0x0091:
        throw r13;
    L_0x0092:
        r0 = r18;
        r13 = r0.zipModel;	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r13 = r13.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r14 = r13.getOffsetOfStartOfCentralDir();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r12.seek(r14);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
    L_0x00a1:
        r13 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r10 = new byte[r13];	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r11 = -1;
        r4 = 0;
        r6 = r5;
    L_0x00a8:
        r13 = r19.size();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r4 < r13) goto L_0x00c0;
    L_0x00ae:
        r9.finish();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r21.endProgressMonitorSuccess();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r6 == 0) goto L_0x00b9;
    L_0x00b6:
        r6.close();	 Catch:{ IOException -> 0x01f6 }
    L_0x00b9:
        if (r9 == 0) goto L_0x00be;
    L_0x00bb:
        r9.close();	 Catch:{ IOException -> 0x01f9 }
    L_0x00be:
        r5 = r6;
    L_0x00bf:
        return;
    L_0x00c0:
        r13 = r21.isCancelAllTasks();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 == 0) goto L_0x00de;
    L_0x00c6:
        r13 = 3;
        r0 = r21;
        r0.setResult(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = 0;
        r0 = r21;
        r0.setState(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r6 == 0) goto L_0x00d7;
    L_0x00d4:
        r6.close();	 Catch:{ IOException -> 0x01e2 }
    L_0x00d7:
        if (r9 == 0) goto L_0x00dc;
    L_0x00d9:
        r9.close();	 Catch:{ IOException -> 0x01e5 }
    L_0x00dc:
        r5 = r6;
        goto L_0x00bf;
    L_0x00de:
        r3 = r20.clone();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r3 = (net.lingala.zip4j.model.ZipParameters) r3;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = r13.getAbsolutePath();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r21;
        r0.setFileName(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = r13.isDirectory();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 != 0) goto L_0x0166;
    L_0x0103:
        r13 = r3.isEncryptFiles();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 == 0) goto L_0x0150;
    L_0x0109:
        r13 = r3.getEncryptionMethod();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 != 0) goto L_0x0150;
    L_0x010f:
        r13 = 3;
        r0 = r21;
        r0.setCurrentOperation(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = r13.getAbsolutePath();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r21;
        r14 = net.lingala.zip4j.util.CRCUtil.computeFileCRC(r13, r0);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (int) r14;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r3.setSourceFileCRC(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = 0;
        r0 = r21;
        r0.setCurrentOperation(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = r21.isCancelAllTasks();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 == 0) goto L_0x0150;
    L_0x0137:
        r13 = 3;
        r0 = r21;
        r0.setResult(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = 0;
        r0 = r21;
        r0.setState(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r6 == 0) goto L_0x0148;
    L_0x0145:
        r6.close();	 Catch:{ IOException -> 0x01e8 }
    L_0x0148:
        if (r9 == 0) goto L_0x014d;
    L_0x014a:
        r9.close();	 Catch:{ IOException -> 0x01eb }
    L_0x014d:
        r5 = r6;
        goto L_0x00bf;
    L_0x0150:
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r14 = net.lingala.zip4j.util.Zip4jUtil.getFileLengh(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r16 = 0;
        r13 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r13 != 0) goto L_0x0166;
    L_0x0162:
        r13 = 0;
        r3.setCompressionMethod(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
    L_0x0166:
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r9.putNextEntry(r13, r3);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = r13.isDirectory();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        if (r13 == 0) goto L_0x0188;
    L_0x017f:
        r9.closeEntry();	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r5 = r6;
    L_0x0183:
        r4 = r4 + 1;
        r6 = r5;
        goto L_0x00a8;
    L_0x0188:
        r5 = new java.io.FileInputStream;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r0 = r19;
        r13 = r0.get(r4);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r13 = (java.io.File) r13;	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
        r5.<init>(r13);	 Catch:{ ZipException -> 0x020a, Exception -> 0x0203, all -> 0x01fc }
    L_0x0195:
        r11 = r5.read(r10);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r13 = -1;
        if (r11 != r13) goto L_0x01b2;
    L_0x019c:
        r9.closeEntry();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        if (r5 == 0) goto L_0x0183;
    L_0x01a1:
        r5.close();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        goto L_0x0183;
    L_0x01a5:
        r2 = move-exception;
        r8 = r9;
    L_0x01a7:
        r0 = r21;
        r0.endProgressMonitorError(r2);	 Catch:{ all -> 0x0086 }
        r13 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0086 }
        r13.<init>(r2);	 Catch:{ all -> 0x0086 }
        throw r13;	 Catch:{ all -> 0x0086 }
    L_0x01b2:
        r13 = r21.isCancelAllTasks();	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        if (r13 == 0) goto L_0x01d3;
    L_0x01b8:
        r13 = 3;
        r0 = r21;
        r0.setResult(r13);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r13 = 0;
        r0 = r21;
        r0.setState(r13);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        if (r5 == 0) goto L_0x01c9;
    L_0x01c6:
        r5.close();	 Catch:{ IOException -> 0x01ee }
    L_0x01c9:
        if (r9 == 0) goto L_0x00bf;
    L_0x01cb:
        r9.close();	 Catch:{ IOException -> 0x01d0 }
        goto L_0x00bf;
    L_0x01d0:
        r13 = move-exception;
        goto L_0x00bf;
    L_0x01d3:
        r13 = 0;
        r9.write(r10, r13, r11);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r14 = (long) r11;	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        r0 = r21;
        r0.updateWorkCompleted(r14);	 Catch:{ ZipException -> 0x007e, Exception -> 0x01a5, all -> 0x01de }
        goto L_0x0195;
    L_0x01de:
        r13 = move-exception;
        r8 = r9;
        goto L_0x0087;
    L_0x01e2:
        r13 = move-exception;
        goto L_0x00d7;
    L_0x01e5:
        r13 = move-exception;
        goto L_0x00dc;
    L_0x01e8:
        r13 = move-exception;
        goto L_0x0148;
    L_0x01eb:
        r13 = move-exception;
        goto L_0x014d;
    L_0x01ee:
        r13 = move-exception;
        goto L_0x01c9;
    L_0x01f0:
        r14 = move-exception;
        goto L_0x008c;
    L_0x01f3:
        r14 = move-exception;
        goto L_0x0091;
    L_0x01f6:
        r13 = move-exception;
        goto L_0x00b9;
    L_0x01f9:
        r13 = move-exception;
        goto L_0x00be;
    L_0x01fc:
        r13 = move-exception;
        r5 = r6;
        r8 = r9;
        goto L_0x0087;
    L_0x0201:
        r2 = move-exception;
        goto L_0x01a7;
    L_0x0203:
        r2 = move-exception;
        r5 = r6;
        r8 = r9;
        goto L_0x01a7;
    L_0x0207:
        r2 = move-exception;
        goto L_0x0080;
    L_0x020a:
        r2 = move-exception;
        r5 = r6;
        r8 = r9;
        goto L_0x0080;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.zip.ZipEngine.initAddFiles(java.util.ArrayList, net.lingala.zip4j.model.ZipParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }

    public void addStreamToZip(InputStream inputStream, ZipParameters parameters) throws ZipException {
        ZipException e;
        Throwable e2;
        Throwable th;
        if (inputStream == null || parameters == null) {
            throw new ZipException("one of the input parameters is null, cannot add stream to zip");
        }
        ZipOutputStream zipOutputStream = null;
        try {
            checkParameters(parameters);
            boolean isZipFileAlreadExists = Zip4jUtil.checkFileExists(this.zipModel.getZipFile());
            SplitOutputStream splitOutputStream = new SplitOutputStream(new File(this.zipModel.getZipFile()), this.zipModel.getSplitLength());
            ZipOutputStream outputStream = new ZipOutputStream(splitOutputStream, this.zipModel);
            if (isZipFileAlreadExists) {
                try {
                    if (this.zipModel.getEndCentralDirRecord() == null) {
                        throw new ZipException("invalid end of central directory record");
                    }
                    splitOutputStream.seek(this.zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                } catch (ZipException e3) {
                    e = e3;
                    zipOutputStream = outputStream;
                } catch (Exception e4) {
                    e2 = e4;
                    zipOutputStream = outputStream;
                } catch (Throwable th2) {
                    th = th2;
                    zipOutputStream = outputStream;
                }
            }
            byte[] readBuff = new byte[4096];
            outputStream.putNextEntry(null, parameters);
            if (!parameters.getFileNameInZip().endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) && !parameters.getFileNameInZip().endsWith("\\")) {
                while (true) {
                    int readLen = inputStream.read(readBuff);
                    if (readLen == -1) {
                        break;
                    }
                    outputStream.write(readBuff, 0, readLen);
                }
            }
            outputStream.closeEntry();
            outputStream.finish();
            if (outputStream != null) {
                try {
                    outputStream.close();
                    return;
                } catch (IOException e5) {
                    return;
                }
            }
            return;
        } catch (ZipException e6) {
            e = e6;
            try {
                throw e;
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Exception e7) {
            e2 = e7;
            throw new ZipException(e2);
        }
        throw th;
        if (zipOutputStream != null) {
            try {
                zipOutputStream.close();
            } catch (IOException e8) {
            }
        }
        throw th;
    }

    public void addFolderToZip(File file, ZipParameters parameters, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (file == null || parameters == null) {
            throw new ZipException("one of the input parameters is null, cannot add folder to zip");
        } else if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
            throw new ZipException("input folder does not exist");
        } else if (!file.isDirectory()) {
            throw new ZipException("input file is not a folder, user addFileToZip method to add files");
        } else if (Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
            String rootFolderPath = parameters.isIncludeRootFolder() ? file.getAbsolutePath() != null ? file.getAbsoluteFile().getParentFile() != null ? file.getAbsoluteFile().getParentFile().getAbsolutePath() : "" : file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "" : file.getAbsolutePath();
            parameters.setDefaultFolderPath(rootFolderPath);
            ArrayList fileList = Zip4jUtil.getFilesInDirectoryRec(file, parameters.isReadHiddenFiles());
            if (parameters.isIncludeRootFolder()) {
                if (fileList == null) {
                    fileList = new ArrayList();
                }
                fileList.add(file);
            }
            addFiles(fileList, parameters, progressMonitor, runInThread);
        } else {
            throw new ZipException(new StringBuffer("cannot read folder: ").append(file.getAbsolutePath()).toString());
        }
    }

    private void checkParameters(ZipParameters parameters) throws ZipException {
        if (parameters == null) {
            throw new ZipException("cannot validate zip parameters");
        } else if (parameters.getCompressionMethod() != 0 && parameters.getCompressionMethod() != 8) {
            throw new ZipException("unsupported compression type");
        } else if (parameters.getCompressionMethod() == 8 && parameters.getCompressionLevel() < 0 && parameters.getCompressionLevel() > 9) {
            throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
        } else if (!parameters.isEncryptFiles()) {
            parameters.setAesKeyStrength(-1);
            parameters.setEncryptionMethod(-1);
        } else if (parameters.getEncryptionMethod() != 0 && parameters.getEncryptionMethod() != 99) {
            throw new ZipException("unsupported encryption method");
        } else if (parameters.getPassword() == null || parameters.getPassword().length <= 0) {
            throw new ZipException("input password is empty or null");
        }
    }

    private void removeFilesIfExists(ArrayList fileList, ZipParameters parameters, ProgressMonitor progressMonitor) throws ZipException {
        if (this.zipModel != null && this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null && this.zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
            RandomAccessFile outputStream = null;
            for (int i = 0; i < fileList.size(); i++) {
                FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, Zip4jUtil.getRelativeFileName(((File) fileList.get(i)).getAbsolutePath(), parameters.getRootFolderInZip(), parameters.getDefaultFolderPath()));
                if (fileHeader != null) {
                    if (outputStream != null) {
                        outputStream.close();
                        outputStream = null;
                    }
                    ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
                    progressMonitor.setCurrentOperation(2);
                    HashMap retMap = archiveMaintainer.initRemoveZipFile(this.zipModel, fileHeader, progressMonitor);
                    if (progressMonitor.isCancelAllTasks()) {
                        progressMonitor.setResult(3);
                        progressMonitor.setState(0);
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                                return;
                            } catch (IOException e) {
                                return;
                            }
                        }
                        return;
                    }
                    progressMonitor.setCurrentOperation(0);
                    if (outputStream == null) {
                        outputStream = prepareFileOutputStream();
                        if (!(retMap == null || retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR) == null)) {
                            try {
                                long offsetCentralDir = Long.parseLong((String) retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR));
                                if (offsetCentralDir >= 0) {
                                    outputStream.seek(offsetCentralDir);
                                } else {
                                    continue;
                                }
                            } catch (NumberFormatException e2) {
                                throw new ZipException("NumberFormatException while parsing offset central directory. Cannot update already existing file header");
                            } catch (Exception e3) {
                                throw new ZipException("Error while parsing offset central directory. Cannot update already existing file header");
                            } catch (Throwable e4) {
                                try {
                                    throw new ZipException(e4);
                                } catch (Throwable th) {
                                    if (outputStream != null) {
                                        try {
                                            outputStream.close();
                                        } catch (IOException e5) {
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e6) {
                }
            }
        }
    }

    private RandomAccessFile prepareFileOutputStream() throws ZipException {
        String outPath = this.zipModel.getZipFile();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            try {
                File outFile = new File(outPath);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                return new RandomAccessFile(outFile, InternalZipConstants.WRITE_MODE);
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
        throw new ZipException("invalid output path");
    }

    private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
        EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
        endCentralDirRecord.setSignature(InternalZipConstants.ENDSIG);
        endCentralDirRecord.setNoOfThisDisk(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
        endCentralDirRecord.setOffsetOfStartOfCentralDir(0);
        return endCentralDirRecord;
    }

    private long calculateTotalWork(ArrayList fileList, ZipParameters parameters) throws ZipException {
        if (fileList == null) {
            throw new ZipException("file list is null, cannot calculate total work");
        }
        long totalWork = 0;
        int i = 0;
        while (i < fileList.size()) {
            if ((fileList.get(i) instanceof File) && ((File) fileList.get(i)).exists()) {
                if (parameters.isEncryptFiles() && parameters.getEncryptionMethod() == 0) {
                    totalWork += Zip4jUtil.getFileLengh((File) fileList.get(i)) * 2;
                } else {
                    totalWork += Zip4jUtil.getFileLengh((File) fileList.get(i));
                }
                if (!(this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() <= 0)) {
                    FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, Zip4jUtil.getRelativeFileName(((File) fileList.get(i)).getAbsolutePath(), parameters.getRootFolderInZip(), parameters.getDefaultFolderPath()));
                    if (fileHeader != null) {
                        totalWork += Zip4jUtil.getFileLengh(new File(this.zipModel.getZipFile())) - fileHeader.getCompressedSize();
                    }
                }
            }
            i++;
        }
        return totalWork;
    }
}
