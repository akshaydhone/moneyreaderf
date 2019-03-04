package com.google.android.gms.internal.ads;

import java.io.IOException;

public final class zzbfp extends zzbfc<zzbfp> {
    private zzbfq zzecz;
    public zzbfo[] zzeda;
    private byte[] zzedb;
    private byte[] zzedc;
    private Integer zzedd;

    public zzbfp() {
        this.zzecz = null;
        this.zzeda = zzbfo.zzagt();
        this.zzedb = null;
        this.zzedc = null;
        this.zzedd = null;
        this.zzebk = null;
        this.zzebt = -1;
    }

    public final /* synthetic */ zzbfi zza(zzbez zzbez) throws IOException {
        while (true) {
            int zzabk = zzbez.zzabk();
            switch (zzabk) {
                case 0:
                    break;
                case 10:
                    if (this.zzecz == null) {
                        this.zzecz = new zzbfq();
                    }
                    zzbez.zza(this.zzecz);
                    continue;
                case 18:
                    int zzb = zzbfl.zzb(zzbez, 18);
                    zzabk = this.zzeda == null ? 0 : this.zzeda.length;
                    Object obj = new zzbfo[(zzb + zzabk)];
                    if (zzabk != 0) {
                        System.arraycopy(this.zzeda, 0, obj, 0, zzabk);
                    }
                    while (zzabk < obj.length - 1) {
                        obj[zzabk] = new zzbfo();
                        zzbez.zza(obj[zzabk]);
                        zzbez.zzabk();
                        zzabk++;
                    }
                    obj[zzabk] = new zzbfo();
                    zzbez.zza(obj[zzabk]);
                    this.zzeda = obj;
                    continue;
                case 26:
                    this.zzedb = zzbez.readBytes();
                    continue;
                case 34:
                    this.zzedc = zzbez.readBytes();
                    continue;
                case 40:
                    this.zzedd = Integer.valueOf(zzbez.zzabn());
                    continue;
                default:
                    if (!super.zza(zzbez, zzabk)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }

    public final void zza(zzbfa zzbfa) throws IOException {
        if (this.zzecz != null) {
            zzbfa.zza(1, this.zzecz);
        }
        if (this.zzeda != null && this.zzeda.length > 0) {
            for (zzbfi zzbfi : this.zzeda) {
                if (zzbfi != null) {
                    zzbfa.zza(2, zzbfi);
                }
            }
        }
        if (this.zzedb != null) {
            zzbfa.zza(3, this.zzedb);
        }
        if (this.zzedc != null) {
            zzbfa.zza(4, this.zzedc);
        }
        if (this.zzedd != null) {
            zzbfa.zzm(5, this.zzedd.intValue());
        }
        super.zza(zzbfa);
    }

    protected final int zzr() {
        int zzr = super.zzr();
        if (this.zzecz != null) {
            zzr += zzbfa.zzb(1, this.zzecz);
        }
        if (this.zzeda != null && this.zzeda.length > 0) {
            int i = zzr;
            for (zzbfi zzbfi : this.zzeda) {
                if (zzbfi != null) {
                    i += zzbfa.zzb(2, zzbfi);
                }
            }
            zzr = i;
        }
        if (this.zzedb != null) {
            zzr += zzbfa.zzb(3, this.zzedb);
        }
        if (this.zzedc != null) {
            zzr += zzbfa.zzb(4, this.zzedc);
        }
        return this.zzedd != null ? zzr + zzbfa.zzq(5, this.zzedd.intValue()) : zzr;
    }
}
