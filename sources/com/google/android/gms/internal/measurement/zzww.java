package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzww extends zzws<Long> {
    zzww(zzxc zzxc, String str, Long l) {
        super(zzxc, str, l);
    }

    private final Long zzez(String str) {
        try {
            return Long.valueOf(Long.parseLong(str));
        } catch (NumberFormatException e) {
            String str2 = this.zzbnh;
            Log.e("PhenotypeFlag", new StringBuilder((String.valueOf(str2).length() + 25) + String.valueOf(str).length()).append("Invalid long value for ").append(str2).append(": ").append(str).toString());
            return null;
        }
    }

    protected final /* synthetic */ Object zzey(String str) {
        return zzez(str);
    }
}
