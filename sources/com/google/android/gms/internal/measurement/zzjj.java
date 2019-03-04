package com.google.android.gms.internal.measurement;

import android.support.annotation.WorkerThread;

final class zzjj extends zzem {
    private final /* synthetic */ zzjh zzapx;

    zzjj(zzjh zzjh, zzhi zzhi) {
        this.zzapx = zzjh;
        super(zzhi);
    }

    @WorkerThread
    public final void run() {
        this.zzapx.zzkk();
    }
}
