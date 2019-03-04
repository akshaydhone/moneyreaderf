package com.google.firebase.storage;

import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_storage.zzg;
import com.google.android.gms.internal.firebase_storage.zzp;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StreamDownloadTask.StreamProcessor;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

public class StorageReference {
    private final Uri zzas;
    private final FirebaseStorage zzat;

    StorageReference(@NonNull Uri uri, @NonNull FirebaseStorage firebaseStorage) {
        boolean z = true;
        Preconditions.checkArgument(uri != null, "storageUri cannot be null");
        if (firebaseStorage == null) {
            z = false;
        }
        Preconditions.checkArgument(z, "FirebaseApp cannot be null");
        this.zzas = uri;
        this.zzat = firebaseStorage;
    }

    @NonNull
    public StorageReference child(@NonNull String str) {
        Preconditions.checkArgument(!TextUtils.isEmpty(str), "childName cannot be null or empty");
        String zzd = zzg.zzd(str);
        try {
            return new StorageReference(this.zzas.buildUpon().appendEncodedPath(zzg.zzb(zzd)).build(), this.zzat);
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "StorageReference";
            String str3 = "Unable to create a valid default Uri. ";
            String valueOf = String.valueOf(zzd);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), th);
            throw new IllegalArgumentException("childName");
        }
    }

    public Task<Void> delete() {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zzu.zza(new zzb(this, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    public boolean equals(Object obj) {
        return !(obj instanceof StorageReference) ? false : ((StorageReference) obj).toString().equals(toString());
    }

    @NonNull
    public List<FileDownloadTask> getActiveDownloadTasks() {
        return zzt.zzm().zzb(this);
    }

    @NonNull
    public List<UploadTask> getActiveUploadTasks() {
        return zzt.zzm().zza(this);
    }

    @NonNull
    public String getBucket() {
        return this.zzas.getAuthority();
    }

    @NonNull
    public Task<byte[]> getBytes(long j) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        StorageTask streamDownloadTask = new StreamDownloadTask(this);
        ((StorageTask) streamDownloadTask.zza(new zzi(this, j, taskCompletionSource)).addOnSuccessListener(new zzh(this, taskCompletionSource))).addOnFailureListener(new zzg(this, taskCompletionSource));
        streamDownloadTask.zzf();
        return taskCompletionSource.getTask();
    }

    @NonNull
    public Task<Uri> getDownloadUrl() {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        Task metadata = getMetadata();
        metadata.addOnSuccessListener(new zze(this, taskCompletionSource));
        metadata.addOnFailureListener(new zzf(this, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    @NonNull
    public FileDownloadTask getFile(@NonNull Uri uri) {
        StorageTask fileDownloadTask = new FileDownloadTask(this, uri);
        fileDownloadTask.zzf();
        return fileDownloadTask;
    }

    @NonNull
    public FileDownloadTask getFile(@NonNull File file) {
        return getFile(Uri.fromFile(file));
    }

    @NonNull
    public Task<StorageMetadata> getMetadata() {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zzu.zza(new zzc(this, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    @NonNull
    public String getName() {
        String path = this.zzas.getPath();
        int lastIndexOf = path.lastIndexOf(47);
        return lastIndexOf != -1 ? path.substring(lastIndexOf + 1) : path;
    }

    @Nullable
    public StorageReference getParent() {
        String path = this.zzas.getPath();
        if (TextUtils.isEmpty(path) || path.equals(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
            return null;
        }
        int lastIndexOf = path.lastIndexOf(47);
        return new StorageReference(this.zzas.buildUpon().path(lastIndexOf == -1 ? InternalZipConstants.ZIP_FILE_SEPARATOR : path.substring(0, lastIndexOf)).build(), this.zzat);
    }

    @NonNull
    public String getPath() {
        return this.zzas.getPath();
    }

    @NonNull
    public StorageReference getRoot() {
        return new StorageReference(this.zzas.buildUpon().path("").build(), this.zzat);
    }

    @NonNull
    public FirebaseStorage getStorage() {
        return this.zzat;
    }

    @NonNull
    public StreamDownloadTask getStream() {
        StorageTask streamDownloadTask = new StreamDownloadTask(this);
        streamDownloadTask.zzf();
        return streamDownloadTask;
    }

    @NonNull
    public StreamDownloadTask getStream(@NonNull StreamProcessor streamProcessor) {
        StorageTask streamDownloadTask = new StreamDownloadTask(this);
        streamDownloadTask.zza(streamProcessor);
        streamDownloadTask.zzf();
        return streamDownloadTask;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @NonNull
    public UploadTask putBytes(@NonNull byte[] bArr) {
        Preconditions.checkArgument(bArr != null, "bytes cannot be null");
        StorageTask uploadTask = new UploadTask(this, null, bArr);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putBytes(@NonNull byte[] bArr, @NonNull StorageMetadata storageMetadata) {
        boolean z = true;
        Preconditions.checkArgument(bArr != null, "bytes cannot be null");
        if (storageMetadata == null) {
            z = false;
        }
        Preconditions.checkArgument(z, "metadata cannot be null");
        StorageTask uploadTask = new UploadTask(this, storageMetadata, bArr);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putFile(@NonNull Uri uri) {
        Preconditions.checkArgument(uri != null, "uri cannot be null");
        StorageTask uploadTask = new UploadTask(this, null, uri, null);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putFile(@NonNull Uri uri, @NonNull StorageMetadata storageMetadata) {
        boolean z = true;
        Preconditions.checkArgument(uri != null, "uri cannot be null");
        if (storageMetadata == null) {
            z = false;
        }
        Preconditions.checkArgument(z, "metadata cannot be null");
        StorageTask uploadTask = new UploadTask(this, storageMetadata, uri, null);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putFile(@NonNull Uri uri, @Nullable StorageMetadata storageMetadata, @Nullable Uri uri2) {
        boolean z = true;
        Preconditions.checkArgument(uri != null, "uri cannot be null");
        if (storageMetadata == null) {
            z = false;
        }
        Preconditions.checkArgument(z, "metadata cannot be null");
        StorageTask uploadTask = new UploadTask(this, storageMetadata, uri, uri2);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putStream(@NonNull InputStream inputStream) {
        Preconditions.checkArgument(inputStream != null, "stream cannot be null");
        StorageTask uploadTask = new UploadTask(this, null, inputStream);
        uploadTask.zzf();
        return uploadTask;
    }

    @NonNull
    public UploadTask putStream(@NonNull InputStream inputStream, @NonNull StorageMetadata storageMetadata) {
        boolean z = true;
        Preconditions.checkArgument(inputStream != null, "stream cannot be null");
        if (storageMetadata == null) {
            z = false;
        }
        Preconditions.checkArgument(z, "metadata cannot be null");
        StorageTask uploadTask = new UploadTask(this, storageMetadata, inputStream);
        uploadTask.zzf();
        return uploadTask;
    }

    public String toString() {
        String authority = this.zzas.getAuthority();
        String encodedPath = this.zzas.getEncodedPath();
        return new StringBuilder((String.valueOf(authority).length() + 5) + String.valueOf(encodedPath).length()).append("gs://").append(authority).append(encodedPath).toString();
    }

    @NonNull
    public Task<StorageMetadata> updateMetadata(@NonNull StorageMetadata storageMetadata) {
        Preconditions.checkNotNull(storageMetadata);
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zzu.zza(new zzac(this, taskCompletionSource, storageMetadata));
        return taskCompletionSource.getTask();
    }

    @NonNull
    final zzp zzd() throws RemoteException {
        return zzp.zzb(getStorage().getApp());
    }

    @NonNull
    final Uri zze() {
        return this.zzas;
    }
}
