package com.google.android.gms.internal.firebase_storage;

import android.app.Activity;
import android.support.annotation.NonNull;

final class zzd {
    @NonNull
    private final Object cookie;
    @NonNull
    private final Activity zzdl;
    @NonNull
    private final Runnable zzdm;

    public zzd(@NonNull Activity activity, @NonNull Runnable runnable, @NonNull Object obj) {
        this.zzdl = activity;
        this.zzdm = runnable;
        this.cookie = obj;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzd)) {
            return false;
        }
        zzd zzd = (zzd) obj;
        return zzd.cookie.equals(this.cookie) && zzd.zzdm == this.zzdm && zzd.zzdl == this.zzdl;
    }

    @NonNull
    public final Activity getActivity() {
        return this.zzdl;
    }

    public final int hashCode() {
        return this.cookie.hashCode();
    }

    @NonNull
    public final Runnable zzj() {
        return this.zzdm;
    }

    @NonNull
    public final Object zzu() {
        return this.cookie;
    }
}
