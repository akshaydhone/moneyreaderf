package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzwy extends zzws<Boolean> {
    zzwy(zzxc zzxc, String str, Boolean bool) {
        super(zzxc, str, bool);
    }

    protected final /* synthetic */ Object zzey(String str) {
        if (zzwn.zzbmi.matcher(str).matches()) {
            return Boolean.valueOf(true);
        }
        if (zzwn.zzbmj.matcher(str).matches()) {
            return Boolean.valueOf(false);
        }
        String str2 = this.zzbnh;
        Log.e("PhenotypeFlag", new StringBuilder((String.valueOf(str2).length() + 28) + String.valueOf(str).length()).append("Invalid boolean value for ").append(str2).append(": ").append(str).toString());
        return null;
    }
}
