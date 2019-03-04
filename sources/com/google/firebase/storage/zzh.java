package com.google.firebase.storage;

import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StreamDownloadTask.TaskSnapshot;

final class zzh implements OnSuccessListener<TaskSnapshot> {
    private final /* synthetic */ TaskCompletionSource zzav;

    zzh(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        this.zzav = taskCompletionSource;
    }

    public final /* synthetic */ void onSuccess(Object obj) {
        if (!this.zzav.getTask().isComplete()) {
            Log.e("StorageReference", "getBytes 'succeeded', but failed to set a Result.");
            this.zzav.setException(StorageException.fromErrorStatus(Status.RESULT_INTERNAL_ERROR));
        }
    }
}
