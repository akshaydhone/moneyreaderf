package com.google.firebase.storage;

import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StreamDownloadTask.StreamProcessor;
import com.google.firebase.storage.StreamDownloadTask.TaskSnapshot;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class zzi implements StreamProcessor {
    private final /* synthetic */ TaskCompletionSource zzav;
    private final /* synthetic */ long zzaw;

    zzi(StorageReference storageReference, long j, TaskCompletionSource taskCompletionSource) {
        this.zzaw = j;
        this.zzav = taskCompletionSource;
    }

    public final void doInBackground(TaskSnapshot taskSnapshot, InputStream inputStream) throws IOException {
        int i = 0;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[16384];
            while (true) {
                int read = inputStream.read(bArr, 0, 16384);
                if (read == -1) {
                    break;
                }
                i += read;
                if (((long) i) > this.zzaw) {
                    Log.e("StorageReference", "the maximum allowed buffer size was exceeded.");
                    throw new IndexOutOfBoundsException("the maximum allowed buffer size was exceeded.");
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            byteArrayOutputStream.flush();
            this.zzav.setResult(byteArrayOutputStream.toByteArray());
        } finally {
            inputStream.close();
        }
    }
}
