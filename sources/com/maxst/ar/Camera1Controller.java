package com.maxst.ar;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import com.maxst.ar.CameraDevice.FocusMode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Camera1Controller extends CameraController implements PreviewCallback {
    private static final String FALSE = "false";
    private static final String TAG = Camera1Controller.class.getSimpleName();
    private static final String TRUE = "true";
    private Camera camera;
    private int cameraId;
    private CameraSize cameraSize = new CameraSize(0, 0);
    private boolean keepAlive = true;
    private int preferredHeight = 480;
    private int preferredWidth = 640;

    /* renamed from: com.maxst.ar.Camera1Controller$1 */
    class C02521 implements AutoFocusCallback {
        C02521() {
        }

        public void onAutoFocus(boolean success, Camera camera) {
        }
    }

    Camera1Controller() {
    }

    private int startInternal() {
        if (this.camera != null && this.keepAlive) {
            return ResultCode.UnknownError.getValue();
        }
        try {
            this.camera = Camera.open(this.cameraId);
            Parameters params = this.camera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            if (Build.MODEL.equalsIgnoreCase("embt2") || Build.MODEL.equalsIgnoreCase("EMBT3C")) {
                Log.i(TAG, "This is moverio camera. Skip focus mode setting");
            } else if (focusModes != null) {
                if (focusModes.size() > 0) {
                    if (focusModes.contains("continuous-video")) {
                        params.setFocusMode("continuous-video");
                        Log.i(TAG, "FOCUS_MODE_CONTINUOUS_VIDEO");
                    } else if (focusModes.contains("continuous-picture")) {
                        params.setFocusMode("continuous-picture");
                        Log.i(TAG, "FOCUS_MODE_CONTINUOUS_VIDEO");
                    } else if (focusModes.contains("auto")) {
                        params.setFocusMode("auto");
                        Log.i(TAG, "FOCUS_MODE_AUTO");
                    }
                }
            }
            List<Integer> formats = params.getSupportedPreviewFormats();
            List<Size> cameraPreviewList = params.getSupportedPreviewSizes();
            ArrayList<CameraSize> cameraSizes = new ArrayList();
            for (Size size : cameraPreviewList) {
                cameraSizes.add(new CameraSize(size.width, size.height));
            }
            this.cameraSize = getOptimalPreviewSize(cameraSizes, this.preferredWidth, this.preferredHeight);
            params.setPreviewSize(this.cameraSize.width, this.cameraSize.height);
            params.setPreviewFormat(17);
            PixelFormat p = new PixelFormat();
            PixelFormat.getPixelFormatInfo(params.getPreviewFormat(), p);
            int bufSize = (((this.cameraSize.width * this.cameraSize.height) * p.bitsPerPixel) / 8) * 2;
            this.camera.addCallbackBuffer(new byte[bufSize]);
            this.camera.addCallbackBuffer(new byte[bufSize]);
            this.camera.addCallbackBuffer(new byte[bufSize]);
            this.camera.addCallbackBuffer(new byte[bufSize]);
            this.camera.setPreviewCallbackWithBuffer(this);
            try {
                this.camera.setParameters(params);
            } catch (Exception e) {
                Log.d(TAG, "setParameters fails");
            }
            this.keepAlive = true;
            try {
                if (this.cameraSurfaceTexture == null) {
                    this.cameraSurfaceTexture = new SurfaceTexture(-1);
                }
                this.camera.setPreviewTexture(this.cameraSurfaceTexture);
                this.camera.startPreview();
                return ResultCode.Success.getValue();
            } catch (IOException e2) {
                e2.printStackTrace();
                return ResultCode.UnknownError.getValue();
            }
        } catch (Exception e3) {
            Log.i("JLog", "Camera normal exception");
            return ResultCode.UnknownError.getValue();
        }
    }

    int start(int cameraId, int width, int height) {
        if (!checkPermission()) {
            return ResultCode.CameraPermissionIsNotGranted.getValue();
        }
        this.cameraId = cameraId;
        this.preferredWidth = width;
        this.preferredHeight = height;
        if (this.camera != null) {
            return ResultCode.CameraAlreadyOpened.getValue();
        }
        this.keepAlive = true;
        return startInternal();
    }

    void stop() {
        if (this.camera != null) {
            this.camera.lock();
            this.camera.setPreviewCallback(null);
            this.camera.setPreviewCallbackWithBuffer(null);
            if (this.cameraSurfaceTexture != null) {
                this.cameraSurfaceTexture.setOnFrameAvailableListener(null);
                this.cameraSurfaceTexture.release();
                this.cameraSurfaceTexture = null;
            }
            this.cameraSize.width = 0;
            this.cameraSize.height = 0;
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.keepAlive = false;
            Log.i(TAG, "Android camera stop");
        }
    }

    int getWidth() {
        return this.cameraSize.width;
    }

    int getHeight() {
        return this.cameraSize.height;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (this.keepAlive && data != null) {
            CameraController.setNewCameraFrame(data, data.length, this.cameraSize.width, this.cameraSize.height, ColorFormat.YUV420sp.getValue());
            camera.addCallbackBuffer(data);
        }
    }

    boolean setFocusMode(int focusMode) {
        boolean doPostSetAction = false;
        if (this.camera == null) {
            return false;
        }
        try {
            Parameters params = this.camera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            this.camera.cancelAutoFocus();
            switch (FocusMode.values()[focusMode - 1]) {
                case FOCUS_MODE_CONTINUOUS_AUTO:
                    if (focusModes.contains("continuous-video")) {
                        params.setFocusMode("continuous-video");
                        break;
                    }
                    return false;
                case FOCUS_MODE_AUTO:
                    params.setFocusMode("auto");
                    doPostSetAction = true;
                    break;
            }
            this.camera.setParameters(params);
            if (doPostSetAction) {
                this.camera.autoFocus(new C02521());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object[] getParamList() {
        List<String> paramList = new ArrayList();
        try {
            String[] paramArray = this.camera.getParameters().flatten().split("[=;]");
            for (int i = 0; i < paramArray.length / 2; i++) {
                paramList.add(paramArray[i * 2]);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e.getMessage());
        }
        return paramList.toArray();
    }

    public boolean setParam(String key, String value) {
        try {
            Parameters params = this.camera.getParameters();
            params.set(key, value);
            this.camera.setParameters(params);
            return true;
        } catch (Exception e) {
            Log.e("CameraDevice", "Set configuration error");
            return false;
        }
    }

    boolean setFlashLightMode(boolean toggle) {
        if (this.camera == null) {
            return false;
        }
        Parameters params = this.camera.getParameters();
        if (toggle) {
            params.setFlashMode("torch");
        } else {
            params.setFlashMode("off");
        }
        try {
            this.camera.setParameters(params);
            return true;
        } catch (Exception e) {
            Log.e("CameraDevice", "setFlashLightMode error");
            return false;
        }
    }

    boolean setAutoWhiteBalanceLock(boolean toggle) {
        if (this.camera != null) {
            Parameters params = this.camera.getParameters();
            params.setAutoWhiteBalanceLock(toggle);
            try {
                this.camera.setParameters(params);
            } catch (Exception e) {
                Log.e("CameraDevice", "setAutoWhiteBalanceLock error");
            }
        }
        return false;
    }

    private boolean checkPermission() {
        Context androidContext = MaxstARJNI.getContext();
        return androidContext.getPackageManager().checkPermission("android.permission.CAMERA", androidContext.getPackageName()) == 0;
    }
}
