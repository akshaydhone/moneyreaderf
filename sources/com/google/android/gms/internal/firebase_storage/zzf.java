package com.google.android.gms.internal.firebase_storage;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.firebase.FirebaseApp;
import java.util.Random;

public final class zzf {
    private static zzh zzdt = new zzi();
    private static Clock zzdu = DefaultClock.getInstance();
    private static Random zzdv = new Random();
    private FirebaseApp zzdw;
    private long zzdx;
    private volatile boolean zzdy;

    public zzf(FirebaseApp firebaseApp, long j) {
        this.zzdw = firebaseApp;
        this.zzdx = j;
    }

    public static boolean zze(int i) {
        return (i >= 500 && i < 600) || i == -2 || i == 429 || i == 408;
    }

    public final void cancel() {
        this.zzdy = true;
    }

    public final void reset() {
        this.zzdy = false;
    }

    public final void zza(@NonNull zzq zzq, boolean z) {
        Preconditions.checkNotNull(zzq);
        long elapsedRealtime = zzdu.elapsedRealtime() + this.zzdx;
        if (z) {
            zzq.zza(zzk.zza(this.zzdw), this.zzdw.getApplicationContext());
        } else {
            zzq.zzg(zzk.zza(this.zzdw));
        }
        int i = 1000;
        while (zzdu.elapsedRealtime() + ((long) i) <= elapsedRealtime && !zzq.zzab() && zze(zzq.getResultCode())) {
            try {
                zzdt.zzf(zzdv.nextInt(250) + i);
                if (i < 30000) {
                    if (zzq.getResultCode() != -2) {
                        i <<= 1;
                        Log.w("ExponenentialBackoff", "network error occurred, backing off/sleeping.");
                    } else {
                        Log.w("ExponenentialBackoff", "network unavailable, sleeping.");
                        i = 1000;
                    }
                }
                if (!this.zzdy) {
                    zzq.reset();
                    if (z) {
                        zzq.zza(zzk.zza(this.zzdw), this.zzdw.getApplicationContext());
                    } else {
                        zzq.zzg(zzk.zza(this.zzdw));
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                Log.w("ExponenentialBackoff", "thread interrupted during exponential backoff.");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
