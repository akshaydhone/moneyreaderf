package com.google.android.gms.internal.firebase_storage;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.storage.zzu;
import java.util.concurrent.Executor;

public final class zzj {
    private static boolean zzea = false;
    private final Handler handler;
    private final Executor zzdz;

    public zzj(@Nullable Executor executor) {
        this.zzdz = executor;
        if (this.zzdz == null) {
            this.handler = new Handler(Looper.getMainLooper());
        } else {
            this.handler = null;
        }
    }

    public final void zze(@NonNull Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (this.handler != null) {
            this.handler.post(runnable);
        } else if (this.zzdz != null) {
            this.zzdz.execute(runnable);
        } else {
            zzu.zzd(runnable);
        }
    }
}
