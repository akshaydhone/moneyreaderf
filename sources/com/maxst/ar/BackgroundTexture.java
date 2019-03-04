package com.maxst.ar;

public class BackgroundTexture {
    private long cMemPtr;

    BackgroundTexture(long cPtr) {
        this.cMemPtr = cPtr;
    }

    long getcMemPtr() {
        return this.cMemPtr;
    }

    public int getTextureId() {
        return MaxstARJNI.BackgroundTexture_getTextureId(this.cMemPtr);
    }
}
