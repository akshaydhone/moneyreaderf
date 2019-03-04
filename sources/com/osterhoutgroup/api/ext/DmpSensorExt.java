package com.osterhoutgroup.api.ext;

import android.content.Context;
import android.os.DmpSensorManager;
import android.os.DmpSensorManager.DmpEventListenerWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class DmpSensorExt {
    public static final int EVENT_DISPLAY_ORIENTATION = 2;
    public static final int EVENT_FLICK = 4;
    public static final int EVENT_MOTION = 1;
    public static final int EVENT_ORIENTATION = 3;
    public static final int EVENT_PEDOMETER_STEPS = 5;
    public static final int EVENT_TAP = 0;
    public static final int FLAG_EVENT_DISPLAY_ORIENTATION = 4;
    public static final int FLAG_EVENT_DMP_ALL = 63;
    public static final int FLAG_EVENT_FLICK = 16;
    public static final int FLAG_EVENT_MOTION = 2;
    public static final int FLAG_EVENT_ORIENTATION = 8;
    public static final int FLAG_EVENT_PEDOMETER_STEPS = 32;
    public static final int FLAG_EVENT_TAP = 1;
    private static final String TAG = "DmpSensorExt";
    private static boolean sIsOdgHardware;
    private static final ArrayList<EventWrapper> sListeners = new ArrayList();
    private Context mContext;
    private DmpSensorManager mDmpSensorManager;

    private class EventWrapper implements DmpEventListenerWrapper {
        private DmpEventListener mEvent;
        private int mFlags;

        public EventWrapper(DmpEventListener l, int flags) {
            this.mEvent = l;
            this.mFlags = flags;
        }

        public DmpEventListener getListener() {
            return this.mEvent;
        }

        public void updateFlags(int flags) {
            this.mFlags = flags;
        }

        public boolean onDmpEvent(int type, int arg1, int arg2, long time) {
            return this.mEvent.onDmpEvent(type, arg1, arg2, time);
        }
    }

    static {
        sIsOdgHardware = true;
        try {
            Class.forName("android.os.DmpSensorManager", false, DmpSensorManager.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            sIsOdgHardware = false;
        }
    }

    public DmpSensorExt(Context context) {
        this.mContext = context;
        if (sIsOdgHardware) {
            this.mDmpSensorManager = (DmpSensorManager) this.mContext.getSystemService("dmp_service");
            return;
        }
        throw new RuntimeException("android.os.DmpSensorManager not found");
    }

    public void registerDmpSensorEvent(DmpEventListener listener, int flags) {
        Throwable th;
        if (listener != null) {
            synchronized (sListeners) {
                try {
                    EventWrapper l;
                    EventWrapper l2;
                    Iterator i$ = sListeners.iterator();
                    while (i$.hasNext()) {
                        EventWrapper i = (EventWrapper) i$.next();
                        if (i.getListener() == listener) {
                            l = i;
                            break;
                        }
                    }
                    l = null;
                    if (l == null) {
                        try {
                            l2 = new EventWrapper(listener, flags);
                            sListeners.add(l2);
                        } catch (Throwable th2) {
                            th = th2;
                            l2 = l;
                            throw th;
                        }
                    }
                    l.updateFlags(flags);
                    l2 = l;
                    this.mDmpSensorManager.registerDmpSensorEvent(l2, flags);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public void unregisterDmpSensorEvent(DmpEventListener listener) {
        if (listener != null) {
            synchronized (sListeners) {
                EventWrapper l = null;
                int size = sListeners.size();
                for (int i = 0; i < size; i++) {
                    l = (EventWrapper) sListeners.get(i);
                    if (l.getListener() == listener) {
                        sListeners.remove(i);
                        break;
                    }
                }
                if (sListeners.size() == 0) {
                    this.mDmpSensorManager.unregisterDmpSensorEvent(l);
                }
            }
        }
    }
}
