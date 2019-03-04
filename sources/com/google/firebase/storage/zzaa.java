package com.google.firebase.storage;

final class zzaa implements Runnable {
    private final /* synthetic */ zzx zzcs;
    private final /* synthetic */ ProvideError zzct;
    private final /* synthetic */ Object zzcu;

    zzaa(zzx zzx, Object obj, ProvideError provideError) {
        this.zzcs = zzx;
        this.zzcu = obj;
        this.zzct = provideError;
    }

    public final void run() {
        this.zzcs.zzcq.zza(this.zzcu, this.zzct);
    }
}
