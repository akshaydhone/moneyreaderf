package com.google.android.gms.internal.ads;

final class zzvm implements Runnable {
    private final /* synthetic */ zzvw zzbqi;
    private final /* synthetic */ zzuu zzbqj;
    private final /* synthetic */ zzvf zzbqk;

    zzvm(zzvf zzvf, zzvw zzvw, zzuu zzuu) {
        this.zzbqk = zzvf;
        this.zzbqi = zzvw;
        this.zzbqj = zzuu;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r3 = this;
        r0 = r3.zzbqk;
        r1 = r0.mLock;
        monitor-enter(r1);
        r0 = r3.zzbqi;	 Catch:{ all -> 0x0035 }
        r0 = r0.getStatus();	 Catch:{ all -> 0x0035 }
        r2 = -1;
        if (r0 == r2) goto L_0x0019;
    L_0x0010:
        r0 = r3.zzbqi;	 Catch:{ all -> 0x0035 }
        r0 = r0.getStatus();	 Catch:{ all -> 0x0035 }
        r2 = 1;
        if (r0 != r2) goto L_0x001b;
    L_0x0019:
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
    L_0x001a:
        return;
    L_0x001b:
        r0 = r3.zzbqi;	 Catch:{ all -> 0x0035 }
        r0.reject();	 Catch:{ all -> 0x0035 }
        r0 = com.google.android.gms.internal.ads.zzaoe.zzcvy;	 Catch:{ all -> 0x0035 }
        r2 = r3.zzbqj;	 Catch:{ all -> 0x0035 }
        r2.getClass();	 Catch:{ all -> 0x0035 }
        r2 = com.google.android.gms.internal.ads.zzvn.zza(r2);	 Catch:{ all -> 0x0035 }
        r0.execute(r2);	 Catch:{ all -> 0x0035 }
        r0 = "Could not receive loaded message in a timely manner. Rejecting.";
        com.google.android.gms.internal.ads.zzakb.m29v(r0);	 Catch:{ all -> 0x0035 }
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
        goto L_0x001a;
    L_0x0035:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.ads.zzvm.run():void");
    }
}
