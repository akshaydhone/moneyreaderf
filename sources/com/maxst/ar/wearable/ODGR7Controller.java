package com.maxst.ar.wearable;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import com.osterhoutgroup.api.ext.ExtendDisplay;

public class ODGR7Controller extends WearableDeviceController {
    private static final String TAG = ODGR7Controller.class.getSimpleName();

    ODGR7Controller(Activity activity, String modelName, boolean supportedDevice) {
        super(activity, modelName, supportedDevice);
    }

    public void extendSurface(boolean toggle) {
        super.extendSurface(toggle);
        extendSurfaceInternal(toggle);
    }

    public void extendWindow(boolean toggle) {
        super.extendWindow(toggle);
        extendWindowInternal(toggle);
    }

    public void setStereoMode(boolean toggle) {
        super.setStereoMode(toggle);
        setStereoModeInternal(toggle);
    }

    private void setStereoModeInternal(final boolean toggle) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                Window window = ODGR7Controller.this.activity.getWindow();
                if (window != null) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    ExtendDisplay.getDisplayMetrics(ODGR7Controller.this.activity, window, metrics);
                    Log.d(ODGR7Controller.TAG, "Extend Window as : " + toggle);
                    Log.d(ODGR7Controller.TAG, "Before setting");
                    Log.d(ODGR7Controller.TAG, "Screen width : " + metrics.widthPixels);
                    Log.d(ODGR7Controller.TAG, "Screen height : " + metrics.heightPixels);
                    ExtendDisplay.extendWindow(window, toggle);
                    ExtendDisplay.getDisplayMetrics(ODGR7Controller.this.activity, window, metrics);
                    Log.d(ODGR7Controller.TAG, "After setting");
                    Log.d(ODGR7Controller.TAG, "Screen width : " + metrics.widthPixels);
                    Log.d(ODGR7Controller.TAG, "Screen height : " + metrics.heightPixels);
                    SurfaceView surfaceView = ODGR7Controller.this.getSurfaceViewInViewTree((ViewGroup) window.getDecorView());
                    if (surfaceView != null) {
                        Log.d(ODGR7Controller.TAG, "SurfaceView is not null");
                        ExtendDisplay.extendSurface(surfaceView, toggle);
                        LayoutParams layoutParams = surfaceView.getLayoutParams();
                        if (toggle) {
                            layoutParams.width = metrics.widthPixels;
                            surfaceView.setLayoutParams(layoutParams);
                            return;
                        }
                        layoutParams.width = metrics.widthPixels;
                        surfaceView.setLayoutParams(layoutParams);
                    }
                }
            }
        });
    }

    private void extendSurfaceInternal(final boolean toggle) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                SurfaceView surfaceView = ODGR7Controller.this.getSurfaceViewInViewTree((ViewGroup) ODGR7Controller.this.activity.getWindow().getDecorView());
                if (surfaceView != null) {
                    ExtendDisplay.extendSurface(surfaceView, toggle);
                    Log.d(ODGR7Controller.TAG, "Extend SurfaceView as : " + toggle);
                    if (toggle) {
                        DisplayMetrics metrics = new DisplayMetrics();
                        ExtendDisplay.getDisplayMetrics(ODGR7Controller.this.activity, surfaceView, metrics);
                        LayoutParams lp = surfaceView.getLayoutParams();
                        lp.width = metrics.widthPixels;
                        lp.height = metrics.heightPixels;
                        surfaceView.setLayoutParams(lp);
                        surfaceView.requestLayout();
                        Log.d(ODGR7Controller.TAG, "SurfaceView width : " + metrics.widthPixels);
                        Log.d(ODGR7Controller.TAG, "SurfaceView height : " + metrics.heightPixels);
                    }
                }
            }
        });
    }

    private void extendWindowInternal(final boolean toggle) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                Window window = ODGR7Controller.this.activity.getWindow();
                if (window != null) {
                    ExtendDisplay.extendWindow(window, toggle);
                    DisplayMetrics metrics = new DisplayMetrics();
                    ExtendDisplay.getDisplayMetrics(ODGR7Controller.this.activity, window, metrics);
                    Log.d(ODGR7Controller.TAG, "Extend Window as : " + toggle);
                    Log.d(ODGR7Controller.TAG, "window width : " + metrics.widthPixels);
                    Log.d(ODGR7Controller.TAG, "window height : " + metrics.heightPixels);
                }
            }
        });
    }

    private SurfaceView getSurfaceViewInViewTree(ViewGroup viewGroup) {
        int numOfChild = viewGroup.getChildCount();
        for (int i = 0; i < numOfChild; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof SurfaceView) {
                return (SurfaceView) view;
            }
            if (view instanceof ViewGroup) {
                View surfaceView = getSurfaceViewInViewTree((ViewGroup) view);
                if (surfaceView != null) {
                    return surfaceView;
                }
            }
        }
        return null;
    }
}
