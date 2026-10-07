/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package co.aospa.sense.vendor.util;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConUtil {
    public static String getRaw(Context context, String string, String string2, String string3, boolean bl) {
        File file = new File(context.getDir("faceunlock_data", 0), string2);
        if (file.exists() || file.mkdirs()) {
            File file2 = new File(file, string3);
            if (!bl && file2.exists()) {
                return file2.getAbsolutePath();
            }
            byte[] byArray = new byte[1024];
            try {
                int n;
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                InputStream inputStream = context.getResources().openRawResource(context.getResources().getIdentifier(string, "raw", "co.aospa.sense"));
                while ((n = inputStream.read(byArray)) != -1) {
                    fileOutputStream.write(byArray, 0, n);
                }
                String string4 = file2.getAbsolutePath();
                if (inputStream != null) {
                    inputStream.close();
                }
                fileOutputStream.close();
                return string4;
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
