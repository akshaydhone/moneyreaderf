package com.google.firebase.storage;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

public abstract class StorageTask<TResult extends ProvideError> extends ControllableTask<TResult> {
    private static final HashMap<Integer, HashSet<Integer>> zzax = new HashMap();
    private static final HashMap<Integer, HashSet<Integer>> zzay = new HashMap();
    protected final Object mSyncObject = new Object();
    @VisibleForTesting
    private final zzx<OnSuccessListener<? super TResult>, TResult> zzaz = new zzx(this, 128, new zzj(this));
    @VisibleForTesting
    private final zzx<OnFailureListener, TResult> zzba = new zzx(this, 320, new zzk(this));
    @VisibleForTesting
    private final zzx<OnCompleteListener<TResult>, TResult> zzbb = new zzx(this, 448, new zzl(this));
    @VisibleForTesting
    private final zzx<OnProgressListener<? super TResult>, TResult> zzbc = new zzx(this, -465, zzm.zzbh);
    @VisibleForTesting
    private final zzx<OnPausedListener<? super TResult>, TResult> zzbd = new zzx(this, 16, zzn.zzbh);
    private volatile int zzbe = 1;
    private TResult zzbf;

    protected interface ProvideError {
        Exception getError();
    }

    public class SnapshotBase implements ProvideError {
        private final Exception zzbl;
        private final /* synthetic */ StorageTask zzbm;

        public SnapshotBase(@Nullable StorageTask storageTask, Exception exception) {
            this.zzbm = storageTask;
            if (exception != null) {
                this.zzbl = exception;
            } else if (storageTask.isCanceled()) {
                this.zzbl = StorageException.fromErrorStatus(Status.RESULT_CANCELED);
            } else if (storageTask.zzg() == 64) {
                this.zzbl = StorageException.fromErrorStatus(Status.RESULT_INTERNAL_ERROR);
            } else {
                this.zzbl = null;
            }
        }

        @Nullable
        public Exception getError() {
            return this.zzbl;
        }

        @NonNull
        public StorageReference getStorage() {
            return getTask().getStorage();
        }

        @NonNull
        public StorageTask<TResult> getTask() {
            return this.zzbm;
        }
    }

    static {
        zzax.put(Integer.valueOf(1), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(16), Integer.valueOf(256)})));
        zzax.put(Integer.valueOf(2), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(8), Integer.valueOf(32)})));
        zzax.put(Integer.valueOf(4), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(8), Integer.valueOf(32)})));
        zzax.put(Integer.valueOf(16), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(256)})));
        zzax.put(Integer.valueOf(64), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(256)})));
        zzay.put(Integer.valueOf(1), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(64)})));
        zzay.put(Integer.valueOf(2), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(4), Integer.valueOf(64), Integer.valueOf(128)})));
        zzay.put(Integer.valueOf(4), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(4), Integer.valueOf(64), Integer.valueOf(128)})));
        zzay.put(Integer.valueOf(8), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(16), Integer.valueOf(64), Integer.valueOf(128)})));
        zzay.put(Integer.valueOf(32), new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(256), Integer.valueOf(64), Integer.valueOf(128)})));
    }

    protected StorageTask() {
    }

    @NonNull
    private final <TContinuationResult> Task<TContinuationResult> zza(@Nullable Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzbb.zza(null, executor, new zzo(this, continuation, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    private static String zza(int i) {
        switch (i) {
            case 1:
                return "INTERNAL_STATE_NOT_STARTED";
            case 2:
                return "INTERNAL_STATE_QUEUED";
            case 4:
                return "INTERNAL_STATE_IN_PROGRESS";
            case 8:
                return "INTERNAL_STATE_PAUSING";
            case 16:
                return "INTERNAL_STATE_PAUSED";
            case 32:
                return "INTERNAL_STATE_CANCELING";
            case 64:
                return "INTERNAL_STATE_FAILURE";
            case 128:
                return "INTERNAL_STATE_SUCCESS";
            case 256:
                return "INTERNAL_STATE_CANCELED";
            default:
                return "Unknown Internal State!";
        }
    }

    @VisibleForTesting
    private final boolean zza(int[] iArr, boolean z) {
        HashMap hashMap = z ? zzax : zzay;
        synchronized (this.mSyncObject) {
            String zza;
            String str;
            int length = iArr.length;
            int i = 0;
            while (i < length) {
                int i2 = iArr[i];
                HashSet hashSet = (HashSet) hashMap.get(Integer.valueOf(this.zzbe));
                if (hashSet == null || !hashSet.contains(Integer.valueOf(i2))) {
                    i++;
                } else {
                    this.zzbe = i2;
                    switch (this.zzbe) {
                        case 2:
                            zzt.zzm().zza(this);
                            onQueued();
                            break;
                        case 4:
                            onProgress();
                            break;
                        case 16:
                            onPaused();
                            break;
                        case 64:
                            onFailure();
                            break;
                        case 128:
                            onSuccess();
                            break;
                        case 256:
                            onCanceled();
                            break;
                    }
                    this.zzaz.zzq();
                    this.zzba.zzq();
                    this.zzbb.zzq();
                    this.zzbd.zzq();
                    this.zzbc.zzq();
                    if (Log.isLoggable("StorageTask", 3)) {
                        zza = zza(i2);
                        String zza2 = zza(this.zzbe);
                        Log.d("StorageTask", new StringBuilder((String.valueOf(zza).length() + 53) + String.valueOf(zza2).length()).append("changed internal state to: ").append(zza).append(" isUser: ").append(z).append(" from state:").append(zza2).toString());
                    }
                    return true;
                }
            }
            zza = "StorageTask";
            if (iArr.length == 0) {
                str = "";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i22 : iArr) {
                    stringBuilder.append(zza(i22)).append(", ");
                }
                str = stringBuilder.substring(0, stringBuilder.length() - 2);
            }
            String zza3 = zza(this.zzbe);
            Log.w(zza, new StringBuilder((String.valueOf(str).length() + 62) + String.valueOf(zza3).length()).append("unable to change internal state to: ").append(str).append(" isUser: ").append(z).append(" from state:").append(zza3).toString());
            return false;
        }
    }

    @NonNull
    private final <TContinuationResult> Task<TContinuationResult> zzb(@Nullable Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzbb.zza(null, executor, new zzp(this, continuation, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    private final TResult zzi() {
        if (this.zzbf != null) {
            return this.zzbf;
        }
        if (!isComplete()) {
            return null;
        }
        if (this.zzbf == null) {
            this.zzbf = zzh();
        }
        return this.zzbf;
    }

    private final void zzk() {
        if (!isComplete() && !isPaused() && this.zzbe != 2 && !zza(256, false)) {
            zza(64, false);
        }
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        Preconditions.checkNotNull(onCompleteListener);
        Preconditions.checkNotNull(activity);
        this.zzbb.zza(activity, null, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        Preconditions.checkNotNull(onCompleteListener);
        this.zzbb.zza(null, null, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnCompleteListener(@NonNull Executor executor, @NonNull OnCompleteListener<TResult> onCompleteListener) {
        Preconditions.checkNotNull(onCompleteListener);
        Preconditions.checkNotNull(executor);
        this.zzbb.zza(null, executor, onCompleteListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        Preconditions.checkNotNull(onFailureListener);
        Preconditions.checkNotNull(activity);
        this.zzba.zza(activity, null, onFailureListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        Preconditions.checkNotNull(onFailureListener);
        this.zzba.zza(null, null, onFailureListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        Preconditions.checkNotNull(onFailureListener);
        Preconditions.checkNotNull(executor);
        this.zzba.zza(null, executor, onFailureListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull Activity activity, @NonNull OnPausedListener<? super TResult> onPausedListener) {
        Preconditions.checkNotNull(onPausedListener);
        Preconditions.checkNotNull(activity);
        this.zzbd.zza(activity, null, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull OnPausedListener<? super TResult> onPausedListener) {
        Preconditions.checkNotNull(onPausedListener);
        this.zzbd.zza(null, null, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnPausedListener(@NonNull Executor executor, @NonNull OnPausedListener<? super TResult> onPausedListener) {
        Preconditions.checkNotNull(onPausedListener);
        Preconditions.checkNotNull(executor);
        this.zzbd.zza(null, executor, onPausedListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull Activity activity, @NonNull OnProgressListener<? super TResult> onProgressListener) {
        Preconditions.checkNotNull(onProgressListener);
        Preconditions.checkNotNull(activity);
        this.zzbc.zza(activity, null, onProgressListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull OnProgressListener<? super TResult> onProgressListener) {
        Preconditions.checkNotNull(onProgressListener);
        this.zzbc.zza(null, null, onProgressListener);
        return this;
    }

    public StorageTask<TResult> addOnProgressListener(@NonNull Executor executor, @NonNull OnProgressListener<? super TResult> onProgressListener) {
        Preconditions.checkNotNull(onProgressListener);
        Preconditions.checkNotNull(executor);
        this.zzbc.zza(null, executor, onProgressListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        Preconditions.checkNotNull(activity);
        Preconditions.checkNotNull(onSuccessListener);
        this.zzaz.zza(activity, null, onSuccessListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        Preconditions.checkNotNull(onSuccessListener);
        this.zzaz.zza(null, null, onSuccessListener);
        return this;
    }

    @NonNull
    public StorageTask<TResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(onSuccessListener);
        this.zzaz.zza(null, executor, onSuccessListener);
        return this;
    }

    public boolean cancel() {
        return zza(new int[]{256, 32}, true);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Continuation<TResult, TContinuationResult> continuation) {
        return zza(null, (Continuation) continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWith(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation) {
        return zza(executor, (Continuation) continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return zzb(null, continuation);
    }

    @NonNull
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation) {
        return zzb(executor, continuation);
    }

    @Nullable
    public Exception getException() {
        return zzi() == null ? null : zzi().getError();
    }

    public TResult getResult() {
        if (zzi() == null) {
            throw new IllegalStateException();
        }
        Throwable error = zzi().getError();
        if (error == null) {
            return zzi();
        }
        throw new RuntimeExecutionException(error);
    }

    public <X extends Throwable> TResult getResult(@NonNull Class<X> cls) throws Throwable {
        if (zzi() == null) {
            throw new IllegalStateException();
        } else if (cls.isInstance(zzi().getError())) {
            throw ((Throwable) cls.cast(zzi().getError()));
        } else {
            Throwable error = zzi().getError();
            if (error == null) {
                return zzi();
            }
            throw new RuntimeExecutionException(error);
        }
    }

    public TResult getSnapshot() {
        return zzh();
    }

    @VisibleForTesting
    abstract StorageReference getStorage();

    public boolean isCanceled() {
        return this.zzbe == 256;
    }

    public boolean isComplete() {
        return ((this.zzbe & 128) == 0 && (this.zzbe & 320) == 0) ? false : true;
    }

    public boolean isInProgress() {
        return (this.zzbe & -465) != 0;
    }

    public boolean isPaused() {
        return (this.zzbe & 16) != 0;
    }

    public boolean isSuccessful() {
        return (this.zzbe & 128) != 0;
    }

    protected void onCanceled() {
    }

    protected void onFailure() {
    }

    protected void onPaused() {
    }

    protected void onProgress() {
    }

    protected void onQueued() {
    }

    protected void onSuccess() {
    }

    public boolean pause() {
        return zza(new int[]{16, 8}, true);
    }

    public StorageTask<TResult> removeOnCompleteListener(@NonNull OnCompleteListener<TResult> onCompleteListener) {
        Preconditions.checkNotNull(onCompleteListener);
        this.zzbb.zzc(onCompleteListener);
        return this;
    }

    public StorageTask<TResult> removeOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        Preconditions.checkNotNull(onFailureListener);
        this.zzba.zzc(onFailureListener);
        return this;
    }

    public StorageTask<TResult> removeOnPausedListener(@NonNull OnPausedListener<? super TResult> onPausedListener) {
        Preconditions.checkNotNull(onPausedListener);
        this.zzbd.zzc(onPausedListener);
        return this;
    }

    public StorageTask<TResult> removeOnProgressListener(@NonNull OnProgressListener<? super TResult> onProgressListener) {
        Preconditions.checkNotNull(onProgressListener);
        this.zzbc.zzc(onProgressListener);
        return this;
    }

    public StorageTask<TResult> removeOnSuccessListener(@NonNull OnSuccessListener<? super TResult> onSuccessListener) {
        Preconditions.checkNotNull(onSuccessListener);
        this.zzaz.zzc(onSuccessListener);
        return this;
    }

    @VisibleForTesting
    void resetState() {
    }

    public boolean resume() {
        if (!zza(2, true)) {
            return false;
        }
        resetState();
        schedule();
        return true;
    }

    @VisibleForTesting
    abstract void run();

    @VisibleForTesting
    abstract void schedule();

    @VisibleForTesting
    @NonNull
    abstract TResult zza();

    @VisibleForTesting
    final boolean zza(int i, boolean z) {
        return zza(new int[]{i}, z);
    }

    @VisibleForTesting
    final boolean zzf() {
        if (!zza(2, false)) {
            return false;
        }
        schedule();
        return true;
    }

    @VisibleForTesting
    final int zzg() {
        return this.zzbe;
    }

    @VisibleForTesting
    @NonNull
    final TResult zzh() {
        TResult zza;
        synchronized (this.mSyncObject) {
            zza = zza();
        }
        return zza;
    }

    @VisibleForTesting
    final Runnable zzj() {
        return new zzq(this);
    }

    final /* synthetic */ void zzl() {
        try {
            run();
        } finally {
            zzk();
        }
    }
}
