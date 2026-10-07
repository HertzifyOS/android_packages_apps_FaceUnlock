/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  com.android.faceunlock.vendor.util.UnlockEncryptor
 */
package com.megvii.facepp.sdk;

import android.util.Log;

import com.android.faceunlock.vendor.util.UnlockEncryptor;
import com.megvii.facepp.sdk.Lite;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FeatureRestoreHelper {
    private static final int BUFFER_SIZE = 8192;
    private static final int RESTORE_IMAGE_SIZE = 144;
    private static final String TAG = "FeatureRestoreHelper";
    public static final byte[] sMagic = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
    private UnlockEncryptor mEncryptor;

    public void setUnlockEncryptor(UnlockEncryptor unlockEncryptor) {
        this.mEncryptor = unlockEncryptor;
    }

    public void saveRestoreImage(byte[] byArray, String string, int n) {
        Log.i((String)TAG, (String)("saveRestoreImage: length: " + byArray.length + " id " + n));
        this.writeFile(this.getRestoreFile(string, n).getAbsolutePath(), byArray);
    }

    public void deleteRestoreImage(String string, int n) {
        Log.i((String)TAG, (String)("deleteRestoreImage: id " + n));
        this.getRestoreFile(string, n).delete();
    }

    public int restoreAllFeature(String string) {
        File[] fileArray = new File(string).listFiles();
        if (fileArray == null || fileArray.length == 0) {
            return 24;
        }
        int n = 0;
        for (File file : fileArray) {
            int n2;
            String string2 = file.getName();
            if (!string2.startsWith("restore_") || string2.length() <= 8) continue;
            Log.i((String)TAG, (String)("restoreAllFeature: " + string2));
            try {
                n2 = Integer.parseInt(string2.substring(8));
            }
            catch (NumberFormatException numberFormatException) {
                n2 = -1;
            }
            if (n2 == -1) continue;
            byte[] byArray = this.readFile(file.getAbsolutePath());
            Log.i((String)TAG, (String)("restoreAllFeature: update old feature " + n2));
            if (this.restoreFeatureAtPosition(n2, byArray) != 0) continue;
            ++n;
        }
        return n == 0 ? 24 : 0;
    }

    private int restoreFeatureAtPosition(int n, byte[] byArray) {
        return Lite.getInstance().updateFeature(byArray, 144, 144, 90, true, new byte[10000], new byte[40000], n);
    }

    private File getRestoreFile(String string, int n) {
        return new File(string, "restore_" + n);
    }

    private void writeFile(String string, byte[] byArray) {
        File file = new File(string);
        if (file.exists()) {
            file.delete();
        }
        UnlockEncryptor unlockEncryptor = this.mEncryptor;
        int n = 0;
        if (unlockEncryptor != null) {
            byte[] byArray2 = unlockEncryptor.encrypt(byArray);
            int n2 = byArray2.length;
            byte[] byArray3 = sMagic;
            byte[] byArray4 = new byte[n2 + byArray3.length];
            System.arraycopy(byArray3, 0, byArray4, 0, byArray3.length);
            System.arraycopy(byArray2, 0, byArray4, byArray3.length, byArray2.length);
            byArray = byArray4;
        }
        int n3 = byArray.length;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(string);
            while (n3 > n) {
                int n4 = n3 - n;
                if (n4 > 8192) {
                    n4 = 8192;
                }
                fileOutputStream.write(byArray, n, n4);
                n += n4;
            }
        }
        catch (IOException iOException) {
            Log.e((String)TAG, (String)"writeFile failed", (Throwable)iOException);
        }
    }

    private byte[] readFile(String string) {
        File file = new File(string);
        if (!file.exists()) {
            return null;
        }
        int n = (int)file.length();
        byte[] byArray = new byte[n];
        try {
            int n2;
            FileInputStream fileInputStream = new FileInputStream(string);
            for (int i = 0; n > i; i += fileInputStream.read(byArray, i, n2)) {
                n2 = n - i;
                if (n2 <= 8192) continue;
                n2 = 8192;
            }
            if (this.mEncryptor != null && this.startWithMagic(byArray)) {
                byte[] byArray2 = sMagic;
                int n3 = n - byArray2.length;
                byte[] byArray3 = new byte[n3];
                System.arraycopy(byArray, byArray2.length, byArray3, 0, n3);
                return this.mEncryptor.decrypt(byArray3);
            }
            return byArray;
        }
        catch (IOException iOException) {
            Log.e((String)TAG, (String)"readFile failed", (Throwable)iOException);
            return byArray;
        }
    }

    private boolean startWithMagic(byte[] byArray) {
        if (byArray.length < sMagic.length) {
            return false;
        }
        int n = 0;
        byte[] byArray2;
        while (n < (byArray2 = sMagic).length) {
            if (byArray[n] != byArray2[n]) {
                return false;
            }
            ++n;
        }
        return true;
    }
}
