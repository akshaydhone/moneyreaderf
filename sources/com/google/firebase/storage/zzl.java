package com.google.firebase.storage;

import com.google.android.gms.tasks.OnCompleteListener;

final /* synthetic */ class zzl implements zzab {
    private final StorageTask zzbg;

    zzl(StorageTask storageTask) {
        this.zzbg = storageTask;
    }

    public final void zza(Object obj, Object obj2) {
        StorageTask storageTask = this.zzbg;
        OnCompleteListener onCompleteListener = (OnCompleteListener) obj;
        zzt.zzm().zzb(storageTask);
        onCompleteListener.onComplete(storageTask);
    }
}
