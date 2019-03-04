package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.ParseException;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement.Param;
import java.util.ArrayList;
import java.util.List;

public final class zzfc extends zzhh {
    private final zzfd zzaig = new zzfd(this, getContext(), "google_app_measurement_local.db");
    private boolean zzaih;

    zzfc(zzgl zzgl) {
        super(zzgl);
    }

    @WorkerThread
    @VisibleForTesting
    private final SQLiteDatabase getWritableDatabase() throws SQLiteException {
        if (this.zzaih) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzaig.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzaih = true;
        return null;
    }

    @WorkerThread
    private final boolean zza(int i, byte[] bArr) {
        zzab();
        if (this.zzaih) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Param.TYPE, Integer.valueOf(i));
        contentValues.put("entry", bArr);
        int i2 = 0;
        int i3 = 5;
        while (i2 < 5) {
            SQLiteDatabase sQLiteDatabase = null;
            Cursor cursor = null;
            try {
                sQLiteDatabase = getWritableDatabase();
                if (sQLiteDatabase == null) {
                    this.zzaih = true;
                    if (sQLiteDatabase != null) {
                        sQLiteDatabase.close();
                    }
                    return false;
                }
                sQLiteDatabase.beginTransaction();
                long j = 0;
                cursor = sQLiteDatabase.rawQuery("select count(1) from messages", null);
                if (cursor != null && cursor.moveToFirst()) {
                    j = cursor.getLong(0);
                }
                if (j >= 100000) {
                    zzge().zzim().log("Data loss, local db full");
                    j = (100000 - j) + 1;
                    long delete = (long) sQLiteDatabase.delete("messages", "rowid in (select rowid from messages order by rowid asc limit ?)", new String[]{Long.toString(j)});
                    if (delete != j) {
                        zzge().zzim().zzd("Different delete count than expected in local db. expected, received, difference", Long.valueOf(j), Long.valueOf(delete), Long.valueOf(j - delete));
                    }
                }
                sQLiteDatabase.insertOrThrow("messages", null, contentValues);
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                return true;
            } catch (SQLiteFullException e) {
                zzge().zzim().zzg("Error writing entry to local database", e);
                this.zzaih = true;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i2++;
            } catch (SQLiteDatabaseLockedException e2) {
                SystemClock.sleep((long) i3);
                i3 += 20;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i2++;
            } catch (SQLiteException e3) {
                if (sQLiteDatabase != null) {
                    if (sQLiteDatabase.inTransaction()) {
                        sQLiteDatabase.endTransaction();
                    }
                }
                zzge().zzim().zzg("Error writing entry to local database", e3);
                this.zzaih = true;
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
                i2++;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                }
            }
        }
        zzge().zzip().log("Failed to write entry to local database");
        return false;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final void resetAnalyticsData() {
        zzab();
        try {
            int delete = getWritableDatabase().delete("messages", null, null) + 0;
            if (delete > 0) {
                zzge().zzit().zzg("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zzg("Error resetting local analytics data. error", e);
        }
    }

    public final boolean zza(zzeu zzeu) {
        Parcel obtain = Parcel.obtain();
        zzeu.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(0, marshall);
        }
        zzge().zzip().log("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzjx zzjx) {
        Parcel obtain = Parcel.obtain();
        zzjx.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zza(1, marshall);
        }
        zzge().zzip().log("User property too long for local database. Sending directly to service");
        return false;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final boolean zzc(zzed zzed) {
        zzgb();
        byte[] zza = zzka.zza((Parcelable) zzed);
        if (zza.length <= 131072) {
            return zza(2, zza);
        }
        zzge().zzip().log("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }

    public final List<AbstractSafeParcelable> zzp(int i) {
        SQLiteDatabase sQLiteDatabase;
        Cursor cursor;
        Cursor cursor2;
        int i2;
        Throwable th;
        SQLiteException sQLiteException;
        Object obj;
        Throwable th2;
        Parcel obtain;
        zzab();
        if (this.zzaih) {
            return null;
        }
        List<AbstractSafeParcelable> arrayList = new ArrayList();
        if (!getContext().getDatabasePath("google_app_measurement_local.db").exists()) {
            return arrayList;
        }
        int i3 = 5;
        int i4 = 0;
        while (i4 < 5) {
            SQLiteDatabase sQLiteDatabase2 = null;
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                if (writableDatabase == null) {
                    try {
                        this.zzaih = true;
                        if (writableDatabase != null) {
                            writableDatabase.close();
                        }
                        return null;
                    } catch (SQLiteFullException e) {
                        sQLiteDatabase = writableDatabase;
                        SQLiteFullException sQLiteFullException = e;
                        cursor = null;
                    } catch (SQLiteDatabaseLockedException e2) {
                        cursor2 = null;
                        sQLiteDatabase2 = writableDatabase;
                        try {
                            SystemClock.sleep((long) i3);
                            i2 = i3 + 20;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            if (sQLiteDatabase2 != null) {
                                sQLiteDatabase2.close();
                            }
                            i4++;
                            i3 = i2;
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (SQLiteException e3) {
                        cursor2 = null;
                        sQLiteException = e3;
                        sQLiteDatabase2 = writableDatabase;
                        SQLiteException sQLiteException2 = sQLiteException;
                        if (sQLiteDatabase2 != null) {
                            if (sQLiteDatabase2.inTransaction()) {
                                sQLiteDatabase2.endTransaction();
                            }
                        }
                        zzge().zzim().zzg("Error reading entries from local database", obj);
                        this.zzaih = true;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        if (sQLiteDatabase2 != null) {
                            sQLiteDatabase2.close();
                            i2 = i3;
                            i4++;
                            i3 = i2;
                        }
                        i2 = i3;
                        i4++;
                        i3 = i2;
                    } catch (Throwable th4) {
                        cursor2 = null;
                        th2 = th4;
                        sQLiteDatabase2 = writableDatabase;
                        th = th2;
                    }
                } else {
                    writableDatabase.beginTransaction();
                    cursor2 = writableDatabase.query("messages", new String[]{"rowid", Param.TYPE, "entry"}, null, null, null, null, "rowid asc", Integer.toString(100));
                    long j = -1;
                    while (cursor2.moveToNext()) {
                        try {
                            j = cursor2.getLong(0);
                            int i5 = cursor2.getInt(1);
                            byte[] blob = cursor2.getBlob(2);
                            if (i5 == 0) {
                                Parcel obtain2 = Parcel.obtain();
                                try {
                                    obtain2.unmarshall(blob, 0, blob.length);
                                    obtain2.setDataPosition(0);
                                    zzeu zzeu = (zzeu) zzeu.CREATOR.createFromParcel(obtain2);
                                    obtain2.recycle();
                                    if (zzeu != null) {
                                        arrayList.add(zzeu);
                                    }
                                } catch (ParseException e4) {
                                    zzge().zzim().log("Failed to load event from local database");
                                    obtain2.recycle();
                                } catch (Throwable th42) {
                                    obtain2.recycle();
                                    throw th42;
                                }
                            } else if (i5 == 1) {
                                obtain = Parcel.obtain();
                                try {
                                    obtain.unmarshall(blob, 0, blob.length);
                                    obtain.setDataPosition(0);
                                    r1 = (zzjx) zzjx.CREATOR.createFromParcel(obtain);
                                    obtain.recycle();
                                } catch (ParseException e5) {
                                    zzge().zzim().log("Failed to load user property from local database");
                                    obtain.recycle();
                                    r1 = null;
                                } catch (Throwable th422) {
                                    obtain.recycle();
                                    throw th422;
                                }
                                if (r1 != null) {
                                    arrayList.add(r1);
                                }
                            } else if (i5 == 2) {
                                obtain = Parcel.obtain();
                                try {
                                    obtain.unmarshall(blob, 0, blob.length);
                                    obtain.setDataPosition(0);
                                    r1 = (zzed) zzed.CREATOR.createFromParcel(obtain);
                                    obtain.recycle();
                                } catch (ParseException e6) {
                                    zzge().zzim().log("Failed to load user property from local database");
                                    obtain.recycle();
                                    r1 = null;
                                } catch (Throwable th4222) {
                                    obtain.recycle();
                                    throw th4222;
                                }
                                if (r1 != null) {
                                    arrayList.add(r1);
                                }
                            } else {
                                zzge().zzim().log("Unknown record type in local database");
                            }
                        } catch (SQLiteFullException e7) {
                            SQLiteFullException sQLiteFullException2 = e7;
                            cursor = cursor2;
                            sQLiteDatabase = writableDatabase;
                            obj = sQLiteFullException2;
                        } catch (SQLiteDatabaseLockedException e8) {
                            sQLiteDatabase2 = writableDatabase;
                        } catch (SQLiteException e32) {
                            sQLiteException = e32;
                            sQLiteDatabase2 = writableDatabase;
                            obj = sQLiteException;
                        } catch (Throwable th42222) {
                            th2 = th42222;
                            sQLiteDatabase2 = writableDatabase;
                            th = th2;
                        }
                    }
                    if (writableDatabase.delete("messages", "rowid <= ?", new String[]{Long.toString(j)}) < arrayList.size()) {
                        zzge().zzim().log("Fewer entries removed from local database than expected");
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    if (writableDatabase != null) {
                        writableDatabase.close();
                    }
                    return arrayList;
                }
            } catch (SQLiteFullException e9) {
                obj = e9;
                sQLiteDatabase = null;
                cursor = null;
                try {
                    zzge().zzim().zzg("Error reading entries from local database", obj);
                    this.zzaih = true;
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (sQLiteDatabase != null) {
                        sQLiteDatabase.close();
                        i2 = i3;
                        i4++;
                        i3 = i2;
                    }
                    i2 = i3;
                    i4++;
                    i3 = i2;
                } catch (Throwable th5) {
                    th = th5;
                    Cursor cursor3 = cursor;
                    sQLiteDatabase2 = sQLiteDatabase;
                    cursor2 = cursor3;
                }
            } catch (SQLiteDatabaseLockedException e10) {
                cursor2 = null;
                SystemClock.sleep((long) i3);
                i2 = i3 + 20;
                if (cursor2 != null) {
                    cursor2.close();
                }
                if (sQLiteDatabase2 != null) {
                    sQLiteDatabase2.close();
                }
                i4++;
                i3 = i2;
            } catch (SQLiteException e11) {
                obj = e11;
                cursor2 = null;
                if (sQLiteDatabase2 != null) {
                    if (sQLiteDatabase2.inTransaction()) {
                        sQLiteDatabase2.endTransaction();
                    }
                }
                zzge().zzim().zzg("Error reading entries from local database", obj);
                this.zzaih = true;
                if (cursor2 != null) {
                    cursor2.close();
                }
                if (sQLiteDatabase2 != null) {
                    sQLiteDatabase2.close();
                    i2 = i3;
                    i4++;
                    i3 = i2;
                }
                i2 = i3;
                i4++;
                i3 = i2;
            } catch (Throwable th6) {
                th = th6;
                cursor2 = null;
            }
        }
        zzge().zzip().log("Failed to read events from database in reasonable time");
        return null;
        if (cursor2 != null) {
            cursor2.close();
        }
        if (sQLiteDatabase2 != null) {
            sQLiteDatabase2.close();
        }
        throw th;
    }
}
