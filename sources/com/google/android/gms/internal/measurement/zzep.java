package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Iterator;

public final class zzep {
    final String name;
    private final String origin;
    final long timestamp;
    final long zzafp;
    final zzer zzafq;
    final String zzti;

    zzep(zzgl zzgl, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzer zzer;
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        this.zzti = str2;
        this.name = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.origin = str;
        this.timestamp = j;
        this.zzafp = j2;
        if (this.zzafp != 0 && this.zzafp > this.timestamp) {
            zzgl.zzge().zzip().zzg("Event created with reverse previous/current timestamps. appId", zzfg.zzbm(str2));
        }
        if (bundle == null || bundle.isEmpty()) {
            zzer = new zzer(new Bundle());
        } else {
            Bundle bundle2 = new Bundle(bundle);
            Iterator it = bundle2.keySet().iterator();
            while (it.hasNext()) {
                String str4 = (String) it.next();
                if (str4 == null) {
                    zzgl.zzge().zzim().log("Param name can't be null");
                    it.remove();
                } else {
                    Object zzh = zzgl.zzgb().zzh(str4, bundle2.get(str4));
                    if (zzh == null) {
                        zzgl.zzge().zzip().zzg("Param value can't be null", zzgl.zzga().zzbk(str4));
                        it.remove();
                    } else {
                        zzgl.zzgb().zza(bundle2, str4, zzh);
                    }
                }
            }
            zzer = new zzer(bundle2);
        }
        this.zzafq = zzer;
    }

    private zzep(zzgl zzgl, String str, String str2, String str3, long j, long j2, zzer zzer) {
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(zzer);
        this.zzti = str2;
        this.name = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.origin = str;
        this.timestamp = j;
        this.zzafp = j2;
        if (this.zzafp != 0 && this.zzafp > this.timestamp) {
            zzgl.zzge().zzip().zze("Event created with reverse previous/current timestamps. appId, name", zzfg.zzbm(str2), zzfg.zzbm(str3));
        }
        this.zzafq = zzer;
    }

    public final String toString() {
        String str = this.zzti;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzafq);
        return new StringBuilder(((String.valueOf(str).length() + 33) + String.valueOf(str2).length()) + String.valueOf(valueOf).length()).append("Event{appId='").append(str).append("', name='").append(str2).append("', params=").append(valueOf).append('}').toString();
    }

    final zzep zza(zzgl zzgl, long j) {
        return new zzep(zzgl, this.origin, this.zzti, this.name, this.timestamp, j, this.zzafq);
    }
}
