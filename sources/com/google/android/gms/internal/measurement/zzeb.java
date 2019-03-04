package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class zzeb extends zzjq {
    zzeb(zzjr zzjr) {
        super(zzjr);
    }

    private final Boolean zza(double d, zzkg zzkg) {
        try {
            return zza(new BigDecimal(d), zzkg, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzkg zzkg) {
        try {
            return zza(new BigDecimal(j), zzkg, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @VisibleForTesting
    private static Boolean zza(Boolean bool, boolean z) {
        return bool == null ? null : Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, int i, boolean z, String str2, List<String> list, String str3) {
        if (str == null) {
            return null;
        }
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!(z || i == 1)) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (i) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException e) {
                    zzge().zzip().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                return Boolean.valueOf(str.startsWith(str2));
            case 3:
                return Boolean.valueOf(str.endsWith(str2));
            case 4:
                return Boolean.valueOf(str.contains(str2));
            case 5:
                return Boolean.valueOf(str.equals(str2));
            case 6:
                return Boolean.valueOf(list.contains(str));
            default:
                return null;
        }
    }

    private final Boolean zza(String str, zzkg zzkg) {
        Boolean bool = null;
        if (zzka.zzck(str)) {
            try {
                bool = zza(new BigDecimal(str), zzkg, 0.0d);
            } catch (NumberFormatException e) {
            }
        }
        return bool;
    }

    @VisibleForTesting
    private final Boolean zza(String str, zzki zzki) {
        int i = 0;
        String str2 = null;
        Preconditions.checkNotNull(zzki);
        if (str == null || zzki.zzash == null || zzki.zzash.intValue() == 0) {
            return null;
        }
        List list;
        if (zzki.zzash.intValue() == 6) {
            if (zzki.zzask == null || zzki.zzask.length == 0) {
                return null;
            }
        } else if (zzki.zzasi == null) {
            return null;
        }
        int intValue = zzki.zzash.intValue();
        boolean z = zzki.zzasj != null && zzki.zzasj.booleanValue();
        String toUpperCase = (z || intValue == 1 || intValue == 6) ? zzki.zzasi : zzki.zzasi.toUpperCase(Locale.ENGLISH);
        if (zzki.zzask == null) {
            list = null;
        } else {
            String[] strArr = zzki.zzask;
            if (z) {
                list = Arrays.asList(strArr);
            } else {
                list = new ArrayList();
                int length = strArr.length;
                while (i < length) {
                    list.add(strArr[i].toUpperCase(Locale.ENGLISH));
                    i++;
                }
            }
        }
        if (intValue == 1) {
            str2 = toUpperCase;
        }
        return zza(str, intValue, z, toUpperCase, list, str2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.android.gms.common.util.VisibleForTesting
    private static java.lang.Boolean zza(java.math.BigDecimal r10, com.google.android.gms.internal.measurement.zzkg r11, double r12) {
        /*
        r8 = 4;
        r7 = -1;
        r1 = 0;
        r0 = 1;
        r2 = 0;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r11);
        r3 = r11.zzarz;
        if (r3 == 0) goto L_0x0014;
    L_0x000c:
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != 0) goto L_0x0016;
    L_0x0014:
        r0 = r2;
    L_0x0015:
        return r0;
    L_0x0016:
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0028;
    L_0x001e:
        r3 = r11.zzasc;
        if (r3 == 0) goto L_0x0026;
    L_0x0022:
        r3 = r11.zzasd;
        if (r3 != 0) goto L_0x002e;
    L_0x0026:
        r0 = r2;
        goto L_0x0015;
    L_0x0028:
        r3 = r11.zzasb;
        if (r3 != 0) goto L_0x002e;
    L_0x002c:
        r0 = r2;
        goto L_0x0015;
    L_0x002e:
        r3 = r11.zzarz;
        r6 = r3.intValue();
        r3 = r11.zzarz;
        r3 = r3.intValue();
        if (r3 != r8) goto L_0x0066;
    L_0x003c:
        r3 = r11.zzasc;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 == 0) goto L_0x004c;
    L_0x0044:
        r3 = r11.zzasd;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 != 0) goto L_0x004e;
    L_0x004c:
        r0 = r2;
        goto L_0x0015;
    L_0x004e:
        r4 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = r11.zzasc;	 Catch:{ NumberFormatException -> 0x0063 }
        r4.<init>(r3);	 Catch:{ NumberFormatException -> 0x0063 }
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r11.zzasd;	 Catch:{ NumberFormatException -> 0x0063 }
        r3.<init>(r5);	 Catch:{ NumberFormatException -> 0x0063 }
        r5 = r2;
    L_0x005d:
        if (r6 != r8) goto L_0x007e;
    L_0x005f:
        if (r4 != 0) goto L_0x0080;
    L_0x0061:
        r0 = r2;
        goto L_0x0015;
    L_0x0063:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x0066:
        r3 = r11.zzasb;
        r3 = com.google.android.gms.internal.measurement.zzka.zzck(r3);
        if (r3 != 0) goto L_0x0070;
    L_0x006e:
        r0 = r2;
        goto L_0x0015;
    L_0x0070:
        r3 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x007b }
        r4 = r11.zzasb;	 Catch:{ NumberFormatException -> 0x007b }
        r3.<init>(r4);	 Catch:{ NumberFormatException -> 0x007b }
        r4 = r2;
        r5 = r3;
        r3 = r2;
        goto L_0x005d;
    L_0x007b:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0015;
    L_0x007e:
        if (r5 == 0) goto L_0x0083;
    L_0x0080:
        switch(r6) {
            case 1: goto L_0x0085;
            case 2: goto L_0x0092;
            case 3: goto L_0x00a0;
            case 4: goto L_0x00ee;
            default: goto L_0x0083;
        };
    L_0x0083:
        r0 = r2;
        goto L_0x0015;
    L_0x0085:
        r2 = r10.compareTo(r5);
        if (r2 != r7) goto L_0x0090;
    L_0x008b:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x0090:
        r0 = r1;
        goto L_0x008b;
    L_0x0092:
        r2 = r10.compareTo(r5);
        if (r2 != r0) goto L_0x009e;
    L_0x0098:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x009e:
        r0 = r1;
        goto L_0x0098;
    L_0x00a0:
        r2 = 0;
        r2 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x00e0;
    L_0x00a6:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.subtract(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r0) goto L_0x00de;
    L_0x00bf:
        r2 = new java.math.BigDecimal;
        r2.<init>(r12);
        r3 = new java.math.BigDecimal;
        r4 = 2;
        r3.<init>(r4);
        r2 = r2.multiply(r3);
        r2 = r5.add(r2);
        r2 = r10.compareTo(r2);
        if (r2 != r7) goto L_0x00de;
    L_0x00d8:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00de:
        r0 = r1;
        goto L_0x00d8;
    L_0x00e0:
        r2 = r10.compareTo(r5);
        if (r2 != 0) goto L_0x00ec;
    L_0x00e6:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x00ec:
        r0 = r1;
        goto L_0x00e6;
    L_0x00ee:
        r2 = r10.compareTo(r4);
        if (r2 == r7) goto L_0x0100;
    L_0x00f4:
        r2 = r10.compareTo(r3);
        if (r2 == r0) goto L_0x0100;
    L_0x00fa:
        r0 = java.lang.Boolean.valueOf(r0);
        goto L_0x0015;
    L_0x0100:
        r0 = r1;
        goto L_0x00fa;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzeb.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzkg, double):java.lang.Boolean");
    }

    @WorkerThread
    final zzkm[] zza(String str, zzkn[] zzknArr, zzks[] zzksArr) {
        int intValue;
        BitSet bitSet;
        BitSet bitSet2;
        int i;
        zzhg zzix;
        int i2;
        int length;
        Map map;
        Map map2;
        zzkm zzkm;
        Preconditions.checkNotEmpty(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        Map zzbf = zzix().zzbf(str);
        if (zzbf != null) {
            for (Integer intValue2 : zzbf.keySet()) {
                intValue = intValue2.intValue();
                zzkr zzkr = (zzkr) zzbf.get(Integer.valueOf(intValue));
                bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue));
                bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue));
                if (bitSet == null) {
                    bitSet = new BitSet();
                    arrayMap2.put(Integer.valueOf(intValue), bitSet);
                    bitSet2 = new BitSet();
                    arrayMap3.put(Integer.valueOf(intValue), bitSet2);
                }
                for (i = 0; i < (zzkr.zzauk.length << 6); i++) {
                    if (zzka.zza(zzkr.zzauk, i)) {
                        zzge().zzit().zze("Filter already evaluated. audience ID, filter ID", Integer.valueOf(intValue), Integer.valueOf(i));
                        bitSet2.set(i);
                        if (zzka.zza(zzkr.zzaul, i)) {
                            bitSet.set(i);
                        }
                    }
                }
                zzkm zzkm2 = new zzkm();
                arrayMap.put(Integer.valueOf(intValue), zzkm2);
                zzkm2.zzasy = Boolean.valueOf(false);
                zzkm2.zzasx = zzkr;
                zzkm2.zzasw = new zzkr();
                zzkm2.zzasw.zzaul = zzka.zza(bitSet);
                zzkm2.zzasw.zzauk = zzka.zza(bitSet2);
            }
        }
        if (zzknArr != null) {
            zzkn zzkn = null;
            long j = 0;
            Long l = null;
            ArrayMap arrayMap4 = new ArrayMap();
            for (zzkn zzkn2 : zzknArr) {
                zzko[] zzkoArr;
                String str2;
                Long l2;
                long j2;
                zzkn zzkn3;
                zzeq zzf;
                zzeq zzeq;
                long j3;
                int intValue3;
                BitSet bitSet3;
                BitSet bitSet4;
                String str3 = zzkn2.name;
                zzko[] zzkoArr2 = zzkn2.zzata;
                if (zzgg().zzd(str, zzew.zzahv)) {
                    int length2;
                    zzko zzko;
                    zzgb();
                    Long l3 = (Long) zzka.zzb(zzkn2, "_eid");
                    Object obj = l3 != null ? 1 : null;
                    Object obj2 = (obj == null || !str3.equals("_ep")) ? null : 1;
                    if (obj2 != null) {
                        zzgb();
                        str3 = (String) zzka.zzb(zzkn2, "_en");
                        if (TextUtils.isEmpty(str3)) {
                            zzge().zzim().zzg("Extra parameter without an event name. eventId", l3);
                        } else {
                            Long l4;
                            int i3;
                            if (zzkn == null || l == null || l3.longValue() != l.longValue()) {
                                Pair zza = zzix().zza(str, l3);
                                if (zza == null || zza.first == null) {
                                    zzge().zzim().zze("Extra parameter without existing main event. eventName, eventId", str3, l3);
                                } else {
                                    zzkn zzkn4 = (zzkn) zza.first;
                                    j = ((Long) zza.second).longValue();
                                    zzgb();
                                    l4 = (Long) zzka.zzb(zzkn4, "_eid");
                                    zzkn = zzkn4;
                                }
                            } else {
                                l4 = l;
                            }
                            j--;
                            if (j <= 0) {
                                zzix = zzix();
                                zzix.zzab();
                                zzix.zzge().zzit().zzg("Clearing complex main event info. appId", str);
                                try {
                                    zzix.getWritableDatabase().execSQL("delete from main_event_params where app_id=?", new String[]{str});
                                } catch (SQLiteException e) {
                                    zzix.zzge().zzim().zzg("Error clearing complex main event", e);
                                }
                            } else {
                                zzix().zza(str, l3, j, zzkn);
                            }
                            zzko[] zzkoArr3 = new zzko[(zzkn.zzata.length + zzkoArr2.length)];
                            i2 = 0;
                            zzko[] zzkoArr4 = zzkn.zzata;
                            length2 = zzkoArr4.length;
                            i = 0;
                            while (i < length2) {
                                zzko = zzkoArr4[i];
                                zzgb();
                                if (zzka.zza(zzkn2, zzko.name) == null) {
                                    i3 = i2 + 1;
                                    zzkoArr3[i2] = zzko;
                                } else {
                                    i3 = i2;
                                }
                                i++;
                                i2 = i3;
                            }
                            if (i2 > 0) {
                                length = zzkoArr2.length;
                                i3 = 0;
                                while (i3 < length) {
                                    i = i2 + 1;
                                    zzkoArr3[i2] = zzkoArr2[i3];
                                    i3++;
                                    i2 = i;
                                }
                                zzkoArr = i2 == zzkoArr3.length ? zzkoArr3 : (zzko[]) Arrays.copyOf(zzkoArr3, i2);
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                zzkn3 = zzkn;
                            } else {
                                zzge().zzip().zzg("No unique parameters in main event. eventName", str3);
                                zzkoArr = zzkoArr2;
                                str2 = str3;
                                l2 = l4;
                                j2 = j;
                                zzkn3 = zzkn;
                            }
                        }
                    } else if (obj != null) {
                        zzgb();
                        Long valueOf = Long.valueOf(0);
                        l = zzka.zzb(zzkn2, "_epc");
                        if (l != null) {
                            valueOf = l;
                        }
                        j = valueOf.longValue();
                        if (j <= 0) {
                            zzge().zzip().zzg("Complex event with zero extra param count. eventName", str3);
                            zzkoArr = zzkoArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            zzkn3 = zzkn2;
                        } else {
                            zzix().zza(str, l3, j, zzkn2);
                            zzkoArr = zzkoArr2;
                            str2 = str3;
                            l2 = l3;
                            j2 = j;
                            zzkn3 = zzkn2;
                        }
                    }
                    zzf = zzix().zzf(str, zzkn2.name);
                    if (zzf != null) {
                        zzge().zzip().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbm(str), zzga().zzbj(str2));
                        zzeq = new zzeq(str, zzkn2.name, 1, 1, zzkn2.zzatb.longValue(), 0, null, null, null);
                    } else {
                        zzeq = zzf.zzie();
                    }
                    zzix().zza(zzeq);
                    j3 = zzeq.zzafr;
                    map = (Map) arrayMap4.get(str2);
                    if (map != null) {
                        map = zzix().zzk(str, str2);
                        if (map == null) {
                            map = new ArrayMap();
                        }
                        arrayMap4.put(str2, map);
                        map2 = map;
                    } else {
                        map2 = map;
                    }
                    for (Integer intValue22 : r7.keySet()) {
                        intValue3 = intValue22.intValue();
                        if (hashSet.contains(Integer.valueOf(intValue3))) {
                            bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue3));
                            bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue3));
                            if (((zzkm) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                                zzkm = new zzkm();
                                arrayMap.put(Integer.valueOf(intValue3), zzkm);
                                zzkm.zzasy = Boolean.valueOf(true);
                                bitSet = new BitSet();
                                arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                                bitSet2 = new BitSet();
                                arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                                bitSet3 = bitSet2;
                                bitSet4 = bitSet;
                            } else {
                                bitSet3 = bitSet2;
                                bitSet4 = bitSet;
                            }
                            for (zzke zzke : (List) r7.get(Integer.valueOf(intValue3))) {
                                if (zzge().isLoggable(2)) {
                                    zzge().zzit().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), zzke.zzarp, zzga().zzbj(zzke.zzarq));
                                    zzge().zzit().zzg("Filter definition", zzga().zza(zzke));
                                }
                                if (zzke.zzarp != null || zzke.zzarp.intValue() > 256) {
                                    zzge().zzip().zze("Invalid event filter ID. appId, id", zzfg.zzbm(str), String.valueOf(zzke.zzarp));
                                } else if (bitSet4.get(zzke.zzarp.intValue())) {
                                    zzge().zzit().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue3), zzke.zzarp);
                                } else {
                                    Boolean zza2;
                                    zzfi zzit;
                                    String str4;
                                    Boolean bool;
                                    if (zzke.zzart != null) {
                                        zza2 = zza(j3, zzke.zzart);
                                        if (zza2 == null) {
                                            zza2 = null;
                                        } else if (!zza2.booleanValue()) {
                                            zza2 = Boolean.valueOf(false);
                                        }
                                        zzit = zzge().zzit();
                                        str4 = "Event filter result";
                                        if (zza2 != null) {
                                            obj = "null";
                                        } else {
                                            bool = zza2;
                                        }
                                        zzit.zzg(str4, obj);
                                        if (zza2 != null) {
                                            hashSet.add(Integer.valueOf(intValue3));
                                        } else {
                                            bitSet3.set(zzke.zzarp.intValue());
                                            if (zza2.booleanValue()) {
                                                bitSet4.set(zzke.zzarp.intValue());
                                            }
                                        }
                                    }
                                    Set hashSet2 = new HashSet();
                                    for (zzkf zzkf : zzke.zzarr) {
                                        if (TextUtils.isEmpty(zzkf.zzary)) {
                                            zzge().zzip().zzg("null or empty param name in filter. event", zzga().zzbj(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        hashSet2.add(zzkf.zzary);
                                    }
                                    ArrayMap arrayMap5 = new ArrayMap();
                                    for (zzko zzko2 : r19) {
                                        if (hashSet2.contains(zzko2.name)) {
                                            if (zzko2.zzate == null) {
                                                if (zzko2.zzarc == null) {
                                                    if (zzko2.zzajf == null) {
                                                        zzge().zzip().zze("Unknown value for param. event, param", zzga().zzbj(str2), zzga().zzbk(zzko2.name));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    arrayMap5.put(zzko2.name, zzko2.zzajf);
                                                } else {
                                                    arrayMap5.put(zzko2.name, zzko2.zzarc);
                                                }
                                            } else {
                                                arrayMap5.put(zzko2.name, zzko2.zzate);
                                            }
                                        }
                                    }
                                    for (zzkf zzkf2 : zzke.zzarr) {
                                        boolean equals = Boolean.TRUE.equals(zzkf2.zzarx);
                                        String str5 = zzkf2.zzary;
                                        if (TextUtils.isEmpty(str5)) {
                                            zzge().zzip().zzg("Event has empty param name. event", zzga().zzbj(str2));
                                            zza2 = null;
                                            break;
                                        }
                                        Object obj3 = arrayMap5.get(str5);
                                        if (obj3 instanceof Long) {
                                            if (zzkf2.zzarw == null) {
                                                zzge().zzip().zze("No number filter for long param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Long) obj3).longValue(), zzkf2.zzarw);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof Double) {
                                            if (zzkf2.zzarw == null) {
                                                zzge().zzip().zze("No number filter for double param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                zza2 = null;
                                                break;
                                            }
                                            zza2 = zza(((Double) obj3).doubleValue(), zzkf2.zzarw);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 instanceof String) {
                                            if (zzkf2.zzarv == null) {
                                                if (zzkf2.zzarw != null) {
                                                    if (!zzka.zzck((String) obj3)) {
                                                        zzge().zzip().zze("Invalid param value for number filter. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                        zza2 = null;
                                                        break;
                                                    }
                                                    zza2 = zza((String) obj3, zzkf2.zzarw);
                                                } else {
                                                    zzge().zzip().zze("No filter for String param. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                                    zza2 = null;
                                                    break;
                                                }
                                            }
                                            zza2 = zza((String) obj3, zzkf2.zzarv);
                                            if (zza2 == null) {
                                                zza2 = null;
                                                break;
                                            }
                                            if (((!zza2.booleanValue() ? 1 : 0) ^ equals) != 0) {
                                                zza2 = Boolean.valueOf(false);
                                                break;
                                            }
                                        } else if (obj3 == null) {
                                            zzge().zzit().zze("Missing param for filter. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                            zza2 = Boolean.valueOf(false);
                                        } else {
                                            zzge().zzip().zze("Unknown param type. event, param", zzga().zzbj(str2), zzga().zzbk(str5));
                                            zza2 = null;
                                        }
                                    }
                                    zza2 = Boolean.valueOf(true);
                                    zzit = zzge().zzit();
                                    str4 = "Event filter result";
                                    if (zza2 != null) {
                                        bool = zza2;
                                    } else {
                                        obj = "null";
                                    }
                                    zzit.zzg(str4, obj);
                                    if (zza2 != null) {
                                        bitSet3.set(zzke.zzarp.intValue());
                                        if (zza2.booleanValue()) {
                                            bitSet4.set(zzke.zzarp.intValue());
                                        }
                                    } else {
                                        hashSet.add(Integer.valueOf(intValue3));
                                    }
                                }
                            }
                        } else {
                            zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                        }
                    }
                    l = l2;
                    j = j2;
                    zzkn = zzkn3;
                }
                zzkoArr = zzkoArr2;
                str2 = str3;
                l2 = l;
                j2 = j;
                zzkn3 = zzkn;
                zzf = zzix().zzf(str, zzkn2.name);
                if (zzf != null) {
                    zzeq = zzf.zzie();
                } else {
                    zzge().zzip().zze("Event aggregate wasn't created during raw event logging. appId, event", zzfg.zzbm(str), zzga().zzbj(str2));
                    zzeq = new zzeq(str, zzkn2.name, 1, 1, zzkn2.zzatb.longValue(), 0, null, null, null);
                }
                zzix().zza(zzeq);
                j3 = zzeq.zzafr;
                map = (Map) arrayMap4.get(str2);
                if (map != null) {
                    map2 = map;
                } else {
                    map = zzix().zzk(str, str2);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap4.put(str2, map);
                    map2 = map;
                }
                while (r11.hasNext()) {
                    intValue3 = intValue22.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue3))) {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue3));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue3));
                        if (((zzkm) arrayMap.get(Integer.valueOf(intValue3))) != null) {
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        } else {
                            zzkm = new zzkm();
                            arrayMap.put(Integer.valueOf(intValue3), zzkm);
                            zzkm.zzasy = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue3), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue3), bitSet2);
                            bitSet3 = bitSet2;
                            bitSet4 = bitSet;
                        }
                        for (zzke zzke2 : (List) r7.get(Integer.valueOf(intValue3))) {
                            if (zzge().isLoggable(2)) {
                                zzge().zzit().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue3), zzke2.zzarp, zzga().zzbj(zzke2.zzarq));
                                zzge().zzit().zzg("Filter definition", zzga().zza(zzke2));
                            }
                            if (zzke2.zzarp != null) {
                            }
                            zzge().zzip().zze("Invalid event filter ID. appId, id", zzfg.zzbm(str), String.valueOf(zzke2.zzarp));
                        }
                    } else {
                        zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(intValue3));
                    }
                }
                l = l2;
                j = j2;
                zzkn = zzkn3;
            }
        }
        if (zzksArr != null) {
            Map arrayMap6 = new ArrayMap();
            for (zzks zzks : zzksArr) {
                map = (Map) arrayMap6.get(zzks.name);
                if (map == null) {
                    map = zzix().zzl(str, zzks.name);
                    if (map == null) {
                        map = new ArrayMap();
                    }
                    arrayMap6.put(zzks.name, map);
                    map2 = map;
                } else {
                    map2 = map;
                }
                for (Integer intValue222 : r7.keySet()) {
                    length = intValue222.intValue();
                    if (hashSet.contains(Integer.valueOf(length))) {
                        zzge().zzit().zzg("Skipping failed audience ID", Integer.valueOf(length));
                    } else {
                        bitSet = (BitSet) arrayMap2.get(Integer.valueOf(length));
                        bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(length));
                        if (((zzkm) arrayMap.get(Integer.valueOf(length))) == null) {
                            zzkm = new zzkm();
                            arrayMap.put(Integer.valueOf(length), zzkm);
                            zzkm.zzasy = Boolean.valueOf(true);
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(length), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(length), bitSet2);
                        }
                        for (zzkh zzkh : (List) r7.get(Integer.valueOf(length))) {
                            if (zzge().isLoggable(2)) {
                                zzge().zzit().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(length), zzkh.zzarp, zzga().zzbl(zzkh.zzasf));
                                zzge().zzit().zzg("Filter definition", zzga().zza(zzkh));
                            }
                            if (zzkh.zzarp == null || zzkh.zzarp.intValue() > 256) {
                                zzge().zzip().zze("Invalid property filter ID. appId, id", zzfg.zzbm(str), String.valueOf(zzkh.zzarp));
                                hashSet.add(Integer.valueOf(length));
                                break;
                            } else if (bitSet.get(zzkh.zzarp.intValue())) {
                                zzge().zzit().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(length), zzkh.zzarp);
                            } else {
                                Boolean bool2;
                                Object obj4;
                                zzkf zzkf3 = zzkh.zzasg;
                                if (zzkf3 == null) {
                                    zzge().zzip().zzg("Missing property filter. property", zzga().zzbl(zzks.name));
                                    bool2 = null;
                                } else {
                                    boolean equals2 = Boolean.TRUE.equals(zzkf3.zzarx);
                                    if (zzks.zzate != null) {
                                        if (zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No number filter for long property. property", zzga().zzbl(zzks.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(zzks.zzate.longValue(), zzkf3.zzarw), equals2);
                                        }
                                    } else if (zzks.zzarc != null) {
                                        if (zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No number filter for double property. property", zzga().zzbl(zzks.name));
                                            bool2 = null;
                                        } else {
                                            bool2 = zza(zza(zzks.zzarc.doubleValue(), zzkf3.zzarw), equals2);
                                        }
                                    } else if (zzks.zzajf == null) {
                                        zzge().zzip().zzg("User property has no value, property", zzga().zzbl(zzks.name));
                                        bool2 = null;
                                    } else if (zzkf3.zzarv == null) {
                                        if (zzkf3.zzarw == null) {
                                            zzge().zzip().zzg("No string or number filter defined. property", zzga().zzbl(zzks.name));
                                        } else if (zzka.zzck(zzks.zzajf)) {
                                            bool2 = zza(zza(zzks.zzajf, zzkf3.zzarw), equals2);
                                        } else {
                                            zzge().zzip().zze("Invalid user property value for Numeric number filter. property, value", zzga().zzbl(zzks.name), zzks.zzajf);
                                        }
                                        bool2 = null;
                                    } else {
                                        bool2 = zza(zza(zzks.zzajf, zzkf3.zzarv), equals2);
                                    }
                                }
                                zzfi zzit2 = zzge().zzit();
                                String str6 = "Property filter result";
                                if (bool2 == null) {
                                    obj4 = "null";
                                } else {
                                    Boolean bool3 = bool2;
                                }
                                zzit2.zzg(str6, obj4);
                                if (bool2 == null) {
                                    hashSet.add(Integer.valueOf(length));
                                } else {
                                    bitSet2.set(zzkh.zzarp.intValue());
                                    if (bool2.booleanValue()) {
                                        bitSet.set(zzkh.zzarp.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        zzkm[] zzkmArr = new zzkm[arrayMap2.size()];
        i2 = 0;
        for (Integer intValue2222 : arrayMap2.keySet()) {
            intValue = intValue2222.intValue();
            if (!hashSet.contains(Integer.valueOf(intValue))) {
                zzkm = (zzkm) arrayMap.get(Integer.valueOf(intValue));
                zzkm2 = zzkm == null ? new zzkm() : zzkm;
                int i4 = i2 + 1;
                zzkmArr[i2] = zzkm2;
                zzkm2.zzarl = Integer.valueOf(intValue);
                zzkm2.zzasw = new zzkr();
                zzkm2.zzasw.zzaul = zzka.zza((BitSet) arrayMap2.get(Integer.valueOf(intValue)));
                zzkm2.zzasw.zzauk = zzka.zza((BitSet) arrayMap3.get(Integer.valueOf(intValue)));
                zzix = zzix();
                zzace zzace = zzkm2.zzasw;
                zzix.zzch();
                zzix.zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(zzace);
                try {
                    byte[] bArr = new byte[zzace.zzvm()];
                    zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
                    zzace.zza(zzb);
                    zzb.zzve();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("audience_id", Integer.valueOf(intValue));
                    contentValues.put("current_results", bArr);
                    try {
                        if (zzix.getWritableDatabase().insertWithOnConflict("audience_filter_values", null, contentValues, 5) == -1) {
                            zzix.zzge().zzim().zzg("Failed to insert filter results (got -1). appId", zzfg.zzbm(str));
                        }
                        i2 = i4;
                    } catch (SQLiteException e2) {
                        zzix.zzge().zzim().zze("Error storing filter results. appId", zzfg.zzbm(str), e2);
                        i2 = i4;
                    }
                } catch (IOException e3) {
                    zzix.zzge().zzim().zze("Configuration loss. Failed to serialize filter results. appId", zzfg.zzbm(str), e3);
                    i2 = i4;
                }
            }
        }
        return (zzkm[]) Arrays.copyOf(zzkmArr, i2);
    }

    protected final boolean zzhf() {
        return false;
    }
}
