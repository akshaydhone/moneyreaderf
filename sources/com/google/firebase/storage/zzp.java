package com.google.firebase.storage;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzp implements OnCompleteListener {
    private final StorageTask zzbg;
    private final Continuation zzbi;
    private final TaskCompletionSource zzbj;

    zzp(StorageTask storageTask, Continuation continuation, TaskCompletionSource taskCompletionSource) {
        this.zzbg = storageTask;
        this.zzbi = continuation;
        this.zzbj = taskCompletionSource;
    }

    public final void onComplete(Task task) {
        Task task2 = this.zzbg;
        Continuation continuation = this.zzbi;
        TaskCompletionSource taskCompletionSource = this.zzbj;
        try {
            task2 = (Task) continuation.then(task2);
            if (!taskCompletionSource.getTask().isComplete()) {
                if (task2 == null) {
                    taskCompletionSource.setException(new NullPointerException("Continuation returned null"));
                    return;
                }
                task2.addOnSuccessListener(new zzr(taskCompletionSource));
                task2.addOnFailureListener(new zzs(taskCompletionSource));
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
