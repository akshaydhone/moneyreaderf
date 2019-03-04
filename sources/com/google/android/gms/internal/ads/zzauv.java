package com.google.android.gms.internal.ads;

import com.google.android.gms.internal.ads.zzaxi.zzb;
import java.security.GeneralSecurityException;

final class zzauv implements zzaug<zzatz> {
    zzauv() {
    }

    private final zzatz zzd(zzbah zzbah) throws GeneralSecurityException {
        try {
            zzawe zzr = zzawe.zzr(zzbah);
            if (zzr instanceof zzawe) {
                zzr = zzr;
                zzazq.zzj(zzr.getVersion(), 0);
                zzazq.zzbi(zzr.zzwv().size());
                return new zzayj(zzr.zzwv().toByteArray());
            }
            throw new GeneralSecurityException("expected AesGcmKey proto");
        } catch (zzbbu e) {
            throw new GeneralSecurityException("expected AesGcmKey proto");
        }
    }

    public final int getVersion() {
        return 0;
    }

    public final /* synthetic */ Object zza(zzbah zzbah) throws GeneralSecurityException {
        return zzd(zzbah);
    }

    public final /* synthetic */ Object zza(zzbcu zzbcu) throws GeneralSecurityException {
        if (zzbcu instanceof zzawe) {
            zzawe zzawe = (zzawe) zzbcu;
            zzazq.zzj(zzawe.getVersion(), 0);
            zzazq.zzbi(zzawe.zzwv().size());
            return new zzayj(zzawe.zzwv().toByteArray());
        }
        throw new GeneralSecurityException("expected AesGcmKey proto");
    }

    public final zzbcu zzb(zzbah zzbah) throws GeneralSecurityException {
        try {
            return zzb(zzawg.zzt(zzbah));
        } catch (Throwable e) {
            throw new GeneralSecurityException("expected serialized AesGcmKeyFormat proto", e);
        }
    }

    public final zzbcu zzb(zzbcu zzbcu) throws GeneralSecurityException {
        if (zzbcu instanceof zzawg) {
            zzawg zzawg = (zzawg) zzbcu;
            zzazq.zzbi(zzawg.getKeySize());
            return zzawe.zzxk().zzs(zzbah.zzo(zzazl.zzbh(zzawg.getKeySize()))).zzao(0).zzadi();
        }
        throw new GeneralSecurityException("expected AesGcmKeyFormat proto");
    }

    public final zzaxi zzc(zzbah zzbah) throws GeneralSecurityException {
        return (zzaxi) zzaxi.zzyz().zzeb("type.googleapis.com/google.crypto.tink.AesGcmKey").zzai(((zzawe) zzb(zzbah)).zzaav()).zzb(zzb.SYMMETRIC).zzadi();
    }
}
