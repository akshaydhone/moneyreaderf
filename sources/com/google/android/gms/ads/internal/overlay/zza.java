package com.google.android.gms.ads.internal.overlay;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.zzbv;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.google.android.gms.internal.ads.zzadh;
import com.google.android.gms.internal.ads.zzakb;
import com.google.android.gms.internal.ads.zzakk;
import com.google.android.gms.internal.ads.zzane;
import com.google.android.gms.internal.ads.zzkb;
import com.google.android.gms.internal.ads.zznk;
import net.lingala.zip4j.util.InternalZipConstants;

@zzadh
public final class zza {
    private static boolean zza(Context context, Intent intent, zzt zzt) {
        try {
            String str = "Launching an intent: ";
            String valueOf = String.valueOf(intent.toURI());
            zzakb.m29v(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            zzbv.zzek();
            zzakk.zza(context, intent);
            if (zzt != null) {
                zzt.zzbl();
            }
            return true;
        } catch (ActivityNotFoundException e) {
            zzane.zzdk(e.getMessage());
            return false;
        }
    }

    public static boolean zza(Context context, zzc zzc, zzt zzt) {
        if (zzc == null) {
            zzane.zzdk("No intent data for launcher overlay.");
            return false;
        }
        zznk.initialize(context);
        if (zzc.intent != null) {
            return zza(context, zzc.intent, zzt);
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(zzc.url)) {
            zzane.zzdk("Open GMSG did not contain a URL.");
            return false;
        }
        if (TextUtils.isEmpty(zzc.mimeType)) {
            intent.setData(Uri.parse(zzc.url));
        } else {
            intent.setDataAndType(Uri.parse(zzc.url), zzc.mimeType);
        }
        intent.setAction("android.intent.action.VIEW");
        if (!TextUtils.isEmpty(zzc.packageName)) {
            intent.setPackage(zzc.packageName);
        }
        if (!TextUtils.isEmpty(zzc.zzbxj)) {
            String[] split = zzc.zzbxj.split(InternalZipConstants.ZIP_FILE_SEPARATOR, 2);
            if (split.length < 2) {
                String str = "Could not parse component name from open GMSG: ";
                String valueOf = String.valueOf(zzc.zzbxj);
                zzane.zzdk(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                return false;
            }
            intent.setClassName(split[0], split[1]);
        }
        Object obj = zzc.zzbxk;
        if (!TextUtils.isEmpty(obj)) {
            int parseInt;
            try {
                parseInt = Integer.parseInt(obj);
            } catch (NumberFormatException e) {
                zzane.zzdk("Could not parse intent flags.");
                parseInt = 0;
            }
            intent.addFlags(parseInt);
        }
        if (((Boolean) zzkb.zzik().zzd(zznk.zzbea)).booleanValue()) {
            intent.addFlags(ErrorDialogData.BINDER_CRASH);
            intent.putExtra("android.support.customtabs.extra.user_opt_out", true);
        } else {
            if (((Boolean) zzkb.zzik().zzd(zznk.zzbdz)).booleanValue()) {
                zzbv.zzek();
                zzakk.zzb(context, intent);
            }
        }
        return zza(context, intent, zzt);
    }
}
