package com.google.android.gms.internal.firebase_storage;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public final class zze {
    private static final Runtime zzdo = Runtime.getRuntime();
    private byte[] buffer = new byte[262144];
    private final InputStream zzdp;
    private int zzdq = 0;
    private boolean zzdr = false;
    private boolean zzds = true;

    public zze(InputStream inputStream, int i) {
        this.zzdp = inputStream;
    }

    private final int zzd(int i) {
        int max = Math.max(this.buffer.length << 1, i);
        long maxMemory = zzdo.maxMemory() - (zzdo.totalMemory() - zzdo.freeMemory());
        if (!this.zzds || ((long) max) >= maxMemory) {
            Log.w("AdaptiveStreamBuffer", "Turning off adaptive buffer resizing to conserve memory.");
        } else {
            try {
                Object obj = new byte[max];
                System.arraycopy(this.buffer, 0, obj, 0, this.zzdq);
                this.buffer = obj;
            } catch (OutOfMemoryError e) {
                Log.w("AdaptiveStreamBuffer", "Turning off adaptive buffer resizing due to low memory.");
                this.zzds = false;
            }
        }
        return this.buffer.length;
    }

    public final int available() {
        return this.zzdq;
    }

    public final void close() throws IOException {
        this.zzdp.close();
    }

    public final boolean isFinished() {
        return this.zzdr;
    }

    public final int zzb(int i) throws IOException {
        if (i <= this.zzdq) {
            this.zzdq -= i;
            System.arraycopy(this.buffer, i, this.buffer, 0, this.zzdq);
            return i;
        }
        this.zzdq = 0;
        int i2 = this.zzdq;
        while (i2 < i) {
            long skip = this.zzdp.skip((long) (i - i2));
            if (skip > 0) {
                i2 = (int) (((long) i2) + skip);
            } else if (skip != 0) {
                continue;
            } else if (this.zzdp.read() == -1) {
                return i2;
            } else {
                i2++;
            }
        }
        return i2;
    }

    public final int zzc(int i) throws IOException {
        if (i > this.buffer.length) {
            i = Math.min(i, zzd(i));
        }
        while (this.zzdq < i) {
            int read = this.zzdp.read(this.buffer, this.zzdq, i - this.zzdq);
            if (read == -1) {
                this.zzdr = true;
                break;
            }
            this.zzdq = read + this.zzdq;
        }
        return this.zzdq;
    }

    public final byte[] zzv() {
        return this.buffer;
    }
}
