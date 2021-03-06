package com.google.android.gms.internal.ads;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.internal.zzbv;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzw;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Arrays;
import java.util.Collections;
import org.json.JSONObject;

@zzadh
public final class zzagr extends zzd implements zzahu {
    private static zzagr zzcle;
    private boolean zzclf;
    private final zzago zzclg = new zzago(this.zzvw, this.zzwh, this, this, this);
    private boolean zzyu;
    @VisibleForTesting
    private final zzaix zzyv;

    public zzagr(Context context, zzw zzw, zzjn zzjn, zzxn zzxn, zzang zzang) {
        super(context, zzjn, null, zzxn, zzang, zzw);
        zzcle = this;
        this.zzyv = new zzaix(context, null);
    }

    private static zzaji zzc(zzaji zzaji) {
        zzakb.m29v("Creating mediation ad response for non-mediated rewarded ad.");
        try {
            JSONObject zzb = zzafs.zzb(zzaji.zzcos);
            zzb.remove("impression_urls");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER, zzaji.zzcgs.zzacp);
            String jSONObject2 = jSONObject.toString();
            zzwx zzwx = new zzwx(zzb.toString(), null, Arrays.asList(new String[]{"com.google.ads.mediation.admob.AdMobAdapter"}), null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), jSONObject2, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null, null, null, null, Collections.emptyList(), null, -1);
            return new zzaji(zzaji.zzcgs, zzaji.zzcos, new zzwy(Arrays.asList(new zzwx[]{zzwx}), ((Long) zzkb.zzik().zzd(zznk.zzbao)).longValue(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false, "", -1, 0, 1, null, 0, -1, -1, false), zzaji.zzacv, zzaji.errorCode, zzaji.zzcoh, zzaji.zzcoi, zzaji.zzcob, zzaji.zzcoq, null);
        } catch (Throwable e) {
            zzane.zzb("Unable to generate ad state for non-mediated rewarded video.", e);
            return new zzaji(zzaji.zzcgs, zzaji.zzcos, null, zzaji.zzacv, 0, zzaji.zzcoh, zzaji.zzcoi, zzaji.zzcob, zzaji.zzcoq, null);
        }
    }

    public static zzagr zzox() {
        return zzcle;
    }

    public final void destroy() {
        this.zzclg.destroy();
        super.destroy();
    }

    public final boolean isLoaded() {
        Preconditions.checkMainThread("isLoaded must be called on the main UI thread.");
        return this.zzvw.zzact == null && this.zzvw.zzacu == null && this.zzvw.zzacw != null;
    }

    public final void onContextChanged(Context context) {
        this.zzclg.onContextChanged(context);
    }

    public final void onRewardedVideoAdClosed() {
        if (zzbv.zzfh().zzw(this.zzvw.zzrt)) {
            this.zzyv.zzx(false);
        }
        zzbn();
    }

    public final void onRewardedVideoAdLeftApplication() {
        zzbo();
    }

    public final void onRewardedVideoAdOpened() {
        if (zzbv.zzfh().zzw(this.zzvw.zzrt)) {
            this.zzyv.zzx(true);
        }
        zza(this.zzvw.zzacw, false);
        zzbp();
    }

    public final void onRewardedVideoCompleted() {
        this.zzclg.zzow();
        zzbu();
    }

    public final void onRewardedVideoStarted() {
        this.zzclg.zzov();
        zzbt();
    }

    public final void pause() {
        this.zzclg.pause();
    }

    public final void resume() {
        this.zzclg.resume();
    }

    public final void setImmersiveMode(boolean z) {
        Preconditions.checkMainThread("setImmersiveMode must be called on the main UI thread.");
        this.zzyu = z;
    }

    public final void zza(zzahk zzahk) {
        Preconditions.checkMainThread("loadAd must be called on the main UI thread.");
        if (TextUtils.isEmpty(zzahk.zzacp)) {
            zzane.zzdk("Invalid ad unit id. Aborting.");
            zzakk.zzcrm.post(new zzags(this));
            return;
        }
        this.zzclf = false;
        this.zzvw.zzacp = zzahk.zzacp;
        this.zzyv.setAdUnitId(zzahk.zzacp);
        super.zzb(zzahk.zzccv);
    }

    public final void zza(zzaji zzaji, zznx zznx) {
        if (zzaji.errorCode != -2) {
            zzakk.zzcrm.post(new zzagt(this, zzaji));
            return;
        }
        this.zzvw.zzacx = zzaji;
        if (zzaji.zzcod == null) {
            this.zzvw.zzacx = zzc(zzaji);
        }
        this.zzclg.zzou();
    }

    public final boolean zza(zzajh zzajh, zzajh zzajh2) {
        zzb(zzajh2, false);
        return zzago.zza(zzajh, zzajh2);
    }

    protected final boolean zza(zzjj zzjj, zzajh zzajh, boolean z) {
        return false;
    }

    protected final void zzbn() {
        this.zzvw.zzacw = null;
        super.zzbn();
    }

    public final void zzc(@Nullable zzaig zzaig) {
        zzaig zzd = this.zzclg.zzd(zzaig);
        if (zzbv.zzfh().zzw(this.zzvw.zzrt) && zzd != null) {
            zzbv.zzfh().zza(this.zzvw.zzrt, zzbv.zzfh().zzab(this.zzvw.zzrt), this.zzvw.zzacp, zzd.type, zzd.zzcmk);
        }
        zza(zzd);
    }

    @Nullable
    public final zzaib zzca(String str) {
        return this.zzclg.zzca(str);
    }

    public final void zzdm() {
        onAdClicked();
    }

    public final void zzoy() {
        Preconditions.checkMainThread("showAd must be called on the main UI thread.");
        if (isLoaded()) {
            this.zzclg.zzw(this.zzyu);
        } else {
            zzane.zzdk("The reward video has not loaded.");
        }
    }
}
