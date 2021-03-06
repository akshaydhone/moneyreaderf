package com.google.android.gms.internal.ads;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;

@zzadh
public final class zzpz extends Image {
    private final Drawable mDrawable;
    private final Uri mUri;
    private final double zzbhv;
    private final zzpw zzbkm;

    public zzpz(zzpw zzpw) {
        Drawable drawable;
        double d;
        Uri uri = null;
        this.zzbkm = zzpw;
        try {
            IObjectWrapper zzjy = this.zzbkm.zzjy();
            if (zzjy != null) {
                drawable = (Drawable) ObjectWrapper.unwrap(zzjy);
                this.mDrawable = drawable;
                uri = this.zzbkm.getUri();
                this.mUri = uri;
                d = 1.0d;
                d = this.zzbkm.getScale();
                this.zzbhv = d;
            }
        } catch (Throwable e) {
            zzane.zzb("", e);
        }
        Object obj = uri;
        this.mDrawable = drawable;
        try {
            uri = this.zzbkm.getUri();
        } catch (Throwable e2) {
            zzane.zzb("", e2);
        }
        this.mUri = uri;
        d = 1.0d;
        try {
            d = this.zzbkm.getScale();
        } catch (Throwable e3) {
            zzane.zzb("", e3);
        }
        this.zzbhv = d;
    }

    public final Drawable getDrawable() {
        return this.mDrawable;
    }

    public final double getScale() {
        return this.zzbhv;
    }

    public final Uri getUri() {
        return this.mUri;
    }
}
