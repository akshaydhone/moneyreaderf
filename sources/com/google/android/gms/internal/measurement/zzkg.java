package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkg extends zzaby<zzkg> {
    public Integer zzarz;
    public Boolean zzasa;
    public String zzasb;
    public String zzasc;
    public String zzasd;

    public zzkg() {
        this.zzarz = null;
        this.zzasa = null;
        this.zzasb = null;
        this.zzasc = null;
        this.zzasd = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    private final zzkg zzd(zzabv zzabv) throws IOException {
        while (true) {
            int zzuw = zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    int position = zzabv.getPosition();
                    try {
                        int zzuy = zzabv.zzuy();
                        if (zzuy < 0 || zzuy > 4) {
                            throw new IllegalArgumentException(zzuy + " is not a valid enum ComparisonType");
                        }
                        this.zzarz = Integer.valueOf(zzuy);
                        continue;
                    } catch (IllegalArgumentException e) {
                        zzabv.zzam(position);
                        zza(zzabv, zzuw);
                        break;
                    }
                case 16:
                    this.zzasa = Boolean.valueOf(zzabv.zzux());
                    continue;
                case 26:
                    this.zzasb = zzabv.readString();
                    continue;
                case 34:
                    this.zzasc = zzabv.readString();
                    continue;
                case 42:
                    this.zzasd = zzabv.readString();
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
        if (!(obj instanceof zzkg)) {
            return false;
        }
        zzkg zzkg = (zzkg) obj;
        if (this.zzarz == null) {
            if (zzkg.zzarz != null) {
                return false;
            }
        } else if (!this.zzarz.equals(zzkg.zzarz)) {
            return false;
        }
        if (this.zzasa == null) {
            if (zzkg.zzasa != null) {
                return false;
            }
        } else if (!this.zzasa.equals(zzkg.zzasa)) {
            return false;
        }
        if (this.zzasb == null) {
            if (zzkg.zzasb != null) {
                return false;
            }
        } else if (!this.zzasb.equals(zzkg.zzasb)) {
            return false;
        }
        if (this.zzasc == null) {
            if (zzkg.zzasc != null) {
                return false;
            }
        } else if (!this.zzasc.equals(zzkg.zzasc)) {
            return false;
        }
        if (this.zzasd == null) {
            if (zzkg.zzasd != null) {
                return false;
            }
        } else if (!this.zzasd.equals(zzkg.zzasd)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkg.zzbww == null || zzkg.zzbww.isEmpty() : this.zzbww.equals(zzkg.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzasd == null ? 0 : this.zzasd.hashCode()) + (((this.zzasc == null ? 0 : this.zzasc.hashCode()) + (((this.zzasb == null ? 0 : this.zzasb.hashCode()) + (((this.zzasa == null ? 0 : this.zzasa.hashCode()) + (((this.zzarz == null ? 0 : this.zzarz.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzarz != null) {
            zza += zzabw.zzf(1, this.zzarz.intValue());
        }
        if (this.zzasa != null) {
            this.zzasa.booleanValue();
            zza += zzabw.zzaq(2) + 1;
        }
        if (this.zzasb != null) {
            zza += zzabw.zzc(3, this.zzasb);
        }
        if (this.zzasc != null) {
            zza += zzabw.zzc(4, this.zzasc);
        }
        return this.zzasd != null ? zza + zzabw.zzc(5, this.zzasd) : zza;
    }

    public final void zza(zzabw zzabw) throws IOException {
        if (this.zzarz != null) {
            zzabw.zze(1, this.zzarz.intValue());
        }
        if (this.zzasa != null) {
            zzabw.zza(2, this.zzasa.booleanValue());
        }
        if (this.zzasb != null) {
            zzabw.zzb(3, this.zzasb);
        }
        if (this.zzasc != null) {
            zzabw.zzb(4, this.zzasc);
        }
        if (this.zzasd != null) {
            zzabw.zzb(5, this.zzasd);
        }
        super.zza(zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv zzabv) throws IOException {
        return zzd(zzabv);
    }
}
