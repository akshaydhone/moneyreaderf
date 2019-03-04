package com.osterhoutgroup.api.ext;

import android.content.Context;
import android.os.OdgHardwareManager;
import java.io.File;

public class OdgHardwareExt {
    public static final int BRIGHTNESS_LEVEL_BRIGHT = 1;
    public static final int BRIGHTNESS_LEVEL_DIM = 0;
    public static final int DISPLAY_BOTH_OFF = 4;
    public static final int DISPLAY_BOTH_ON = 1;
    public static final int DISPLAY_LEFT_OFF_RIGHT_ON = 3;
    public static final int DISPLAY_LEFT_ON_RIGHT_OFF = 2;
    public static final int FORCE_BACK_MIC = 12;
    public static final int FORCE_BUILTIN_MIC = 10;
    public static final int FORCE_DEFAULT_MIC = 0;
    public static final int FORCE_HEADSET_MIC = 11;
    private static final String TAG = "OdgHardwareExt";
    private static boolean sIsOdgHardware;
    private Context mContext;
    private OdgHardwareManager mOdgHardware;

    static {
        sIsOdgHardware = true;
        try {
            Class.forName("android.os.OdgHardwareManager", false, OdgHardwareManager.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            sIsOdgHardware = false;
        }
    }

    public OdgHardwareExt(Context context) {
        this.mContext = context;
        if (sIsOdgHardware) {
            this.mOdgHardware = (OdgHardwareManager) context.getSystemService("odg_hardware");
        }
    }

    public void setTrackpadMode(int source) {
        if (sIsOdgHardware) {
            this.mOdgHardware.setTrackpadMode(source);
        }
    }

    public void setPendingTrackpadChange(int source) {
        if (sIsOdgHardware) {
            this.mOdgHardware.setPendingTrackpadChange(source);
        }
    }

    public int getTrackpadMode() {
        if (sIsOdgHardware) {
            return this.mOdgHardware.getTrackpadMode();
        }
        return 65540;
    }

    public void setBrightnessLevel(int level) {
        if (sIsOdgHardware) {
            this.mOdgHardware.setBrightnessLevel(level);
        }
    }

    public int getBrightnessLevel() {
        if (sIsOdgHardware) {
            return this.mOdgHardware.getBrightnessLevel();
        }
        return 1;
    }

    public void setDualDisplay(int display) {
        if (sIsOdgHardware) {
            this.mOdgHardware.setDualDisplay(display);
        }
    }

    public int getDualDisplay() {
        if (sIsOdgHardware) {
            return this.mOdgHardware.getDualDisplay();
        }
        return 1;
    }

    public void setMicForRecording(int config) {
        if (sIsOdgHardware) {
            this.mOdgHardware.setMicForRecording(config);
        }
    }

    public File getExternalAltStorageDirectory() {
        if (sIsOdgHardware) {
            return this.mOdgHardware.getExternalAltStorageDirectory();
        }
        return null;
    }

    public String getExternalAltStorageState() {
        if (sIsOdgHardware) {
            return this.mOdgHardware.getExternalAltStorageState();
        }
        return "";
    }
}
