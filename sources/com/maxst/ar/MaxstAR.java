package com.maxst.ar;

import android.content.Context;

public class MaxstAR {
    public static void init(Context context, String appKey) {
        MaxstARJNI.init(context, appKey);
    }

    public static void deinit() {
        MaxstARJNI.deinit();
    }

    public static Context getApplicationContext() {
        return MaxstARJNI.getContext();
    }

    public static boolean isInitialized() {
        return MaxstARJNI.isInitialized();
    }

    public static void onResume() {
        MaxstARJNI.onResume();
    }

    public static void onPause() {
        MaxstARJNI.onPause();
    }

    public static void onSurfaceCreated() {
        MaxstARJNI.onSurfaceCreated();
    }

    public static void onSurfaceChanged(int viewWidth, int viewHeight) {
        MaxstARJNI.onSurfaceChanged(viewWidth, viewHeight);
    }

    public static void setScreenOrientation(int orientation) {
        MaxstARJNI.setScreenOrientation(SystemUtil.getInstance().getRealOrientation(orientation));
    }
}
