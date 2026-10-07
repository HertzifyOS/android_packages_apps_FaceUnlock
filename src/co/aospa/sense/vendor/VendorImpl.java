/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package co.aospa.sense.vendor;

import android.content.Context;
import co.aospa.sense.vendor.Vendor;
import co.aospa.sense.vendor.impl.FacePPImpl;

public class VendorImpl
extends Vendor {
    private final Vendor mFaceManager;

    public VendorImpl(Context context) {
        this.mFaceManager = new FacePPImpl(context);
    }

    @Override
    public int compare(byte[] byArray, int n, int n2, int n3, boolean bl, boolean bl2, int[] nArray) {
        return this.mFaceManager.compare(byArray, n, n2, n3, bl, bl2, nArray);
    }

    @Override
    public void compareStart() {
        this.mFaceManager.compareStart();
    }

    @Override
    public void compareStop() {
        this.mFaceManager.compareStop();
    }

    @Override
    public void deleteFeature(int n) {
        this.mFaceManager.deleteFeature(n);
    }

    @Override
    public int getFeatureCount() {
        return this.mFaceManager.getFeatureCount();
    }

    @Override
    public void init() {
        this.mFaceManager.init();
    }

    @Override
    public void release() {
        this.mFaceManager.release();
    }

    @Override
    public int saveFeature(byte[] byArray, int n, int n2, int n3, boolean bl, byte[] byArray2, byte[] byArray3, int[] nArray) {
        return this.mFaceManager.saveFeature(byArray, n, n2, n3, bl, byArray2, byArray3, nArray);
    }

    @Override
    public void saveFeatureStart() {
        this.mFaceManager.saveFeatureStart();
    }

    @Override
    public void saveFeatureStop() {
        this.mFaceManager.saveFeatureStop();
    }

    @Override
    public void setDetectArea(int n, int n2, int n3, int n4) {
        this.mFaceManager.setDetectArea(n, n2, n3, n4);
    }
}
