package com.google.firebase.storage;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzr implements OnSuccessListener {
    private final TaskCompletionSource zzbk;

    zzr(TaskCompletionSource taskCompletionSource) {
        this.zzbk = taskCompletionSource;
    }

    public final void onSuccess(Object obj) {
        this.zzbk.setResult(obj);
    }
}
