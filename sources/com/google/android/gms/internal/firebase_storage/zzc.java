package com.google.android.gms.internal.firebase_storage;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzc {
    private static final zzc zzdi = new zzc();
    private final Map<Object, zzd> zzdj = new HashMap();
    private final Object zzdk = new Object();

    private static class zza extends LifecycleCallback {
        private final List<zzd> zzdn = new ArrayList();

        private zza(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.mLifecycleFragment.addCallback("StorageOnStopCallback", this);
        }

        public static zza zza(Activity activity) {
            LifecycleFragment fragment = LifecycleCallback.getFragment(new LifecycleActivity(activity));
            zza zza = (zza) fragment.getCallbackOrNull("StorageOnStopCallback", zza.class);
            return zza == null ? new zza(fragment) : zza;
        }

        @MainThread
        public void onStop() {
            ArrayList arrayList;
            synchronized (this.zzdn) {
                arrayList = new ArrayList(this.zzdn);
                this.zzdn.clear();
            }
            arrayList = arrayList;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                zzd zzd = (zzd) obj;
                if (zzd != null) {
                    Log.d("StorageOnStopCallback", "removing subscription from activity.");
                    zzd.zzj().run();
                    zzc.zzt().zzd(zzd.zzu());
                }
            }
        }

        public final void zza(zzd zzd) {
            synchronized (this.zzdn) {
                this.zzdn.add(zzd);
            }
        }

        public final void zzb(zzd zzd) {
            synchronized (this.zzdn) {
                this.zzdn.remove(zzd);
            }
        }
    }

    private zzc() {
    }

    @NonNull
    public static zzc zzt() {
        return zzdi;
    }

    public final void zza(@NonNull Activity activity, @NonNull Object obj, @NonNull Runnable runnable) {
        synchronized (this.zzdk) {
            zzd zzd = new zzd(activity, runnable, obj);
            zza.zza(activity).zza(zzd);
            this.zzdj.put(obj, zzd);
        }
    }

    public final void zzd(@NonNull Object obj) {
        synchronized (this.zzdk) {
            zzd zzd = (zzd) this.zzdj.get(obj);
            if (zzd != null) {
                zza.zza(zzd.getActivity()).zzb(zzd);
            }
        }
    }
}
