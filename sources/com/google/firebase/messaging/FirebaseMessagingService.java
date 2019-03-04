package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.firebase.iid.zzan;
import com.google.firebase.iid.zzb;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzdg = new ArrayDeque(10);

    static void zzj(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }

    static boolean zzk(Bundle bundle) {
        return bundle == null ? false : "1".equals(bundle.getString("google.c.a.e"));
    }

    @WorkerThread
    public void onDeletedMessages() {
    }

    @WorkerThread
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    @WorkerThread
    public void onMessageSent(String str) {
    }

    @WorkerThread
    public void onSendError(String str, Exception exception) {
    }

    protected final Intent zzb(Intent intent) {
        return zzan.zzad().zzae();
    }

    public final boolean zzc(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (zzk(intent.getExtras())) {
            zzb.zzd(this, intent);
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzd(android.content.Intent r11) {
        /*
        r10 = this;
        r6 = 3;
        r5 = 2;
        r3 = -1;
        r4 = 1;
        r2 = 0;
        r0 = r11.getAction();
        if (r0 != 0) goto L_0x000d;
    L_0x000b:
        r0 = "";
    L_0x000d:
        r1 = r0.hashCode();
        switch(r1) {
            case 75300319: goto L_0x003c;
            case 366519424: goto L_0x0032;
            default: goto L_0x0014;
        };
    L_0x0014:
        r0 = r3;
    L_0x0015:
        switch(r0) {
            case 0: goto L_0x0046;
            case 1: goto L_0x01b3;
            default: goto L_0x0018;
        };
    L_0x0018:
        r1 = "FirebaseMessaging";
        r2 = "Unknown intent action: ";
        r0 = r11.getAction();
        r0 = java.lang.String.valueOf(r0);
        r3 = r0.length();
        if (r3 == 0) goto L_0x01c2;
    L_0x002a:
        r0 = r2.concat(r0);
    L_0x002e:
        android.util.Log.d(r1, r0);
    L_0x0031:
        return;
    L_0x0032:
        r1 = "com.google.android.c2dm.intent.RECEIVE";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0014;
    L_0x003a:
        r0 = r2;
        goto L_0x0015;
    L_0x003c:
        r1 = "com.google.firebase.messaging.NOTIFICATION_DISMISS";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0014;
    L_0x0044:
        r0 = r4;
        goto L_0x0015;
    L_0x0046:
        r0 = "google.message_id";
        r1 = r11.getStringExtra(r0);
        r0 = android.text.TextUtils.isEmpty(r1);
        if (r0 == 0) goto L_0x00bb;
    L_0x0052:
        r0 = 0;
        r0 = com.google.android.gms.tasks.Tasks.forResult(r0);
    L_0x0057:
        r7 = android.text.TextUtils.isEmpty(r1);
        if (r7 == 0) goto L_0x00ce;
    L_0x005d:
        r1 = r2;
    L_0x005e:
        if (r1 != 0) goto L_0x008a;
    L_0x0060:
        r1 = "message_type";
        r1 = r11.getStringExtra(r1);
        if (r1 != 0) goto L_0x006a;
    L_0x0068:
        r1 = "gcm";
    L_0x006a:
        r7 = r1.hashCode();
        switch(r7) {
            case -2062414158: goto L_0x011d;
            case 102161: goto L_0x0113;
            case 814694033: goto L_0x0133;
            case 814800675: goto L_0x0128;
            default: goto L_0x0071;
        };
    L_0x0071:
        r2 = r3;
    L_0x0072:
        switch(r2) {
            case 0: goto L_0x013e;
            case 1: goto L_0x017e;
            case 2: goto L_0x0183;
            case 3: goto L_0x018e;
            default: goto L_0x0075;
        };
    L_0x0075:
        r2 = "FirebaseMessaging";
        r3 = "Received message with unknown type: ";
        r1 = java.lang.String.valueOf(r1);
        r4 = r1.length();
        if (r4 == 0) goto L_0x01ac;
    L_0x0083:
        r1 = r3.concat(r1);
    L_0x0087:
        android.util.Log.w(r2, r1);
    L_0x008a:
        r2 = 1;
        r1 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ ExecutionException -> 0x0092, InterruptedException -> 0x01c9, TimeoutException -> 0x01cc }
        com.google.android.gms.tasks.Tasks.await(r0, r2, r1);	 Catch:{ ExecutionException -> 0x0092, InterruptedException -> 0x01c9, TimeoutException -> 0x01cc }
        goto L_0x0031;
    L_0x0092:
        r0 = move-exception;
    L_0x0093:
        r1 = "FirebaseMessaging";
        r0 = java.lang.String.valueOf(r0);
        r2 = java.lang.String.valueOf(r0);
        r2 = r2.length();
        r2 = r2 + 20;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "Message ack failed: ";
        r2 = r3.append(r2);
        r0 = r2.append(r0);
        r0 = r0.toString();
        android.util.Log.w(r1, r0);
        goto L_0x0031;
    L_0x00bb:
        r0 = new android.os.Bundle;
        r0.<init>();
        r7 = "google.message_id";
        r0.putString(r7, r1);
        r7 = com.google.firebase.iid.zzs.zzc(r10);
        r0 = r7.zza(r5, r0);
        goto L_0x0057;
    L_0x00ce:
        r7 = zzdg;
        r7 = r7.contains(r1);
        if (r7 == 0) goto L_0x00fc;
    L_0x00d6:
        r7 = "FirebaseMessaging";
        r7 = android.util.Log.isLoggable(r7, r6);
        if (r7 == 0) goto L_0x00f3;
    L_0x00de:
        r7 = "FirebaseMessaging";
        r8 = "Received duplicate message: ";
        r1 = java.lang.String.valueOf(r1);
        r9 = r1.length();
        if (r9 == 0) goto L_0x00f6;
    L_0x00ec:
        r1 = r8.concat(r1);
    L_0x00f0:
        android.util.Log.d(r7, r1);
    L_0x00f3:
        r1 = r4;
        goto L_0x005e;
    L_0x00f6:
        r1 = new java.lang.String;
        r1.<init>(r8);
        goto L_0x00f0;
    L_0x00fc:
        r7 = zzdg;
        r7 = r7.size();
        r8 = 10;
        if (r7 < r8) goto L_0x010b;
    L_0x0106:
        r7 = zzdg;
        r7.remove();
    L_0x010b:
        r7 = zzdg;
        r7.add(r1);
        r1 = r2;
        goto L_0x005e;
    L_0x0113:
        r4 = "gcm";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x0071;
    L_0x011b:
        goto L_0x0072;
    L_0x011d:
        r2 = "deleted_messages";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0071;
    L_0x0125:
        r2 = r4;
        goto L_0x0072;
    L_0x0128:
        r2 = "send_event";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0071;
    L_0x0130:
        r2 = r5;
        goto L_0x0072;
    L_0x0133:
        r2 = "send_error";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0071;
    L_0x013b:
        r2 = r6;
        goto L_0x0072;
    L_0x013e:
        r1 = r11.getExtras();
        r1 = zzk(r1);
        if (r1 == 0) goto L_0x014b;
    L_0x0148:
        com.google.firebase.messaging.zzb.zzc(r10, r11);
    L_0x014b:
        r1 = r11.getExtras();
        if (r1 != 0) goto L_0x0156;
    L_0x0151:
        r1 = new android.os.Bundle;
        r1.<init>();
    L_0x0156:
        r2 = "android.support.content.wakelockid";
        r1.remove(r2);
        r2 = com.google.firebase.messaging.zza.zzf(r1);
        if (r2 == 0) goto L_0x0174;
    L_0x0161:
        r2 = com.google.firebase.messaging.zza.zzd(r10);
        r2 = r2.zzh(r1);
        if (r2 != 0) goto L_0x008a;
    L_0x016b:
        r2 = zzk(r1);
        if (r2 == 0) goto L_0x0174;
    L_0x0171:
        com.google.firebase.messaging.zzb.zzf(r10, r11);
    L_0x0174:
        r2 = new com.google.firebase.messaging.RemoteMessage;
        r2.<init>(r1);
        r10.onMessageReceived(r2);
        goto L_0x008a;
    L_0x017e:
        r10.onDeletedMessages();
        goto L_0x008a;
    L_0x0183:
        r1 = "google.message_id";
        r1 = r11.getStringExtra(r1);
        r10.onMessageSent(r1);
        goto L_0x008a;
    L_0x018e:
        r1 = "google.message_id";
        r1 = r11.getStringExtra(r1);
        if (r1 != 0) goto L_0x019c;
    L_0x0196:
        r1 = "message_id";
        r1 = r11.getStringExtra(r1);
    L_0x019c:
        r2 = new com.google.firebase.messaging.SendException;
        r3 = "error";
        r3 = r11.getStringExtra(r3);
        r2.<init>(r3);
        r10.onSendError(r1, r2);
        goto L_0x008a;
    L_0x01ac:
        r1 = new java.lang.String;
        r1.<init>(r3);
        goto L_0x0087;
    L_0x01b3:
        r0 = r11.getExtras();
        r0 = zzk(r0);
        if (r0 == 0) goto L_0x0031;
    L_0x01bd:
        com.google.firebase.messaging.zzb.zze(r10, r11);
        goto L_0x0031;
    L_0x01c2:
        r0 = new java.lang.String;
        r0.<init>(r2);
        goto L_0x002e;
    L_0x01c9:
        r0 = move-exception;
        goto L_0x0093;
    L_0x01cc:
        r0 = move-exception;
        goto L_0x0093;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.zzd(android.content.Intent):void");
    }
}
