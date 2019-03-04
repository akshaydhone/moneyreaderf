package com.maxst.ar;

public enum ResultCode {
    Success(0),
    CameraPermissionIsNotResolved(100),
    CameraDevicedRestricted(101),
    CameraPermissionIsNotGranted(102),
    CameraAlreadyOpened(103),
    TrackerAlreadyStarted(200),
    UnknownError(1000);
    
    private int value;

    private ResultCode(int value) {
        this.value = value;
    }

    int getValue() {
        return this.value;
    }

    static ResultCode getCodeFromInt(int value) {
        for (ResultCode code : values()) {
            if (code.getValue() == value) {
                return code;
            }
        }
        return Success;
    }
}
