package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.GuardedBy;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class zzwp {
    private static final ConcurrentHashMap<Uri, zzwp> zzbmt = new ConcurrentHashMap();
    private static final String[] zzbna = new String[]{"key", Param.VALUE};
    private final Uri uri;
    private final ContentResolver zzbmu;
    private final ContentObserver zzbmv;
    private final Object zzbmw = new Object();
    private volatile Map<String, String> zzbmx;
    private final Object zzbmy = new Object();
    @GuardedBy("listenersLock")
    private final List<zzwr> zzbmz = new ArrayList();

    private zzwp(ContentResolver contentResolver, Uri uri) {
        this.zzbmu = contentResolver;
        this.uri = uri;
        this.zzbmv = new zzwq(this, null);
    }

    public static zzwp zza(ContentResolver contentResolver, Uri uri) {
        zzwp zzwp = (zzwp) zzbmt.get(uri);
        if (zzwp != null) {
            return zzwp;
        }
        zzwp zzwp2 = new zzwp(contentResolver, uri);
        zzwp = (zzwp) zzbmt.putIfAbsent(uri, zzwp2);
        if (zzwp != null) {
            return zzwp;
        }
        zzwp2.zzbmu.registerContentObserver(zzwp2.uri, false, zzwp2.zzbmv);
        return zzwp2;
    }

    private final Map<String, String> zzrv() {
        Cursor query;
        try {
            Map<String, String> hashMap = new HashMap();
            query = this.zzbmu.query(this.uri, zzbna, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    hashMap.put(query.getString(0), query.getString(1));
                }
                query.close();
            }
            return hashMap;
        } catch (SecurityException e) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        } catch (SQLiteException e2) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        } catch (Throwable th) {
            query.close();
        }
    }

    private final void zzrw() {
        synchronized (this.zzbmy) {
            for (zzwr zzrx : this.zzbmz) {
                zzrx.zzrx();
            }
        }
    }

    public final Map<String, String> zzrt() {
        Map<String, String> zzrv = zzws.zzd("gms:phenotype:phenotype_flag:debug_disable_caching", false) ? zzrv() : this.zzbmx;
        if (zzrv == null) {
            synchronized (this.zzbmw) {
                zzrv = this.zzbmx;
                if (zzrv == null) {
                    zzrv = zzrv();
                    this.zzbmx = zzrv;
                }
            }
        }
        return zzrv != null ? zzrv : Collections.emptyMap();
    }

    public final void zzru() {
        synchronized (this.zzbmw) {
            this.zzbmx = null;
        }
    }
}
