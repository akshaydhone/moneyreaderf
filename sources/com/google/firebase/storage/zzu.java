package com.google.firebase.storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class zzu {
    private static zzu zzbp = new zzu();
    private static BlockingQueue<Runnable> zzbq = new LinkedBlockingQueue();
    private static final ThreadPoolExecutor zzbr = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, zzbq, new zzv("Command-"));
    private static BlockingQueue<Runnable> zzbs = new LinkedBlockingQueue();
    private static final ThreadPoolExecutor zzbt = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS, zzbs, new zzv("Upload-"));
    private static BlockingQueue<Runnable> zzbu = new LinkedBlockingQueue();
    private static final ThreadPoolExecutor zzbv = new ThreadPoolExecutor(3, 3, 5, TimeUnit.SECONDS, zzbu, new zzv("Download-"));
    private static BlockingQueue<Runnable> zzbw = new LinkedBlockingQueue();
    private static final ThreadPoolExecutor zzbx = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, zzbw, new zzv("Callbacks-"));

    static {
        zzbr.allowCoreThreadTimeOut(true);
        zzbt.allowCoreThreadTimeOut(true);
        zzbv.allowCoreThreadTimeOut(true);
        zzbx.allowCoreThreadTimeOut(true);
    }

    public static void zza(Runnable runnable) {
        zzbr.execute(runnable);
    }

    public static void zzb(Runnable runnable) {
        zzbt.execute(runnable);
    }

    public static void zzc(Runnable runnable) {
        zzbv.execute(runnable);
    }

    public static void zzd(Runnable runnable) {
        zzbx.execute(runnable);
    }
}
