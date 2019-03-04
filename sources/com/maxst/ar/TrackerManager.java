package com.maxst.ar;

public class TrackerManager {
    public static final int TRACKER_TYPE_CODE_SCANNER = 1;
    public static final int TRACKER_TYPE_IMAGE = 2;
    public static final int TRACKER_TYPE_INSTANT = 32;
    public static final int TRACKER_TYPE_OBJECT = 8;
    public static final int TRACKER_TYPE_SLAM = 16;
    private static TrackerManager instance = null;
    private SurfaceMesh surfaceMesh;
    private TrackingState trackingState = new TrackingState();

    public enum TrackingOption {
        NORMAL_TRACKING(1),
        EXTENDED_TRACKING(2),
        MULTI_TRACKING(4);
        
        private int value;

        private TrackingOption(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static TrackerManager getInstance() {
        if (instance == null) {
            instance = new TrackerManager();
        }
        return instance;
    }

    private TrackerManager() {
    }

    public void startTracker(int trackerType) {
        MaxstARJNI.TrackerManager_startTracker(trackerType);
    }

    public void stopTracker() {
        MaxstARJNI.TrackerManager_stopTracker();
    }

    public void destroyTracker() {
        MaxstARJNI.TrackerManager_destroyTracker();
    }

    public void addTrackerData(String trackingFileName, boolean isAndroidAssetFile) {
        MaxstARJNI.TrackerManager_addTrackerData(trackingFileName, isAndroidAssetFile);
    }

    public void removeTrackerData(String trackingFileName) {
        MaxstARJNI.TrackerManager_removeTrackerData(trackingFileName);
    }

    public void loadTrackerData() {
        MaxstARJNI.TrackerManager_loadTrackerData();
    }

    public boolean isTrackerDataLoadCompleted() {
        return MaxstARJNI.TrackerManager_isTrackerDataLoadCompleted();
    }

    public TrackingState updateTrackingState() {
        this.trackingState.setCMemPtr(MaxstARJNI.TrackerManager_updateTrackingState());
        return this.trackingState;
    }

    public void findSurface() {
        MaxstARJNI.TrackerManager_findSurface();
    }

    public void quitFindingSurface() {
        MaxstARJNI.TrackerManager_quitFindingSurface();
    }

    public void setTrackingOption(TrackingOption option) {
        MaxstARJNI.TrackerManager_setTrackingOption(option.getValue());
    }

    public SurfaceThumbnail saveSurfaceData(String fileName) {
        long cPtr = MaxstARJNI.TrackerManager_saveSurfaceData(fileName);
        if (cPtr != 0) {
            return new SurfaceThumbnail(cPtr);
        }
        return null;
    }

    public void getWorldPositionFromScreenCoordinate(float[] screenCoord, float[] worldPos) {
        MaxstARJNI.TrackerManager_getWorldPositionFromScreenCoordinate(screenCoord, worldPos);
    }

    public int getKeyframeCount() {
        return MaxstARJNI.TrackerManager_getKeyframeCount();
    }

    public int getFeatureCount() {
        return MaxstARJNI.TrackerManager_getFeatureCount();
    }

    public SurfaceMesh getSurfaceMesh() {
        if (this.surfaceMesh == null) {
            this.surfaceMesh = new SurfaceMesh();
        }
        this.surfaceMesh.updateSurfaceMesh();
        return this.surfaceMesh;
    }

    public void saveFrames() {
        MaxstARJNI.TrackerManager_saveFrames();
    }
}
