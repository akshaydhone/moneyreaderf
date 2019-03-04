package com.maxst.ar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class SensorController implements SensorEventListener {
    private Sensor mRotVectSensor;
    private float[] mRotationMatrix = new float[9];
    private SensorManager mSensorManager;
    private float[] mTruncatedRotationVector = new float[4];

    static SensorController create(Context context) {
        SensorController instance = new SensorController(context);
        MaxstARJNI.setSensorController(instance);
        return instance;
    }

    private SensorController(Context context) {
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRotVectSensor = this.mSensorManager.getDefaultSensor(11);
    }

    static void destroy() {
        MaxstARJNI.releaseSensorController();
    }

    public void start() {
        this.mSensorManager.registerListener(this, this.mRotVectSensor, 0);
    }

    public void stop() {
        this.mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 11) {
            if (event.values.length > 4) {
                System.arraycopy(event.values, 0, this.mTruncatedRotationVector, 0, 4);
                SensorManager.getRotationMatrixFromVector(this.mRotationMatrix, this.mTruncatedRotationVector);
            } else {
                SensorManager.getRotationMatrixFromVector(this.mRotationMatrix, event.values);
            }
            MaxstARJNI.setNewSensorData(this.mRotationMatrix);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
