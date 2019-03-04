package com.maxst.ar;

public class SurfaceThumbnail {
    private long cMemPtr;

    SurfaceThumbnail(long cMemPtr) {
        this.cMemPtr = cMemPtr;
    }

    public int getWidth() {
        return MaxstARJNI.SurfaceThumbnail_getWidth(this.cMemPtr);
    }

    public int getHeight() {
        return MaxstARJNI.SurfaceThumbnail_getHeight(this.cMemPtr);
    }

    public int getBpp() {
        return MaxstARJNI.SurfaceThumbnail_getBpp(this.cMemPtr);
    }

    public ColorFormat getFormat() {
        return ColorFormat.fromValue(MaxstARJNI.SurfaceThumbnail_getFormat(this.cMemPtr));
    }

    public int getLength() {
        return MaxstARJNI.SurfaceThumbnail_getLength(this.cMemPtr);
    }

    public byte[] getData() {
        byte[] data = new byte[getLength()];
        MaxstARJNI.SurfaceThumbnail_getData(this.cMemPtr, data);
        return data;
    }
}
