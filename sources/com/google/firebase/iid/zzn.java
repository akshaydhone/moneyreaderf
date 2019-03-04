package com.google.firebase.iid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzn implements Continuation<Bundle, String> {
    private final /* synthetic */ zzl zzbc;

    zzn(zzl zzl) {
        this.zzbc = zzl;
    }

    public final /* synthetic */ Object then(@NonNull Task task) throws Exception {
        return this.zzbc.zza((Bundle) task.getResult(IOException.class));
    }
}
