package com.google.firebase.storage;

final class zzz implements Runnable {
    private final /* synthetic */ Object zzcr;
    private final /* synthetic */ zzx zzcs;
    private final /* synthetic */ ProvideError zzct;

    zzz(zzx zzx, Object obj, ProvideError provideError) {
        this.zzcs = zzx;
        this.zzcr = obj;
        this.zzct = provideError;
    }

    public final void run() {
        this.zzcs.zzcq.zza(this.zzcr, this.zzct);
    }
}
