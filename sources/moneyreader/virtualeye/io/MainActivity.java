package moneyreader.virtualeye.io;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.CameraDevice.FocusMode;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackerManager.TrackingOption;
import com.maxst.ar.TrackingResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.onesignal.OneSignal;
import com.onesignal.OneSignal.OSInFocusDisplayOption;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import moneyreader.virtualeye.io.util.BackgroundRenderHelper;

public class MainActivity extends AppCompatActivity {
    public static HashMap<String, Country> currencyAbberivation = new C03091();
    private String country;
    private String currentNote = "a";
    boolean flashLight = false;
    private GLSurfaceView glSurfaceView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private int preferCameraResolution = 0;
    private String prevNote = "b";
    private TextView textType;
    private TextView textView;
    private TextToSpeech tts;

    /* renamed from: moneyreader.virtualeye.io.MainActivity$1 */
    static class C03091 extends HashMap<String, Country> {
        C03091() {
            put("Pakistan", new Country("pkr", "Pakistani Rupees"));
            put("India", new Country("inr", "Indian Rupees"));
            put("Romania", new Country("lei", "Romanian Leu"));
            put("Nepal", new Country("npr", "Nepalese Rupees"));
            put("USA", new Country("usd", "USA Dollar"));
            put("Indonesia", new Country("idr", "Indonesian rupiah"));
            put("Hong Kong", new Country("hkd", "Hong Kong Dollar"));
            put("Saudi Arab", new Country("sar", "Saudi Arabia Riyal"));
            put("Jamaica", new Country("jmd", "Jamaican Dollar"));
            put("Sweden", new Country("sek", "Swedish Krona"));
            put("Bangladesh", new Country("bdt", "Bangladeshi Taka"));
            put("Brazil", new Country("brl", "Brazilian Real"));
            put("Sri Lanka", new Country("lkr", "Sri Lankan Rupee"));
            put("Australia", new Country("aud", "Australian Dollar"));
            put("Malaysia", new Country("myr", "Malaysian Ringgit"));
            put("Canada", new Country("cad", "Canadian Dollar"));
            put("England", new Country("gbp", "British Pound"));
            put("Europe", new Country("eur", "Euro"));
            put("Bahamas", new Country("bsd", "Bahamian dollar"));
        }
    }

    /* renamed from: moneyreader.virtualeye.io.MainActivity$3 */
    class C03103 implements OnInitListener {
        C03103() {
        }

        public void onInit(int status) {
            if (status == 0) {
                MainActivity.this.tts.setLanguage(Locale.ENGLISH);
                MainActivity.this.speakOut("Scanning " + ((Country) MainActivity.currencyAbberivation.get(MainActivity.this.country)).currency);
                return;
            }
            Log.e("TTS", "Initilization Failed!");
        }
    }

    /* renamed from: moneyreader.virtualeye.io.MainActivity$4 */
    class C03124 implements Renderer {
        private BackgroundRenderHelper backgroundRenderHelper;
        private int surfaceHeight;
        private int surfaceWidth;

        C03124() {
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            this.backgroundRenderHelper = new BackgroundRenderHelper();
            this.backgroundRenderHelper.init();
            MaxstAR.onSurfaceCreated();
        }

        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            this.surfaceWidth = width;
            this.surfaceHeight = height;
            MaxstAR.onSurfaceChanged(width, height);
        }

        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(16640);
            GLES20.glViewport(0, 0, this.surfaceWidth, this.surfaceHeight);
            this.backgroundRenderHelper.drawBackground();
            TrackingResult trackingResult = TrackerManager.getInstance().updateTrackingState().getTrackingResult();
            Log.d("ImageTracker", " count : " + trackingResult.getCount());
            if (trackingResult.getCount() > 0) {
                for (int i = 0; i < trackingResult.getCount(); i++) {
                    Trackable trackable = trackingResult.getTrackable(i);
                    Log.d("ImageTracker", trackable.getName());
                    MainActivity.this.currentNote = trackable.getName();
                    if (!MainActivity.this.prevNote.equals(MainActivity.this.currentNote)) {
                        String[] parts = trackable.getName().split("-");
                        final String note = parts[0];
                        final String type = ((Country) MainActivity.currencyAbberivation.get(MainActivity.this.country)).currency;
                        String result = note + " " + type + ", " + parts[2];
                        MainActivity.this.speakOut(result);
                        MainActivity.this.textView.post(new Runnable() {
                            public void run() {
                                MainActivity.this.textView.setText(note);
                                MainActivity.this.textType.setText(type);
                            }
                        });
                        Bundle params = new Bundle();
                        params.putString(Param.CURRENCY, result);
                        MainActivity.this.mFirebaseAnalytics.logEvent("scanned_note", params);
                    }
                    MainActivity.this.prevNote = MainActivity.this.currentNote;
                }
                return;
            }
            MainActivity.this.prevNote = "";
        }
    }

    public static class Country {
        public String currency;
        public String name;

        public Country(String name, String currency) {
            this.name = name;
            this.currency = currency;
        }
    }

    /* renamed from: moneyreader.virtualeye.io.MainActivity$2 */
    class C03862 extends PermissionHandler {
        C03862() {
        }

        public void onGranted() {
            MainActivity.this.setContentView((int) C0313R.layout.activity_main);
            MainActivity.this.initApplication();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setImportantForAccessibility(getWindow().getDecorView(), 2);
        if (getSharedPreferences("setting", 0).getInt("first_run_", 0) == 0) {
            showSelectCountry();
        } else {
            Permissions.check((Context) this, "android.permission.CAMERA", null, new C03862());
        }
    }

    void initApplication() {
        MaxstAR.init(getApplicationContext(), "wr7ZB1jjNC7hFd2U3271aYB6zzy7PzBGjlT/oCbjzco=");
        MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);
        OneSignal.startInit(this).inFocusDisplaying(OSInFocusDisplayOption.Notification).unsubscribeWhenNotificationsAreDisabled(true).init();
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        this.country = getSharedPreferences("setting", 0).getString("country_", "Pakistan");
        this.tts = new TextToSpeech(this, new C03103());
        this.textView = (TextView) findViewById(C0313R.id.result_label);
        this.textType = (TextView) findViewById(C0313R.id.result_label2);
        this.glSurfaceView = (GLSurfaceView) findViewById(C0313R.id.gl_surface_view);
        this.glSurfaceView.setEGLContextClientVersion(2);
        this.glSurfaceView.setRenderer(new C03124());
        String key = ((Country) currencyAbberivation.get(this.country)).name;
        for (File directory : getCacheDir().listFiles()) {
            Log.d("File", directory.getName());
            if (directory.getName().contains(key)) {
                for (File file : directory.listFiles()) {
                    if (file.getName().endsWith(".2dmap")) {
                        Log.d("File", file.getAbsolutePath());
                        TrackerManager.getInstance().addTrackerData(file.getAbsolutePath(), false);
                    }
                }
            }
        }
        TrackerManager.getInstance().loadTrackerData();
        this.preferCameraResolution = getSharedPreferences("pref", 0).getInt("cam_resolution", 1);
    }

    protected void onResume() {
        super.onResume();
        if (this.glSurfaceView != null) {
            this.glSurfaceView.onResume();
            TrackerManager.getInstance().setTrackingOption(TrackingOption.NORMAL_TRACKING);
            TrackerManager.getInstance().startTracker(2);
            CameraDevice.getInstance().setFocusMode(FocusMode.FOCUS_MODE_CONTINUOUS_AUTO);
            ResultCode resultCode = ResultCode.Success;
            switch (this.preferCameraResolution) {
                case 0:
                    resultCode = CameraDevice.getInstance().start(0, 640, 480);
                    break;
                case 1:
                    resultCode = CameraDevice.getInstance().start(0, 1280, 720);
                    break;
            }
            this.flashLight = false;
            if (resultCode != ResultCode.Success) {
                Toast.makeText(this, "Camera Open Failed", 0).show();
                finish();
            }
            MaxstAR.onResume();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.glSurfaceView != null) {
            this.glSurfaceView.onPause();
            TrackerManager.getInstance().stopTracker();
            CameraDevice.getInstance().stop();
            MaxstAR.onPause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
        TrackerManager.getInstance().destroyTracker();
        MaxstAR.deinit();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == 2) {
            Toast.makeText(this, "landscape", 0).show();
        } else if (newConfig.orientation == 1) {
            Toast.makeText(this, "portrait", 0).show();
        }
        MaxstAR.setScreenOrientation(newConfig.orientation);
    }

    private void speakOut(String text) {
        this.tts.speak(text, 1, null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0313R.menu.menu, menu);
        menu.getItem(1).setTitle(this.country + " is selected");
        menu.getItem(0).setTitle("Flash light " + (this.flashLight ? "On" : "Off"));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0313R.id.flash_light_btn) {
            CameraDevice cameraDevice = CameraDevice.getInstance();
            Bundle params;
            if (this.flashLight) {
                cameraDevice.setFlashLightMode(false);
                this.flashLight = false;
                speakOut("flash light off");
                params = new Bundle();
                params.putString("state", "off");
                this.mFirebaseAnalytics.logEvent("flash_light", params);
            } else {
                cameraDevice.setFlashLightMode(true);
                this.flashLight = true;
                speakOut("flash light on");
                params = new Bundle();
                params.putString("state", "on");
                this.mFirebaseAnalytics.logEvent("flash_light", params);
            }
        } else if (id == C0313R.id.select_country) {
            showSelectCountry();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelectCountry() {
        Intent intent = new Intent(this, CoutrySelectActivity.class);
        intent.addFlags(67108864);
        Set<String> countries = new HashSet();
        for (Entry<String, Country> entry : currencyAbberivation.entrySet()) {
            countries.add(entry.getKey());
        }
        intent.putExtra("countries", (String[]) countries.toArray(new String[countries.size()]));
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, 1).show();
    }
}
