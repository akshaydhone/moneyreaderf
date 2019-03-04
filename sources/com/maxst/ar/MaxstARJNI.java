package com.maxst.ar;

import android.content.Context;

class MaxstARJNI {
    static native void BackgroundRenderer_begin(long j);

    static native void BackgroundRenderer_deinitRendering();

    static native void BackgroundRenderer_end();

    static native long BackgroundRenderer_getBackgroundTexture();

    static native void BackgroundRenderer_renderBackgroundToTexture();

    static native void BackgroundRenderer_setRenderingOption(int i);

    static native int BackgroundTexture_getTextureId(long j);

    static native void CameraDevice_flipVideo(int i, boolean z);

    static native void CameraDevice_getBackgroundPlaneProjectionMatrix(float[] fArr);

    static native String[] CameraDevice_getParamList();

    static native void CameraDevice_getProjectionMatrix(float[] fArr);

    static native void CameraDevice_releaseCameraController();

    static native boolean CameraDevice_setAutoWhiteBalanceLock(boolean z);

    static native boolean CameraDevice_setBoolTypeParameter(String str, boolean z);

    static native void CameraDevice_setCameraController(CameraController cameraController);

    static native boolean CameraDevice_setFlashLightMode(boolean z);

    static native boolean CameraDevice_setFocusMode(int i);

    static native boolean CameraDevice_setIntTypeParameter(String str, int i);

    static native void CameraDevice_setNewFrame(byte[] bArr, int i, int i2, int i3, int i4);

    static native boolean CameraDevice_setRangeTypeParameter(String str, int i, int i2);

    static native boolean CameraDevice_setStringTypeParameter(String str, String str2);

    static native int CameraDevice_start(int i, int i2, int i3);

    static native void CameraDevice_stop();

    static native void SurfaceMesh_getIndexBuffer(long j, short[] sArr, int i);

    static native int SurfaceMesh_getIndexCount(long j);

    static native float SurfaceMesh_getInitializingProgress(long j);

    static native void SurfaceMesh_getVertexBuffer(long j, float[] fArr, int i);

    static native int SurfaceMesh_getVertexCount(long j);

    static native void SurfaceThumbnail_clear(long j);

    static native int SurfaceThumbnail_getBpp(long j);

    static native void SurfaceThumbnail_getData(long j, byte[] bArr);

    static native int SurfaceThumbnail_getFormat(long j);

    static native int SurfaceThumbnail_getHeight(long j);

    static native int SurfaceThumbnail_getLength(long j);

    static native int SurfaceThumbnail_getWidth(long j);

    static native float Trackable_getDistance(long j);

    static native float Trackable_getHeight(long j);

    static native String Trackable_getId(long j);

    static native String Trackable_getName(long j);

    static native void Trackable_getPose(long j, float[] fArr);

    static native float Trackable_getWidth(long j);

    static native void TrackedImage_getData(long j, byte[] bArr, int i);

    static native int TrackedImage_getFormat(long j);

    static native int TrackedImage_getHeight(long j);

    static native int TrackedImage_getLength(long j);

    static native int TrackedImage_getWidth(long j);

    static native void TrackerManager_addTrackerData(String str, boolean z);

    static native void TrackerManager_destroyTracker();

    static native void TrackerManager_findSurface();

    static native int TrackerManager_getFeatureCount();

    static native int TrackerManager_getKeyframeCount();

    static native float TrackerManager_getSlamInitializationProgress();

    static native long TrackerManager_getSurfaceMesh();

    static native void TrackerManager_getWorldPositionFromScreenCoordinate(float[] fArr, float[] fArr2);

    static native boolean TrackerManager_isTrackerDataLoadCompleted();

    static native void TrackerManager_loadTrackerData();

    static native void TrackerManager_quitFindingSurface();

    static native void TrackerManager_removeTrackerData(String str);

    static native void TrackerManager_saveFrames();

    static native long TrackerManager_saveSurfaceData(String str);

    static native void TrackerManager_setTrackingOption(int i);

    static native void TrackerManager_startTracker(int i);

    static native void TrackerManager_stopTracker();

    static native long TrackerManager_updateTrackingState();

    static native int TrackingResult_getCount(long j);

    static native long TrackingResult_getTrackable(long j, int i);

    static native String TrackingState_getCodeScanResult(long j);

    static native long TrackingState_getImage(long j);

    static native long TrackingState_getTrackingResult(long j);

    static native void WearableCalibration_deinit();

    static native void WearableCalibration_getDistancePos(int i, float[] fArr);

    static native float[] WearableCalibration_getProjectionMatrix(int i);

    static native float[] WearableCalibration_getScreenCoordinate();

    static native void WearableCalibration_getTargetGLPosition(int i, float[] fArr);

    static native void WearableCalibration_getTargetGLScale(int i, float[] fArr);

    static native float[] WearableCalibration_getViewport(int i);

    static native boolean WearableCalibration_init(String str, int i, int i2);

    static native boolean WearableCalibration_isActivated();

    static native void WearableCalibration_loadDefaultProfile(String str);

    static native boolean WearableCalibration_readProfile(String str);

    static native void WearableCalibration_setCameraToEyePose(int i, int i2, float[] fArr);

    static native boolean WearableCalibration_setProfile(byte[] bArr);

    static native void WearableCalibration_setSurfaceSize(int i, int i2);

    static native boolean WearableCalibration_writeProfile(String str);

    static native void deinit();

    static native Context getContext();

    static native int getLicenseType();

    static native void init(Context context, String str);

    static native boolean isInitialized();

    static native void onPause();

    static native void onResume();

    static native void onSurfaceChanged(int i, int i2);

    static native void onSurfaceCreated();

    static native void releaseSensorController();

    static native void setNewSensorData(float[] fArr);

    static native void setScreenOrientation(int i);

    static native void setSensorController(SensorController sensorController);

    static native void startSensor();

    static native void stopSensor();

    MaxstARJNI() {
    }

    static {
        System.loadLibrary("MaxstAR");
    }
}
