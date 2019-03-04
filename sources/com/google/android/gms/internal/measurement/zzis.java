package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzis implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    zzis(zzii zzii, AtomicReference atomicReference, String str, String str2, String str3, zzdz zzdz) {
        this.zzape = zzii;
        this.zzapf = atomicReference;
        this.zzanj = str;
        this.zzanh = str2;
        this.zzani = str3;
        this.zzane = zzdz;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r6 = this;
        r1 = r6.zzapf;
        monitor-enter(r1);
        r0 = r6.zzape;	 Catch:{ RemoteException -> 0x006a }
        r0 = r0.zzaoy;	 Catch:{ RemoteException -> 0x006a }
        if (r0 != 0) goto L_0x0034;
    L_0x000b:
        r0 = r6.zzape;	 Catch:{ RemoteException -> 0x006a }
        r0 = r0.zzge();	 Catch:{ RemoteException -> 0x006a }
        r0 = r0.zzim();	 Catch:{ RemoteException -> 0x006a }
        r2 = "Failed to get conditional properties";
        r3 = r6.zzanj;	 Catch:{ RemoteException -> 0x006a }
        r3 = com.google.android.gms.internal.measurement.zzfg.zzbm(r3);	 Catch:{ RemoteException -> 0x006a }
        r4 = r6.zzanh;	 Catch:{ RemoteException -> 0x006a }
        r5 = r6.zzani;	 Catch:{ RemoteException -> 0x006a }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006a }
        r0 = r6.zzapf;	 Catch:{ RemoteException -> 0x006a }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006a }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006a }
        r0 = r6.zzapf;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
    L_0x0033:
        return;
    L_0x0034:
        r2 = r6.zzanj;	 Catch:{ RemoteException -> 0x006a }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006a }
        if (r2 == 0) goto L_0x005a;
    L_0x003c:
        r2 = r6.zzapf;	 Catch:{ RemoteException -> 0x006a }
        r3 = r6.zzanh;	 Catch:{ RemoteException -> 0x006a }
        r4 = r6.zzani;	 Catch:{ RemoteException -> 0x006a }
        r5 = r6.zzane;	 Catch:{ RemoteException -> 0x006a }
        r0 = r0.zza(r3, r4, r5);	 Catch:{ RemoteException -> 0x006a }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006a }
    L_0x004b:
        r0 = r6.zzape;	 Catch:{ RemoteException -> 0x006a }
        r0.zzcu();	 Catch:{ RemoteException -> 0x006a }
        r0 = r6.zzapf;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
    L_0x0055:
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
        goto L_0x0033;
    L_0x0057:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
        throw r0;
    L_0x005a:
        r2 = r6.zzapf;	 Catch:{ RemoteException -> 0x006a }
        r3 = r6.zzanj;	 Catch:{ RemoteException -> 0x006a }
        r4 = r6.zzanh;	 Catch:{ RemoteException -> 0x006a }
        r5 = r6.zzani;	 Catch:{ RemoteException -> 0x006a }
        r0 = r0.zze(r3, r4, r5);	 Catch:{ RemoteException -> 0x006a }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006a }
        goto L_0x004b;
    L_0x006a:
        r0 = move-exception;
        r2 = r6.zzape;	 Catch:{ all -> 0x0091 }
        r2 = r2.zzge();	 Catch:{ all -> 0x0091 }
        r2 = r2.zzim();	 Catch:{ all -> 0x0091 }
        r3 = "Failed to get conditional properties";
        r4 = r6.zzanj;	 Catch:{ all -> 0x0091 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x0091 }
        r5 = r6.zzanh;	 Catch:{ all -> 0x0091 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0091 }
        r0 = r6.zzapf;	 Catch:{ all -> 0x0091 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0091 }
        r0.set(r2);	 Catch:{ all -> 0x0091 }
        r0 = r6.zzapf;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        goto L_0x0055;
    L_0x0091:
        r0 = move-exception;
        r2 = r6.zzapf;	 Catch:{ all -> 0x0057 }
        r2.notify();	 Catch:{ all -> 0x0057 }
        throw r0;	 Catch:{ all -> 0x0057 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzis.run():void");
    }
}
