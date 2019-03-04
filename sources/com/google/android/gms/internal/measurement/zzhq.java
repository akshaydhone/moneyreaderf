package com.google.android.gms.internal.measurement;

final class zzhq implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaoa;

    zzhq(zzhk zzhk, long j) {
        this.zzanw = zzhk;
        this.zzaoa = j;
    }

    public final void run() {
        boolean z = true;
        zzhg zzhg = this.zzanw;
        long j = this.zzaoa;
        zzhg.zzab();
        zzhg.zzch();
        zzhg.zzge().zzis().log("Resetting analytics data (FE)");
        zzhg.zzgc().zzkj();
        if (zzhg.zzgg().zzba(zzhg.zzfv().zzah())) {
            zzhg.zzgf().zzajz.set(j);
        }
        boolean isEnabled = zzhg.zzacw.isEnabled();
        if (!zzhg.zzgg().zzhg()) {
            zzhg.zzgf().zzh(!isEnabled);
        }
        zzhg.zzfx().resetAnalyticsData();
        if (isEnabled) {
            z = false;
        }
        zzhg.zzanu = z;
    }
}
