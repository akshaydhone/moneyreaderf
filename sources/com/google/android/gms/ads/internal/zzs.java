package com.google.android.gms.ads.internal;

import com.google.android.gms.internal.ads.zzane;
import com.google.android.gms.internal.ads.zzov;

final class zzs implements Runnable {
    private final /* synthetic */ zzq zzwt;
    private final /* synthetic */ zzov zzwu;

    zzs(zzq zzq, zzov zzov) {
        this.zzwt = zzq;
        this.zzwu = zzov;
    }

    public final void run() {
        try {
            if (this.zzwt.zzvw.zzadg != null) {
                this.zzwt.zzvw.zzadg.zza(this.zzwu);
                this.zzwt.zzb(this.zzwu.zzka());
            }
        } catch (Throwable e) {
            zzane.zzd("#007 Could not call remote method.", e);
        }
    }
}
