package com.google.firebase.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzg;
import com.google.android.gms.internal.firebase_storage.zzk;
import com.google.android.gms.internal.firebase_storage.zzp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageMetadata {
    private String mPath;
    private FirebaseStorage zzaa;
    private String zzab;
    private String zzac;
    private zza<String> zzad;
    private String zzae;
    private String zzaf;
    private String zzag;
    private long zzah;
    private String zzai;
    private zza<String> zzaj;
    private zza<String> zzak;
    private zza<String> zzal;
    private zza<String> zzam;
    private zza<Map<String, String>> zzan;
    private String[] zzao;
    private StorageReference zzd;

    public static class Builder {
        private StorageMetadata zzap;
        private boolean zzaq;

        public Builder() {
            this.zzap = new StorageMetadata();
        }

        public Builder(StorageMetadata storageMetadata) {
            this.zzap = new StorageMetadata(false);
        }

        private Builder(JSONObject jSONObject) throws JSONException {
            this.zzap = new StorageMetadata();
            if (jSONObject != null) {
                String str;
                this.zzap.zzac = jSONObject.optString("generation");
                this.zzap.mPath = jSONObject.optString("name");
                this.zzap.zzab = jSONObject.optString("bucket");
                this.zzap.zzae = jSONObject.optString("metageneration");
                this.zzap.zzaf = jSONObject.optString("timeCreated");
                this.zzap.zzag = jSONObject.optString("updated");
                this.zzap.zzah = jSONObject.optLong("size");
                this.zzap.zzai = jSONObject.optString("md5Hash");
                this.zzap.zza(jSONObject.optString("downloadTokens"));
                if (jSONObject.has("metadata") && !jSONObject.isNull("metadata")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("metadata");
                    Iterator keys = jSONObject2.keys();
                    while (keys.hasNext()) {
                        str = (String) keys.next();
                        setCustomMetadata(str, jSONObject2.getString(str));
                    }
                }
                str = zza(jSONObject, "contentType");
                if (str != null) {
                    setContentType(str);
                }
                str = zza(jSONObject, "cacheControl");
                if (str != null) {
                    setCacheControl(str);
                }
                str = zza(jSONObject, "contentDisposition");
                if (str != null) {
                    setContentDisposition(str);
                }
                str = zza(jSONObject, "contentEncoding");
                if (str != null) {
                    setContentEncoding(str);
                }
                str = zza(jSONObject, "contentLanguage");
                if (str != null) {
                    setContentLanguage(str);
                }
                this.zzaq = true;
            }
        }

        Builder(JSONObject jSONObject, StorageReference storageReference) throws JSONException {
            this(jSONObject);
            this.zzap.zzd = storageReference;
        }

        @Nullable
        private static String zza(JSONObject jSONObject, String str) throws JSONException {
            return (!jSONObject.has(str) || jSONObject.isNull(str)) ? null : jSONObject.getString(str);
        }

        public StorageMetadata build() {
            return new StorageMetadata(this.zzaq);
        }

        public Builder setCacheControl(@Nullable String str) {
            this.zzap.zzaj = zza.zzb(str);
            return this;
        }

        public Builder setContentDisposition(@Nullable String str) {
            this.zzap.zzak = zza.zzb(str);
            return this;
        }

        public Builder setContentEncoding(@Nullable String str) {
            this.zzap.zzal = zza.zzb(str);
            return this;
        }

        public Builder setContentLanguage(@Nullable String str) {
            this.zzap.zzam = zza.zzb(str);
            return this;
        }

        public Builder setContentType(@Nullable String str) {
            this.zzap.zzad = zza.zzb(str);
            return this;
        }

        public Builder setCustomMetadata(String str, String str2) {
            if (!this.zzap.zzan.zzc()) {
                this.zzap.zzan = zza.zzb(new HashMap());
            }
            ((Map) this.zzap.zzan.getValue()).put(str, str2);
            return this;
        }
    }

    private static class zza<T> {
        @Nullable
        private final T value;
        private final boolean zzar;

        private zza(@Nullable T t, boolean z) {
            this.zzar = z;
            this.value = t;
        }

        static <T> zza<T> zza(T t) {
            return new zza(t, false);
        }

        static <T> zza<T> zzb(@Nullable T t) {
            return new zza(t, true);
        }

        @Nullable
        final T getValue() {
            return this.value;
        }

        final boolean zzc() {
            return this.zzar;
        }
    }

    public StorageMetadata() {
        this.mPath = null;
        this.zzaa = null;
        this.zzd = null;
        this.zzab = null;
        this.zzac = null;
        this.zzad = zza.zza("");
        this.zzae = null;
        this.zzaf = null;
        this.zzag = null;
        this.zzai = null;
        this.zzaj = zza.zza("");
        this.zzak = zza.zza("");
        this.zzal = zza.zza("");
        this.zzam = zza.zza("");
        this.zzan = zza.zza(Collections.emptyMap());
        this.zzao = null;
    }

    private StorageMetadata(@NonNull StorageMetadata storageMetadata, boolean z) {
        this.mPath = null;
        this.zzaa = null;
        this.zzd = null;
        this.zzab = null;
        this.zzac = null;
        this.zzad = zza.zza("");
        this.zzae = null;
        this.zzaf = null;
        this.zzag = null;
        this.zzai = null;
        this.zzaj = zza.zza("");
        this.zzak = zza.zza("");
        this.zzal = zza.zza("");
        this.zzam = zza.zza("");
        this.zzan = zza.zza(Collections.emptyMap());
        this.zzao = null;
        Preconditions.checkNotNull(storageMetadata);
        this.mPath = storageMetadata.mPath;
        this.zzaa = storageMetadata.zzaa;
        this.zzd = storageMetadata.zzd;
        this.zzab = storageMetadata.zzab;
        this.zzad = storageMetadata.zzad;
        this.zzaj = storageMetadata.zzaj;
        this.zzak = storageMetadata.zzak;
        this.zzal = storageMetadata.zzal;
        this.zzam = storageMetadata.zzam;
        this.zzan = storageMetadata.zzan;
        this.zzao = storageMetadata.zzao;
        if (z) {
            this.zzai = storageMetadata.zzai;
            this.zzah = storageMetadata.zzah;
            this.zzag = storageMetadata.zzag;
            this.zzaf = storageMetadata.zzaf;
            this.zzae = storageMetadata.zzae;
            this.zzac = storageMetadata.zzac;
        }
    }

    private final void zza(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.zzao = str.split(",");
        }
    }

    @Nullable
    public String getBucket() {
        return this.zzab;
    }

    @Nullable
    public String getCacheControl() {
        return (String) this.zzaj.getValue();
    }

    @Nullable
    public String getContentDisposition() {
        return (String) this.zzak.getValue();
    }

    @Nullable
    public String getContentEncoding() {
        return (String) this.zzal.getValue();
    }

    @Nullable
    public String getContentLanguage() {
        return (String) this.zzam.getValue();
    }

    public String getContentType() {
        return (String) this.zzad.getValue();
    }

    public long getCreationTimeMillis() {
        return zzk.zze(this.zzaf);
    }

    public String getCustomMetadata(@NonNull String str) {
        return TextUtils.isEmpty(str) ? null : (String) ((Map) this.zzan.getValue()).get(str);
    }

    @NonNull
    public Set<String> getCustomMetadataKeys() {
        return ((Map) this.zzan.getValue()).keySet();
    }

    @Nullable
    @Deprecated
    public Uri getDownloadUrl() {
        List downloadUrls = getDownloadUrls();
        return (downloadUrls == null || downloadUrls.size() <= 0) ? null : (Uri) downloadUrls.get(0);
    }

    @Nullable
    @Deprecated
    public List<Uri> getDownloadUrls() {
        List arrayList = new ArrayList();
        if (!(this.zzao == null || this.zzd == null)) {
            try {
                Object zzb = zzp.zzb(this.zzd.getStorage().getApp()).zzb(this.zzd.zze());
                if (!TextUtils.isEmpty(zzb)) {
                    for (Object obj : this.zzao) {
                        if (!TextUtils.isEmpty(obj)) {
                            arrayList.add(Uri.parse(new StringBuilder((String.valueOf(zzb).length() + 17) + String.valueOf(obj).length()).append(zzb).append("?alt=media&token=").append(obj).toString()));
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e("StorageMetadata", "Unexpected error getting DownloadUrls.", e);
            }
        }
        return arrayList;
    }

    @Nullable
    public String getGeneration() {
        return this.zzac;
    }

    @Nullable
    public String getMd5Hash() {
        return this.zzai;
    }

    @Nullable
    public String getMetadataGeneration() {
        return this.zzae;
    }

    @Nullable
    public String getName() {
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        int lastIndexOf = path.lastIndexOf(47);
        return lastIndexOf != -1 ? path.substring(lastIndexOf + 1) : path;
    }

    @NonNull
    public String getPath() {
        return this.mPath != null ? this.mPath : "";
    }

    @Nullable
    public StorageReference getReference() {
        if (this.zzd != null || this.zzaa == null) {
            return this.zzd;
        }
        String bucket = getBucket();
        String path = getPath();
        if (TextUtils.isEmpty(bucket) || TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            return new StorageReference(new android.net.Uri.Builder().scheme("gs").authority(bucket).encodedPath(zzg.zzb(path)).build(), this.zzaa);
        } catch (Throwable e) {
            Log.e("StorageMetadata", new StringBuilder((String.valueOf(bucket).length() + 38) + String.valueOf(path).length()).append("Unable to create a valid default Uri. ").append(bucket).append(path).toString(), e);
            throw new IllegalStateException(e);
        }
    }

    public long getSizeBytes() {
        return this.zzah;
    }

    public long getUpdatedTimeMillis() {
        return zzk.zze(this.zzag);
    }

    @NonNull
    final JSONObject zzb() throws JSONException {
        Map hashMap = new HashMap();
        if (this.zzad.zzc()) {
            hashMap.put("contentType", getContentType());
        }
        if (this.zzan.zzc()) {
            hashMap.put("metadata", new JSONObject((Map) this.zzan.getValue()));
        }
        if (this.zzaj.zzc()) {
            hashMap.put("cacheControl", getCacheControl());
        }
        if (this.zzak.zzc()) {
            hashMap.put("contentDisposition", getContentDisposition());
        }
        if (this.zzal.zzc()) {
            hashMap.put("contentEncoding", getContentEncoding());
        }
        if (this.zzam.zzc()) {
            hashMap.put("contentLanguage", getContentLanguage());
        }
        return new JSONObject(hashMap);
    }
}
