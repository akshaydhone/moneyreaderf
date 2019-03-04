package com.google.firebase.storage;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zze;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzk;
import com.google.android.gms.internal.firebase_storage.zzp;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.firebase.storage.StorageMetadata.Builder;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import net.lingala.zip4j.util.InternalZipConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadTask extends StorageTask<TaskSnapshot> {
    private volatile StorageMetadata zzap;
    private final Uri zzcw;
    private final long zzcx;
    private final zze zzcy;
    private final AtomicLong zzcz = new AtomicLong(0);
    private final StorageReference zzd;
    private int zzda = 262144;
    private boolean zzdb;
    private volatile Uri zzdc = null;
    private volatile Exception zzdd = null;
    private volatile String zzde;
    private zzf zzf;
    private volatile Exception zzk = null;
    private volatile int zzm = 0;

    public class TaskSnapshot extends SnapshotBase {
        private final StorageMetadata zzap;
        private final Uri zzdc;
        private final /* synthetic */ UploadTask zzdg;
        private final long zzdh;

        TaskSnapshot(UploadTask uploadTask, Exception exception, long j, Uri uri, StorageMetadata storageMetadata) {
            this.zzdg = uploadTask;
            super(uploadTask, exception);
            this.zzdh = j;
            this.zzdc = uri;
            this.zzap = storageMetadata;
        }

        public long getBytesTransferred() {
            return this.zzdh;
        }

        @Nullable
        @Deprecated
        public Uri getDownloadUrl() {
            StorageMetadata metadata = getMetadata();
            return metadata != null ? metadata.getDownloadUrl() : null;
        }

        @Nullable
        public StorageMetadata getMetadata() {
            return this.zzap;
        }

        public long getTotalByteCount() {
            return this.zzdg.getTotalByteCount();
        }

        @Nullable
        public Uri getUploadSessionUri() {
            return this.zzdc;
        }
    }

    UploadTask(StorageReference storageReference, StorageMetadata storageMetadata, Uri uri, Uri uri2) {
        Context applicationContext;
        Throwable th;
        InputStream inputStream;
        Exception exception;
        String str;
        String str2;
        String valueOf;
        long j;
        InputStream inputStream2;
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(uri);
        this.zzd = storageReference;
        this.zzap = storageMetadata;
        this.zzcw = uri;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxUploadRetryTimeMillis());
        long j2 = -1;
        try {
            applicationContext = this.zzd.getStorage().getApp().getApplicationContext();
            ContentResolver contentResolver = applicationContext.getContentResolver();
            try {
                ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(this.zzcw, InternalZipConstants.READ_MODE);
                if (openFileDescriptor != null) {
                    j2 = openFileDescriptor.getStatSize();
                    openFileDescriptor.close();
                }
            } catch (Throwable e) {
                th = e;
                applicationContext = -1;
                try {
                    Log.w("UploadTask", "NullPointerException during file size calculation.", th);
                    j2 = -1;
                } catch (Exception e2) {
                    inputStream = null;
                    exception = e2;
                    str = "UploadTask";
                    str2 = "could not locate file for uploading:";
                    valueOf = String.valueOf(this.zzcw.toString());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                    this.zzk = exception;
                    j = applicationContext;
                    inputStream2 = inputStream;
                    j2 = j;
                    this.zzcx = j2;
                    this.zzcy = new zze(inputStream2, 262144);
                    this.zzdb = true;
                    this.zzdc = uri2;
                }
            } catch (Throwable e3) {
                th = e3;
                long j3 = -1;
                Throwable th2 = th;
                str2 = "UploadTask";
                String str3 = "could not retrieve file size for upload ";
                String valueOf2 = String.valueOf(this.zzcw.toString());
                Log.w(str2, valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3), th2);
                j2 = j3;
            }
            InputStream openInputStream = contentResolver.openInputStream(this.zzcw);
            if (openInputStream != null) {
                if (j2 == -1) {
                    try {
                        int available = openInputStream.available();
                        if (available >= 0) {
                            j2 = (long) available;
                        }
                    } catch (IOException e4) {
                    }
                }
                try {
                    inputStream2 = new BufferedInputStream(openInputStream);
                } catch (Exception e5) {
                    exception = e5;
                    inputStream = openInputStream;
                    applicationContext = j2;
                    str = "UploadTask";
                    str2 = "could not locate file for uploading:";
                    valueOf = String.valueOf(this.zzcw.toString());
                    if (valueOf.length() != 0) {
                    }
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                    this.zzk = exception;
                    j = applicationContext;
                    inputStream2 = inputStream;
                    j2 = j;
                    this.zzcx = j2;
                    this.zzcy = new zze(inputStream2, 262144);
                    this.zzdb = true;
                    this.zzdc = uri2;
                }
            }
            inputStream2 = openInputStream;
        } catch (Exception e52) {
            Exception exception2 = e52;
            applicationContext = j2;
            inputStream = null;
            exception = exception2;
            str = "UploadTask";
            str2 = "could not locate file for uploading:";
            valueOf = String.valueOf(this.zzcw.toString());
            if (valueOf.length() != 0) {
            }
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            this.zzk = exception;
            j = applicationContext;
            inputStream2 = inputStream;
            j2 = j;
            this.zzcx = j2;
            this.zzcy = new zze(inputStream2, 262144);
            this.zzdb = true;
            this.zzdc = uri2;
        }
        this.zzcx = j2;
        this.zzcy = new zze(inputStream2, 262144);
        this.zzdb = true;
        this.zzdc = uri2;
    }

    UploadTask(StorageReference storageReference, StorageMetadata storageMetadata, InputStream inputStream) {
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(inputStream);
        this.zzcx = -1;
        this.zzd = storageReference;
        this.zzap = storageMetadata;
        this.zzcy = new zze(inputStream, 262144);
        this.zzdb = false;
        this.zzcw = null;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxUploadRetryTimeMillis());
    }

    UploadTask(StorageReference storageReference, StorageMetadata storageMetadata, byte[] bArr) {
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(bArr);
        this.zzcx = (long) bArr.length;
        this.zzd = storageReference;
        this.zzap = storageMetadata;
        this.zzcw = null;
        this.zzcy = new zze(new ByteArrayInputStream(bArr), 262144);
        this.zzdb = true;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxUploadRetryTimeMillis());
    }

    private final boolean zza(boolean z) {
        try {
            zzq zzb = this.zzd.zzd().zzb(this.zzd.zze(), this.zzdc.toString());
            if ("final".equals(this.zzde)) {
                return false;
            }
            if (z) {
                if (!zzc(zzb)) {
                    return false;
                }
            } else if (!zzb(zzb)) {
                return false;
            }
            if ("final".equals(zzb.zzh("X-Goog-Upload-Status"))) {
                this.zzk = new IOException("The server has terminated the upload session");
                return false;
            }
            Object zzh = zzb.zzh("X-Goog-Upload-Size-Received");
            long parseLong = !TextUtils.isEmpty(zzh) ? Long.parseLong(zzh) : 0;
            long j = this.zzcz.get();
            if (j > parseLong) {
                this.zzk = new IOException("Unexpected error. The server lost a chunk update.");
                return false;
            }
            if (j < parseLong) {
                try {
                    if (((long) this.zzcy.zzb((int) (parseLong - j))) != parseLong - j) {
                        this.zzk = new IOException("Unexpected end of stream encountered.");
                        return false;
                    } else if (!this.zzcz.compareAndSet(j, parseLong)) {
                        Log.e("UploadTask", "Somehow, the uploaded bytes changed during an uploaded.  This should nothappen");
                        this.zzk = new IllegalStateException("uploaded bytes changed unexpectedly.");
                        return false;
                    }
                } catch (Throwable e) {
                    Log.e("UploadTask", "Unable to recover position in Stream during resumable upload", e);
                    this.zzk = e;
                    return false;
                }
            }
            return true;
        } catch (Throwable e2) {
            Log.e("UploadTask", "Unable to recover status during resumable upload", e2);
            this.zzk = e2;
            return false;
        }
    }

    private final boolean zzb(zzq zzq) {
        zzq.zza(zzk.zza(this.zzd.getStorage().getApp()), this.zzd.getStorage().getApp().getApplicationContext());
        return zzd(zzq);
    }

    private final boolean zzc(zzq zzq) {
        this.zzf.zza(zzq, true);
        return zzd(zzq);
    }

    private final boolean zzd(zzq zzq) {
        int resultCode = zzq.getResultCode();
        if (zzf.zze(resultCode)) {
            resultCode = -2;
        }
        this.zzm = resultCode;
        this.zzdd = zzq.getException();
        this.zzde = zzq.zzh("X-Goog-Upload-Status");
        resultCode = this.zzm;
        Object obj = (resultCode == 308 || (resultCode >= 200 && resultCode < 300)) ? 1 : null;
        return obj != null && this.zzdd == null;
    }

    private final boolean zzr() {
        if (zzg() == 128) {
            return false;
        }
        if (Thread.interrupted()) {
            this.zzk = new InterruptedException();
            zza(64, false);
            return false;
        } else if (zzg() == 32) {
            zza(256, false);
            return false;
        } else if (zzg() == 8) {
            zza(16, false);
            return false;
        } else if (!zzs()) {
            return false;
        } else {
            if (this.zzdc == null) {
                if (this.zzk == null) {
                    this.zzk = new IllegalStateException("Unable to obtain an upload URL.");
                }
                zza(64, false);
                return false;
            } else if (this.zzk != null) {
                zza(64, false);
                return false;
            } else {
                boolean z = this.zzdd != null || this.zzm < 200 || this.zzm >= 300;
                if (!z || zza(true)) {
                    return true;
                }
                if (!zzs()) {
                    return false;
                }
                zza(64, false);
                return false;
            }
        }
    }

    private final boolean zzs() {
        if (!"final".equals(this.zzde)) {
            return true;
        }
        if (this.zzk == null) {
            this.zzk = new IOException("The server has terminated the upload session", this.zzdd);
        }
        zza(64, false);
        return false;
    }

    final StorageReference getStorage() {
        return this.zzd;
    }

    final long getTotalByteCount() {
        return this.zzcx;
    }

    protected void onCanceled() {
        zzq zza;
        this.zzf.cancel();
        if (this.zzdc != null) {
            try {
                zza = this.zzd.zzd().zza(this.zzd.zze(), this.zzdc.toString());
            } catch (Throwable e) {
                Log.e("UploadTask", "Unable to create chunk upload request", e);
            }
            if (zza != null) {
                zzu.zza(new zzad(this, zza));
            }
            this.zzk = StorageException.fromErrorStatus(Status.RESULT_CANCELED);
            super.onCanceled();
        }
        zza = null;
        if (zza != null) {
            zzu.zza(new zzad(this, zza));
        }
        this.zzk = StorageException.fromErrorStatus(Status.RESULT_CANCELED);
        super.onCanceled();
    }

    protected void resetState() {
        this.zzk = null;
        this.zzdd = null;
        this.zzm = 0;
        this.zzde = null;
    }

    final void run() {
        String contentType;
        Throwable e;
        boolean zzr;
        int min;
        Throwable th;
        String str;
        String str2;
        JSONObject jSONObject = null;
        this.zzf.reset();
        if (zza(4, false)) {
            if (this.zzd.getParent() == null) {
                this.zzk = new IllegalArgumentException("Cannot upload to getRoot. You should upload to a storage location such as .getReference('image.png').putFile...");
            }
            if (this.zzk == null) {
                zzq zza;
                if (this.zzdc == null) {
                    if (this.zzap != null) {
                        contentType = this.zzap.getContentType();
                    } else {
                        CharSequence charSequence = null;
                    }
                    if (this.zzcw != null && TextUtils.isEmpty(r0)) {
                        contentType = this.zzd.getStorage().getApp().getApplicationContext().getContentResolver().getType(this.zzcw);
                    }
                    if (TextUtils.isEmpty(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    try {
                        zzp zzd = this.zzd.zzd();
                        Uri zze = this.zzd.zze();
                        if (this.zzap != null) {
                            jSONObject = this.zzap.zzb();
                        }
                        zzq zza2 = zzd.zza(zze, jSONObject, contentType);
                        if (zzc(zza2)) {
                            Object zzh = zza2.zzh("X-Goog-Upload-URL");
                            if (!TextUtils.isEmpty(zzh)) {
                                this.zzdc = Uri.parse(zzh);
                            }
                        }
                    } catch (JSONException e2) {
                        e = e2;
                        Log.e("UploadTask", "Unable to create a network request from metadata", e);
                        this.zzk = e;
                        zzr = zzr();
                        while (zzr) {
                            try {
                                this.zzcy.zzc(this.zzda);
                                min = Math.min(this.zzda, this.zzcy.available());
                                try {
                                    zza = this.zzd.zzd().zza(this.zzd.zze(), this.zzdc.toString(), this.zzcy.zzv(), this.zzcz.get(), min, this.zzcy.isFinished());
                                    if (zzb(zza)) {
                                        this.zzda = 262144;
                                        Log.d("UploadTask", "Resetting chunk size to " + this.zzda);
                                    } else {
                                        this.zzcz.getAndAdd((long) min);
                                        if (this.zzcy.isFinished()) {
                                            this.zzcy.zzb(min);
                                            if (this.zzda < 33554432) {
                                                this.zzda <<= 1;
                                                Log.d("UploadTask", "Increasing chunk size to " + this.zzda);
                                            }
                                        } else {
                                            try {
                                                this.zzap = new Builder(zza.zzae(), this.zzd).build();
                                                zza(4, false);
                                                zza(128, false);
                                            } catch (Throwable e3) {
                                                th = e3;
                                                str = "UploadTask";
                                                str2 = "Unable to parse resulting metadata from upload:";
                                                contentType = String.valueOf(zza.zzz());
                                                Log.e(str, contentType.length() == 0 ? str2.concat(contentType) : new String(str2), th);
                                                this.zzk = th;
                                                zzr = zzr();
                                                if (zzr) {
                                                    zza(4, false);
                                                }
                                            } catch (Throwable e32) {
                                                th = e32;
                                                str = "UploadTask";
                                                str2 = "Unable to parse resulting metadata from upload:";
                                                contentType = String.valueOf(zza.zzz());
                                                if (contentType.length() == 0) {
                                                }
                                                Log.e(str, contentType.length() == 0 ? str2.concat(contentType) : new String(str2), th);
                                                this.zzk = th;
                                                zzr = zzr();
                                                if (zzr) {
                                                    zza(4, false);
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable e322) {
                                    Log.e("UploadTask", "Unable to create chunk upload request", e322);
                                    this.zzk = e322;
                                }
                            } catch (Throwable e3222) {
                                Log.e("UploadTask", "Unable to read bytes for uploading", e3222);
                                this.zzk = e3222;
                            }
                            zzr = zzr();
                            if (zzr) {
                                zza(4, false);
                            }
                        }
                        if (this.zzdb) {
                            return;
                        }
                        return;
                    } catch (RemoteException e4) {
                        e3222 = e4;
                        Log.e("UploadTask", "Unable to create a network request from metadata", e3222);
                        this.zzk = e3222;
                        zzr = zzr();
                        while (zzr) {
                            this.zzcy.zzc(this.zzda);
                            min = Math.min(this.zzda, this.zzcy.available());
                            zza = this.zzd.zzd().zza(this.zzd.zze(), this.zzdc.toString(), this.zzcy.zzv(), this.zzcz.get(), min, this.zzcy.isFinished());
                            if (zzb(zza)) {
                                this.zzda = 262144;
                                Log.d("UploadTask", "Resetting chunk size to " + this.zzda);
                            } else {
                                this.zzcz.getAndAdd((long) min);
                                if (this.zzcy.isFinished()) {
                                    this.zzcy.zzb(min);
                                    if (this.zzda < 33554432) {
                                        this.zzda <<= 1;
                                        Log.d("UploadTask", "Increasing chunk size to " + this.zzda);
                                    }
                                } else {
                                    this.zzap = new Builder(zza.zzae(), this.zzd).build();
                                    zza(4, false);
                                    zza(128, false);
                                }
                            }
                            zzr = zzr();
                            if (zzr) {
                                zza(4, false);
                            }
                        }
                        if (this.zzdb) {
                            return;
                        }
                        return;
                    }
                }
                zza(false);
                zzr = zzr();
                while (zzr) {
                    this.zzcy.zzc(this.zzda);
                    min = Math.min(this.zzda, this.zzcy.available());
                    zza = this.zzd.zzd().zza(this.zzd.zze(), this.zzdc.toString(), this.zzcy.zzv(), this.zzcz.get(), min, this.zzcy.isFinished());
                    if (zzb(zza)) {
                        this.zzda = 262144;
                        Log.d("UploadTask", "Resetting chunk size to " + this.zzda);
                    } else {
                        this.zzcz.getAndAdd((long) min);
                        if (this.zzcy.isFinished()) {
                            this.zzcy.zzb(min);
                            if (this.zzda < 33554432) {
                                this.zzda <<= 1;
                                Log.d("UploadTask", "Increasing chunk size to " + this.zzda);
                            }
                        } else {
                            this.zzap = new Builder(zza.zzae(), this.zzd).build();
                            zza(4, false);
                            zza(128, false);
                        }
                    }
                    zzr = zzr();
                    if (zzr) {
                        zza(4, false);
                    }
                }
                if (this.zzdb && zzg() != 16) {
                    try {
                        this.zzcy.close();
                        return;
                    } catch (Throwable e32222) {
                        Log.e("UploadTask", "Unable to close stream.", e32222);
                        return;
                    }
                }
                return;
            }
            return;
        }
        Log.d("UploadTask", "The upload cannot continue as it is not in a valid state.");
    }

    protected void schedule() {
        zzu.zzb(zzj());
    }

    @NonNull
    final /* synthetic */ ProvideError zza() {
        return new TaskSnapshot(this, StorageException.fromExceptionAndHttpCode(this.zzk != null ? this.zzk : this.zzdd, this.zzm), this.zzcz.get(), this.zzdc, this.zzap);
    }
}
