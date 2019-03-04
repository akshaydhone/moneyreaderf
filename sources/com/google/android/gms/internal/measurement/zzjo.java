package com.google.android.gms.internal.measurement;

final class zzjo extends zzem {
    private final /* synthetic */ zzjr zzapt;
    private final /* synthetic */ zzjn zzapz;

    zzjo(zzjn zzjn, zzhi zzhi, zzjr zzjr) {
        this.zzapz = zzjn;
        this.zzapt = zzjr;
        super(zzhi);
    }

    public final void run() {
        this.zzapz.cancel();
        this.zzapz.zzge().zzit().log("Starting upload from DelayedRunnable");
        this.zzapt.zzks();
    }
}
