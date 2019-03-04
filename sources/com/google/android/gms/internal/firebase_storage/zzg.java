package com.google.android.gms.internal.firebase_storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.io.UnsupportedEncodingException;
import net.lingala.zip4j.util.InternalZipConstants;

public final class zzg {
    @NonNull
    public static String zzb(@Nullable String str) throws UnsupportedEncodingException {
        return TextUtils.isEmpty(str) ? "" : zzc(Uri.encode(str));
    }

    @NonNull
    public static String zzc(@NonNull String str) {
        Preconditions.checkNotNull(str);
        return str.replace("%2F", InternalZipConstants.ZIP_FILE_SEPARATOR);
    }

    @NonNull
    public static String zzd(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (!str.startsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) && !str.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) && !str.contains("//")) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : str.split(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
            if (!TextUtils.isEmpty(str2)) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(InternalZipConstants.ZIP_FILE_SEPARATOR).append(str2);
                } else {
                    stringBuilder.append(str2);
                }
            }
        }
        return stringBuilder.toString();
    }
}
