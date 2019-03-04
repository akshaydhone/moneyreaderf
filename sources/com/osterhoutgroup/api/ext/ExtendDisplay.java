package com.osterhoutgroup.api.ext;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class ExtendDisplay {
    private static final String TAG = "ExtendDisplay";
    private static boolean sSurfaceExtended = false;
    private static boolean sWindowSurfaceExtended = false;

    public static void extendSurface(SurfaceView sv, boolean extend) {
        if (sv != null) {
            sv.extendSurface(extend);
            sSurfaceExtended = extend;
            return;
        }
        throw new IllegalArgumentException("SurfaceView is not valid");
    }

    public static void extendWindow(Window w, boolean extend) {
        if (w != null) {
            if (extend) {
                w.setFlags(Integer.MIN_VALUE, Integer.MIN_VALUE);
            } else {
                w.clearFlags(Integer.MIN_VALUE);
            }
            sWindowSurfaceExtended = extend;
            return;
        }
        throw new IllegalArgumentException("Window is not valid");
    }

    public static void getDisplayMetrics(Context context, Window w, DisplayMetrics outMetrics) {
        if (w != null) {
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
            if (sWindowSurfaceExtended) {
                outMetrics.widthPixels *= 2;
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Window is not valid");
    }

    public static void getDisplayMetrics(Context context, SurfaceView sv, DisplayMetrics outMetrics) {
        if (sv != null) {
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
            if (sSurfaceExtended) {
                outMetrics.widthPixels *= 2;
                return;
            }
            throw new IllegalArgumentException("SurfaceView is not valid");
        }
    }
}
