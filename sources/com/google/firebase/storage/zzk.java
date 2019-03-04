package com.google.firebase.storage;

import com.google.android.gms.tasks.OnFailureListener;

final /* synthetic */ class zzk implements zzab {
    private final StorageTask zzbg;

    zzk(StorageTask storageTask) {
        this.zzbg = storageTask;
    }

    public final void zza(Object obj, Object obj2) {
        OnFailureListener onFailureListener = (OnFailureListener) obj;
        ProvideError provideError = (ProvideError) obj2;
        zzt.zzm().zzb(this.zzbg);
        onFailureListener.onFailure(provideError.getError());
    }
}
