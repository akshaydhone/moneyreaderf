package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import com.onesignal.shortcutbadger.impl.NewHtcHomeBadger;
import java.util.concurrent.atomic.AtomicReference;

public final class zzfe extends zzhh {
    private static final AtomicReference<String[]> zzaij = new AtomicReference();
    private static final AtomicReference<String[]> zzaik = new AtomicReference();
    private static final AtomicReference<String[]> zzail = new AtomicReference();

    zzfe(zzgl zzgl) {
        super(zzgl);
    }

    @Nullable
    private static String zza(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        int i = 0;
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        Preconditions.checkNotNull(atomicReference);
        Preconditions.checkArgument(strArr.length == strArr2.length);
        while (i < strArr.length) {
            if (zzka.zzs(str, strArr[i])) {
                synchronized (atomicReference) {
                    String[] strArr3 = (String[]) atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i] == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(strArr2[i]);
                        stringBuilder.append("(");
                        stringBuilder.append(strArr[i]);
                        stringBuilder.append(")");
                        strArr3[i] = stringBuilder.toString();
                    }
                    str = strArr3[i];
                }
                return str;
            }
            i++;
        }
        return str;
    }

    private static void zza(StringBuilder stringBuilder, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append("  ");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, zzkf zzkf) {
        if (zzkf != null) {
            zza(stringBuilder, i);
            stringBuilder.append("filter {\n");
            zza(stringBuilder, i, "complement", zzkf.zzarx);
            zza(stringBuilder, i, "param_name", zzbk(zzkf.zzary));
            int i2 = i + 1;
            String str = "string_filter";
            zzki zzki = zzkf.zzarv;
            if (zzki != null) {
                zza(stringBuilder, i2);
                stringBuilder.append(str);
                stringBuilder.append(" {\n");
                if (zzki.zzash != null) {
                    Object obj = "UNKNOWN_MATCH_TYPE";
                    switch (zzki.zzash.intValue()) {
                        case 1:
                            obj = "REGEXP";
                            break;
                        case 2:
                            obj = "BEGINS_WITH";
                            break;
                        case 3:
                            obj = "ENDS_WITH";
                            break;
                        case 4:
                            obj = "PARTIAL";
                            break;
                        case 5:
                            obj = "EXACT";
                            break;
                        case 6:
                            obj = "IN_LIST";
                            break;
                    }
                    zza(stringBuilder, i2, "match_type", obj);
                }
                zza(stringBuilder, i2, "expression", zzki.zzasi);
                zza(stringBuilder, i2, "case_sensitive", zzki.zzasj);
                if (zzki.zzask.length > 0) {
                    zza(stringBuilder, i2 + 1);
                    stringBuilder.append("expression_list {\n");
                    for (String str2 : zzki.zzask) {
                        zza(stringBuilder, i2 + 2);
                        stringBuilder.append(str2);
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("}\n");
                }
                zza(stringBuilder, i2);
                stringBuilder.append("}\n");
            }
            zza(stringBuilder, i + 1, "number_filter", zzkf.zzarw);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private final void zza(StringBuilder stringBuilder, int i, String str, zzkg zzkg) {
        if (zzkg != null) {
            zza(stringBuilder, i);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (zzkg.zzarz != null) {
                Object obj = "UNKNOWN_COMPARISON_TYPE";
                switch (zzkg.zzarz.intValue()) {
                    case 1:
                        obj = "LESS_THAN";
                        break;
                    case 2:
                        obj = "GREATER_THAN";
                        break;
                    case 3:
                        obj = "EQUAL";
                        break;
                    case 4:
                        obj = "BETWEEN";
                        break;
                }
                zza(stringBuilder, i, "comparison_type", obj);
            }
            zza(stringBuilder, i, "match_as_float", zzkg.zzasa);
            zza(stringBuilder, i, "comparison_value", zzkg.zzasb);
            zza(stringBuilder, i, "min_comparison_value", zzkg.zzasc);
            zza(stringBuilder, i, "max_comparison_value", zzkg.zzasd);
            zza(stringBuilder, i);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, zzkr zzkr) {
        int i2 = 0;
        if (zzkr != null) {
            int i3;
            int i4;
            zza(stringBuilder, 3);
            stringBuilder.append(str);
            stringBuilder.append(" {\n");
            if (zzkr.zzaul != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("results: ");
                long[] jArr = zzkr.zzaul;
                int length = jArr.length;
                i3 = 0;
                i4 = 0;
                while (i3 < length) {
                    Long valueOf = Long.valueOf(jArr[i3]);
                    int i5 = i4 + 1;
                    if (i4 != 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(valueOf);
                    i3++;
                    i4 = i5;
                }
                stringBuilder.append('\n');
            }
            if (zzkr.zzauk != null) {
                zza(stringBuilder, 4);
                stringBuilder.append("status: ");
                long[] jArr2 = zzkr.zzauk;
                int length2 = jArr2.length;
                i3 = 0;
                while (i2 < length2) {
                    Long valueOf2 = Long.valueOf(jArr2[i2]);
                    i4 = i3 + 1;
                    if (i3 != 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(valueOf2);
                    i2++;
                    i3 = i4;
                }
                stringBuilder.append('\n');
            }
            zza(stringBuilder, 3);
            stringBuilder.append("}\n");
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, String str, Object obj) {
        if (obj != null) {
            zza(stringBuilder, i + 1);
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(obj);
            stringBuilder.append('\n');
        }
    }

    @Nullable
    private final String zzb(zzer zzer) {
        return zzer == null ? null : !zzil() ? zzer.toString() : zzb(zzer.zzif());
    }

    private final boolean zzil() {
        return this.zzacw.zzge().isLoggable(3);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Nullable
    protected final String zza(zzep zzep) {
        if (zzep == null) {
            return null;
        }
        if (!zzil()) {
            return zzep.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Event{appId='");
        stringBuilder.append(zzep.zzti);
        stringBuilder.append("', name='");
        stringBuilder.append(zzbj(zzep.name));
        stringBuilder.append("', params=");
        stringBuilder.append(zzb(zzep.zzafq));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    protected final String zza(zzke zzke) {
        int i = 0;
        if (zzke == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nevent_filter {\n");
        zza(stringBuilder, 0, "filter_id", zzke.zzarp);
        zza(stringBuilder, 0, "event_name", zzbj(zzke.zzarq));
        zza(stringBuilder, 1, "event_count_filter", zzke.zzart);
        stringBuilder.append("  filters {\n");
        zzkf[] zzkfArr = zzke.zzarr;
        int length = zzkfArr.length;
        while (i < length) {
            zza(stringBuilder, 2, zzkfArr[i]);
            i++;
        }
        zza(stringBuilder, 1);
        stringBuilder.append("}\n}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkh zzkh) {
        if (zzkh == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nproperty_filter {\n");
        zza(stringBuilder, 0, "filter_id", zzkh.zzarp);
        zza(stringBuilder, 0, "property_name", zzbl(zzkh.zzasf));
        zza(stringBuilder, 1, zzkh.zzasg);
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    protected final String zza(zzkp zzkp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nbatch {\n");
        if (zzkp.zzatf != null) {
            for (zzkq zzkq : zzkp.zzatf) {
                if (!(zzkq == null || zzkq == null)) {
                    zza(stringBuilder, 1);
                    stringBuilder.append("bundle {\n");
                    zza(stringBuilder, 1, "protocol_version", zzkq.zzath);
                    zza(stringBuilder, 1, "platform", zzkq.zzatp);
                    zza(stringBuilder, 1, "gmp_version", zzkq.zzatt);
                    zza(stringBuilder, 1, "uploading_gmp_version", zzkq.zzatu);
                    zza(stringBuilder, 1, "config_version", zzkq.zzauf);
                    zza(stringBuilder, 1, "gmp_app_id", zzkq.zzadm);
                    zza(stringBuilder, 1, "app_id", zzkq.zzti);
                    zza(stringBuilder, 1, "app_version", zzkq.zzth);
                    zza(stringBuilder, 1, "app_version_major", zzkq.zzaub);
                    zza(stringBuilder, 1, "firebase_instance_id", zzkq.zzado);
                    zza(stringBuilder, 1, "dev_cert_hash", zzkq.zzatx);
                    zza(stringBuilder, 1, "app_store", zzkq.zzadt);
                    zza(stringBuilder, 1, "upload_timestamp_millis", zzkq.zzatk);
                    zza(stringBuilder, 1, "start_timestamp_millis", zzkq.zzatl);
                    zza(stringBuilder, 1, "end_timestamp_millis", zzkq.zzatm);
                    zza(stringBuilder, 1, "previous_bundle_start_timestamp_millis", zzkq.zzatn);
                    zza(stringBuilder, 1, "previous_bundle_end_timestamp_millis", zzkq.zzato);
                    zza(stringBuilder, 1, "app_instance_id", zzkq.zzadl);
                    zza(stringBuilder, 1, "resettable_device_id", zzkq.zzatv);
                    zza(stringBuilder, 1, "device_id", zzkq.zzaue);
                    zza(stringBuilder, 1, "limited_ad_tracking", zzkq.zzatw);
                    zza(stringBuilder, 1, "os_version", zzkq.zzatq);
                    zza(stringBuilder, 1, "device_model", zzkq.zzatr);
                    zza(stringBuilder, 1, "user_default_language", zzkq.zzafn);
                    zza(stringBuilder, 1, "time_zone_offset_minutes", zzkq.zzats);
                    zza(stringBuilder, 1, "bundle_sequential_index", zzkq.zzaty);
                    zza(stringBuilder, 1, "service_upload", zzkq.zzatz);
                    zza(stringBuilder, 1, "health_monitor", zzkq.zzaek);
                    if (!(zzkq.zzaug == null || zzkq.zzaug.longValue() == 0)) {
                        zza(stringBuilder, 1, "android_id", zzkq.zzaug);
                    }
                    if (zzkq.zzauj != null) {
                        zza(stringBuilder, 1, "retry_counter", zzkq.zzauj);
                    }
                    zzks[] zzksArr = zzkq.zzatj;
                    if (zzksArr != null) {
                        for (zzks zzks : zzksArr) {
                            if (zzks != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("user_property {\n");
                                zza(stringBuilder, 2, "set_timestamp_millis", zzks.zzaun);
                                zza(stringBuilder, 2, "name", zzbl(zzks.name));
                                zza(stringBuilder, 2, "string_value", zzks.zzajf);
                                zza(stringBuilder, 2, "int_value", zzks.zzate);
                                zza(stringBuilder, 2, "double_value", zzks.zzarc);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzkm[] zzkmArr = zzkq.zzaua;
                    if (zzkmArr != null) {
                        for (zzkm zzkm : zzkmArr) {
                            if (zzkm != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("audience_membership {\n");
                                zza(stringBuilder, 2, "audience_id", zzkm.zzarl);
                                zza(stringBuilder, 2, "new_audience", zzkm.zzasy);
                                zza(stringBuilder, 2, "current_data", zzkm.zzasw);
                                zza(stringBuilder, 2, "previous_data", zzkm.zzasx);
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zzkn[] zzknArr = zzkq.zzati;
                    if (zzknArr != null) {
                        for (zzkn zzkn : zzknArr) {
                            if (zzkn != null) {
                                zza(stringBuilder, 2);
                                stringBuilder.append("event {\n");
                                zza(stringBuilder, 2, "name", zzbj(zzkn.name));
                                zza(stringBuilder, 2, "timestamp_millis", zzkn.zzatb);
                                zza(stringBuilder, 2, "previous_timestamp_millis", zzkn.zzatc);
                                zza(stringBuilder, 2, NewHtcHomeBadger.COUNT, zzkn.count);
                                zzko[] zzkoArr = zzkn.zzata;
                                if (zzkoArr != null) {
                                    for (zzko zzko : zzkoArr) {
                                        if (zzko != null) {
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("param {\n");
                                            zza(stringBuilder, 3, "name", zzbk(zzko.name));
                                            zza(stringBuilder, 3, "string_value", zzko.zzajf);
                                            zza(stringBuilder, 3, "int_value", zzko.zzate);
                                            zza(stringBuilder, 3, "double_value", zzko.zzarc);
                                            zza(stringBuilder, 3);
                                            stringBuilder.append("}\n");
                                        }
                                    }
                                }
                                zza(stringBuilder, 2);
                                stringBuilder.append("}\n");
                            }
                        }
                    }
                    zza(stringBuilder, 1);
                    stringBuilder.append("}\n");
                }
            }
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    @Nullable
    protected final String zzb(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzil()) {
            return bundle.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append("Bundle[{");
            }
            stringBuilder.append(zzbk(str));
            stringBuilder.append("=");
            stringBuilder.append(bundle.get(str));
        }
        stringBuilder.append("}]");
        return stringBuilder.toString();
    }

    @Nullable
    protected final String zzb(zzeu zzeu) {
        if (zzeu == null) {
            return null;
        }
        if (!zzil()) {
            return zzeu.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("origin=");
        stringBuilder.append(zzeu.origin);
        stringBuilder.append(",name=");
        stringBuilder.append(zzbj(zzeu.name));
        stringBuilder.append(",params=");
        stringBuilder.append(zzb(zzeu.zzafq));
        return stringBuilder.toString();
    }

    @Nullable
    protected final String zzbj(String str) {
        return str == null ? null : zzil() ? zza(str, Event.zzacy, Event.zzacx, zzaij) : str;
    }

    @Nullable
    protected final String zzbk(String str) {
        return str == null ? null : zzil() ? zza(str, Param.zzada, Param.zzacz, zzaik) : str;
    }

    @Nullable
    protected final String zzbl(String str) {
        if (str == null) {
            return null;
        }
        if (!zzil()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, UserProperty.zzadc, UserProperty.zzadb, zzail);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("experiment_id");
        stringBuilder.append("(");
        stringBuilder.append(str);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }
}
