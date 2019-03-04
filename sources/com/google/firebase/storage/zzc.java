package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzp;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageMetadata.Builder;
import org.json.JSONException;

final class zzc implements Runnable {
    private StorageReference zzd;
    private TaskCompletionSource<StorageMetadata> zze;
    private zzf zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxOperationRetryTimeMillis());
    private StorageMetadata zzu;

    public zzc(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<StorageMetadata> taskCompletionSource) {
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(taskCompletionSource);
        this.zzd = storageReference;
        this.zze = taskCompletionSource;
    }

    public final void run() {
        Throwable e;
        String str;
        String str2;
        String valueOf;
        try {
            zzq zzd = zzp.zzb(this.zzd.getStorage().getApp()).zzd(this.zzd.zze());
            this.zzf.zza(zzd, true);
            if (zzd.zzab()) {
                try {
                    this.zzu = new Builder(zzd.zzae(), this.zzd).build();
                } catch (JSONException e2) {
                    e = e2;
                    str = "GetMetadataTask";
                    str2 = "Unable to parse resulting metadata. ";
                    valueOf = String.valueOf(zzd.zzz());
                    Log.e(str, valueOf.length() == 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zze.setException(StorageException.fromException(e));
                    return;
                } catch (RemoteException e3) {
                    e = e3;
                    str = "GetMetadataTask";
                    str2 = "Unable to parse resulting metadata. ";
                    valueOf = String.valueOf(zzd.zzz());
                    if (valueOf.length() == 0) {
                    }
                    Log.e(str, valueOf.length() == 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zze.setException(StorageException.fromException(e));
                    return;
                }
            }
            if (this.zze != null) {
                zzd.zzb(this.zze, this.zzu);
            }
        } catch (Throwable e4) {
            Log.e("GetMetadataTask", "Unable to create firebase storage network request.", e4);
            this.zze.setException(StorageException.fromException(e4));
        }
    }
}
