package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkj extends zzaby<zzkj> {
    private static volatile zzkj[] zzasl;
    public String name;
    public Boolean zzasm;
    public Boolean zzasn;
    public Integer zzaso;

    public zzkj() {
        this.name = null;
        this.zzasm = null;
        this.zzasn = null;
        this.zzaso = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkj[] zzli() {
        if (zzasl == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasl == null) {
                    zzasl = new zzkj[0];
                }
            }
        }
        return zzasl;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkj)) {
            return false;
        }
        zzkj zzkj = (zzkj) obj;
        if (this.name == null) {
            if (zzkj.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzkj.name)) {
            return false;
        }
        if (this.zzasm == null) {
            if (zzkj.zzasm != null) {
                return false;
            }
        } else if (!this.zzasm.equals(zzkj.zzasm)) {
            return false;
        }
        if (this.zzasn == null) {
            if (zzkj.zzasn != null) {
                return false;
            }
        } else if (!this.zzasn.equals(zzkj.zzasn)) {
            return false;
        }
        if (this.zzaso == null) {
            if (zzkj.zzaso != null) {
                return false;
            }
        } else if (!this.zzaso.equals(zzkj.zzaso)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkj.zzbww == null || zzkj.zzbww.isEmpty() : this.zzbww.equals(zzkj.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzaso == null ? 0 : this.zzaso.hashCode()) + (((this.zzasn == null ? 0 : this.zzasn.hashCode()) + (((this.zzasm == null ? 0 : this.zzasm.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.name != null) {
            zza += zzabw.zzc(1, this.name);
        }
        if (this.zzasm != null) {
            this.zzasm.booleanValue();
            zza += zzabw.zzaq(2) + 1;
        }
        if (this.zzasn != null) {
            this.zzasn.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        return this.zzaso != null ? zza + zzabw.zzf(4, this.zzaso.intValue()) : zza;
    }

    public final void zza(zzabw zzabw) throws IOException {
        if (this.name != null) {
            zzabw.zzb(1, this.name);
        }
        if (this.zzasm != null) {
            zzabw.zza(2, this.zzasm.booleanValue());
        }
        if (this.zzasn != null) {
            zzabw.zza(3, this.zzasn.booleanValue());
        }
        if (this.zzaso != null) {
            zzabw.zze(4, this.zzaso.intValue());
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
                    this.name = zzabv.readString();
                    continue;
                case 16:
                    this.zzasm = Boolean.valueOf(zzabv.zzux());
                    continue;
                case 24:
                    this.zzasn = Boolean.valueOf(zzabv.zzux());
                    continue;
                case 32:
                    this.zzaso = Integer.valueOf(zzabv.zzuy());
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
