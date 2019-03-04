package com.nabinbhandari.android.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.nabinbhandari.android.permissions.Permissions.Options;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(23)
public class PermissionsActivity extends Activity {
    static final String EXTRA_OPTIONS = "options";
    static final String EXTRA_PERMISSIONS = "permissions";
    static final String EXTRA_RATIONALE = "rationale";
    private static final int RC_PERMISSION = 6937;
    private static final int RC_SETTINGS = 6739;
    static PermissionHandler permissionHandler;
    private ArrayList<String> allPermissions;
    private boolean cleanHandlerOnDestroy = true;
    private ArrayList<String> deniedPermissions;
    private ArrayList<String> noRationaleList;
    private Options options;

    /* renamed from: com.nabinbhandari.android.permissions.PermissionsActivity$1 */
    class C02601 implements OnClickListener {
        C02601() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                PermissionsActivity.this.requestPermissions((String[]) PermissionsActivity.this.deniedPermissions.toArray(new String[0]), PermissionsActivity.RC_PERMISSION);
            } else {
                PermissionsActivity.this.deny();
            }
        }
    }

    /* renamed from: com.nabinbhandari.android.permissions.PermissionsActivity$2 */
    class C02612 implements OnCancelListener {
        C02612() {
        }

        public void onCancel(DialogInterface dialog) {
            PermissionsActivity.this.deny();
        }
    }

    /* renamed from: com.nabinbhandari.android.permissions.PermissionsActivity$3 */
    class C02623 implements OnCancelListener {
        C02623() {
        }

        public void onCancel(DialogInterface dialog) {
            PermissionsActivity.this.deny();
        }
    }

    /* renamed from: com.nabinbhandari.android.permissions.PermissionsActivity$4 */
    class C02634 implements OnClickListener {
        C02634() {
        }

        public void onClick(DialogInterface dialog, int which) {
            PermissionsActivity.this.deny();
        }
    }

    /* renamed from: com.nabinbhandari.android.permissions.PermissionsActivity$5 */
    class C02645 implements OnClickListener {
        C02645() {
        }

        public void onClick(DialogInterface dialog, int which) {
            PermissionsActivity.this.startActivityForResult(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", PermissionsActivity.this.getPackageName(), null)), PermissionsActivity.RC_SETTINGS);
        }
    }

    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            finish();
            return;
        }
        getWindow().setStatusBarColor(0);
        this.allPermissions = (ArrayList) intent.getSerializableExtra(EXTRA_PERMISSIONS);
        this.options = (Options) intent.getSerializableExtra(EXTRA_OPTIONS);
        if (this.options == null) {
            this.options = new Options();
        }
        this.deniedPermissions = new ArrayList();
        this.noRationaleList = new ArrayList();
        boolean noRationale = true;
        Iterator it = this.allPermissions.iterator();
        while (it.hasNext()) {
            String permission = (String) it.next();
            if (checkSelfPermission(permission) != 0) {
                this.deniedPermissions.add(permission);
                if (shouldShowRequestPermissionRationale(permission)) {
                    noRationale = false;
                } else {
                    this.noRationaleList.add(permission);
                }
            }
        }
        String rationale = intent.getStringExtra(EXTRA_RATIONALE);
        if (noRationale || TextUtils.isEmpty(rationale)) {
            Permissions.log("No rationale.");
            requestPermissions((String[]) this.deniedPermissions.toArray(new String[0]), RC_PERMISSION);
            return;
        }
        Permissions.log("Show rationale.");
        showRationale(rationale);
    }

    private void showRationale(String rationale) {
        OnClickListener listener = new C02601();
        new Builder(this).setTitle(this.options.rationaleDialogTitle).setMessage(rationale).setPositiveButton(17039370, listener).setNegativeButton(17039360, listener).setOnCancelListener(new C02612()).create().show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 0) {
            deny();
            return;
        }
        this.deniedPermissions.clear();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != 0) {
                this.deniedPermissions.add(permissions[i]);
            }
        }
        if (this.deniedPermissions.size() == 0) {
            Permissions.log("Just allowed.");
            grant();
            return;
        }
        ArrayList<String> blockedList = new ArrayList();
        ArrayList<String> justBlockedList = new ArrayList();
        ArrayList<String> justDeniedList = new ArrayList();
        Iterator it = this.deniedPermissions.iterator();
        while (it.hasNext()) {
            String permission = (String) it.next();
            if (shouldShowRequestPermissionRationale(permission)) {
                justDeniedList.add(permission);
            } else {
                blockedList.add(permission);
                if (!this.noRationaleList.contains(permission)) {
                    justBlockedList.add(permission);
                }
            }
        }
        if (justBlockedList.size() > 0) {
            if (permissionHandler != null) {
                permissionHandler.onJustBlocked(this, justBlockedList, this.deniedPermissions);
            }
            finish();
        } else if (justDeniedList.size() > 0) {
            deny();
        } else if (permissionHandler == null || permissionHandler.onBlocked(this, blockedList)) {
            finish();
        } else {
            sendToSettings();
        }
    }

    private void sendToSettings() {
        if (this.options.sendBlockedToSettings) {
            Permissions.log("Ask to go to settings.");
            new Builder(this).setTitle(this.options.settingsDialogTitle).setMessage(this.options.settingsDialogMessage).setPositiveButton(this.options.settingsText, new C02645()).setNegativeButton(17039360, new C02634()).setOnCancelListener(new C02623()).create().show();
            return;
        }
        deny();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SETTINGS && permissionHandler != null) {
            Permissions.check((Context) this, (String[]) this.allPermissions.toArray(new String[0]), null, this.options, permissionHandler);
            this.cleanHandlerOnDestroy = false;
        }
        finish();
    }

    protected void onDestroy() {
        if (this.cleanHandlerOnDestroy) {
            permissionHandler = null;
        }
        super.onDestroy();
    }

    private void deny() {
        if (permissionHandler != null) {
            permissionHandler.onDenied(this, this.deniedPermissions);
        }
        finish();
    }

    private void grant() {
        if (permissionHandler != null) {
            permissionHandler.onGranted();
        }
        finish();
    }
}
