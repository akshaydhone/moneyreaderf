package com.google.android.gms.internal.measurement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build.VERSION;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import javax.annotation.Nullable;

public abstract class zzws<T> {
    private static final Object zzbnc = new Object();
    private static boolean zzbnd = false;
    private static volatile Boolean zzbne = null;
    private static volatile Boolean zzbnf = null;
    @SuppressLint({"StaticFieldLeak"})
    private static Context zzqx = null;
    private final zzxc zzbng;
    final String zzbnh;
    private final String zzbni;
    private final T zzbnj;
    private T zzbnk;
    private volatile zzwp zzbnl;
    private volatile SharedPreferences zzbnm;

    private zzws(zzxc zzxc, String str, T t) {
        this.zzbnk = null;
        this.zzbnl = null;
        this.zzbnm = null;
        if (zzxc.zzbns == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        }
        this.zzbng = zzxc;
        String valueOf = String.valueOf(zzxc.zzbnt);
        String valueOf2 = String.valueOf(str);
        this.zzbni = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        valueOf = String.valueOf(zzxc.zzbnu);
        valueOf2 = String.valueOf(str);
        this.zzbnh = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        this.zzbnj = t;
    }

    public static void init(Context context) {
        synchronized (zzbnc) {
            if (VERSION.SDK_INT < 24 || !context.isDeviceProtectedStorage()) {
                Context applicationContext = context.getApplicationContext();
                if (applicationContext != null) {
                    context = applicationContext;
                }
            }
            if (zzqx != context) {
                zzbne = null;
            }
            zzqx = context;
        }
        zzbnd = false;
    }

    private static zzws<Double> zza(zzxc zzxc, String str, double d) {
        return new zzwz(zzxc, str, Double.valueOf(d));
    }

    private static zzws<Integer> zza(zzxc zzxc, String str, int i) {
        return new zzwx(zzxc, str, Integer.valueOf(i));
    }

    private static zzws<Long> zza(zzxc zzxc, String str, long j) {
        return new zzww(zzxc, str, Long.valueOf(j));
    }

    private static zzws<String> zza(zzxc zzxc, String str, String str2) {
        return new zzxa(zzxc, str, str2);
    }

    private static zzws<Boolean> zza(zzxc zzxc, String str, boolean z) {
        return new zzwy(zzxc, str, Boolean.valueOf(z));
    }

    private static <V> V zza(zzxb<V> zzxb) {
        V zzsc;
        long clearCallingIdentity;
        try {
            zzsc = zzxb.zzsc();
        } catch (SecurityException e) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            zzsc = zzxb.zzsc();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        return zzsc;
    }

    static boolean zzd(String str, boolean z) {
        try {
            return zzsa() ? ((Boolean) zza(new zzwv(str, false))).booleanValue() : false;
        } catch (Throwable e) {
            Log.e("PhenotypeFlag", "Unable to read GServices, returning default value.", e);
            return false;
        }
    }

    @TargetApi(24)
    @Nullable
    private final T zzry() {
        String valueOf;
        if (zzd("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            String str = "PhenotypeFlag";
            String str2 = "Bypass reading Phenotype values for flag: ";
            valueOf = String.valueOf(this.zzbnh);
            Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (this.zzbng.zzbns != null) {
            if (this.zzbnl == null) {
                this.zzbnl = zzwp.zza(zzqx.getContentResolver(), this.zzbng.zzbns);
            }
            valueOf = (String) zza(new zzwt(this, this.zzbnl));
            if (valueOf != null) {
                return zzey(valueOf);
            }
        } else {
            zzxc zzxc = this.zzbng;
        }
        return null;
    }

    @Nullable
    private final T zzrz() {
        String str;
        zzxc zzxc = this.zzbng;
        if (zzsa()) {
            try {
                str = (String) zza(new zzwu(this));
                if (str != null) {
                    return zzey(str);
                }
            } catch (Throwable e) {
                Throwable th = e;
                String str2 = "PhenotypeFlag";
                String str3 = "Unable to read GServices for flag: ";
                str = String.valueOf(this.zzbnh);
                Log.e(str2, str.length() != 0 ? str3.concat(str) : new String(str3), th);
            }
        }
        return null;
    }

    private static boolean zzsa() {
        boolean z = false;
        if (zzbne == null) {
            if (zzqx == null) {
                return false;
            }
            if (PermissionChecker.checkCallingOrSelfPermission(zzqx, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0) {
                z = true;
            }
            zzbne = Boolean.valueOf(z);
        }
        return zzbne.booleanValue();
    }

    public final T get() {
        if (zzqx == null) {
            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
        }
        zzxc zzxc = this.zzbng;
        T zzry = zzry();
        if (zzry != null) {
            return zzry;
        }
        zzry = zzrz();
        return zzry == null ? this.zzbnj : zzry;
    }

    protected abstract T zzey(String str);

    final /* synthetic */ String zzsb() {
        return zzwn.zza(zzqx.getContentResolver(), this.zzbni, null);
    }
}
