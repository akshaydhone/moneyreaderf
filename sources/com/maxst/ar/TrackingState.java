package com.maxst.ar;

public class TrackingState {
    private long cMemPtr;
    private TrackedImage trackedImage = new TrackedImage();
    private TrackingResult trackingResult = new TrackingResult();

    TrackingState() {
    }

    void setCMemPtr(long trackingStateCPtr) {
        this.cMemPtr = trackingStateCPtr;
    }

    public TrackingResult getTrackingResult() {
        this.trackingResult.setCMemPtr(MaxstARJNI.TrackingState_getTrackingResult(this.cMemPtr));
        return this.trackingResult;
    }

    public String getCodeScanResult() {
        return MaxstARJNI.TrackingState_getCodeScanResult(this.cMemPtr);
    }

    public TrackedImage getImage() {
        this.trackedImage.setData(MaxstARJNI.TrackingState_getImage(this.cMemPtr));
        return this.trackedImage;
    }
}
