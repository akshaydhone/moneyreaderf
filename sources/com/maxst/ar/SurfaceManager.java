package com.maxst.ar;

class SurfaceManager {
    private static final String TAG = SurfaceManager.class.getSimpleName();
    private static SurfaceManager instance;
    private CameraController cameraController = CameraController.create();
    private SensorController sensorController;

    public static void init() {
        if (instance == null) {
            instance = new SurfaceManager();
        }
    }

    public static void deinit() {
        CameraController.destroy();
        SensorController.destroy();
        instance = null;
    }

    public static SurfaceManager getInstance() {
        return instance;
    }

    private SurfaceManager() {
        this.cameraController.setSurfaceManager(this);
        this.sensorController = SensorController.create(MaxstARJNI.getContext());
    }

    CameraController getCameraController() {
        return this.cameraController;
    }
}
