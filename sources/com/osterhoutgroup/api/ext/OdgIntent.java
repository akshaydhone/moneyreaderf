package com.osterhoutgroup.api.ext;

public class OdgIntent {
    public static final String VOICE_COMMAND_INTENT = "com.osterhoutgroup.intent.VOICE_COMMAND";
    public static final String VOICE_COMMAND_TYPE = "VOICE_COMMAND_TYPE";

    public static class COMMAND_TYPES {
        public static final int BACKWARD = 65539;
        public static final int FORWARD = 65540;
        public static final int RECORD_VIDEO = 65555;
        public static final int REWIND = 65559;
        public static final int START_RECORDING = 65557;
        public static final int STOP_RECORDING = 65558;
        public static final int TAKE_PHOTO = 65556;
    }
}
