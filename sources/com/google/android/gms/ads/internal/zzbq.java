package com.google.android.gms.ads.internal;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.internal.ads.zzane;
import com.google.android.gms.internal.ads.zzkb;
import com.google.android.gms.internal.ads.zznk;

final class zzbq extends WebViewClient {
    private final /* synthetic */ zzbp zzaba;

    zzbq(zzbp zzbp) {
        this.zzaba = zzbp;
    }

    public final void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        if (this.zzaba.zzxs != null) {
            try {
                this.zzaba.zzxs.onAdFailedToLoad(0);
            } catch (Throwable e) {
                zzane.zzd("#007 Could not call remote method.", e);
            }
        }
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str.startsWith(this.zzaba.zzeb())) {
            return false;
        }
        if (str.startsWith((String) zzkb.zzik().zzd(zznk.zzbcw))) {
            if (this.zzaba.zzxs != null) {
                try {
                    this.zzaba.zzxs.onAdFailedToLoad(3);
                } catch (Throwable e) {
                    zzane.zzd("#007 Could not call remote method.", e);
                }
            }
            this.zzaba.zzk(0);
            return true;
        }
        if (str.startsWith((String) zzkb.zzik().zzd(zznk.zzbcx))) {
            if (this.zzaba.zzxs != null) {
                try {
                    this.zzaba.zzxs.onAdFailedToLoad(0);
                } catch (Throwable e2) {
                    zzane.zzd("#007 Could not call remote method.", e2);
                }
            }
            this.zzaba.zzk(0);
            return true;
        }
        if (str.startsWith((String) zzkb.zzik().zzd(zznk.zzbcy))) {
            if (this.zzaba.zzxs != null) {
                try {
                    this.zzaba.zzxs.onAdLoaded();
                } catch (Throwable e22) {
                    zzane.zzd("#007 Could not call remote method.", e22);
                }
            }
            this.zzaba.zzk(this.zzaba.zzu(str));
            return true;
        } else if (str.startsWith("gmsg://")) {
            return true;
        } else {
            if (this.zzaba.zzxs != null) {
                try {
                    this.zzaba.zzxs.onAdLeftApplication();
                } catch (Throwable e222) {
                    zzane.zzd("#007 Could not call remote method.", e222);
                }
            }
            this.zzaba.zzw(this.zzaba.zzv(str));
            return true;
        }
    }
}
