package com.google.android.gms.internal.firebase_storage;

import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public final class zzo extends zza implements zzn {
    zzo(IBinder iBinder) {
        super(iBinder, "com.google.firebase.storage.network.INetworkRequestFactory");
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        Parcel transactAndReadException = transactAndReadException(9, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, String str) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        obtainAndWriteInterfaceToken.writeString(str);
        Parcel transactAndReadException = transactAndReadException(8, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeString(str);
        Parcel transactAndReadException = transactAndReadException(6, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zza(Uri uri, IObjectWrapper iObjectWrapper, String str, IObjectWrapper iObjectWrapper2, long j, int i, boolean z) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeString(str);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        obtainAndWriteInterfaceToken.writeLong(j);
        obtainAndWriteInterfaceToken.writeInt(i);
        zzb.zza(obtainAndWriteInterfaceToken, z);
        Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final String zzad() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(10, obtainAndWriteInterfaceToken());
        String readString = transactAndReadException.readString();
        transactAndReadException.recycle();
        return readString;
    }

    public final zzl zzb(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final zzl zzb(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        zzl zzl;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeString(str);
        Parcel transactAndReadException = transactAndReadException(7, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            zzl = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzl = queryLocalInterface instanceof zzl ? (zzl) queryLocalInterface : new zzm(readStrongBinder);
        }
        transactAndReadException.recycle();
        return zzl;
    }

    public final String zzb(Uri uri) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        obtainAndWriteInterfaceToken = transactAndReadException(11, obtainAndWriteInterfaceToken);
        String readString = obtainAndWriteInterfaceToken.readString();
        obtainAndWriteInterfaceToken.recycle();
        return readString;
    }
}
