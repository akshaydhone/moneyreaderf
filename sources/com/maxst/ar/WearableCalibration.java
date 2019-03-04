package com.maxst.ar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class WearableCalibration {
    private static final String COLUMN_CALIBRATION_DATA = "Calibration Data";
    private static final String COLUMN_FILE_NAME = "Filename";
    private static final String TAG = WearableCalibration.class.getSimpleName();
    private static WearableCalibration instance = null;
    private String activeProfileName = "";

    public enum DistanceType {
        DISTANCE_NEAR(0),
        DISTANCE_MIDDLE(1),
        DISTANCE_FAR(2),
        DISTANCE_NUM(3);
        
        private int value;

        private DistanceType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum EyeType {
        EYE_LEFT(0),
        EYE_RIGHT(1),
        EYE_NUM(2);
        
        private int value;

        private EyeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static WearableCalibration getInstance() {
        if (instance == null) {
            instance = new WearableCalibration();
        }
        return instance;
    }

    private WearableCalibration() {
    }

    public boolean init(String modelName, int targetWidth, int targetHeight) {
        return MaxstARJNI.WearableCalibration_init(modelName, targetWidth, targetHeight);
    }

    public boolean isActivated() {
        return MaxstARJNI.WearableCalibration_isActivated();
    }

    public void setSurfaceSize(int surfaceWidth, int surfaceHeight) {
        MaxstARJNI.WearableCalibration_setSurfaceSize(surfaceWidth, surfaceHeight);
    }

    public void deinit() {
        MaxstARJNI.WearableCalibration_deinit();
    }

    public float[] getViewport(EyeType eyeType) {
        return MaxstARJNI.WearableCalibration_getViewport(eyeType.getValue());
    }

    public float[] getProjectionMatrix(int eyeType) {
        return MaxstARJNI.WearableCalibration_getProjectionMatrix(eyeType);
    }

    public boolean writeProfile(String filePath) {
        return MaxstARJNI.WearableCalibration_writeProfile(filePath);
    }

    public boolean readProfile(String filePath) {
        return MaxstARJNI.WearableCalibration_readProfile(filePath);
    }

    public void loadDefaultProfile(String modelName) {
        MaxstARJNI.WearableCalibration_loadDefaultProfile(modelName);
    }

    public boolean setProfile(byte[] data) {
        return MaxstARJNI.WearableCalibration_setProfile(data);
    }

    public boolean readActiveProfile(Context context, String wearableDeviceName) {
        Cursor c = context.getContentResolver().query(Uri.parse("content://com.maxst.ar.wearablecali/activeprofile"), null, null, null, null);
        if (c == null || c.getCount() == 0) {
            Log.e(TAG, "No active profile");
            loadDefaultProfile(wearableDeviceName);
            Log.e(TAG, "Load default device profile " + wearableDeviceName);
            return false;
        }
        c.moveToFirst();
        this.activeProfileName = c.getString(c.getColumnIndex(COLUMN_FILE_NAME));
        byte[] data = c.getBlob(c.getColumnIndex(COLUMN_CALIBRATION_DATA));
        Log.d(TAG, "fileName:" + this.activeProfileName);
        this.activeProfileName = this.activeProfileName.substring(0, this.activeProfileName.lastIndexOf("."));
        Log.d(TAG, "data:" + new String(data, 0, data.length));
        return setProfile(data);
    }

    public String getActiveProfileName() {
        return this.activeProfileName;
    }

    public void getDistancePos(DistanceType distanceType, float[] pos) {
        MaxstARJNI.WearableCalibration_getDistancePos(distanceType.getValue(), pos);
    }

    public void setCameraToEyePose(EyeType eyeType, DistanceType distanceType, float[] pose) {
        MaxstARJNI.WearableCalibration_setCameraToEyePose(eyeType.getValue(), distanceType.getValue(), pose);
    }

    public float[] getScreenCoordinate() {
        return MaxstARJNI.WearableCalibration_getScreenCoordinate();
    }

    public void getTargetGLScale(DistanceType distanceType, float[] scale) {
        MaxstARJNI.WearableCalibration_getTargetGLScale(distanceType.getValue(), scale);
    }

    public void getTargetGLPosition(DistanceType distanceType, float[] position) {
        MaxstARJNI.WearableCalibration_getTargetGLPosition(distanceType.getValue(), position);
    }
}
