package com.google.android.gms.internal.measurement;

final class zzih implements Runnable {
    private final /* synthetic */ zzif zzaov;
    private final /* synthetic */ zzie zzaow;

    zzih(zzif zzif, zzie zzie) {
        this.zzaov = zzif;
        this.zzaow = zzie;
    }

    public final void run() {
        this.zzaov.zza(this.zzaow);
        this.zzaov.zzaol = null;
        this.zzaov.zzfx().zzb(null);
    }
}
