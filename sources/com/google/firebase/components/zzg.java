package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class zzg implements ComponentContainer {
    private final List<Component<?>> zzah;
    private final Map<Class<?>, zzi<?>> zzai = new HashMap();

    public zzg(Iterable<ComponentRegistrar> iterable, Component<?>... componentArr) {
        zzh zzh;
        zzh zzh2;
        List<Component> arrayList = new ArrayList();
        for (ComponentRegistrar components : iterable) {
            arrayList.addAll(components.getComponents());
        }
        Collections.addAll(arrayList, componentArr);
        Map hashMap = new HashMap(arrayList.size());
        for (Component component : arrayList) {
            zzh zzh3 = new zzh(component);
            for (Class put : component.zze()) {
                if (hashMap.put(put, zzh3) != null) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", new Object[]{(Class) r5.next()}));
                }
            }
        }
        for (zzh zzh22 : hashMap.values()) {
            for (Dependency dependency : zzh22.zzk().zzf()) {
                if (dependency.zzp()) {
                    zzh = (zzh) hashMap.get(dependency.zzn());
                    if (zzh != null) {
                        zzh22.zza(zzh);
                        zzh.zzb(zzh22);
                    }
                }
            }
        }
        Set<zzh> hashSet = new HashSet(hashMap.values());
        Set hashSet2 = new HashSet();
        for (zzh zzh222 : hashSet) {
            if (zzh222.zzl()) {
                hashSet2.add(zzh222);
            }
        }
        List arrayList2 = new ArrayList();
        while (!hashSet2.isEmpty()) {
            zzh222 = (zzh) hashSet2.iterator().next();
            hashSet2.remove(zzh222);
            arrayList2.add(zzh222.zzk());
            for (zzh zzh4 : zzh222.zzf()) {
                zzh4.zzc(zzh222);
                if (zzh4.zzl()) {
                    hashSet2.add(zzh4);
                }
            }
        }
        if (arrayList2.size() == arrayList.size()) {
            Collections.reverse(arrayList2);
            this.zzah = Collections.unmodifiableList(arrayList2);
            for (Component component2 : this.zzah) {
                zzi zzi = new zzi(component2.zzg(), new zzl(component2.zzf(), this));
                for (Class put2 : component2.zze()) {
                    this.zzai.put(put2, zzi);
                }
            }
            for (Component component22 : this.zzah) {
                for (Dependency dependency2 : component22.zzf()) {
                    if (dependency2.zzo() && !this.zzai.containsKey(dependency2.zzn())) {
                        throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", new Object[]{component22, dependency2.zzn()}));
                    }
                }
            }
            return;
        }
        List arrayList3 = new ArrayList();
        for (zzh zzh2222 : hashSet) {
            if (!(zzh2222.zzl() || zzh2222.zzm())) {
                arrayList3.add(zzh2222.zzk());
            }
        }
        throw new DependencyCycleException(arrayList3);
    }

    public final Object get(Class cls) {
        return ComponentContainer$$CC.get(this, cls);
    }

    public final <T> Provider<T> getProvider(Class<T> cls) {
        zzk.zza(cls, "Null interface requested.");
        return (Provider) this.zzai.get(cls);
    }

    public final void zzb(boolean z) {
        for (Component component : this.zzah) {
            if (component.zzh() || (component.zzi() && z)) {
                get((Class) component.zze().iterator().next());
            }
        }
    }
}
