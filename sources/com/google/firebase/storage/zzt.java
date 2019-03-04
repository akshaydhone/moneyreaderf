package com.google.firebase.storage;

import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class zzt {
    private static final zzt zzbn = new zzt();
    private final Object mSyncObject = new Object();
    private final Map<String, WeakReference<StorageTask>> zzbo = new HashMap();

    zzt() {
    }

    static zzt zzm() {
        return zzbn;
    }

    public final List<UploadTask> zza(@NonNull StorageReference storageReference) {
        List<UploadTask> unmodifiableList;
        synchronized (this.mSyncObject) {
            List arrayList = new ArrayList();
            String storageReference2 = storageReference.toString();
            for (Entry entry : this.zzbo.entrySet()) {
                if (((String) entry.getKey()).startsWith(storageReference2)) {
                    StorageTask storageTask = (StorageTask) ((WeakReference) entry.getValue()).get();
                    if (storageTask instanceof UploadTask) {
                        arrayList.add((UploadTask) storageTask);
                    }
                }
            }
            unmodifiableList = Collections.unmodifiableList(arrayList);
        }
        return unmodifiableList;
    }

    public final void zza(StorageTask storageTask) {
        synchronized (this.mSyncObject) {
            this.zzbo.put(storageTask.getStorage().toString(), new WeakReference(storageTask));
        }
    }

    public final List<FileDownloadTask> zzb(@NonNull StorageReference storageReference) {
        List<FileDownloadTask> unmodifiableList;
        synchronized (this.mSyncObject) {
            List arrayList = new ArrayList();
            String storageReference2 = storageReference.toString();
            for (Entry entry : this.zzbo.entrySet()) {
                if (((String) entry.getKey()).startsWith(storageReference2)) {
                    StorageTask storageTask = (StorageTask) ((WeakReference) entry.getValue()).get();
                    if (storageTask instanceof FileDownloadTask) {
                        arrayList.add((FileDownloadTask) storageTask);
                    }
                }
            }
            unmodifiableList = Collections.unmodifiableList(arrayList);
        }
        return unmodifiableList;
    }

    public final void zzb(StorageTask storageTask) {
        synchronized (this.mSyncObject) {
            String storageReference = storageTask.getStorage().toString();
            WeakReference weakReference = (WeakReference) this.zzbo.get(storageReference);
            StorageTask storageTask2 = weakReference != null ? (StorageTask) weakReference.get() : null;
            if (storageTask2 == null || storageTask2 == storageTask) {
                this.zzbo.remove(storageReference);
            }
        }
    }
}
