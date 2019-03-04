package com.maxst.ar;

public class Trackable {
    private long cMemPtr;

    Trackable(long cMemPtr) {
        this.cMemPtr = cMemPtr;
    }

    public String getId() {
        return MaxstARJNI.Trackable_getId(this.cMemPtr);
    }

    public String getName() {
        return MaxstARJNI.Trackable_getName(this.cMemPtr);
    }

    public float[] getPoseMatrix() {
        float[] pose = new float[16];
        MaxstARJNI.Trackable_getPose(this.cMemPtr, pose);
        return pose;
    }
}
