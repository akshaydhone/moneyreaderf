package com.google.android.gms.internal.measurement;

abstract class zzhh extends zzhg {
    private boolean zzvo;

    zzhh(zzgl zzgl) {
        super(zzgl);
        this.zzacw.zzb(this);
    }

    final boolean isInitialized() {
        return this.zzvo;
    }

    protected final void zzch() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    protected abstract boolean zzhf();

    protected void zzih() {
    }

    public final void zzjw() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzih();
        this.zzacw.zzju();
        this.zzvo = true;
    }

    public final void zzm() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzhf()) {
            this.zzacw.zzju();
            this.zzvo = true;
        }
    }
}
