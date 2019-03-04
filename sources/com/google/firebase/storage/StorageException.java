package com.google.firebase.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StorageException extends FirebaseException {
    public static final int ERROR_BUCKET_NOT_FOUND = -13011;
    public static final int ERROR_CANCELED = -13040;
    public static final int ERROR_INVALID_CHECKSUM = -13031;
    public static final int ERROR_NOT_AUTHENTICATED = -13020;
    public static final int ERROR_NOT_AUTHORIZED = -13021;
    public static final int ERROR_OBJECT_NOT_FOUND = -13010;
    public static final int ERROR_PROJECT_NOT_FOUND = -13012;
    public static final int ERROR_QUOTA_EXCEEDED = -13013;
    public static final int ERROR_RETRY_LIMIT_EXCEEDED = -13030;
    public static final int ERROR_UNKNOWN = -13000;
    private static IOException zzv = new IOException("The operation was canceled.");
    private final int zzw;
    private final int zzx;
    private String zzy;
    private Throwable zzz;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    private StorageException(int i, Throwable th, int i2) {
        String str;
        switch (i) {
            case ERROR_CANCELED /*-13040*/:
                str = "The operation was cancelled.";
                break;
            case ERROR_INVALID_CHECKSUM /*-13031*/:
                str = "Object has a checksum which does not match. Please retry the operation.";
                break;
            case ERROR_RETRY_LIMIT_EXCEEDED /*-13030*/:
                str = "The operation retry limit has been exceeded.";
                break;
            case ERROR_NOT_AUTHORIZED /*-13021*/:
                str = "User does not have permission to access this object.";
                break;
            case ERROR_NOT_AUTHENTICATED /*-13020*/:
                str = "User is not authenticated, please authenticate using Firebase Authentication and try again.";
                break;
            case ERROR_QUOTA_EXCEEDED /*-13013*/:
                str = "Quota for bucket exceeded, please view quota on www.firebase.google.com/storage.";
                break;
            case ERROR_PROJECT_NOT_FOUND /*-13012*/:
                str = "Project does not exist.";
                break;
            case ERROR_BUCKET_NOT_FOUND /*-13011*/:
                str = "Bucket does not exist.";
                break;
            case ERROR_OBJECT_NOT_FOUND /*-13010*/:
                str = "Object does not exist at location.";
                break;
            case ERROR_UNKNOWN /*-13000*/:
                str = "An unknown error occurred, please check the HTTP result code and inner exception for server response.";
                break;
            default:
                str = "An unknown error occurred, please check the HTTP result code and inner exception for server response.";
                break;
        }
        this.zzy = str;
        this.zzz = th;
        this.zzw = i;
        this.zzx = i2;
        String str2 = this.zzy;
        String num = Integer.toString(this.zzw);
        String num2 = Integer.toString(this.zzx);
        Log.e("StorageException", new StringBuilder(((String.valueOf(str2).length() + 52) + String.valueOf(num).length()) + String.valueOf(num2).length()).append("StorageException has occurred.\n").append(str2).append("\n Code: ").append(num).append(" HttpResult: ").append(num2).toString());
        if (this.zzz != null) {
            Log.e("StorageException", this.zzz.getMessage(), this.zzz);
        }
    }

    @NonNull
    public static StorageException fromErrorStatus(@NonNull Status status) {
        Preconditions.checkNotNull(status);
        Preconditions.checkArgument(!status.isSuccess());
        int i = status.isCanceled() ? ERROR_CANCELED : status == Status.RESULT_TIMEOUT ? ERROR_RETRY_LIMIT_EXCEEDED : ERROR_UNKNOWN;
        return new StorageException(i, null, 0);
    }

    @NonNull
    public static StorageException fromException(@NonNull Throwable th) {
        return fromExceptionAndHttpCode(th, 0);
    }

    @Nullable
    public static StorageException fromExceptionAndHttpCode(@Nullable Throwable th, int i) {
        if (th instanceof StorageException) {
            return (StorageException) th;
        }
        Object obj = (i == 0 || (i >= 200 && i < 300)) ? 1 : null;
        if (obj != null && th == null) {
            return null;
        }
        int i2;
        if (!(th instanceof zza)) {
            switch (i) {
                case -2:
                    i2 = ERROR_RETRY_LIMIT_EXCEEDED;
                    break;
                case 401:
                    i2 = ERROR_NOT_AUTHENTICATED;
                    break;
                case 403:
                    i2 = ERROR_NOT_AUTHORIZED;
                    break;
                case Compress.HUFF_TABLE_SIZE /*404*/:
                    i2 = ERROR_OBJECT_NOT_FOUND;
                    break;
                case 409:
                    i2 = ERROR_INVALID_CHECKSUM;
                    break;
                default:
                    i2 = ERROR_UNKNOWN;
                    break;
            }
        }
        i2 = ERROR_CANCELED;
        return new StorageException(i2, th, i);
    }

    public Throwable getCause() {
        return this.zzz == this ? null : this.zzz;
    }

    public int getErrorCode() {
        return this.zzw;
    }

    public int getHttpResultCode() {
        return this.zzx;
    }

    public boolean getIsRecoverableException() {
        return getErrorCode() == ERROR_RETRY_LIMIT_EXCEEDED;
    }

    public String getMessage() {
        return this.zzy;
    }
}
