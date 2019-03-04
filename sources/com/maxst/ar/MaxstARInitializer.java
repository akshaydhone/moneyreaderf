package com.maxst.ar;

import android.app.Activity;

public class MaxstARInitializer {
    public static void init(Activity activity, String appKey) {
        MaxstAR.init(activity.getApplicationContext(), appKey);
    }

    public static void deinit() {
        MaxstAR.deinit();
    }
}
