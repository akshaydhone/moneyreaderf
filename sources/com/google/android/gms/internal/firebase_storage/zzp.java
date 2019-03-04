package com.google.android.gms.internal.firebase_storage;

import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.VersionPolicy;
import com.google.firebase.FirebaseApp;
import org.json.JSONObject;

public final class zzp {
    private static final Object zzdk = new Object();
    @VisibleForTesting
    private static final VersionPolicy zzeb = DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION;
    private static volatile zzp zzec;
    private FirebaseApp zzdw;
    private zzn zzed;
    private Context zzee;

    private zzp(@NonNull FirebaseApp firebaseApp) throws RemoteException {
        this.zzee = firebaseApp.getApplicationContext();
        this.zzdw = firebaseApp;
        try {
            zzn zzn;
            IBinder instantiate = DynamiteModule.load(this.zzee, zzeb, "com.google.android.gms.firebasestorage").instantiate("com.google.firebase.storage.network.NetworkRequestFactoryImpl");
            if (instantiate == null) {
                zzn = null;
            } else {
                IInterface queryLocalInterface = instantiate.queryLocalInterface("com.google.firebase.storage.network.INetworkRequestFactory");
                zzn = queryLocalInterface instanceof zzn ? (zzn) queryLocalInterface : new zzo(instantiate);
            }
            this.zzed = zzn;
            if (this.zzed == null) {
                Log.e("NetworkRqFactoryProxy", "Unable to load Firebase Storage Network layer.");
                throw new RemoteException();
            }
        } catch (Throwable e) {
            Log.e("NetworkRqFactoryProxy", "NetworkRequestFactoryProxy failed with a RemoteException:", e);
            throw new RemoteException();
        }
    }

    public static zzp zzb(@NonNull FirebaseApp firebaseApp) throws RemoteException {
        if (zzec == null) {
            synchronized (zzdk) {
                if (zzec == null) {
                    zzec = new zzp(firebaseApp);
                }
            }
        }
        return zzec;
    }

    private final zzq zze(zzq zzq) {
        zzq.zza("x-firebase-gmpid", this.zzdw.getOptions().getApplicationId());
        return zzq;
    }

    @NonNull
    public final zzq zza(Uri uri, long j) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee), j)));
    }

    @Nullable
    public final zzq zza(Uri uri, String str) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee), str)));
    }

    @NonNull
    public final zzq zza(Uri uri, String str, byte[] bArr, long j, int i, boolean z) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee), str, ObjectWrapper.wrap(bArr), j, i, z)));
    }

    @NonNull
    public final zzq zza(Uri uri, JSONObject jSONObject) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee), ObjectWrapper.wrap(jSONObject))));
    }

    @NonNull
    public final zzq zza(Uri uri, JSONObject jSONObject, String str) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee), ObjectWrapper.wrap(jSONObject), str)));
    }

    @Nullable
    public final String zzad() {
        try {
            return this.zzed.zzad();
        } catch (Throwable e) {
            Log.e("NetworkRqFactoryProxy", "getBackendAuthority failed with a RemoteException:", e);
            return null;
        }
    }

    @NonNull
    public final zzq zzb(Uri uri, String str) throws RemoteException {
        return zze(new zzq(this.zzed.zzb(uri, ObjectWrapper.wrap(this.zzee), str)));
    }

    @Nullable
    public final String zzb(Uri uri) {
        try {
            return this.zzed.zzb(uri);
        } catch (Throwable e) {
            Log.e("NetworkRqFactoryProxy", "getDefaultURL failed with a RemoteException:", e);
            return null;
        }
    }

    @NonNull
    public final zzq zzc(Uri uri) throws RemoteException {
        return zze(new zzq(this.zzed.zza(uri, ObjectWrapper.wrap(this.zzee))));
    }

    @NonNull
    public final zzq zzd(Uri uri) throws RemoteException {
        return zze(new zzq(this.zzed.zzb(uri, ObjectWrapper.wrap(this.zzee))));
    }
}
