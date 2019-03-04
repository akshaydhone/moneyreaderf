package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzf implements OnFailureListener {
    private final /* synthetic */ TaskCompletionSource zzau;

    zzf(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        this.zzau = taskCompletionSource;
    }

    public final void onFailure(@NonNull Exception exception) {
        this.zzau.setException(exception);
    }
}
