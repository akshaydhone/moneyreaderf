package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.common.data.DataBufferSafeParcelable;

final class zzad extends zzab<Bundle> {
    zzad(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    final void zzb(Bundle bundle) {
        Object bundle2 = bundle.getBundle(DataBufferSafeParcelable.DATA_FIELD);
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        finish(bundle2);
    }

    final boolean zzw() {
        return false;
    }
}
