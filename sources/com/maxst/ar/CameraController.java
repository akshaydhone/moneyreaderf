package com.maxst.ar;

import android.graphics.SurfaceTexture;
import android.util.Log;
import java.util.List;

abstract class CameraController {
    static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final String TAG = CameraController.class.getSimpleName();
    CameraState cameraState = CameraState.None;
    SurfaceTexture cameraSurfaceTexture;
    SurfaceManager surfaceManager;

    enum CameraState {
        None,
        Opening,
        Opened,
        Closing,
        Closed
    }

    static native void setNewCameraFrame(byte[] bArr, int i, int i2, int i3, int i4);

    abstract int getHeight();

    abstract Object[] getParamList();

    abstract int getWidth();

    abstract boolean setAutoWhiteBalanceLock(boolean z);

    abstract boolean setFlashLightMode(boolean z);

    abstract boolean setFocusMode(int i);

    abstract boolean setParam(String str, String str2);

    abstract int start(int i, int i2, int i3);

    abstract void stop();

    CameraController() {
    }

    static CameraController create() {
        CameraController instance = new Camera1Controller();
        MaxstARJNI.CameraDevice_setCameraController(instance);
        return instance;
    }

    static void destroy() {
        MaxstARJNI.CameraDevice_releaseCameraController();
    }

    void setSurfaceManager(SurfaceManager surfaceManager) {
        this.surfaceManager = surfaceManager;
    }

    CameraSize getOptimalPreviewSize(List<CameraSize> sizes, int preferredWidth, int preferredHeight) {
        double minRegion = Double.MAX_VALUE;
        CameraSize optimalSize = null;
        for (CameraSize size : sizes) {
            if (size.width <= preferredWidth && size.height <= preferredHeight && ((double) Math.abs((size.width * size.height) - (preferredWidth * preferredHeight))) <= minRegion) {
                minRegion = (double) Math.abs((size.width * size.height) - (preferredWidth * preferredHeight));
                optimalSize = size;
                Log.i(TAG, "Optimal Preview width  : " + optimalSize.width + " height : " + optimalSize.height);
            }
        }
        return optimalSize;
    }
}
