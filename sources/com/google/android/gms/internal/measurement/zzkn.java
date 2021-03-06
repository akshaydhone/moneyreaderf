package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkn extends zzaby<zzkn> {
    private static volatile zzkn[] zzasz;
    public Integer count;
    public String name;
    public zzko[] zzata;
    public Long zzatb;
    public Long zzatc;

    public zzkn() {
        this.zzata = zzko.zzlm();
        this.name = null;
        this.zzatb = null;
        this.zzatc = null;
        this.count = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkn[] zzll() {
        if (zzasz == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasz == null) {
                    zzasz = new zzkn[0];
                }
            }
        }
        return zzasz;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkn)) {
            return false;
        }
        zzkn zzkn = (zzkn) obj;
        if (!zzacc.equals(this.zzata, zzkn.zzata)) {
            return false;
        }
        if (this.name == null) {
            if (zzkn.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzkn.name)) {
            return false;
        }
        if (this.zzatb == null) {
            if (zzkn.zzatb != null) {
                return false;
            }
        } else if (!this.zzatb.equals(zzkn.zzatb)) {
            return false;
        }
        if (this.zzatc == null) {
            if (zzkn.zzatc != null) {
                return false;
            }
        } else if (!this.zzatc.equals(zzkn.zzatc)) {
            return false;
        }
        if (this.count == null) {
            if (zzkn.count != null) {
                return false;
            }
        } else if (!this.count.equals(zzkn.count)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkn.zzbww == null || zzkn.zzbww.isEmpty() : this.zzbww.equals(zzkn.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.count == null ? 0 : this.count.hashCode()) + (((this.zzatc == null ? 0 : this.zzatc.hashCode()) + (((this.zzatb == null ? 0 : this.zzatb.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzata)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzace zzace : this.zzata) {
                if (zzace != null) {
                    zza += zzabw.zzb(1, zzace);
                }
            }
        }
        if (this.name != null) {
            zza += zzabw.zzc(2, this.name);
        }
        if (this.zzatb != null) {
            zza += zzabw.zzc(3, this.zzatb.longValue());
        }
        if (this.zzatc != null) {
            zza += zzabw.zzc(4, this.zzatc.longValue());
        }
        return this.count != null ? zza + zzabw.zzf(5, this.count.intValue()) : zza;
    }

    public final void zza(zzabw zzabw) throws IOException {
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzace zzace : this.zzata) {
                if (zzace != null) {
                    zzabw.zza(1, zzace);
                }
            }
        }
        if (this.name != null) {
            zzabw.zzb(2, this.name);
        }
        if (this.zzatb != null) {
            zzabw.zzb(3, this.zzatb.longValue());
        }
        if (this.zzatc != null) {
            zzabw.zzb(4, this.zzatc.longValue());
        }
        if (this.count != null) {
            zzabw.zze(5, this.count.intValue());
        }
        super.zza(zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv zzabv) throws IOException {
        while (true) {
            int zzuw = zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 10:
                    int zzb = zzach.zzb(zzabv, 10);
                    zzuw = this.zzata == null ? 0 : this.zzata.length;
                    Object obj = new zzko[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzata, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzko();
                        zzabv.zza(obj[zzuw]);
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzko();
                    zzabv.zza(obj[zzuw]);
                    this.zzata = obj;
                    continue;
                case 18:
                    this.name = zzabv.readString();
                    continue;
                case 24:
                    this.zzatb = Long.valueOf(zzabv.zzuz());
                    continue;
                case 32:
                    this.zzatc = Long.valueOf(zzabv.zzuz());
                    continue;
                case 40:
                    this.count = Integer.valueOf(zzabv.zzuy());
                    continue;
                default:
                    if (!super.zza(zzabv, zzuw)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
