package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzacb implements Cloneable {
    private Object value;
    private zzabz<?, ?> zzbxe;
    private List<zzacg> zzbxf = new ArrayList();

    zzacb() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zza()];
        zza(zzabw.zzj(bArr));
        return bArr;
    }

    private final zzacb zzvg() {
        zzacb zzacb = new zzacb();
        try {
            zzacb.zzbxe = this.zzbxe;
            if (this.zzbxf == null) {
                zzacb.zzbxf = null;
            } else {
                zzacb.zzbxf.addAll(this.zzbxf);
            }
            if (this.value != null) {
                if (this.value instanceof zzace) {
                    zzacb.value = (zzace) ((zzace) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    zzacb.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    zzacb.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    zzacb.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    zzacb.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    zzacb.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    zzacb.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    zzacb.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzace[]) {
                    zzace[] zzaceArr = (zzace[]) this.value;
                    r4 = new zzace[zzaceArr.length];
                    zzacb.value = r4;
                    for (r2 = 0; r2 < zzaceArr.length; r2++) {
                        r4[r2] = (zzace) zzaceArr[r2].clone();
                    }
                }
            }
            return zzacb;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzvg();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzacb)) {
            return false;
        }
        zzacb zzacb = (zzacb) obj;
        if (this.value != null && zzacb.value != null) {
            return this.zzbxe == zzacb.zzbxe ? !this.zzbxe.zzbwx.isArray() ? this.value.equals(zzacb.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) zzacb.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) zzacb.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) zzacb.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) zzacb.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) zzacb.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) zzacb.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) zzacb.value) : false;
        } else {
            if (this.zzbxf != null && zzacb.zzbxf != null) {
                return this.zzbxf.equals(zzacb.zzbxf);
            }
            try {
                return Arrays.equals(toByteArray(), zzacb.toByteArray());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    final int zza() {
        int i = 0;
        int i2;
        if (this.value != null) {
            zzabz zzabz = this.zzbxe;
            Object obj = this.value;
            if (!zzabz.zzbwy) {
                return zzabz.zzv(obj);
            }
            int length = Array.getLength(obj);
            for (i2 = 0; i2 < length; i2++) {
                if (Array.get(obj, i2) != null) {
                    i += zzabz.zzv(Array.get(obj, i2));
                }
            }
            return i;
        }
        i2 = 0;
        for (zzacg zzacg : this.zzbxf) {
            i2 = (zzacg.zzbrc.length + (zzabw.zzas(zzacg.tag) + 0)) + i2;
        }
        return i2;
    }

    final void zza(zzabw zzabw) throws IOException {
        if (this.value != null) {
            zzabz zzabz = this.zzbxe;
            Object obj = this.value;
            if (zzabz.zzbwy) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object obj2 = Array.get(obj, i);
                    if (obj2 != null) {
                        zzabz.zza(obj2, zzabw);
                    }
                }
                return;
            }
            zzabz.zza(obj, zzabw);
            return;
        }
        for (zzacg zzacg : this.zzbxf) {
            zzabw.zzar(zzacg.tag);
            zzabw.zzk(zzacg.zzbrc);
        }
    }

    final void zza(zzacg zzacg) throws IOException {
        if (this.zzbxf != null) {
            this.zzbxf.add(zzacg);
            return;
        }
        Object zzb;
        if (this.value instanceof zzace) {
            byte[] bArr = zzacg.zzbrc;
            zzabv zza = zzabv.zza(bArr, 0, bArr.length);
            int zzuy = zza.zzuy();
            if (zzuy != bArr.length - zzabw.zzao(zzuy)) {
                throw zzacd.zzvh();
            }
            zzb = ((zzace) this.value).zzb(zza);
        } else if (this.value instanceof zzace[]) {
            zzace[] zzaceArr = (zzace[]) this.zzbxe.zzi(Collections.singletonList(zzacg));
            zzace[] zzaceArr2 = (zzace[]) this.value;
            zzace[] zzaceArr3 = (zzace[]) Arrays.copyOf(zzaceArr2, zzaceArr2.length + zzaceArr.length);
            System.arraycopy(zzaceArr, 0, zzaceArr3, zzaceArr2.length, zzaceArr.length);
        } else {
            zzb = this.zzbxe.zzi(Collections.singletonList(zzacg));
        }
        this.zzbxe = this.zzbxe;
        this.value = zzb;
        this.zzbxf = null;
    }

    final <T> T zzb(zzabz<?, T> zzabz) {
        if (this.value == null) {
            this.zzbxe = zzabz;
            this.value = zzabz.zzi(this.zzbxf);
            this.zzbxf = null;
        } else if (!this.zzbxe.equals(zzabz)) {
            throw new IllegalStateException("Tried to getExtension with a different Extension.");
        }
        return this.value;
    }
}
