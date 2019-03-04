package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzit implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ boolean zzanz;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    zzit(zzii zzii, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzdz zzdz) {
        this.zzape = zzii;
        this.zzapf = atomicReference;
        this.zzanj = str;
        this.zzanh = str2;
        this.zzani = str3;
        this.zzanz = z;
        this.zzane = zzdz;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzapf;
        monitor-enter(r1);
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006e }
        r0 = r0.zzaoy;	 Catch:{ RemoteException -> 0x006e }
        if (r0 != 0) goto L_0x0034;
    L_0x000b:
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006e }
        r0 = r0.zzge();	 Catch:{ RemoteException -> 0x006e }
        r0 = r0.zzim();	 Catch:{ RemoteException -> 0x006e }
        r2 = "Failed to get user properties";
        r3 = r7.zzanj;	 Catch:{ RemoteException -> 0x006e }
        r3 = com.google.android.gms.internal.measurement.zzfg.zzbm(r3);	 Catch:{ RemoteException -> 0x006e }
        r4 = r7.zzanh;	 Catch:{ RemoteException -> 0x006e }
        r5 = r7.zzani;	 Catch:{ RemoteException -> 0x006e }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006e }
        r0 = r7.zzapf;	 Catch:{ RemoteException -> 0x006e }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006e }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006e }
        r0 = r7.zzapf;	 Catch:{ all -> 0x0059 }
        r0.notify();	 Catch:{ all -> 0x0059 }
        monitor-exit(r1);	 Catch:{ all -> 0x0059 }
    L_0x0033:
        return;
    L_0x0034:
        r2 = r7.zzanj;	 Catch:{ RemoteException -> 0x006e }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006e }
        if (r2 == 0) goto L_0x005c;
    L_0x003c:
        r2 = r7.zzapf;	 Catch:{ RemoteException -> 0x006e }
        r3 = r7.zzanh;	 Catch:{ RemoteException -> 0x006e }
        r4 = r7.zzani;	 Catch:{ RemoteException -> 0x006e }
        r5 = r7.zzanz;	 Catch:{ RemoteException -> 0x006e }
        r6 = r7.zzane;	 Catch:{ RemoteException -> 0x006e }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006e }
    L_0x004d:
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006e }
        r0.zzcu();	 Catch:{ RemoteException -> 0x006e }
        r0 = r7.zzapf;	 Catch:{ all -> 0x0059 }
        r0.notify();	 Catch:{ all -> 0x0059 }
    L_0x0057:
        monitor-exit(r1);	 Catch:{ all -> 0x0059 }
        goto L_0x0033;
    L_0x0059:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0059 }
        throw r0;
    L_0x005c:
        r2 = r7.zzapf;	 Catch:{ RemoteException -> 0x006e }
        r3 = r7.zzanj;	 Catch:{ RemoteException -> 0x006e }
        r4 = r7.zzanh;	 Catch:{ RemoteException -> 0x006e }
        r5 = r7.zzani;	 Catch:{ RemoteException -> 0x006e }
        r6 = r7.zzanz;	 Catch:{ RemoteException -> 0x006e }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006e }
        goto L_0x004d;
    L_0x006e:
        r0 = move-exception;
        r2 = r7.zzape;	 Catch:{ all -> 0x0095 }
        r2 = r2.zzge();	 Catch:{ all -> 0x0095 }
        r2 = r2.zzim();	 Catch:{ all -> 0x0095 }
        r3 = "Failed to get user properties";
        r4 = r7.zzanj;	 Catch:{ all -> 0x0095 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x0095 }
        r5 = r7.zzanh;	 Catch:{ all -> 0x0095 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0095 }
        r0 = r7.zzapf;	 Catch:{ all -> 0x0095 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0095 }
        r0.set(r2);	 Catch:{ all -> 0x0095 }
        r0 = r7.zzapf;	 Catch:{ all -> 0x0059 }
        r0.notify();	 Catch:{ all -> 0x0059 }
        goto L_0x0057;
    L_0x0095:
        r0 = move-exception;
        r2 = r7.zzapf;	 Catch:{ all -> 0x0059 }
        r2.notify();	 Catch:{ all -> 0x0059 }
        throw r0;	 Catch:{ all -> 0x0059 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzit.run():void");
    }
}
