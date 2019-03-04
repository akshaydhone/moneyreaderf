package com.google.firebase.storage;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzc;
import com.google.android.gms.internal.firebase_storage.zzj;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

final class zzx<TListenerType, TResult extends ProvideError> {
    private final Queue<TListenerType> zzcm = new ConcurrentLinkedQueue();
    private final HashMap<TListenerType, zzj> zzcn = new HashMap();
    private StorageTask<TResult> zzco;
    private int zzcp;
    private zzab<TListenerType, TResult> zzcq;

    public zzx(@NonNull StorageTask<TResult> storageTask, int i, @NonNull zzab<TListenerType, TResult> zzab) {
        this.zzco = storageTask;
        this.zzcp = i;
        this.zzcq = zzab;
    }

    public final void zza(@Nullable Activity activity, @Nullable Executor executor, @NonNull TListenerType tListenerType) {
        boolean z = true;
        Preconditions.checkNotNull(tListenerType);
        synchronized (this.zzco.mSyncObject) {
            boolean z2 = (this.zzco.zzg() & this.zzcp) != 0;
            this.zzcm.add(tListenerType);
            zzj zzj = new zzj(executor);
            this.zzcn.put(tListenerType, zzj);
            if (activity != null) {
                if (VERSION.SDK_INT >= 17) {
                    if (activity.isDestroyed()) {
                        z = false;
                    }
                    Preconditions.checkArgument(z, "Activity is already destroyed!");
                }
                zzc.zzt().zza(activity, tListenerType, new zzy(this, tListenerType));
            }
        }
        if (z2) {
            zzj.zze(new zzz(this, tListenerType, this.zzco.zzh()));
        }
    }

    public final void zzc(@NonNull TListenerType tListenerType) {
        Preconditions.checkNotNull(tListenerType);
        synchronized (this.zzco.mSyncObject) {
            this.zzcn.remove(tListenerType);
            this.zzcm.remove(tListenerType);
            zzc.zzt().zzd(tListenerType);
        }
    }

    public final void zzq() {
        if ((this.zzco.zzg() & this.zzcp) != 0) {
            ProvideError zzh = this.zzco.zzh();
            for (Object next : this.zzcm) {
                zzj zzj = (zzj) this.zzcn.get(next);
                if (zzj != null) {
                    zzj.zze(new zzaa(this, next, zzh));
                }
            }
        }
    }
}
