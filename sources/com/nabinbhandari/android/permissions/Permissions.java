package com.nabinbhandari.android.permissions;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Permissions {
    static boolean loggingEnabled = true;

    public static class Options implements Serializable {
        String rationaleDialogTitle = "Permissions Required";
        boolean sendBlockedToSettings = true;
        String settingsDialogMessage = "Required permission(s) have been set not to ask again! Please provide them from settings.";
        String settingsDialogTitle = "Permissions Required";
        String settingsText = "Settings";

        public Options setSettingsText(String settingsText) {
            this.settingsText = settingsText;
            return this;
        }

        public Options setRationaleDialogTitle(String rationaleDialogTitle) {
            this.rationaleDialogTitle = rationaleDialogTitle;
            return this;
        }

        public Options setSettingsDialogTitle(String settingsDialogTitle) {
            this.settingsDialogTitle = settingsDialogTitle;
            return this;
        }

        public Options setSettingsDialogMessage(String settingsDialogMessage) {
            this.settingsDialogMessage = settingsDialogMessage;
            return this;
        }

        public Options sendDontAskAgainToSettings(boolean send) {
            this.sendBlockedToSettings = send;
            return this;
        }
    }

    public static void disableLogging() {
        loggingEnabled = false;
    }

    static void log(String message) {
        if (loggingEnabled) {
            Log.d("Permissions", message);
        }
    }

    public static void check(Context context, String permission, String rationale, PermissionHandler handler) {
        check(context, new String[]{permission}, rationale, null, handler);
    }

    public static void check(Context context, String permission, int rationaleId, PermissionHandler handler) {
        String rationale = null;
        try {
            rationale = context.getString(rationaleId);
        } catch (Exception e) {
        }
        check(context, new String[]{permission}, rationale, null, handler);
    }

    public static void check(Context context, String[] permissions, String rationale, Options options, PermissionHandler handler) {
        if (VERSION.SDK_INT < 23) {
            handler.onGranted();
            log("Android version < 23");
            return;
        }
        ArrayList<String> permissionsList = new ArrayList();
        Collections.addAll(permissionsList, permissions);
        boolean allPermissionProvided = true;
        Iterator it = permissionsList.iterator();
        while (it.hasNext()) {
            if (context.checkSelfPermission((String) it.next()) != 0) {
                allPermissionProvided = false;
                break;
            }
        }
        if (allPermissionProvided) {
            handler.onGranted();
            log("Permission(s) " + (PermissionsActivity.permissionHandler == null ? "already granted." : "just granted from settings."));
            PermissionsActivity.permissionHandler = null;
            return;
        }
        PermissionsActivity.permissionHandler = handler;
        Intent intent = new Intent(context, PermissionsActivity.class);
        intent.putExtra("permissions", permissionsList);
        intent.putExtra("rationale", rationale);
        intent.putExtra("options", options);
        context.startActivity(intent);
    }

    public static void check(Context context, String[] permissions, int rationaleId, Options options, PermissionHandler handler) {
        String rationale = null;
        try {
            rationale = context.getString(rationaleId);
        } catch (Exception e) {
        }
        check(context, permissions, rationale, options, handler);
    }
}
