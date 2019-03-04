package com.google.android.gms.internal.firebase_storage;

import android.net.Uri;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public interface zzn extends IInterface {
    zzl zza(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException;

    zzl zza(Uri uri, IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    zzl zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2) throws RemoteException;

    zzl zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, String str) throws RemoteException;

    zzl zza(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException;

    zzl zza(Uri uri, IObjectWrapper iObjectWrapper, String str, IObjectWrapper iObjectWrapper2, long j, int i, boolean z) throws RemoteException;

    String zzad() throws RemoteException;

    zzl zzb(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException;

    zzl zzb(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException;

    String zzb(Uri uri) throws RemoteException;
}
