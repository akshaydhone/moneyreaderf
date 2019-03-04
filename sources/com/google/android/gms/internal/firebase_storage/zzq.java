package com.google.android.gms.internal.firebase_storage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageException;
import java.io.InputStream;
import java.net.SocketException;
import org.json.JSONObject;

public final class zzq {
    private zzl zzef;
    private Exception zzeg;
    private int zzeh;
    private Exception zzei;

    public zzq(@NonNull zzl zzl) {
        this.zzef = zzl;
    }

    private final void zza(Exception exception, String str) {
        Log.e("NetworkRequestProxy", str, exception);
        this.zzeg = exception;
    }

    @Nullable
    public final Exception getException() {
        try {
            return this.zzei != null ? this.zzei : this.zzeg != null ? this.zzeg : (Exception) ObjectWrapper.unwrap(this.zzef.zzaa());
        } catch (Exception e) {
            zza(e, "getException failed with a RemoteException");
            return null;
        }
    }

    public final int getResultCode() {
        try {
            return this.zzeh != 0 ? this.zzeh : this.zzef.getResultCode();
        } catch (Exception e) {
            zza(e, "getResultCode failed with a RemoteException");
            return 0;
        }
    }

    @Nullable
    public final InputStream getStream() {
        try {
            return (InputStream) ObjectWrapper.unwrap(this.zzef.zzx());
        } catch (Exception e) {
            zza(e, "getStream failed with a RemoteException");
            return null;
        }
    }

    public final void reset() {
        try {
            this.zzeh = 0;
            this.zzei = null;
            this.zzef.reset();
        } catch (Exception e) {
            zza(e, "reset failed with a RemoteException");
        }
    }

    public final void zza(@Nullable String str, @NonNull Context context) {
        try {
            Object obj;
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                this.zzeh = -2;
                this.zzei = new SocketException("Network subsystem is unavailable");
                obj = null;
            } else {
                obj = 1;
            }
            if (obj != null) {
                this.zzef.zzf(str);
            }
        } catch (RemoteException e) {
            zza(this.zzeg, "performRequest failed with a RemoteException");
        }
    }

    public final void zza(String str, String str2) {
        try {
            this.zzef.zza(str, str2);
        } catch (Throwable e) {
            Throwable th = e;
            String str3 = "NetworkRequestProxy";
            String str4 = "Caught remote exception setting custom header:";
            String valueOf = String.valueOf(str);
            Log.e(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4), th);
        }
    }

    public final boolean zzab() {
        boolean z = false;
        try {
            if (this.zzeh != -2 && this.zzei == null) {
                z = this.zzef.zzab();
            }
        } catch (Exception e) {
            zza(e, "isResultSuccess failed with a RemoteException");
        }
        return z;
    }

    public final int zzac() {
        try {
            return this.zzef.zzac();
        } catch (Exception e) {
            zza(e, "getResultingContentLength failed with a RemoteException");
            return 0;
        }
    }

    @NonNull
    public final JSONObject zzae() throws RemoteException {
        return (JSONObject) ObjectWrapper.unwrap(this.zzef.zzy());
    }

    public final <TResult> void zzb(TaskCompletionSource<TResult> taskCompletionSource, TResult tResult) {
        Throwable exception = getException();
        if (zzab() && exception == null) {
            taskCompletionSource.setResult(tResult);
        } else {
            taskCompletionSource.setException(StorageException.fromExceptionAndHttpCode(exception, getResultCode()));
        }
    }

    public final void zzg(@Nullable String str) {
        try {
            this.zzef.zzg(str);
        } catch (Exception e) {
            zza(e, "performRequestStart failed with a RemoteException");
        }
    }

    @Nullable
    public final String zzh(String str) {
        try {
            return this.zzef.zzh(str);
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "NetworkRequestProxy";
            String str3 = "getResultString failed with a RemoteException:";
            String valueOf = String.valueOf(str);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            return null;
        }
    }

    public final void zzw() {
        try {
            if (this.zzef != null) {
                this.zzef.zzw();
            }
        } catch (Exception e) {
            zza(e, "performRequestEnd failed with a RemoteException");
        }
    }

    @Nullable
    public final String zzz() {
        try {
            this.zzef.zzz();
        } catch (Exception e) {
            zza(e, "getRawResult failed with a RemoteException");
        }
        return null;
    }
}
