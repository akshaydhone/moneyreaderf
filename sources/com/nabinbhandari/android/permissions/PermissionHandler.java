package com.nabinbhandari.android.permissions;

import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class PermissionHandler {
    public abstract void onGranted();

    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
        if (Permissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Denied:");
            Iterator it = deniedPermissions.iterator();
            while (it.hasNext()) {
                String permission = (String) it.next();
                builder.append(" ");
                builder.append(permission);
            }
            Permissions.log(builder.toString());
        }
        Toast.makeText(context, "Permission Denied.", 0).show();
    }

    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
        if (Permissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Set not to ask again:");
            Iterator it = blockedList.iterator();
            while (it.hasNext()) {
                String permission = (String) it.next();
                builder.append(" ");
                builder.append(permission);
            }
            Permissions.log(builder.toString());
        }
        return false;
    }

    public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
        if (Permissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Just set not to ask again:");
            Iterator it = justBlockedList.iterator();
            while (it.hasNext()) {
                String permission = (String) it.next();
                builder.append(" ");
                builder.append(permission);
            }
            Permissions.log(builder.toString());
        }
        onDenied(context, deniedPermissions);
    }
}
