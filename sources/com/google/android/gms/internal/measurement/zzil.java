package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzil implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    zzil(zzii zzii, AtomicReference atomicReference, zzdz zzdz) {
        this.zzape = zzii;
        this.zzapf = atomicReference;
        this.zzane = zzdz;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r4 = this;
        r1 = r4.zzapf;
        monitor-enter(r1);
        r0 = r4.zzape;	 Catch:{ RemoteException -> 0x0059 }
        r0 = r0.zzaoy;	 Catch:{ RemoteException -> 0x0059 }
        if (r0 != 0) goto L_0x0021;
    L_0x000b:
        r0 = r4.zzape;	 Catch:{ RemoteException -> 0x0059 }
        r0 = r0.zzge();	 Catch:{ RemoteException -> 0x0059 }
        r0 = r0.zzim();	 Catch:{ RemoteException -> 0x0059 }
        r2 = "Failed to get app instance id";
        r0.log(r2);	 Catch:{ RemoteException -> 0x0059 }
        r0 = r4.zzapf;	 Catch:{ all -> 0x0056 }
        r0.notify();	 Catch:{ all -> 0x0056 }
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
    L_0x0020:
        return;
    L_0x0021:
        r2 = r4.zzapf;	 Catch:{ RemoteException -> 0x0059 }
        r3 = r4.zzane;	 Catch:{ RemoteException -> 0x0059 }
        r0 = r0.zzc(r3);	 Catch:{ RemoteException -> 0x0059 }
        r2.set(r0);	 Catch:{ RemoteException -> 0x0059 }
        r0 = r4.zzapf;	 Catch:{ RemoteException -> 0x0059 }
        r0 = r0.get();	 Catch:{ RemoteException -> 0x0059 }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x0059 }
        if (r0 == 0) goto L_0x004a;
    L_0x0036:
        r2 = r4.zzape;	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.zzfu();	 Catch:{ RemoteException -> 0x0059 }
        r2.zzbr(r0);	 Catch:{ RemoteException -> 0x0059 }
        r2 = r4.zzape;	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.zzgf();	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.zzakb;	 Catch:{ RemoteException -> 0x0059 }
        r2.zzbs(r0);	 Catch:{ RemoteException -> 0x0059 }
    L_0x004a:
        r0 = r4.zzape;	 Catch:{ RemoteException -> 0x0059 }
        r0.zzcu();	 Catch:{ RemoteException -> 0x0059 }
        r0 = r4.zzapf;	 Catch:{ all -> 0x0056 }
        r0.notify();	 Catch:{ all -> 0x0056 }
    L_0x0054:
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
        goto L_0x0020;
    L_0x0056:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
        throw r0;
    L_0x0059:
        r0 = move-exception;
        r2 = r4.zzape;	 Catch:{ all -> 0x006f }
        r2 = r2.zzge();	 Catch:{ all -> 0x006f }
        r2 = r2.zzim();	 Catch:{ all -> 0x006f }
        r3 = "Failed to get app instance id";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x006f }
        r0 = r4.zzapf;	 Catch:{ all -> 0x0056 }
        r0.notify();	 Catch:{ all -> 0x0056 }
        goto L_0x0054;
    L_0x006f:
        r0 = move-exception;
        r2 = r4.zzapf;	 Catch:{ all -> 0x0056 }
        r2.notify();	 Catch:{ all -> 0x0056 }
        throw r0;	 Catch:{ all -> 0x0056 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzil.run():void");
    }
}
