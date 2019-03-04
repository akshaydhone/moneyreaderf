package com.maxst.ar.wearable;

import android.app.Activity;

public class NullWearableController extends WearableDeviceController {
    NullWearableController(Activity activity, String modelName, boolean supportedDevice) {
        super(activity, modelName, supportedDevice);
    }
}
