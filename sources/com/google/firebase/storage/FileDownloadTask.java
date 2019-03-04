package com.google.firebase.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzp;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileDownloadTask extends StorageTask<TaskSnapshot> {
    private StorageReference zzd;
    private zzf zzf;
    private final Uri zzg;
    private long zzh;
    private long zzi = -1;
    private String zzj = null;
    private volatile Exception zzk = null;
    private long zzl = 0;
    private int zzm;

    public class TaskSnapshot extends SnapshotBase {
        private final long zzh;
        private final /* synthetic */ FileDownloadTask zzn;

        TaskSnapshot(@Nullable FileDownloadTask fileDownloadTask, Exception exception, long j) {
            this.zzn = fileDownloadTask;
            super(fileDownloadTask, exception);
            this.zzh = j;
        }

        public long getBytesTransferred() {
            return this.zzh;
        }

        public long getTotalByteCount() {
            return this.zzn.getTotalBytes();
        }
    }

    FileDownloadTask(@NonNull StorageReference storageReference, @NonNull Uri uri) {
        this.zzd = storageReference;
        this.zzg = uri;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxDownloadRetryTimeMillis());
    }

    private final int zza(InputStream inputStream, byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i != bArr.length) {
            try {
                int read = inputStream.read(bArr, i, bArr.length - i);
                if (read == -1) {
                    break;
                }
                i += read;
                i2 = 1;
            } catch (Exception e) {
                this.zzk = e;
            }
        }
        return i2 != 0 ? i : -1;
    }

    private final boolean zza(zzq zzq) throws IOException {
        InputStream stream = zzq.getStream();
        if (stream != null) {
            String str;
            OutputStream fileOutputStream;
            File file = new File(this.zzg.getPath());
            if (!file.exists()) {
                String valueOf;
                if (this.zzl > 0) {
                    String str2 = "FileDownloadTask";
                    String str3 = "The file downloading to has been deleted:";
                    valueOf = String.valueOf(file.getAbsolutePath());
                    Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                    throw new IllegalStateException("expected a file to resume from.");
                } else if (!file.createNewFile()) {
                    str = "FileDownloadTask";
                    String str4 = "unable to create file:";
                    valueOf = String.valueOf(file.getAbsolutePath());
                    Log.w(str, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
                }
            }
            if (this.zzl > 0) {
                str = file.getAbsolutePath();
                Log.d("FileDownloadTask", new StringBuilder(String.valueOf(str).length() + 47).append("Resuming download file ").append(str).append(" at ").append(this.zzl).toString());
                fileOutputStream = new FileOutputStream(file, true);
            } else {
                fileOutputStream = new FileOutputStream(file);
            }
            try {
                byte[] bArr = new byte[262144];
                boolean z = true;
                while (z) {
                    int zza = zza(stream, bArr);
                    if (zza == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, zza);
                    this.zzh += (long) zza;
                    if (this.zzk != null) {
                        Log.d("FileDownloadTask", "Exception occurred during file download. Retrying.", this.zzk);
                        this.zzk = null;
                        z = false;
                    }
                    if (!zza(4, false)) {
                        z = false;
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                stream.close();
                return z;
            } catch (Throwable th) {
                fileOutputStream.flush();
                fileOutputStream.close();
                stream.close();
            }
        } else {
            this.zzk = new IllegalStateException("Unable to open Firebase Storage stream.");
            return false;
        }
    }

    @NonNull
    final StorageReference getStorage() {
        return this.zzd;
    }

    final long getTotalBytes() {
        return this.zzi;
    }

    protected void onCanceled() {
        this.zzf.cancel();
        this.zzk = StorageException.fromErrorStatus(Status.RESULT_CANCELED);
    }

    final void run() {
        if (this.zzk != null) {
            zza(64, false);
        } else if (zza(4, false)) {
            do {
                this.zzh = 0;
                this.zzk = null;
                this.zzf.reset();
                try {
                    zzq zza = zzp.zzb(this.zzd.getStorage().getApp()).zza(this.zzd.zze(), this.zzl);
                    this.zzf.zza(zza, false);
                    this.zzm = zza.getResultCode();
                    this.zzk = zza.getException() != null ? zza.getException() : this.zzk;
                    int i = this.zzm;
                    boolean z = i == 308 || (i >= 200 && i < 300);
                    z = z && this.zzk == null && zzg() == 4;
                    if (z) {
                        this.zzi = (long) zza.zzac();
                        Object zzh = zza.zzh("ETag");
                        if (TextUtils.isEmpty(zzh) || this.zzj == null || this.zzj.equals(zzh)) {
                            this.zzj = zzh;
                            try {
                                z = zza(zza);
                            } catch (Throwable e) {
                                Log.e("FileDownloadTask", "Exception occurred during file write.  Aborting.", e);
                                this.zzk = e;
                            }
                        } else {
                            Log.w("FileDownloadTask", "The file at the server has changed.  Restarting from the beginning.");
                            this.zzl = 0;
                            this.zzj = null;
                            zza.zzw();
                            schedule();
                            return;
                        }
                    }
                    zza.zzw();
                    z = z && this.zzk == null && zzg() == 4;
                    if (z) {
                        zza(128, false);
                        return;
                    }
                    File file = new File(this.zzg.getPath());
                    if (file.exists()) {
                        this.zzl = file.length();
                    } else {
                        this.zzl = 0;
                    }
                    if (zzg() == 8) {
                        zza(16, false);
                        return;
                    } else if (zzg() == 32) {
                        if (!zza(256, false)) {
                            Log.w("FileDownloadTask", "Unable to change download task to final state from " + zzg());
                            return;
                        }
                        return;
                    }
                } catch (Throwable e2) {
                    Log.e("FileDownloadTask", "Unable to create firebase storage network request.", e2);
                    this.zzk = e2;
                    zza(64, false);
                    return;
                }
            } while (this.zzh > 0);
            zza(64, false);
        }
    }

    protected void schedule() {
        zzu.zzc(zzj());
    }

    @NonNull
    final /* synthetic */ ProvideError zza() {
        return new TaskSnapshot(this, StorageException.fromExceptionAndHttpCode(this.zzk, this.zzm), this.zzh + this.zzl);
    }
}
