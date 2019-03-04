package com.maxst.ar;

public class SensorDevice {
    private static SensorDevice instance = null;

    public static SensorDevice getInstance() {
        if (instance == null) {
            instance = new SensorDevice();
        }
        return instance;
    }

    private SensorDevice() {
    }

    public void start() {
        MaxstARJNI.startSensor();
    }

    public void stop() {
        MaxstARJNI.stopSensor();
    }
}
