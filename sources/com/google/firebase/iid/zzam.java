package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzam extends Handler {
    private final /* synthetic */ zzal zzck;

    zzam(zzal zzal, Looper looper) {
        this.zzck = zzal;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzck.zzb(message);
    }
}
