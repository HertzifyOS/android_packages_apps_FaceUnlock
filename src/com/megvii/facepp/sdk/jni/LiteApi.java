/*
 * Decompiled with CFR 0.152.
 */
package com.megvii.facepp.sdk.jni;

import com.megvii.facepp.sdk.Lite;

public class LiteApi {
    public static native int nativeCheckFeatureValid(long var0, int var2);

    public static native int nativeCompare(long var0, byte[] var2, int var3, int var4, int var5, boolean var6, boolean var7, int[] var8);

    public static native int nativeCompareFeatures(long var0, byte[] var2, float[] var3, int var4, boolean var5);

    public static native int nativeCompareMultiImages(long var0, Lite.MGULKImage[] var2, int[] var3);

    public static native int nativeDeleteFeature(long var0, int var2);

    public static native long nativeGetConfig(long var0, Lite.LiteConfig var2);

    public static native int nativeGetFeature(long var0, byte[] var2, int var3, int var4, int var5, byte[] var6);

    public static native int nativeGetFeatureCount();

    public static native String nativeGetVersion(long var0);

    public static native long nativeInitAll(long var0, String var2, String var3, byte[] var4);

    public static native long nativeInitAllWithPath(long var0, String var2, String var3, String var4);

    public static native long nativeInitDetect(long var0, byte[] var2);

    public static native long nativeInitDetectWithPath(long var0, String var2);

    public static native long nativeInitHandle(String var0);

    public static native long nativeInitLive(long var0, String var2, String var3);

    public static native int nativePrepare(long var0);

    public static native int nativePrepareWithPower(long var0, int var2);

    public static native long nativeRelease(long var0);

    public static native long nativeReleaseDetect(long var0);

    public static native long nativeReleaseLive(long var0);

    public static native int nativeReset(long var0);

    public static native int nativeSaveFeature(long var0, byte[] var2, int var3, int var4, int var5, int var6, byte[] var7, byte[] var8, int[] var9);

    public static native int nativeSaveFeatureMultiImages(long var0, Lite.MGULKImage[] var2, byte[] var3, byte[] var4, int[] var5);

    public static native int nativeSetConfig(long var0, float var2, float var3, float var4, float var5, boolean var6, boolean var7);

    public static native int nativeSetConfigV2(long var0, Lite.LiteConfig var2);

    public static native int nativeSetDetectArea(long var0, int var2, int var3, int var4, int var5);

    public static native long nativeSetLogLevel(int var0);

    public static native int nativeUpdateFeature(long var0, byte[] var2, int var3, int var4, int var5, int var6, byte[] var7, byte[] var8, int var9);

    static {
        System.loadLibrary("MegviiUnlock-jni-1.2");
    }
}
