package com.google.firebase.storage;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzp;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzb implements Runnable {
    private StorageReference zzd;
    private TaskCompletionSource<Void> zze;
    private zzf zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxOperationRetryTimeMillis());

    public zzb(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<Void> taskCompletionSource) {
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(taskCompletionSource);
        this.zzd = storageReference;
        this.zze = taskCompletionSource;
    }

    public final void run() {
        try {
            zzq zzc = zzp.zzb(this.zzd.getStorage().getApp()).zzc(this.zzd.zze());
            this.zzf.zza(zzc, true);
            zzc.zzb(this.zze, null);
        } catch (Throwable e) {
            Log.e("DeleteStorageTask", "Unable to create Firebase Storage network request.", e);
            this.zze.setException(StorageException.fromException(e));
        }
    }
}
