package com.google.android.gms.internal.ads;

final class zzavm implements zzaua<zzauk> {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.ads.zzaug<com.google.android.gms.internal.ads.zzauk> zzb(java.lang.String r6, java.lang.String r7, int r8) throws java.security.GeneralSecurityException {
        /*
        r5 = this;
        r0 = -1;
        r4 = 1;
        r1 = 0;
        r2 = r7.toLowerCase();
        r3 = r2.hashCode();
        switch(r3) {
            case 107855: goto L_0x0022;
            default: goto L_0x000e;
        };
    L_0x000e:
        r2 = r0;
    L_0x000f:
        switch(r2) {
            case 0: goto L_0x002c;
            default: goto L_0x0012;
        };
    L_0x0012:
        r0 = new java.security.GeneralSecurityException;
        r2 = "No support for primitive '%s'.";
        r3 = new java.lang.Object[r4];
        r3[r1] = r7;
        r1 = java.lang.String.format(r2, r3);
        r0.<init>(r1);
        throw r0;
    L_0x0022:
        r3 = "mac";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x000e;
    L_0x002a:
        r2 = r1;
        goto L_0x000f;
    L_0x002c:
        r2 = r6.hashCode();
        switch(r2) {
            case 836622442: goto L_0x0046;
            default: goto L_0x0033;
        };
    L_0x0033:
        switch(r0) {
            case 0: goto L_0x0050;
            default: goto L_0x0036;
        };
    L_0x0036:
        r0 = new java.security.GeneralSecurityException;
        r2 = "No support for primitive 'Mac' with key type '%s'.";
        r3 = new java.lang.Object[r4];
        r3[r1] = r6;
        r1 = java.lang.String.format(r2, r3);
        r0.<init>(r1);
        throw r0;
    L_0x0046:
        r2 = "type.googleapis.com/google.crypto.tink.HmacKey";
        r2 = r6.equals(r2);
        if (r2 == 0) goto L_0x0033;
    L_0x004e:
        r0 = r1;
        goto L_0x0033;
    L_0x0050:
        r0 = new com.google.android.gms.internal.ads.zzavk;
        r0.<init>();
        if (r8 <= 0) goto L_0x006e;
    L_0x0057:
        r0 = new java.security.GeneralSecurityException;
        r2 = "No key manager for key type '%s' with version at least %d.";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r3[r1] = r6;
        r1 = java.lang.Integer.valueOf(r8);
        r3[r4] = r1;
        r1 = java.lang.String.format(r2, r3);
        r0.<init>(r1);
        throw r0;
    L_0x006e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.ads.zzavm.zzb(java.lang.String, java.lang.String, int):com.google.android.gms.internal.ads.zzaug<com.google.android.gms.internal.ads.zzauk>");
    }
}
