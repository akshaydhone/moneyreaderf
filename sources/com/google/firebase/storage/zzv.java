package com.google.firebase.storage;

import android.support.annotation.NonNull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

final class zzv implements ThreadFactory {
    private final AtomicInteger zzby = new AtomicInteger(1);
    private final String zzbz;

    zzv(@NonNull String str) {
        this.zzbz = str;
    }

    public final Thread newThread(@NonNull Runnable runnable) {
        String str = this.zzbz;
        Thread thread = new Thread(runnable, new StringBuilder(String.valueOf(str).length() + 27).append("FirebaseStorage-").append(str).append(this.zzby.getAndIncrement()).toString());
        thread.setDaemon(false);
        thread.setPriority(9);
        return thread;
    }
}
