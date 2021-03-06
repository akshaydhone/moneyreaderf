package com.google.android.gms.internal.ads;

import com.google.android.gms.internal.ads.zzbbo.zzb;
import com.google.android.gms.internal.ads.zzbbo.zze;

public final class zzavs extends zzbbo<zzavs, zza> implements zzbcw {
    private static volatile zzbdf<zzavs> zzakh;
    private static final zzavs zzdiq = new zzavs();
    private int zzdih;
    private zzavw zzdio;
    private zzbah zzdip = zzbah.zzdpq;

    public static final class zza extends com.google.android.gms.internal.ads.zzbbo.zza<zzavs, zza> implements zzbcw {
        private zza() {
            super(zzavs.zzdiq);
        }

        public final zza zzam(int i) {
            zzadh();
            ((zzavs) this.zzdtx).setVersion(0);
            return this;
        }

        public final zza zzc(zzavw zzavw) {
            zzadh();
            ((zzavs) this.zzdtx).zzb(zzavw);
            return this;
        }

        public final zza zzm(zzbah zzbah) {
            zzadh();
            ((zzavs) this.zzdtx).zzk(zzbah);
            return this;
        }
    }

    static {
        zzbbo.zza(zzavs.class, zzdiq);
    }

    private zzavs() {
    }

    private final void setVersion(int i) {
        this.zzdih = i;
    }

    private final void zzb(zzavw zzavw) {
        if (zzavw == null) {
            throw new NullPointerException();
        }
        this.zzdio = zzavw;
    }

    private final void zzk(zzbah zzbah) {
        if (zzbah == null) {
            throw new NullPointerException();
        }
        this.zzdip = zzbah;
    }

    public static zzavs zzl(zzbah zzbah) throws zzbbu {
        return (zzavs) zzbbo.zza(zzdiq, zzbah);
    }

    public static zza zzww() {
        return (zza) ((com.google.android.gms.internal.ads.zzbbo.zza) zzdiq.zza(zze.zzdue, null, null));
    }

    public static zzavs zzwx() {
        return zzdiq;
    }

    public final int getVersion() {
        return this.zzdih;
    }

    protected final Object zza(int i, Object obj, Object obj2) {
        switch (zzavt.zzakf[i - 1]) {
            case 1:
                return new zzavs();
            case 2:
                return new zza();
            case 3:
                Object[] objArr = new Object[]{"zzdih", "zzdio", "zzdip"};
                return zzbbo.zza(zzdiq, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0004\u0000\u0000\u0000\u0001\u000b\u0002\t\u0003\n", objArr);
            case 4:
                return zzdiq;
            case 5:
                zzbdf zzbdf = zzakh;
                if (zzbdf != null) {
                    return zzbdf;
                }
                Object obj3;
                synchronized (zzavs.class) {
                    obj3 = zzakh;
                    if (obj3 == null) {
                        obj3 = new zzb(zzdiq);
                        zzakh = obj3;
                    }
                }
                return obj3;
            case 6:
                return Byte.valueOf((byte) 1);
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public final zzavw zzwu() {
        return this.zzdio == null ? zzavw.zzxc() : this.zzdio;
    }

    public final zzbah zzwv() {
        return this.zzdip;
    }
}
