package com.google.firebase.storage;

import com.google.android.gms.internal.firebase_storage.zzk;
import com.google.android.gms.internal.firebase_storage.zzq;

final class zzad implements Runnable {
    private final /* synthetic */ zzq zzdf;
    private final /* synthetic */ UploadTask zzdg;

    zzad(UploadTask uploadTask, zzq zzq) {
        this.zzdg = uploadTask;
        this.zzdf = zzq;
    }

    public final void run() {
        this.zzdf.zza(zzk.zza(this.zzdg.zzd.getStorage().getApp()), this.zzdg.zzd.getStorage().getApp().getApplicationContext());
    }
}
