package com.google.firebase.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class StreamDownloadTask extends StorageTask<TaskSnapshot> {
    private StreamProcessor zzca;
    private long zzcb;
    private InputStream zzcc;
    private zzq zzcd;
    private StorageReference zzd;
    private zzf zzf;
    private long zzh;
    private long zzi = -1;
    private String zzj;
    private volatile Exception zzk = null;
    private volatile int zzm = 0;

    public interface StreamProcessor {
        void doInBackground(TaskSnapshot taskSnapshot, InputStream inputStream) throws IOException;
    }

    static class zza extends InputStream {
        @Nullable
        private StreamDownloadTask zzcf;
        @Nullable
        private InputStream zzcg;
        private Callable<InputStream> zzch;
        private IOException zzci;
        private int zzcj;
        private int zzck;
        private boolean zzcl;

        zza(@NonNull Callable<InputStream> callable, @Nullable StreamDownloadTask streamDownloadTask) {
            this.zzcf = streamDownloadTask;
            this.zzch = callable;
        }

        private final void zza(long j) {
            if (this.zzcf != null) {
                this.zzcf.zza(j);
            }
            this.zzcj = (int) (((long) this.zzcj) + j);
        }

        private final void zzo() throws IOException {
            if (this.zzcf != null && this.zzcf.zzg() == 32) {
                throw new zza();
            }
        }

        private final boolean zzp() throws IOException {
            zzo();
            if (this.zzci != null) {
                try {
                    if (this.zzcg != null) {
                        this.zzcg.close();
                    }
                } catch (IOException e) {
                }
                this.zzcg = null;
                if (this.zzck == this.zzcj) {
                    Log.i("StreamDownloadTask", "Encountered exception during stream operation. Aborting.", this.zzci);
                    return false;
                }
                Log.i("StreamDownloadTask", "Encountered exception during stream operation. Retrying at " + this.zzcj, this.zzci);
                this.zzck = this.zzcj;
                this.zzci = null;
            }
            if (this.zzcl) {
                throw new IOException("Can't perform operation on closed stream");
            }
            if (this.zzcg == null) {
                try {
                    this.zzcg = (InputStream) this.zzch.call();
                } catch (Throwable e2) {
                    if (e2 instanceof IOException) {
                        throw ((IOException) e2);
                    }
                    throw new IOException("Unable to open stream", e2);
                }
            }
            return true;
        }

        public final int available() throws IOException {
            while (zzp()) {
                try {
                    return this.zzcg.available();
                } catch (IOException e) {
                    this.zzci = e;
                }
            }
            throw this.zzci;
        }

        public final void close() throws IOException {
            if (this.zzcg != null) {
                this.zzcg.close();
            }
            this.zzcl = true;
            if (!(this.zzcf == null || this.zzcf.zzcd == null)) {
                this.zzcf.zzcd.zzw();
                this.zzcf.zzcd = null;
            }
            zzo();
        }

        public final void mark(int i) {
        }

        public final boolean markSupported() {
            return false;
        }

        public final int read() throws IOException {
            while (zzp()) {
                try {
                    int read = this.zzcg.read();
                    if (read != -1) {
                        zza(1);
                    }
                    return read;
                } catch (IOException e) {
                    this.zzci = e;
                }
            }
            throw this.zzci;
        }

        public final int read(@NonNull byte[] bArr, int i, int i2) throws IOException {
            IOException iOException;
            int i3 = 0;
            while (zzp()) {
                while (((long) i2) > PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                    int read;
                    int i4;
                    try {
                        read = this.zzcg.read(bArr, i, 262144);
                        if (read == -1) {
                            return i3 == 0 ? -1 : i3;
                        } else {
                            i4 = i3 + read;
                            i += read;
                            i2 -= read;
                            try {
                                zza((long) read);
                                zzo();
                                i3 = i4;
                            } catch (IOException e) {
                                IOException iOException2 = e;
                                i3 = i4;
                                iOException = iOException2;
                            }
                        }
                    } catch (IOException e2) {
                        iOException = e2;
                    }
                }
                if (i2 > 0) {
                    read = this.zzcg.read(bArr, i, i2);
                    if (read == -1) {
                        return i3 == 0 ? -1 : i3;
                    } else {
                        i += read;
                        i4 = i3 + read;
                        i2 -= read;
                        zza((long) read);
                        i3 = i4;
                    }
                }
                if (i2 == 0) {
                    return i3;
                }
            }
            throw this.zzci;
            this.zzci = iOException;
        }

        public final long skip(long j) throws IOException {
            int i = 0;
            while (zzp()) {
                int i2 = i;
                while (j > PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                    long skip;
                    try {
                        skip = this.zzcg.skip(PlaybackStateCompat.ACTION_SET_REPEAT_MODE);
                        if (skip < 0) {
                            return i2 == 0 ? -1 : (long) i2;
                        } else {
                            i2 = (int) (((long) i2) + skip);
                            j -= skip;
                            zza(skip);
                            zzo();
                        }
                    } catch (IOException e) {
                        IOException iOException = e;
                        i = i2;
                        this.zzci = iOException;
                    }
                }
                if (j > 0) {
                    skip = this.zzcg.skip(j);
                    if (skip < 0) {
                        return i2 == 0 ? -1 : (long) i2;
                    } else {
                        i2 = (int) (((long) i2) + skip);
                        j -= skip;
                        zza(skip);
                    }
                }
                i = i2;
                if (j == 0) {
                    return (long) i;
                }
            }
            throw this.zzci;
        }
    }

    public class TaskSnapshot extends SnapshotBase {
        private final /* synthetic */ StreamDownloadTask zzce;
        private final long zzh;

        TaskSnapshot(@Nullable StreamDownloadTask streamDownloadTask, Exception exception, long j) {
            this.zzce = streamDownloadTask;
            super(streamDownloadTask, exception);
            this.zzh = j;
        }

        public long getBytesTransferred() {
            return this.zzh;
        }

        public InputStream getStream() {
            return this.zzce.zzcc;
        }

        public long getTotalByteCount() {
            return this.zzce.getTotalBytes();
        }
    }

    StreamDownloadTask(@NonNull StorageReference storageReference) {
        this.zzd = storageReference;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxDownloadRetryTimeMillis());
    }

    private final InputStream zzn() throws Exception {
        this.zzf.reset();
        if (this.zzcd != null) {
            this.zzcd.zzw();
        }
        try {
            this.zzcd = this.zzd.zzd().zza(this.zzd.zze(), this.zzh);
            this.zzf.zza(this.zzcd, false);
            this.zzm = this.zzcd.getResultCode();
            this.zzk = this.zzcd.getException() != null ? this.zzcd.getException() : this.zzk;
            int i = this.zzm;
            boolean z = i == 308 || (i >= 200 && i < 300);
            z = z && this.zzk == null && zzg() == 4;
            if (z) {
                Object zzh = this.zzcd.zzh("ETag");
                if (TextUtils.isEmpty(zzh) || this.zzj == null || this.zzj.equals(zzh)) {
                    this.zzj = zzh;
                    if (this.zzi == -1) {
                        this.zzi = (long) this.zzcd.zzac();
                    }
                    return this.zzcd.getStream();
                }
                this.zzm = 409;
                throw new IOException("The ETag on the server changed.");
            }
            throw new IOException("Could not open resulting stream.");
        } catch (Throwable e) {
            Log.e("StreamDownloadTask", "Unable to create firebase storage network request.", e);
            throw e;
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

    protected void onProgress() {
        this.zzcb = this.zzh;
    }

    public boolean pause() {
        throw new UnsupportedOperationException("this operation is not supported on StreamDownloadTask.");
    }

    public boolean resume() {
        throw new UnsupportedOperationException("this operation is not supported on StreamDownloadTask.");
    }

    final void run() {
        if (this.zzk != null) {
            zza(64, false);
        } else if (zza(4, false)) {
            zza zza = new zza(new zzw(this), this);
            this.zzcc = new BufferedInputStream(zza);
            try {
                zza.zzp();
                if (this.zzca != null) {
                    try {
                        this.zzca.doInBackground((TaskSnapshot) zzh(), this.zzcc);
                    } catch (Throwable e) {
                        Log.w("StreamDownloadTask", "Exception occurred calling doInBackground.", e);
                        this.zzk = e;
                    }
                }
            } catch (Throwable e2) {
                Log.d("StreamDownloadTask", "Initial opening of Stream failed", e2);
                this.zzk = e2;
            }
            if (this.zzcc == null) {
                this.zzcd.zzw();
                this.zzcd = null;
            }
            boolean z = this.zzk == null && zzg() == 4;
            if (z) {
                zza(4, false);
                zza(128, false);
                return;
            }
            if (!zza(zzg() == 32 ? 256 : 64, false)) {
                Log.w("StreamDownloadTask", "Unable to change download task to final state from " + zzg());
            }
        }
    }

    protected void schedule() {
        zzu.zzc(zzj());
    }

    @NonNull
    final /* synthetic */ ProvideError zza() {
        return new TaskSnapshot(this, StorageException.fromExceptionAndHttpCode(this.zzk, this.zzm), this.zzcb);
    }

    final StreamDownloadTask zza(@NonNull StreamProcessor streamProcessor) {
        Preconditions.checkNotNull(streamProcessor);
        Preconditions.checkState(this.zzca == null);
        this.zzca = streamProcessor;
        return this;
    }

    final void zza(long j) {
        this.zzh += j;
        if (this.zzcb + PlaybackStateCompat.ACTION_SET_REPEAT_MODE > this.zzh) {
            return;
        }
        if (zzg() == 4) {
            zza(4, false);
        } else {
            this.zzcb = this.zzh;
        }
    }
}
