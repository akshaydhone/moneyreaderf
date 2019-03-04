package com.maxst.ar.wearable;

import android.app.Activity;
import com.epson.moverio.btcontrol.DisplayControl;

public class BT300SeriesController extends WearableDeviceController {
    private DisplayControl displayControl;

    BT300SeriesController(Activity activity, String modelName, boolean supportedDevice) {
        super(activity, modelName, supportedDevice);
        this.displayControl = new DisplayControl(activity);
    }

    public void extendSurface(boolean toggle) {
        int i;
        super.extendSurface(toggle);
        DisplayControl displayControl = this.displayControl;
        if (toggle) {
            i = 1;
        } else {
            i = 0;
        }
        displayControl.setMode(i, false);
    }

    public void extendWindow(boolean toggle) {
        int i;
        super.extendWindow(toggle);
        DisplayControl displayControl = this.displayControl;
        if (toggle) {
            i = 1;
        } else {
            i = 0;
        }
        displayControl.setMode(i, false);
    }

    public void setStereoMode(boolean toggle) {
        int i;
        super.setStereoMode(toggle);
        DisplayControl displayControl = this.displayControl;
        if (toggle) {
            i = 1;
        } else {
            i = 0;
        }
        displayControl.setMode(i, false);
    }
}
