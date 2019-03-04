package com.google.firebase.storage;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzs implements OnFailureListener {
    private final TaskCompletionSource zzbk;

    zzs(TaskCompletionSource taskCompletionSource) {
        this.zzbk = taskCompletionSource;
    }

    public final void onFailure(Exception exception) {
        this.zzbk.setException(exception);
    }
}
