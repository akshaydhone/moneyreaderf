package com.google.firebase.storage;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzo implements OnCompleteListener {
    private final StorageTask zzbg;
    private final Continuation zzbi;
    private final TaskCompletionSource zzbj;

    zzo(StorageTask storageTask, Continuation continuation, TaskCompletionSource taskCompletionSource) {
        this.zzbg = storageTask;
        this.zzbi = continuation;
        this.zzbj = taskCompletionSource;
    }

    public final void onComplete(Task task) {
        Task task2 = this.zzbg;
        Continuation continuation = this.zzbi;
        TaskCompletionSource taskCompletionSource = this.zzbj;
        try {
            Object then = continuation.then(task2);
            if (!taskCompletionSource.getTask().isComplete()) {
                taskCompletionSource.setResult(then);
            }
        } catch (Exception e) {
            if (e.getCause() instanceof Exception) {
                taskCompletionSource.setException((Exception) e.getCause());
            } else {
                taskCompletionSource.setException(e);
            }
        } catch (Exception e2) {
            taskCompletionSource.setException(e2);
        }
    }
}
