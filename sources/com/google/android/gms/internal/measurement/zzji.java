package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

final class zzji extends zzem {
    private final /* synthetic */ zzjh zzapx;

    zzji(zzjh zzjh, zzhi zzhi) {
        this.zzapx = zzjh;
        super(zzhi);
    }

    @WorkerThread
    public final void run() {
        zzhg zzhg = this.zzapx;
        zzhg.zzab();
        zzhg.zzge().zzit().zzg("Session started, time", Long.valueOf(zzhg.zzbt().elapsedRealtime()));
        zzhg.zzgf().zzakk.set(false);
        zzhg.zzfu().zza("auto", "_s", new Bundle());
        zzhg.zzgf().zzakl.set(zzhg.zzbt().currentTimeMillis());
    }
}
