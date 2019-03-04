package com.maxst.ar.wearable;

import android.app.Activity;
import android.os.Build;

public abstract class WearableDeviceController {
    protected Activity activity;
    private String modelName;
    private boolean stereoEnabled = false;
    private boolean supportedWearableDevice = false;
    private boolean surfaceExtended = false;
    private boolean windowExtended = false;

    public static WearableDeviceController createDeviceController(Activity activity) {
        if (Build.MODEL.equalsIgnoreCase("embt2")) {
            return new BT200Controller(activity, Build.MODEL, true);
        }
        if (Build.MODEL.equalsIgnoreCase("EMBT3C") || Build.MODEL.equalsIgnoreCase("EMBT3S")) {
            return new BT300SeriesController(activity, Build.MODEL, true);
        }
        if (Build.MODEL.equalsIgnoreCase("R7-W")) {
            return new ODGR7Controller(activity, Build.MODEL, true);
        }
        return new NullWearableController(activity, Build.MODEL, false);
    }

    WearableDeviceController(Activity activity, String modelName, boolean supportedDevice) {
        this.activity = activity;
        this.modelName = modelName;
        this.supportedWearableDevice = supportedDevice;
    }

    public boolean isSupportedWearableDevice() {
        return this.supportedWearableDevice;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void extendSurface(boolean toggle) {
        this.surfaceExtended = toggle;
    }

    public void extendWindow(boolean toggle) {
        this.windowExtended = toggle;
    }

    public void setStereoMode(boolean toggle) {
        this.stereoEnabled = toggle;
    }

    public boolean isSurfaceExtended() {
        return this.surfaceExtended;
    }

    public boolean isWindowExtended() {
        return this.windowExtended;
    }

    public boolean isStereoEnabled() {
        return this.stereoEnabled;
    }
}
