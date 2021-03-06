package com.google.android.gms.internal.ads;

import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import com.google.android.gms.measurement.AppMeasurement.Param;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzadh
public final class zzacp implements zzacd<zzos> {
    private final boolean zzcbk;

    public zzacp(boolean z) {
        this.zzcbk = z;
    }

    public final /* synthetic */ zzpb zza(zzabv zzabv, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        String valueOf;
        View view = null;
        SimpleArrayMap simpleArrayMap = new SimpleArrayMap();
        SimpleArrayMap simpleArrayMap2 = new SimpleArrayMap();
        Future zzg = zzabv.zzg(jSONObject);
        zzanz zzc = zzabv.zzc(jSONObject, "video");
        JSONArray jSONArray = jSONObject.getJSONArray("custom_assets");
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            String string = jSONObject2.getString(Param.TYPE);
            if ("string".equals(string)) {
                simpleArrayMap2.put(jSONObject2.getString("name"), jSONObject2.getString("string_value"));
            } else if ("image".equals(string)) {
                simpleArrayMap.put(jSONObject2.getString("name"), zzabv.zza(jSONObject2, "image_value", this.zzcbk));
            } else {
                String str = "Unknown custom asset type: ";
                valueOf = String.valueOf(string);
                zzane.zzdk(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
        zzaqw zzc2 = zzabv.zzc(zzc);
        valueOf = jSONObject.getString("custom_template_id");
        SimpleArrayMap simpleArrayMap3 = new SimpleArrayMap();
        for (int i2 = 0; i2 < simpleArrayMap.size(); i2++) {
            simpleArrayMap3.put(simpleArrayMap.keyAt(i2), ((Future) simpleArrayMap.valueAt(i2)).get());
        }
        zzoj zzoj = (zzoj) zzg.get();
        zzlo zztm = zzc2 != null ? zzc2.zztm() : null;
        if (zzc2 != null) {
            view = zzc2.getView();
        }
        return new zzos(valueOf, simpleArrayMap3, simpleArrayMap2, zzoj, zztm, view);
    }
}
