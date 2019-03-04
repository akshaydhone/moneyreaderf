package com.maxst.ar;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

@TargetApi(23)
class Camera2Controller extends CameraController {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String TAG = Camera2Controller.class.getSimpleName();
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CameraDevice cameraDevice;
    private CameraSize cameraSize = new CameraSize(0, 0);
    private CameraCaptureSession captureSession;
    private boolean keepSendNewFrame = false;
    private final OnImageAvailableListener mOnPreviewFrameListener = new C02541();
    private final StateCallback mStateCallback = new C02552();
    long nextTime = 0;
    private ImageReader previewReader;
    private CaptureRequest previewRequest;
    private Builder previewRequestBuilder;
    private int testCount = 0;
    long time = 0;
    private byte[] yuvBuffer = null;

    /* renamed from: com.maxst.ar.Camera2Controller$1 */
    class C02541 implements OnImageAvailableListener {
        C02541() {
        }

        public void onImageAvailable(ImageReader reader) {
            Camera2Controller.this.time = System.currentTimeMillis() - Camera2Controller.this.nextTime;
            Log.d("FrameRate", String.valueOf(Camera2Controller.this.time));
            ColorFormat colorFormat = ColorFormat.YUV420_888;
            Image acquiredImage = reader.acquireNextImage();
            if (acquiredImage != null) {
                Assert.assertTrue("Image format is not ImageFormat.YUV_420_888", acquiredImage.getFormat() == 35);
                int width = acquiredImage.getWidth();
                int height = acquiredImage.getHeight();
                Plane yPlane = acquiredImage.getPlanes()[0];
                int ySize = yPlane.getBuffer().remaining();
                Plane uPlane = acquiredImage.getPlanes()[1];
                Plane vPlane = acquiredImage.getPlanes()[2];
                int uSize = uPlane.getBuffer().remaining();
                int vSize = vPlane.getBuffer().remaining();
                int margin = 0;
                if ((ySize / 2) - vSize > 0) {
                    margin = (ySize / 2) - vSize;
                }
                if (Camera2Controller.this.yuvBuffer == null || Camera2Controller.this.yuvBuffer.length < (ySize + uSize) + vSize) {
                    Camera2Controller.this.yuvBuffer = new byte[(((ySize + uSize) + vSize) + 100)];
                }
                yPlane.getBuffer().get(Camera2Controller.this.yuvBuffer, 0, ySize);
                if (uPlane.getPixelStride() == 1) {
                    uPlane.getBuffer().get(Camera2Controller.this.yuvBuffer, ySize, uSize);
                    vPlane.getBuffer().get(Camera2Controller.this.yuvBuffer, ySize + uSize, vSize);
                    colorFormat = ColorFormat.YUV420;
                } else {
                    vPlane.getBuffer().get(Camera2Controller.this.yuvBuffer, ySize, vSize);
                    uPlane.getBuffer().get(Camera2Controller.this.yuvBuffer, (ySize + vSize) + margin, uSize);
                    colorFormat = ColorFormat.YUV420_888;
                }
                if (Camera2Controller.this.keepSendNewFrame) {
                    CameraController.setNewCameraFrame(Camera2Controller.this.yuvBuffer, Camera2Controller.this.yuvBuffer.length, width, height, colorFormat.getValue());
                }
                acquiredImage.close();
                Camera2Controller.this.nextTime = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: com.maxst.ar.Camera2Controller$2 */
    class C02552 extends StateCallback {
        C02552() {
        }

        public void onOpened(CameraDevice cameraDevice) {
            Camera2Controller.this.cameraDevice = cameraDevice;
            Camera2Controller.this.createCameraPreviewSession();
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            Camera2Controller.this.cameraDevice = null;
            Camera2Controller.this.cameraState = CameraState.None;
        }

        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            Camera2Controller.this.cameraDevice = null;
            Camera2Controller.this.cameraState = CameraState.None;
        }
    }

    /* renamed from: com.maxst.ar.Camera2Controller$3 */
    class C02563 extends CameraCaptureSession.StateCallback {
        C02563() {
        }

        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            if (Camera2Controller.this.cameraDevice != null) {
                Camera2Controller.this.captureSession = cameraCaptureSession;
                Camera2Controller.this.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(4));
                Camera2Controller.this.previewRequest = Camera2Controller.this.previewRequestBuilder.build();
                try {
                    Camera2Controller.this.captureSession.setRepeatingRequest(Camera2Controller.this.previewRequest, null, null);
                    Camera2Controller.this.cameraState = CameraState.Opened;
                    Camera2Controller.this.keepSendNewFrame = true;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    Camera2Controller.this.cameraState = CameraState.None;
                }
            }
        }

        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            Camera2Controller.this.cameraState = CameraState.None;
        }
    }

    Camera2Controller() {
    }

    private void startInternal(int id, int width, int height) {
        startBackgroundThread();
        try {
            CameraManager manager = (CameraManager) MaxstARJNI.getContext().getSystemService("camera");
            String strCameraId = manager.getCameraIdList()[id];
            Size[] sizes = ((StreamConfigurationMap) manager.getCameraCharacteristics(strCameraId).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(35);
            ArrayList<CameraSize> cameraSizes = new ArrayList();
            for (Size size : sizes) {
                cameraSizes.add(new CameraSize(size.getWidth(), size.getHeight()));
            }
            this.cameraSize = getOptimalPreviewSize(cameraSizes, width, height);
            this.previewReader = ImageReader.newInstance(this.cameraSize.width, this.cameraSize.height, 35, 2);
            this.previewReader.setOnImageAvailableListener(this.mOnPreviewFrameListener, null);
            manager.openCamera(strCameraId, this.mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            this.cameraState = CameraState.None;
        }
    }

    public int start(int cameraId, int width, int height) {
        if (!checkPermission()) {
            return ResultCode.CameraPermissionIsNotGranted.getValue();
        }
        if (this.cameraState != CameraState.None && this.cameraState != CameraState.Closed) {
            return ResultCode.CameraAlreadyOpened.getValue();
        }
        this.cameraState = CameraState.Opening;
        startInternal(cameraId, width, height);
        return ResultCode.Success.getValue();
    }

    public void stop() {
        this.keepSendNewFrame = false;
        if (this.cameraState == CameraState.Opened) {
            this.cameraState = CameraState.Closing;
            closeCamera();
            stopBackgroundThread();
            this.cameraSize.width = 0;
            this.cameraSize.height = 0;
            if (this.cameraSurfaceTexture != null) {
                this.cameraSurfaceTexture.release();
                this.cameraSurfaceTexture = null;
            }
            this.cameraState = CameraState.Closed;
        }
    }

    @TargetApi(21)
    private void createCameraPreviewSession() {
        try {
            Surface surface = this.previewReader.getSurface();
            List<Surface> surfaces = new ArrayList();
            surfaces.add(surface);
            this.previewRequestBuilder = this.cameraDevice.createCaptureRequest(1);
            this.previewRequestBuilder.addTarget(surface);
            this.cameraDevice.createCaptureSession(surfaces, new C02563(), null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            this.cameraState = CameraState.None;
        }
    }

    private void closeCamera() {
        if (this.captureSession != null) {
            this.captureSession.close();
            this.captureSession = null;
        }
        if (this.previewReader != null) {
            this.previewReader.setOnImageAvailableListener(null, null);
            this.previewReader.close();
            this.previewReader = null;
        }
        if (this.cameraDevice != null) {
            this.cameraDevice.close();
            this.cameraDevice = null;
        }
        Log.i(TAG, "Android closeCamera completed");
        this.yuvBuffer = null;
    }

    private void startBackgroundThread() {
        this.backgroundThread = new HandlerThread("CameraBackground");
        this.backgroundThread.start();
        this.backgroundHandler = new Handler(this.backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        this.backgroundThread.quitSafely();
        try {
            this.backgroundThread.join();
            this.backgroundThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int getWidth() {
        return this.cameraSize.width;
    }

    int getHeight() {
        return this.cameraSize.height;
    }

    boolean setFocusMode(int focusMode) {
        return false;
    }

    boolean setFlashLightMode(boolean on) {
        return false;
    }

    boolean setAutoWhiteBalanceLock(boolean toggle) {
        return false;
    }

    Object[] getParamList() {
        return new String[0];
    }

    boolean setParam(String key, String value) {
        return false;
    }

    private boolean checkPermission() {
        Context androidContext = MaxstARJNI.getContext();
        return androidContext.getPackageManager().checkPermission("android.permission.CAMERA", androidContext.getPackageName()) == 0;
    }
}
