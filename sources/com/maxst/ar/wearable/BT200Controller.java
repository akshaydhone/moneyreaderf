package com.maxst.ar.wearable;

import android.app.Activity;
import jp.epson.moverio.bt200.DisplayControl;

public class BT200Controller extends WearableDeviceController {
    private DisplayControl displayControl;

    BT200Controller(Activity activity, String modelName, boolean supportedDevice) {
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
