package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzki extends zzaby<zzki> {
    public Integer zzash;
    public String zzasi;
    public Boolean zzasj;
    public String[] zzask;

    public zzki() {
        this.zzash = null;
        this.zzasi = null;
        this.zzasj = null;
        this.zzask = zzach.zzbxq;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    private final zzki zze(zzabv zzabv) throws IOException {
        int position;
        while (true) {
            int zzuw = zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    position = zzabv.getPosition();
                    try {
                        int zzuy = zzabv.zzuy();
                        if (zzuy < 0 || zzuy > 6) {
                            throw new IllegalArgumentException(zzuy + " is not a valid enum MatchType");
                        }
                        this.zzash = Integer.valueOf(zzuy);
                        continue;
                    } catch (IllegalArgumentException e) {
                        zzabv.zzam(position);
                        zza(zzabv, zzuw);
                        break;
                    }
                case 18:
                    this.zzasi = zzabv.readString();
                    continue;
                case 24:
                    this.zzasj = Boolean.valueOf(zzabv.zzux());
                    continue;
                case 34:
                    position = zzach.zzb(zzabv, 34);
                    zzuw = this.zzask == null ? 0 : this.zzask.length;
                    Object obj = new String[(position + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzask, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = zzabv.readString();
                        zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = zzabv.readString();
                    this.zzask = obj;
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

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzki)) {
            return false;
        }
        zzki zzki = (zzki) obj;
        if (this.zzash == null) {
            if (zzki.zzash != null) {
                return false;
            }
        } else if (!this.zzash.equals(zzki.zzash)) {
            return false;
        }
        if (this.zzasi == null) {
            if (zzki.zzasi != null) {
                return false;
            }
        } else if (!this.zzasi.equals(zzki.zzasi)) {
            return false;
        }
        if (this.zzasj == null) {
            if (zzki.zzasj != null) {
                return false;
            }
        } else if (!this.zzasj.equals(zzki.zzasj)) {
            return false;
        }
        return !zzacc.equals(this.zzask, zzki.zzask) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? zzki.zzbww == null || zzki.zzbww.isEmpty() : this.zzbww.equals(zzki.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzasj == null ? 0 : this.zzasj.hashCode()) + (((this.zzasi == null ? 0 : this.zzasi.hashCode()) + (((this.zzash == null ? 0 : this.zzash.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzask)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i = 0;
        int zza = super.zza();
        if (this.zzash != null) {
            zza += zzabw.zzf(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            zza += zzabw.zzc(2, this.zzasi);
        }
        if (this.zzasj != null) {
            this.zzasj.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        if (this.zzask == null || this.zzask.length <= 0) {
            return zza;
        }
        int i2 = 0;
        int i3 = 0;
        while (i < this.zzask.length) {
            String str = this.zzask[i];
            if (str != null) {
                i3++;
                i2 += zzabw.zzfm(str);
            }
            i++;
        }
        return (zza + i2) + (i3 * 1);
    }

    public final void zza(zzabw zzabw) throws IOException {
        if (this.zzash != null) {
            zzabw.zze(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            zzabw.zzb(2, this.zzasi);
        }
        if (this.zzasj != null) {
            zzabw.zza(3, this.zzasj.booleanValue());
        }
        if (this.zzask != null && this.zzask.length > 0) {
            for (String str : this.zzask) {
                if (str != null) {
                    zzabw.zzb(4, str);
                }
            }
        }
        super.zza(zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv zzabv) throws IOException {
        return zze(zzabv);
    }
}
