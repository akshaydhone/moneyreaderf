package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkp extends zzaby<zzkp> {
    public zzkq[] zzatf;

    public zzkp() {
        this.zzatf = zzkq.zzln();
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkp)) {
            return false;
        }
        zzkp zzkp = (zzkp) obj;
        return !zzacc.equals(this.zzatf, zzkp.zzatf) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? zzkp.zzbww == null || zzkp.zzbww.isEmpty() : this.zzbww.equals(zzkp.zzbww);
    }

    public final int hashCode() {
        int hashCode = (((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzatf)) * 31;
        int hashCode2 = (this.zzbww == null || this.zzbww.isEmpty()) ? 0 : this.zzbww.hashCode();
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (zzace zzace : this.zzatf) {
                if (zzace != null) {
                    zza += zzabw.zzb(1, zzace);
                }
            }
        }
        return zza;
    }

    public final void zza(zzabw zzabw) throws IOException {
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (zzace zzace : this.zzatf) {
                if (zzace != null) {
                    zzabw.zza(1, zzace);
                }
            }
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
                    zzuw = this.zzatf == null ? 0 : this.zzatf.length;
                    Object obj = new zzkq[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzatf, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkq();
                        zzabv.zza(obj[zzuw]);
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkq();
                    zzabv.zza(obj[zzuw]);
                    this.zzatf = obj;
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
