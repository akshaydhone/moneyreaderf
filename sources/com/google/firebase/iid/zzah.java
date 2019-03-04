package com.google.firebase.iid;

import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.concurrent.GuardedBy;

final class zzah {
    @GuardedBy("this")
    private final Map<Pair<String, String>, TaskCompletionSource<String>> zzca = new ArrayMap();

    zzah() {
    }

    private static String zza(TaskCompletionSource<String> taskCompletionSource) throws IOException {
        Throwable cause;
        try {
            return (String) Tasks.await(taskCompletionSource.getTask());
        } catch (ExecutionException e) {
            cause = e.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            } else if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else {
                throw new IOException(cause);
            }
        } catch (Throwable cause2) {
            throw new IOException(cause2);
        }
    }

    private static String zza(zzak zzak, TaskCompletionSource<String> taskCompletionSource) throws IOException {
        Exception e;
        try {
            String zzp = zzak.zzp();
            taskCompletionSource.setResult(zzp);
            return zzp;
        } catch (IOException e2) {
            e = e2;
            taskCompletionSource.setException(e);
            throw e;
        } catch (RuntimeException e3) {
            e = e3;
            taskCompletionSource.setException(e);
            throw e;
        }
    }

    private final synchronized zzak zzb(String str, String str2, zzak zzak) {
        zzak zzai;
        Pair pair = new Pair(str, str2);
        TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zzca.get(pair);
        if (taskCompletionSource != null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(pair);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 29).append("Joining ongoing request for: ").append(valueOf).toString());
            }
            zzai = new zzai(taskCompletionSource);
        } else {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf2 = String.valueOf(pair);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 24).append("Making new request for: ").append(valueOf2).toString());
            }
            TaskCompletionSource taskCompletionSource2 = new TaskCompletionSource();
            this.zzca.put(pair, taskCompletionSource2);
            zzai = new zzaj(this, zzak, taskCompletionSource2, pair);
        }
        return zzai;
    }

    final /* synthetic */ String zza(zzak zzak, TaskCompletionSource taskCompletionSource, Pair pair) throws IOException {
        try {
            String zza = zza(zzak, taskCompletionSource);
            synchronized (this) {
                this.zzca.remove(pair);
            }
            return zza;
        } catch (Throwable th) {
            synchronized (this) {
                this.zzca.remove(pair);
            }
        }
    }

    @WorkerThread
    final String zza(String str, String str2, zzak zzak) throws IOException {
        return zzb(str, str2, zzak).zzp();
    }
}
