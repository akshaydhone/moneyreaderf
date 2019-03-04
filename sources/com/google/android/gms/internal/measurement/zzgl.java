package com.google.android.gms.internal.measurement;

import android.app.Application;
import android.content.Context;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.atomic.AtomicReference;

public class zzgl extends zzjr implements zzhi {
    private static volatile zzgl zzame;
    private final long zzaem;
    private final zzef zzamf;
    private final zzfr zzamg;
    private final zzfg zzamh;
    private final zzgg zzami;
    private final zzjh zzamj;
    private final AppMeasurement zzamk;
    private final FirebaseAnalytics zzaml;
    private final zzka zzamm;
    private final zzfe zzamn;
    private final zzif zzamo;
    private final zzhk zzamp;
    private final zzdu zzamq;
    private zzfc zzamr;
    private zzii zzams;
    private zzeo zzamt;
    private zzfb zzamu;
    private zzfx zzamv;
    private Boolean zzamw;
    private long zzamx;
    private int zzamy;
    private int zzamz;
    private final Context zzqx;
    private final Clock zzro;
    private boolean zzvo = false;

    private zzgl(zzhj zzhj) {
        Preconditions.checkNotNull(zzhj);
        zza(this);
        this.zzqx = zzhj.zzqx;
        zzws.init(this.zzqx);
        this.zzaqs = -1;
        this.zzro = DefaultClock.getInstance();
        this.zzaem = this.zzro.currentTimeMillis();
        this.zzamf = new zzef(this);
        zzhh zzfr = new zzfr(this);
        zzfr.zzm();
        this.zzamg = zzfr;
        zzfr = new zzfg(this);
        zzfr.zzm();
        this.zzamh = zzfr;
        zzfr = new zzka(this);
        zzfr.zzm();
        this.zzamm = zzfr;
        zzfr = new zzfe(this);
        zzfr.zzm();
        this.zzamn = zzfr;
        this.zzamq = new zzdu(this);
        zzfr = new zzif(this);
        zzfr.zzm();
        this.zzamo = zzfr;
        zzfr = new zzhk(this);
        zzfr.zzm();
        this.zzamp = zzfr;
        this.zzamk = new AppMeasurement(this);
        this.zzaml = new FirebaseAnalytics(this);
        zzfr = new zzjh(this);
        zzfr.zzm();
        this.zzamj = zzfr;
        zzfr = new zzgg(this);
        zzfr.zzm();
        this.zzami = zzfr;
        if (this.zzqx.getApplicationContext() instanceof Application) {
            zzhg zzfu = zzfu();
            if (zzfu.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzfu.getContext().getApplicationContext();
                if (zzfu.zzanp == null) {
                    zzfu.zzanp = new zzid(zzfu);
                }
                application.unregisterActivityLifecycleCallbacks(zzfu.zzanp);
                application.registerActivityLifecycleCallbacks(zzfu.zzanp);
                zzfu.zzge().zzit().log("Registered activity lifecycle callback");
            }
        } else {
            zzge().zzip().log("Application context is not an Application");
        }
        zzjq zzfk = new zzfk(this);
        zzfk.zzm();
        this.zzaqb = zzfk;
        zzfk = new zzgf(this);
        zzfk.zzm();
        this.zzaqa = zzfk;
        this.zzami.zzc(new zzgm(this, zzhj));
    }

    private static void zza(zzhg zzhg) {
        if (zzhg == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzhh zzhh) {
        if (zzhh == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzhh.isInitialized()) {
            String valueOf = String.valueOf(zzhh.getClass());
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 27).append("Component not initialized: ").append(valueOf).toString());
        }
    }

    private final void zzch() {
        if (!this.zzvo) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    static void zzfr() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    public static zzgl zzg(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzame == null) {
            synchronized (zzgl.class) {
                if (zzame == null) {
                    zzame = new zzgl(new zzhj(context));
                }
            }
        }
        return zzame;
    }

    public final Context getContext() {
        return this.zzqx;
    }

    @WorkerThread
    public final boolean isEnabled() {
        boolean z = false;
        zzab();
        zzch();
        if (zzgg().zzhg()) {
            return false;
        }
        Boolean zzas = zzgg().zzas("firebase_analytics_collection_enabled");
        if (zzas != null) {
            z = zzas.booleanValue();
        } else if (!GoogleServices.isMeasurementExplicitlyDisabled()) {
            z = true;
        }
        return zzgf().zzg(z);
    }

    @WorkerThread
    protected final void start() {
        boolean z = false;
        zzab();
        if (zzgf().zzaju.get() == 0) {
            zzgf().zzaju.set(zzbt().currentTimeMillis());
        }
        if (Long.valueOf(zzgf().zzajz.get()).longValue() == 0) {
            zzge().zzit().zzg("Persisting first open", Long.valueOf(this.zzaem));
            zzgf().zzajz.set(this.zzaem);
        }
        if (zzjv()) {
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                String zziz = zzgf().zziz();
                if (zziz == null) {
                    zzgf().zzbq(zzfv().getGmpAppId());
                } else if (!zziz.equals(zzfv().getGmpAppId())) {
                    zzge().zzir().log("Rechecking which service to use due to a GMP App Id change");
                    zzgf().zzjc();
                    this.zzams.disconnect();
                    this.zzams.zzdf();
                    zzgf().zzbq(zzfv().getGmpAppId());
                    zzgf().zzajz.set(this.zzaem);
                    zzgf().zzakb.zzbs(null);
                }
            }
            zzfu().zzbr(zzgf().zzakb.zzjg());
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                boolean isEnabled = isEnabled();
                if (!(zzgf().zzjf() || zzgg().zzhg())) {
                    zzfr zzgf = zzgf();
                    if (!isEnabled) {
                        z = true;
                    }
                    zzgf.zzh(z);
                }
                if (!zzgg().zzaz(zzfv().zzah()) || isEnabled) {
                    zzfu().zzkb();
                }
                zzfx().zza(new AtomicReference());
            }
        } else if (isEnabled()) {
            if (!zzgb().zzx("android.permission.INTERNET")) {
                zzge().zzim().log("App is missing INTERNET permission");
            }
            if (!zzgb().zzx("android.permission.ACCESS_NETWORK_STATE")) {
                zzge().zzim().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!Wrappers.packageManager(getContext()).isCallerInstantApp()) {
                if (!zzgb.zza(getContext())) {
                    zzge().zzim().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzjc.zza(getContext(), false)) {
                    zzge().zzim().log("AppMeasurementService not registered/enabled");
                }
            }
            zzge().zzim().log("Uploading is not possible. App measurement disabled");
        }
        super.start();
    }

    @WorkerThread
    final void zza(zzhj zzhj) {
        zzfi zzir;
        zzab();
        zzhh zzeo = new zzeo(this);
        zzeo.zzm();
        this.zzamt = zzeo;
        zzeo = new zzfb(this);
        zzeo.zzm();
        this.zzamu = zzeo;
        zzhh zzfc = new zzfc(this);
        zzfc.zzm();
        this.zzamr = zzfc;
        zzfc = new zzii(this);
        zzfc.zzm();
        this.zzams = zzfc;
        this.zzamm.zzjw();
        this.zzamg.zzjw();
        this.zzamv = new zzfx(this);
        this.zzamu.zzjw();
        zzge().zzir().zzg("App measurement is starting up, version", Long.valueOf(12451));
        zzge().zzir().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String zzah = zzeo.zzah();
        if (zzgb().zzcj(zzah)) {
            zzir = zzge().zzir();
            zzah = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzir = zzge().zzir();
            String str = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ";
            zzah = String.valueOf(zzah);
            zzah = zzah.length() != 0 ? str.concat(zzah) : new String(str);
        }
        zzir.log(zzah);
        zzge().zzis().log("Debug-level message logging enabled");
        if (this.zzamy != this.zzamz) {
            zzge().zzim().zze("Not all components initialized", Integer.valueOf(this.zzamy), Integer.valueOf(this.zzamz));
        }
        super.zza((zzjw) zzhj);
        this.zzvo = true;
    }

    @WorkerThread
    public final void zzab() {
        zzgd().zzab();
    }

    final void zzb(zzhh zzhh) {
        this.zzamy++;
    }

    public final Clock zzbt() {
        return this.zzro;
    }

    public final zzdu zzft() {
        zza(this.zzamq);
        return this.zzamq;
    }

    public final zzhk zzfu() {
        zza(this.zzamp);
        return this.zzamp;
    }

    public final zzfb zzfv() {
        zza(this.zzamu);
        return this.zzamu;
    }

    public final zzeo zzfw() {
        zza(this.zzamt);
        return this.zzamt;
    }

    public final zzii zzfx() {
        zza(this.zzams);
        return this.zzams;
    }

    public final zzif zzfy() {
        zza(this.zzamo);
        return this.zzamo;
    }

    public final zzfc zzfz() {
        zza(this.zzamr);
        return this.zzamr;
    }

    public final zzfe zzga() {
        zza(this.zzamn);
        return this.zzamn;
    }

    public final zzka zzgb() {
        zza(this.zzamm);
        return this.zzamm;
    }

    public final zzjh zzgc() {
        zza(this.zzamj);
        return this.zzamj;
    }

    public final zzgg zzgd() {
        zza(this.zzami);
        return this.zzami;
    }

    public final zzfg zzge() {
        zza(this.zzamh);
        return this.zzamh;
    }

    public final zzfr zzgf() {
        zza(this.zzamg);
        return this.zzamg;
    }

    public final zzef zzgg() {
        return this.zzamf;
    }

    public final zzfg zzjo() {
        return (this.zzamh == null || !this.zzamh.isInitialized()) ? null : this.zzamh;
    }

    public final zzfx zzjp() {
        return this.zzamv;
    }

    final zzgg zzjq() {
        return this.zzami;
    }

    public final AppMeasurement zzjr() {
        return this.zzamk;
    }

    public final FirebaseAnalytics zzjs() {
        return this.zzaml;
    }

    final long zzjt() {
        Long valueOf = Long.valueOf(zzgf().zzajz.get());
        return valueOf.longValue() == 0 ? this.zzaem : Math.min(this.zzaem, valueOf.longValue());
    }

    final void zzju() {
        this.zzamz++;
    }

    @WorkerThread
    protected final boolean zzjv() {
        boolean z = false;
        zzch();
        zzab();
        if (this.zzamw == null || this.zzamx == 0 || !(this.zzamw == null || this.zzamw.booleanValue() || Math.abs(zzbt().elapsedRealtime() - this.zzamx) <= 1000)) {
            this.zzamx = zzbt().elapsedRealtime();
            if (zzgb().zzx("android.permission.INTERNET") && zzgb().zzx("android.permission.ACCESS_NETWORK_STATE") && (Wrappers.packageManager(getContext()).isCallerInstantApp() || (zzgb.zza(getContext()) && zzjc.zza(getContext(), false)))) {
                z = true;
            }
            this.zzamw = Boolean.valueOf(z);
            if (this.zzamw.booleanValue()) {
                this.zzamw = Boolean.valueOf(zzgb().zzcg(zzfv().getGmpAppId()));
            }
        }
        return this.zzamw.booleanValue();
    }
}
