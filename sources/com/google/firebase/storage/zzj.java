package com.google.firebase.storage;

import com.google.android.gms.tasks.OnSuccessListener;

final /* synthetic */ class zzj implements zzab {
    private final StorageTask zzbg;

    zzj(StorageTask storageTask) {
        this.zzbg = storageTask;
    }

    public final void zza(Object obj, Object obj2) {
        OnSuccessListener onSuccessListener = (OnSuccessListener) obj;
        ProvideError provideError = (ProvideError) obj2;
        zzt.zzm().zzb(this.zzbg);
        onSuccessListener.onSuccess(provideError);
    }
}
