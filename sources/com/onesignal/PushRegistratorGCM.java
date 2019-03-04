package com.onesignal;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.stats.CodePackage;

class PushRegistratorGCM extends PushRegistratorAbstractGoogle {
    PushRegistratorGCM() {
    }

    String getProviderName() {
        return CodePackage.GCM;
    }

    String getToken(String senderId) throws Throwable {
        return GoogleCloudMessaging.getInstance(OneSignal.appContext).register(new String[]{senderId});
    }
}
