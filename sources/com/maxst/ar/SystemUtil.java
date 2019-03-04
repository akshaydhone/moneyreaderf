package com.maxst.ar;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

class SystemUtil {
    private static final String TAG = SystemUtil.class.getSimpleName();
    private static SystemUtil instance = null;

    private enum DeviceType {
        UNCLASSIFIED(0),
        SMARTPHONE_LIKE(1),
        TABLET_LIKE(2);
        
        private int value;

        private DeviceType(int value) {
            this.value = value;
        }

        int getValue() {
            return this.value;
        }
    }

    static SystemUtil getInstance() {
        if (instance == null) {
            instance = new SystemUtil();
        }
        return instance;
    }

    private SystemUtil() {
    }

    int getDeviceType() {
        int deviceType = 0;
        Context context = MaxstARJNI.getContext();
        int currentWindowOrientation = context.getResources().getConfiguration().orientation;
        int displayRotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
        switch (currentWindowOrientation) {
            case 1:
            case 3:
                deviceType = (displayRotation == 0 || displayRotation == 2) ? DeviceType.SMARTPHONE_LIKE.getValue() : DeviceType.TABLET_LIKE.getValue();
                break;
            case 2:
                if (displayRotation == 0 || displayRotation == 2) {
                    deviceType = DeviceType.TABLET_LIKE.getValue();
                } else {
                    deviceType = DeviceType.SMARTPHONE_LIKE.getValue();
                }
                break;
        }
        Log.i(TAG, "deviceType : " + deviceType);
        return deviceType;
    }

    int getRealOrientation(int orientation) {
        int displayRotation = ((WindowManager) MaxstAR.getApplicationContext().getSystemService("window")).getDefaultDisplay().getRotation();
        if (Build.MODEL.equalsIgnoreCase("M300")) {
            if (displayRotation == 0) {
                return 4;
            }
            if (2 == displayRotation) {
                return 3;
            }
            return 1;
        } else if (orientation == 1) {
            if (displayRotation == 0) {
                return 1;
            }
            if (displayRotation == 2) {
                return 2;
            }
            if (displayRotation == 1) {
                return 2;
            }
            if (displayRotation == 3) {
                return 1;
            }
            return 3;
        } else if (orientation != 2) {
            return 3;
        } else {
            if (displayRotation == 0) {
                return 3;
            }
            if (displayRotation == 2) {
                return 4;
            }
            if (displayRotation == 3) {
                return 4;
            }
            return 3;
        }
    }
}
