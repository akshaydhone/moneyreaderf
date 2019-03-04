package com.google.firebase.components;

import java.util.HashSet;
import java.util.Set;

final class zzh {
    private final Component<?> zzaj;
    private final Set<zzh> zzak = new HashSet();
    private final Set<zzh> zzal = new HashSet();

    zzh(Component<?> component) {
        this.zzaj = component;
    }

    final void zza(zzh zzh) {
        this.zzak.add(zzh);
    }

    final void zzb(zzh zzh) {
        this.zzal.add(zzh);
    }

    final void zzc(zzh zzh) {
        this.zzal.remove(zzh);
    }

    final Set<zzh> zzf() {
        return this.zzak;
    }

    final Component<?> zzk() {
        return this.zzaj;
    }

    final boolean zzl() {
        return this.zzal.isEmpty();
    }

    final boolean zzm() {
        return this.zzak.isEmpty();
    }
}
