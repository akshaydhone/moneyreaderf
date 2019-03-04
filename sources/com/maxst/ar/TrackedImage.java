package com.maxst.ar;

public class TrackedImage {
    private long cPtr = 0;
    private ColorFormat colorFormat;
    private byte[] data;
    private int height;
    private int length;
    private int width;

    protected void setData(long cPtr) {
        if (cPtr != 0) {
            this.width = MaxstARJNI.TrackedImage_getWidth(cPtr);
            this.height = MaxstARJNI.TrackedImage_getHeight(cPtr);
            this.length = MaxstARJNI.TrackedImage_getLength(cPtr);
            this.colorFormat = ColorFormat.fromValue(MaxstARJNI.TrackedImage_getFormat(cPtr));
            if (this.width != 0 && this.height != 0) {
                if (this.data == null) {
                    this.data = new byte[this.length];
                }
                MaxstARJNI.TrackedImage_getData(cPtr, this.data, this.length);
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLength() {
        return this.length;
    }

    public ColorFormat getFormat() {
        return this.colorFormat;
    }

    public byte[] getData() {
        return this.data;
    }
}
