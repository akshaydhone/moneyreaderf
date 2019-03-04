package com.maxst.ar;

import android.support.graphics.drawable.PathInterpolatorCompat;

public class SurfaceMesh {
    private static final int MAX_INDICES = 20000;
    private static final int MAX_VERTICES = 1000;
    private static short[] indexBuffer = null;
    private static float[] vertexBuffer = null;
    private long cMemPtr;
    private int indexCount;
    private float progress;
    private int vertexCount;

    SurfaceMesh() {
    }

    void updateSurfaceMesh() {
        this.cMemPtr = MaxstARJNI.TrackerManager_getSurfaceMesh();
        if (vertexBuffer == null) {
            vertexBuffer = new float[PathInterpolatorCompat.MAX_NUM_POINTS];
        }
        if (indexBuffer == null) {
            indexBuffer = new short[MAX_INDICES];
        }
        this.progress = MaxstARJNI.SurfaceMesh_getInitializingProgress(this.cMemPtr);
        this.vertexCount = MaxstARJNI.SurfaceMesh_getVertexCount(this.cMemPtr);
        this.indexCount = MaxstARJNI.SurfaceMesh_getIndexCount(this.cMemPtr);
        MaxstARJNI.SurfaceMesh_getVertexBuffer(this.cMemPtr, vertexBuffer, this.vertexCount * 3);
        MaxstARJNI.SurfaceMesh_getIndexBuffer(this.cMemPtr, indexBuffer, this.indexCount);
    }

    public float getInitialProgress() {
        return this.progress;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    public int getIndexCount() {
        return this.indexCount;
    }

    public float[] getVertexBuffer() {
        return vertexBuffer;
    }

    public short[] getIndexBuffer() {
        return indexBuffer;
    }
}
