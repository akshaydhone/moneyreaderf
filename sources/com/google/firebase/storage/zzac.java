package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.internal.firebase_storage.zzf;
import com.google.android.gms.internal.firebase_storage.zzq;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageMetadata.Builder;
import org.json.JSONException;

final class zzac implements Runnable {
    private final StorageMetadata zzcv;
    private final StorageReference zzd;
    private final TaskCompletionSource<StorageMetadata> zze;
    private zzf zzf;
    private StorageMetadata zzu = null;

    public zzac(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<StorageMetadata> taskCompletionSource, @NonNull StorageMetadata storageMetadata) {
        this.zzd = storageReference;
        this.zze = taskCompletionSource;
        this.zzcv = storageMetadata;
        this.zzf = new zzf(this.zzd.getStorage().getApp(), this.zzd.getStorage().getMaxOperationRetryTimeMillis());
    }

    public final void run() {
        Throwable e;
        String str;
        String str2;
        String valueOf;
        try {
            zzq zza = this.zzd.zzd().zza(this.zzd.zze(), this.zzcv.zzb());
            this.zzf.zza(zza, true);
            if (zza.zzab()) {
                try {
                    this.zzu = new Builder(zza.zzae(), this.zzd).build();
                } catch (JSONException e2) {
                    e = e2;
                    str = "UpdateMetadataTask";
                    str2 = "Unable to parse a valid JSON object from resulting metadata:";
                    valueOf = String.valueOf(zza.zzz());
                    Log.e(str, valueOf.length() == 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zze.setException(StorageException.fromException(e));
                    return;
                } catch (RemoteException e3) {
                    e = e3;
                    str = "UpdateMetadataTask";
                    str2 = "Unable to parse a valid JSON object from resulting metadata:";
                    valueOf = String.valueOf(zza.zzz());
                    if (valueOf.length() == 0) {
                    }
                    Log.e(str, valueOf.length() == 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zze.setException(StorageException.fromException(e));
                    return;
                }
            }
            if (this.zze != null) {
                zza.zzb(this.zze, this.zzu);
            }
        } catch (JSONException e4) {
            e = e4;
            Log.e("UpdateMetadataTask", "Unable to create the request from metadata.", e);
            this.zze.setException(StorageException.fromException(e));
        } catch (RemoteException e5) {
            e = e5;
            Log.e("UpdateMetadataTask", "Unable to create the request from metadata.", e);
            this.zze.setException(StorageException.fromException(e));
        }
    }
}
