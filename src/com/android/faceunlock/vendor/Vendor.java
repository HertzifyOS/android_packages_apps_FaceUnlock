/*
 * Decompiled with CFR 0.152.
 */
package com.android.faceunlock.vendor;

public abstract class Vendor {
    public abstract int compare(byte[] var1, int var2, int var3, int var4, boolean var5, boolean var6, int[] var7);

    public abstract void compareStart();

    public abstract void compareStop();

    public abstract void deleteFeature(int var1);

    public abstract int getFeatureCount();

    public abstract void init();

    public abstract void release();

    public abstract int saveFeature(byte[] var1, int var2, int var3, int var4, boolean var5, byte[] var6, byte[] var7, int[] var8);

    public abstract void saveFeatureStart();

    public abstract void saveFeatureStop();

    public abstract void setDetectArea(int var1, int var2, int var3, int var4);
}
