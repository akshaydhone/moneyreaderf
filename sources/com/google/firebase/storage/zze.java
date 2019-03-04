package com.google.firebase.storage;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zze implements OnSuccessListener<StorageMetadata> {
    private final /* synthetic */ TaskCompletionSource zzau;

    zze(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        this.zzau = taskCompletionSource;
    }

    public final /* synthetic */ void onSuccess(Object obj) {
        this.zzau.setResult(((StorageMetadata) obj).getDownloadUrl());
    }
}
