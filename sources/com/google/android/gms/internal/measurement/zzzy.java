package com.google.android.gms.internal.measurement;

public class zzzy {
    private static final zzzi zzbsw = zzzi.zzte();
    private zzyw zzbsx;
    private volatile zzaal zzbsy;
    private volatile zzyw zzbsz;

    private final zzaal zzb(zzaal zzaal) {
        if (this.zzbsy == null) {
            synchronized (this) {
                if (this.zzbsy != null) {
                } else {
                    try {
                        this.zzbsy = zzaal;
                        this.zzbsz = zzyw.zzbqx;
                    } catch (zzzt e) {
                        this.zzbsy = zzaal;
                        this.zzbsz = zzyw.zzbqx;
                    }
                }
            }
        }
        return this.zzbsy;
    }

    private final zzyw zztp() {
        if (this.zzbsz != null) {
            return this.zzbsz;
        }
        synchronized (this) {
            if (this.zzbsz != null) {
                zzyw zzyw = this.zzbsz;
                return zzyw;
            }
            if (this.zzbsy == null) {
                this.zzbsz = zzyw.zzbqx;
            } else {
                this.zzbsz = this.zzbsy.zztp();
            }
            zzyw = this.zzbsz;
            return zzyw;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzzy)) {
            return false;
        }
        zzzy zzzy = (zzzy) obj;
        zzaal zzaal = this.zzbsy;
        zzaal zzaal2 = zzzy.zzbsy;
        return (zzaal == null && zzaal2 == null) ? zztp().equals(zzzy.zztp()) : (zzaal == null || zzaal2 == null) ? zzaal != null ? zzaal.equals(zzzy.zzb(zzaal.zztz())) : zzb(zzaal2.zztz()).equals(zzaal2) : zzaal.equals(zzaal2);
    }

    public int hashCode() {
        return 1;
    }

    public final zzaal zzc(zzaal zzaal) {
        zzaal zzaal2 = this.zzbsy;
        this.zzbsx = null;
        this.zzbsz = null;
        this.zzbsy = zzaal;
        return zzaal2;
    }
}
