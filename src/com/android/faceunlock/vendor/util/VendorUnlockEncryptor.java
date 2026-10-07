/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.security.keystore.KeyProtection$Builder
 *  android.util.Log
 */
package com.android.faceunlock.vendor.util;

import android.security.keystore.KeyProtection;
import android.util.Log;
import com.android.faceunlock.vendor.util.UnlockEncryptor;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class VendorUnlockEncryptor
implements UnlockEncryptor {
    private static final String TAG = "VendorUnlockEncryptor";
    public static final String AKS_PROVIDER = "AndroidKeyStore";
    private static final int PROFILE_KEY_IV_SIZE = 12;
    public static final String SEED_ALIAS = "seed_faceunlock";

    public VendorUnlockEncryptor() {
        this.saveSeed();
    }

    private boolean saveSeed() {
        try {
            KeyStore keyStore = KeyStore.getInstance(AKS_PROVIDER);
            keyStore.load(null);
            if (keyStore.containsAlias(SEED_ALIAS)) {
                Log.i((String)TAG, (String)"key is already created");
                return true;
            }
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(new SecureRandom());
            keyStore.setEntry(SEED_ALIAS, new KeyStore.SecretKeyEntry(keyGenerator.generateKey()), (KeyStore.ProtectionParameter)new KeyProtection.Builder(1).setBlockModes(new String[]{"GCM"}).setUserAuthenticationRequired(false).setEncryptionPaddings(new String[]{"NoPadding"}).build());
            Log.i((String)TAG, (String)"create key successfully");
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            String string = TAG;
            Log.e((String)string, (String)("Exception in store. " + exception.toString()));
            return false;
        }
    }

    private byte[] encryptData(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        try {
            SecretKey secretKey;
            KeyStore keyStore = KeyStore.getInstance(AKS_PROVIDER);
            keyStore.load(null);
            if (keyStore.containsAlias(SEED_ALIAS)) {
                secretKey = (SecretKey)keyStore.getKey(SEED_ALIAS, null);
            } else {
                Log.i((String)TAG, (String)"key not exist, create key!");
                this.saveSeed();
                secretKey = (SecretKey)keyStore.getKey(SEED_ALIAS, null);
            }
            if (secretKey != null) {
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(1, secretKey);
                byte[] byArray2 = cipher.doFinal(byArray);
                byte[] byArray3 = cipher.getIV();
                if (byArray3.length == 12) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(byArray3);
                    byteArrayOutputStream.write(byArray2);
                    return byteArrayOutputStream.toByteArray();
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            String string = TAG;
            Log.e((String)string, (String)("Exception in encrypt. " + exception.toString()));
        }
        return new byte[0];
    }

    private byte[] decryptData(byte[] byArray) {
        SecretKey secretKey = null;
        if (byArray == null) {
            return null;
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(AKS_PROVIDER);
            keyStore.load(null);
            if (keyStore.containsAlias(SEED_ALIAS)) {
                secretKey = (SecretKey)keyStore.getKey(SEED_ALIAS, null);
            } else {
                Log.e((String)TAG, (String)"key not exist, something is wrong!");
            }
            if (secretKey != null) {
                byte[] byArray2 = Arrays.copyOfRange(byArray, 0, 12);
                byte[] byArray3 = Arrays.copyOfRange(byArray, 12, byArray.length);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(2, (Key)secretKey, new GCMParameterSpec(128, byArray2));
                return cipher.doFinal(byArray3);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            String string = TAG;
            Log.e((String)string, (String)("Exception in decrypt. " + exception.toString()));
        }
        return new byte[0];
    }

    @Override
    public byte[] encrypt(byte[] byArray) {
        return this.encryptData(byArray);
    }

    @Override
    public byte[] decrypt(byte[] byArray) {
        return this.decryptData(byArray);
    }
}
