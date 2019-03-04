package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzg implements OnFailureListener {
    private final /* synthetic */ TaskCompletionSource zzav;

    zzg(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        this.zzav = taskCompletionSource;
    }

    public final void onFailure(@NonNull Exception exception) {
        this.zzav.setException(StorageException.fromExceptionAndHttpCode(exception, 0));
    }
}
