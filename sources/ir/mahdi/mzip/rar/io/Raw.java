package ir.mahdi.mzip.rar.io;

import android.support.v4.view.MotionEventCompat;

public class Raw {
    public static final short readShortBigEndian(byte[] array, int pos) {
        return (short) ((array[pos + 1] & 255) | ((short) (((short) ((array[pos] & 255) | (short) 0)) << 8)));
    }

    public static final int readIntBigEndian(byte[] array, int pos) {
        return ((((((0 | (array[pos] & 255)) << 8) | (array[pos + 1] & 255)) << 8) | (array[pos + 2] & 255)) << 8) | (array[pos + 3] & 255);
    }

    public static final long readLongBigEndian(byte[] array, int pos) {
        return (long) (((((((((((((((0 | (array[pos] & 255)) << 8) | (array[pos + 1] & 255)) << 8) | (array[pos + 2] & 255)) << 8) | (array[pos + 3] & 255)) << 8) | (array[pos + 4] & 255)) << 8) | (array[pos + 5] & 255)) << 8) | (array[pos + 6] & 255)) << 8) | (array[pos + 7] & 255));
    }

    public static final short readShortLittleEndian(byte[] array, int pos) {
        return (short) ((array[pos] & 255) + ((short) (((short) ((array[pos + 1] & 255) + (short) 0)) << 8)));
    }

    public static final int readIntLittleEndian(byte[] array, int pos) {
        return ((((array[pos + 3] & 255) << 24) | ((array[pos + 2] & 255) << 16)) | ((array[pos + 1] & 255) << 8)) | (array[pos] & 255);
    }

    public static final long readIntLittleEndianAsLong(byte[] array, int pos) {
        return ((((((long) array[pos + 3]) & 255) << 24) | ((((long) array[pos + 2]) & 255) << 16)) | ((((long) array[pos + 1]) & 255) << 8)) | (((long) array[pos]) & 255);
    }

    public static final long readLongLittleEndian(byte[] array, int pos) {
        return (long) (((((((((((((((0 | (array[pos + 7] & 255)) << 8) | (array[pos + 6] & 255)) << 8) | (array[pos + 5] & 255)) << 8) | (array[pos + 4] & 255)) << 8) | (array[pos + 3] & 255)) << 8) | (array[pos + 2] & 255)) << 8) | (array[pos + 1] & 255)) << 8) | array[pos]);
    }

    public static final void writeShortBigEndian(byte[] array, int pos, short value) {
        array[pos] = (byte) (value >>> 8);
        array[pos + 1] = (byte) (value & 255);
    }

    public static final void writeIntBigEndian(byte[] array, int pos, int value) {
        array[pos] = (byte) ((value >>> 24) & 255);
        array[pos + 1] = (byte) ((value >>> 16) & 255);
        array[pos + 2] = (byte) ((value >>> 8) & 255);
        array[pos + 3] = (byte) (value & 255);
    }

    public static final void writeLongBigEndian(byte[] array, int pos, long value) {
        array[pos] = (byte) ((int) (value >>> 56));
        array[pos + 1] = (byte) ((int) (value >>> 48));
        array[pos + 2] = (byte) ((int) (value >>> 40));
        array[pos + 3] = (byte) ((int) (value >>> 32));
        array[pos + 4] = (byte) ((int) (value >>> 24));
        array[pos + 5] = (byte) ((int) (value >>> 16));
        array[pos + 6] = (byte) ((int) (value >>> 8));
        array[pos + 7] = (byte) ((int) (255 & value));
    }

    public static final void writeShortLittleEndian(byte[] array, int pos, short value) {
        array[pos + 1] = (byte) (value >>> 8);
        array[pos] = (byte) (value & 255);
    }

    public static final void incShortLittleEndian(byte[] array, int pos, int dv) {
        int c = ((array[pos] & 255) + (dv & 255)) >>> 8;
        array[pos] = (byte) (array[pos] + (dv & 255));
        if (c > 0 || (MotionEventCompat.ACTION_POINTER_INDEX_MASK & dv) != 0) {
            int i = pos + 1;
            array[i] = (byte) (array[i] + (((dv >>> 8) & 255) + c));
        }
    }

    public static final void writeIntLittleEndian(byte[] array, int pos, int value) {
        array[pos + 3] = (byte) (value >>> 24);
        array[pos + 2] = (byte) (value >>> 16);
        array[pos + 1] = (byte) (value >>> 8);
        array[pos] = (byte) (value & 255);
    }

    public static final void writeLongLittleEndian(byte[] array, int pos, long value) {
        array[pos + 7] = (byte) ((int) (value >>> 56));
        array[pos + 6] = (byte) ((int) (value >>> 48));
        array[pos + 5] = (byte) ((int) (value >>> 40));
        array[pos + 4] = (byte) ((int) (value >>> 32));
        array[pos + 3] = (byte) ((int) (value >>> 24));
        array[pos + 2] = (byte) ((int) (value >>> 16));
        array[pos + 1] = (byte) ((int) (value >>> 8));
        array[pos] = (byte) ((int) (255 & value));
    }
}
