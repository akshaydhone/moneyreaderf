package com.maxst.ar;

import java.util.Arrays;
import java.util.List;

public class CameraDevice {
    private static CameraDevice instance = null;

    public enum FlipDirection {
        HORIZONTAL(0),
        VERTICAL(1);
        
        private int value;

        private FlipDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum FocusMode {
        FOCUS_MODE_CONTINUOUS_AUTO(1),
        FOCUS_MODE_AUTO(2);
        
        private int value;

        private FocusMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static CameraDevice getInstance() {
        if (MaxstAR.isInitialized()) {
            if (instance == null) {
                instance = new CameraDevice();
            }
            return instance;
        }
        throw new RuntimeException("MaxstAR has not been initialized!");
    }

    private CameraDevice() {
    }

    public ResultCode start(int cameraId, int width, int height) {
        return ResultCode.getCodeFromInt(MaxstARJNI.CameraDevice_start(cameraId, width, height));
    }

    public void stop() {
        MaxstARJNI.CameraDevice_stop();
    }

    public void setNewFrame(byte[] data, int length, int width, int height, ColorFormat format) {
        if (MaxstARJNI.getLicenseType() == 2) {
            MaxstARJNI.CameraDevice_setNewFrame(data, length, width, height, format.getValue());
            return;
        }
        try {
            throw new Exception("Set external camera image is activated only enterprise license!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setFocusMode(FocusMode focusMode) {
        return MaxstARJNI.CameraDevice_setFocusMode(focusMode.getValue());
    }

    public boolean setFlashLightMode(boolean toggle) {
        return MaxstARJNI.CameraDevice_setFlashLightMode(toggle);
    }

    public boolean setAutoWhiteBalanceLock(boolean toggle) {
        return MaxstARJNI.CameraDevice_setAutoWhiteBalanceLock(toggle);
    }

    public void flipVideo(FlipDirection direction, boolean toggle) {
        MaxstARJNI.CameraDevice_flipVideo(direction.getValue(), toggle);
    }

    public List<String> getParamList() {
        return Arrays.asList(MaxstARJNI.CameraDevice_getParamList());
    }

    public boolean setParam(String paramKey, boolean paramValue) {
        return MaxstARJNI.CameraDevice_setBoolTypeParameter(paramKey, paramValue);
    }

    public boolean setParam(String paramKey, int paramValue) {
        return MaxstARJNI.CameraDevice_setIntTypeParameter(paramKey, paramValue);
    }

    public boolean setParam(String paramKey, int min, int max) {
        return MaxstARJNI.CameraDevice_setRangeTypeParameter(paramKey, min, max);
    }

    public boolean setParam(String paramKey, String paramValue) {
        return MaxstARJNI.CameraDevice_setStringTypeParameter(paramKey, paramValue);
    }

    public float[] getProjectionMatrix() {
        float[] projection = new float[16];
        MaxstARJNI.CameraDevice_getProjectionMatrix(projection);
        return projection;
    }

    public float[] getBackgroundPlaneProjectionMatrix() {
        float[] projection = new float[16];
        MaxstARJNI.CameraDevice_getBackgroundPlaneProjectionMatrix(projection);
        return projection;
    }
}
