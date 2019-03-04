package com.maxst.ar;

public class TrackingResult {
    private long cMemPtr;

    TrackingResult() {
    }

    void setCMemPtr(long trackingResultCPtr) {
        this.cMemPtr = trackingResultCPtr;
    }

    public int getCount() {
        return MaxstARJNI.TrackingResult_getCount(this.cMemPtr);
    }

    public Trackable getTrackable(int index) {
        return new Trackable(MaxstARJNI.TrackingResult_getTrackable(this.cMemPtr, index));
    }
}
