package com.google.android.gms.internal.ads;

import java.util.Map;

public final class zzamb extends zzr<zzp> {
    private final zzaoj<zzp> zzctn;
    private final Map<String, String> zzcto;
    private final zzamy zzctp;

    public zzamb(String str, zzaoj<zzp> zzaoj) {
        this(str, null, zzaoj);
    }

    private zzamb(String str, Map<String, String> map, zzaoj<zzp> zzaoj) {
        super(0, str, new zzamc(zzaoj));
        this.zzcto = null;
        this.zzctn = zzaoj;
        this.zzctp = new zzamy();
        this.zzctp.zza(str, "GET", null, null);
    }

    protected final zzx<zzp> zza(zzp zzp) {
        return zzx.zza(zzp, zzap.zzb(zzp));
    }

    protected final /* synthetic */ void zza(Object obj) {
        zzp zzp = (zzp) obj;
        this.zzctp.zza(zzp.zzab, zzp.statusCode);
        zzamy zzamy = this.zzctp;
        byte[] bArr = zzp.data;
        if (zzamy.isEnabled() && bArr != null) {
            zzamy.zzf(bArr);
        }
        this.zzctn.set(zzp);
    }
}
