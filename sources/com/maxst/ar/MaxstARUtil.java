package com.maxst.ar;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.Assert;

public class MaxstARUtil {
    public static byte[] readYuvBytesFromFile(String srcFile) {
        File file = new File(srcFile);
        byte[] bytes = new byte[((int) file.length())];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void writeYuvBytesToFile(byte[] input, int width, int height, int format, String dstFile) {
        FileOutputStream fileOutputStream;
        IOException e;
        try {
            FileOutputStream fos = new FileOutputStream(new File(dstFile));
            try {
                fos.write(intToByteArray(width), 0, 4);
                fos.write(intToByteArray(height), 0, 4);
                fos.write(intToByteArray(format), 0, 4);
                fos.write(input, 0, input.length);
                fos.close();
                fileOutputStream = fos;
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = fos;
                e.printStackTrace();
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public static Bitmap getBitmapFromAsset(String fileName, AssetManager assets) {
        try {
            return BitmapFactory.decodeStream(new BufferedInputStream(assets.open(fileName, 3)));
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((ViewCompat.MEASURED_STATE_MASK & i) >> 24), (byte) ((16711680 & i) >> 16), (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8), (byte) (i & 255)};
    }

    public static int byteArrayToInt(byte[] bytes, int start) {
        Assert.assertTrue("Byte buffer length is not 4", bytes.length - start >= 4);
        return (((bytes[start + 3] & 255) | ((bytes[start + 2] & 255) << 8)) | ((bytes[start + 1] & 255) << 16)) | ((bytes[start] & 255) << 24);
    }
}
