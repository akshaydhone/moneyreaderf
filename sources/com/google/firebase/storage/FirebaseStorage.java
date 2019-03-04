package com.google.firebase.storage;

import android.net.Uri;
import android.net.Uri.Builder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzk;
import com.google.firebase.FirebaseApp;
import java.util.HashMap;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;

public class FirebaseStorage {
    private static final Map<String, Map<String, FirebaseStorage>> zzo = new HashMap();
    @NonNull
    private final FirebaseApp zzp;
    @Nullable
    private final String zzq;
    private long zzr = 600000;
    private long zzs = 600000;
    private long zzt = 120000;

    private FirebaseStorage(@Nullable String str, @NonNull FirebaseApp firebaseApp) {
        this.zzq = str;
        this.zzp = firebaseApp;
    }

    @NonNull
    public static FirebaseStorage getInstance() {
        FirebaseApp instance = FirebaseApp.getInstance();
        Preconditions.checkArgument(instance != null, "You must call FirebaseApp.initialize() first.");
        return getInstance(instance);
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull FirebaseApp firebaseApp) {
        Preconditions.checkArgument(firebaseApp != null, "Null is not a valid value for the FirebaseApp.");
        String storageBucket = firebaseApp.getOptions().getStorageBucket();
        if (storageBucket == null) {
            return zza(firebaseApp, null);
        }
        String valueOf;
        try {
            String str = "gs://";
            valueOf = String.valueOf(firebaseApp.getOptions().getStorageBucket());
            return zza(firebaseApp, zzk.zza(firebaseApp, valueOf.length() != 0 ? str.concat(valueOf) : new String(str)));
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "FirebaseStorage";
            String str3 = "Unable to parse bucket:";
            valueOf = String.valueOf(storageBucket);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            throw new IllegalArgumentException("The storage Uri could not be parsed.");
        }
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        Preconditions.checkArgument(firebaseApp != null, "Null is not a valid value for the FirebaseApp.");
        if (str.toLowerCase().startsWith("gs://")) {
            try {
                return zza(firebaseApp, zzk.zza(firebaseApp, str));
            } catch (Throwable e) {
                Throwable th = e;
                String str2 = "FirebaseStorage";
                String str3 = "Unable to parse url:";
                String valueOf = String.valueOf(str);
                Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
                throw new IllegalArgumentException("The storage Uri could not be parsed.");
            }
        }
        throw new IllegalArgumentException("Please use a gs:// URL for your Firebase Storage bucket.");
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull String str) {
        FirebaseApp instance = FirebaseApp.getInstance();
        Preconditions.checkArgument(instance != null, "You must call FirebaseApp.initialize() first.");
        return getInstance(instance, str);
    }

    private static FirebaseStorage zza(@NonNull FirebaseApp firebaseApp, @Nullable Uri uri) {
        String host = uri != null ? uri.getHost() : null;
        if (uri == null || TextUtils.isEmpty(uri.getPath())) {
            FirebaseStorage firebaseStorage;
            synchronized (zzo) {
                Map map;
                Map map2 = (Map) zzo.get(firebaseApp.getName());
                if (map2 == null) {
                    HashMap hashMap = new HashMap();
                    zzo.put(firebaseApp.getName(), hashMap);
                    map = hashMap;
                } else {
                    map = map2;
                }
                firebaseStorage = (FirebaseStorage) map.get(host);
                if (firebaseStorage == null) {
                    firebaseStorage = new FirebaseStorage(host, firebaseApp);
                    map.put(host, firebaseStorage);
                }
            }
            return firebaseStorage;
        }
        throw new IllegalArgumentException("The storage Uri cannot contain a path element.");
    }

    @NonNull
    private final StorageReference zza(@NonNull Uri uri) {
        Preconditions.checkNotNull(uri, "uri must not be null");
        Object obj = this.zzq;
        boolean z = TextUtils.isEmpty(obj) || uri.getAuthority().equalsIgnoreCase(obj);
        Preconditions.checkArgument(z, "The supplied bucketname does not match the storage bucket of the current instance.");
        return new StorageReference(uri, this);
    }

    @NonNull
    public FirebaseApp getApp() {
        return this.zzp;
    }

    public long getMaxDownloadRetryTimeMillis() {
        return this.zzs;
    }

    public long getMaxOperationRetryTimeMillis() {
        return this.zzt;
    }

    public long getMaxUploadRetryTimeMillis() {
        return this.zzr;
    }

    @NonNull
    public StorageReference getReference() {
        if (!TextUtils.isEmpty(this.zzq)) {
            return zza(new Builder().scheme("gs").authority(this.zzq).path(InternalZipConstants.ZIP_FILE_SEPARATOR).build());
        }
        throw new IllegalStateException("FirebaseApp was not initialized with a bucket name.");
    }

    @NonNull
    public StorageReference getReference(@NonNull String str) {
        Preconditions.checkArgument(!TextUtils.isEmpty(str), "location must not be null or empty");
        String toLowerCase = str.toLowerCase();
        if (!toLowerCase.startsWith("gs://") && !toLowerCase.startsWith("https://") && !toLowerCase.startsWith("http://")) {
            return getReference().child(str);
        }
        throw new IllegalArgumentException("location should not be a full URL.");
    }

    @NonNull
    public StorageReference getReferenceFromUrl(@NonNull String str) {
        Preconditions.checkArgument(!TextUtils.isEmpty(str), "location must not be null or empty");
        String toLowerCase = str.toLowerCase();
        if (toLowerCase.startsWith("gs://") || toLowerCase.startsWith("https://") || toLowerCase.startsWith("http://")) {
            try {
                Uri zza = zzk.zza(this.zzp, str);
                if (zza != null) {
                    return zza(zza);
                }
                throw new IllegalArgumentException("The storage Uri could not be parsed.");
            } catch (Throwable e) {
                Throwable th = e;
                String str2 = "FirebaseStorage";
                String str3 = "Unable to parse location:";
                toLowerCase = String.valueOf(str);
                Log.e(str2, toLowerCase.length() != 0 ? str3.concat(toLowerCase) : new String(str3), th);
                throw new IllegalArgumentException("The storage Uri could not be parsed.");
            }
        }
        throw new IllegalArgumentException("The storage Uri could not be parsed.");
    }

    public void setMaxDownloadRetryTimeMillis(long j) {
        this.zzs = j;
    }

    public void setMaxOperationRetryTimeMillis(long j) {
        this.zzt = j;
    }

    public void setMaxUploadRetryTimeMillis(long j) {
        this.zzr = j;
    }
}
