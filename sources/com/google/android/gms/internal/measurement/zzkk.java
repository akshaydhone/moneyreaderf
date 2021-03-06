package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkk extends zzaby<zzkk> {
    public String zzadm;
    public Long zzasp;
    private Integer zzasq;
    public zzkl[] zzasr;
    public zzkj[] zzass;
    public zzkd[] zzast;

    public zzkk() {
        this.zzasp = null;
        this.zzadm = null;
        this.zzasq = null;
        this.zzasr = zzkl.zzlj();
        this.zzass = zzkj.zzli();
        this.zzast = zzkd.zzle();
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkk)) {
            return false;
        }
        zzkk zzkk = (zzkk) obj;
        if (this.zzasp == null) {
            if (zzkk.zzasp != null) {
                return false;
            }
        } else if (!this.zzasp.equals(zzkk.zzasp)) {
            return false;
        }
        if (this.zzadm == null) {
            if (zzkk.zzadm != null) {
                return false;
            }
        } else if (!this.zzadm.equals(zzkk.zzadm)) {
            return false;
        }
        if (this.zzasq == null) {
            if (zzkk.zzasq != null) {
                return false;
            }
        } else if (!this.zzasq.equals(zzkk.zzasq)) {
            return false;
        }
        return !zzacc.equals(this.zzasr, zzkk.zzasr) ? false : !zzacc.equals(this.zzass, zzkk.zzass) ? false : !zzacc.equals(this.zzast, zzkk.zzast) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? zzkk.zzbww == null || zzkk.zzbww.isEmpty() : this.zzbww.equals(zzkk.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((this.zzasq == null ? 0 : this.zzasq.hashCode()) + (((this.zzadm == null ? 0 : this.zzadm.hashCode()) + (((this.zzasp == null ? 0 : this.zzasp.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzasr)) * 31) + zzacc.hashCode(this.zzass)) * 31) + zzacc.hashCode(this.zzast)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i;
        int i2 = 0;
        int zza = super.zza();
        if (this.zzasp != null) {
            zza += zzabw.zzc(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            zza += zzabw.zzc(2, this.zzadm);
        }
        if (this.zzasq != null) {
            zza += zzabw.zzf(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            i = zza;
            for (zzace zzace : this.zzasr) {
                if (zzace != null) {
                    i += zzabw.zzb(4, zzace);
                }
            }
            zza = i;
        }
        if (this.zzass != null && this.zzass.length > 0) {
            i = zza;
            for (zzace zzace2 : this.zzass) {
                if (zzace2 != null) {
                    i += zzabw.zzb(5, zzace2);
                }
            }
            zza = i;
        }
        if (this.zzast != null && this.zzast.length > 0) {
            while (i2 < this.zzast.length) {
                zzace zzace3 = this.zzast[i2];
                if (zzace3 != null) {
                    zza += zzabw.zzb(6, zzace3);
                }
                i2++;
            }
        }
        return zza;
    }

    public final void zza(zzabw zzabw) throws IOException {
        int i = 0;
        if (this.zzasp != null) {
            zzabw.zzb(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            zzabw.zzb(2, this.zzadm);
        }
        if (this.zzasq != null) {
            zzabw.zze(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            for (zzace zzace : this.zzasr) {
                if (zzace != null) {
                    zzabw.zza(4, zzace);
                }
            }
        }
        if (this.zzass != null && this.zzass.length > 0) {
            for (zzace zzace2 : this.zzass) {
                if (zzace2 != null) {
                    zzabw.zza(5, zzace2);
                }
            }
        }
        if (this.zzast != null && this.zzast.length > 0) {
            while (i < this.zzast.length) {
                zzace zzace3 = this.zzast[i];
                if (zzace3 != null) {
                    zzabw.zza(6, zzace3);
                }
                i++;
            }
        }
        super.zza(zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv zzabv) throws IOException {
        while (true) {
            int zzuw = zzabv.zzuw();
            int zzb;
            Object obj;
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    this.zzasp = Long.valueOf(zzabv.zzuz());
                    continue;
                case 18:
                    this.zzadm = zzabv.readString();
                    continue;
                case 24:
                    this.zzasq = Integer.valueOf(zzabv.zzuy());
                    continue;
                case 34:
                    zzb = zzach.zzb(zzabv, 34);
                    zzuw = this.zzasr == null ? 0 : this.zzasr.length;
                    obj = new zzkl[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzasr, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkl();
                        zzabv.zza(obj[zzuw]);
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkl();
                    zzabv.zza(obj[zzuw]);
                    this.zzasr = obj;
                    continue;
                case 42:
                    zzb = zzach.zzb(zzabv, 42);
                    zzuw = this.zzass == null ? 0 : this.zzass.length;
                    obj = new zzkj[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzass, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkj();
                        zzabv.zza(obj[zzuw]);
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkj();
                    zzabv.zza(obj[zzuw]);
                    this.zzass = obj;
                    continue;
                case 50:
                    zzb = zzach.zzb(zzabv, 50);
                    zzuw = this.zzast == null ? 0 : this.zzast.length;
                    obj = new zzkd[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzast, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkd();
                        zzabv.zza(obj[zzuw]);
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkd();
                    zzabv.zza(obj[zzuw]);
                    this.zzast = obj;
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
