package com.google.firebase.storage;

import java.io.InputStream;
import java.util.concurrent.Callable;

final class zzw implements Callable<InputStream> {
    private final /* synthetic */ StreamDownloadTask zzce;

    zzw(StreamDownloadTask streamDownloadTask) {
        this.zzce = streamDownloadTask;
    }

    public final /* synthetic */ Object call() throws Exception {
        return this.zzce.zzn();
    }
}
