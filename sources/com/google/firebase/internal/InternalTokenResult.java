package com.google.firebase.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Objects;

@KeepForSdk
public class InternalTokenResult {
    private String zzz;

    @KeepForSdk
    public InternalTokenResult(@Nullable String str) {
        this.zzz = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InternalTokenResult)) {
            return false;
        }
        return Objects.equal(this.zzz, ((InternalTokenResult) obj).zzz);
    }

    @Nullable
    @KeepForSdk
    public String getToken() {
        return this.zzz;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzz);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("token", this.zzz).toString();
    }
}
