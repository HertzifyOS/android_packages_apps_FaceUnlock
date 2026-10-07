/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  com.android.faceunlock.util.PreferenceHelper
 */
package com.android.faceunlock.vendor.impl;

import android.content.Context;
import android.util.Log;
import com.android.faceunlock.util.PreferenceHelper;
import com.android.faceunlock.vendor.Vendor;
import com.android.faceunlock.vendor.impl.MegviiFaceUnlockImpl;
import com.android.faceunlock.vendor.util.ConUtil;
import com.android.faceunlock.vendor.util.VendorUnlockEncryptor;

import java.io.File;

public class FacePPImpl
extends Vendor {
    private static final String TAG = FacePPImpl.class.getSimpleName();
    private static final boolean DEBUG = true;
    private static final String SDK_VERSION = "1";
    private final Context mContext;
    private SERVICE_STATE mCurrentState = SERVICE_STATE.INITING;
    private final PreferenceHelper mPreferenceHelper;

    public FacePPImpl(Context context) {
        this.mContext = context;
        this.mPreferenceHelper = new PreferenceHelper(context);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void init() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            String string;
            if (this.mCurrentState != SERVICE_STATE.INITING) {
                Log.d((String)TAG, (String)" Has been init, ignore");
                return;
            }
            String string2 = TAG;
            Log.i((String)string2, (String)"init start");
            boolean bl = !SDK_VERSION.equals(this.mPreferenceHelper.getStringValueByKey("sdk_version"));
            File file = this.mContext.getDir("faceunlock_data", 0);
            if (!file.exists()) {
                file.mkdirs();
            }
            if ((string = ConUtil.getRaw(this.mContext, "model_file", "model", "model_file", bl)) == null) {
                Log.e((String)string2, (String)"Unavalibale memory, init failed, stop self");
                return;
            }
            String string3 = ConUtil.getRaw(this.mContext, "panorama_mgb", "model", "panorama_mgb", bl);
            MegviiFaceUnlockImpl.getInstance().initHandle(file.getAbsolutePath(), new VendorUnlockEncryptor());
            Log.i((String)string2, (String)"init stop");
            if (MegviiFaceUnlockImpl.getInstance().initAllWithPath(string3, "", string) != 0) {
                Log.e((String)string2, (String)"init failed, stop self");
                return;
            }
            if (bl) {
                this.restoreFeature();
                this.mPreferenceHelper.saveStringValue("sdk_version", SDK_VERSION);
            }
            this.mCurrentState = SERVICE_STATE.IDLE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void restoreFeature() {
        Log.i((String)TAG, (String)"RestoreFeature");
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            MegviiFaceUnlockImpl.getInstance().prepare();
            MegviiFaceUnlockImpl.getInstance().restoreFeature();
            MegviiFaceUnlockImpl.getInstance().reset();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void compareStart() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState == SERVICE_STATE.INITING) {
                this.init();
            }
            if (this.mCurrentState == SERVICE_STATE.UNLOCKING) {
                return;
            }
            if (this.mCurrentState != SERVICE_STATE.IDLE) {
                String string = TAG;
                Log.e((String)string, (String)("unlock start failed: current state: " + (Object)((Object)this.mCurrentState)));
                return;
            }
            Log.i((String)TAG, (String)"compareStart");
            MegviiFaceUnlockImpl.getInstance().prepare();
            this.mCurrentState = SERVICE_STATE.UNLOCKING;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int compare(byte[] byArray, int n, int n2, int n3, boolean bl, boolean bl2, int[] nArray) {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState != SERVICE_STATE.UNLOCKING) {
                String string = TAG;
                Log.e((String)string, (String)("compare failed: current state: " + (Object)((Object)this.mCurrentState)));
                return -1;
            }
            int n4 = MegviiFaceUnlockImpl.getInstance().compare(byArray, n, n2, n3, bl, bl2, nArray);
            String string = TAG;
            Log.i((String)string, (String)("compare finish: " + n4));
            if (n4 == 0) {
                this.compareStop();
            }
            return n4;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void compareStop() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState != SERVICE_STATE.UNLOCKING) {
                String string = TAG;
                Log.e((String)string, (String)("compareStop failed: current state: " + (Object)((Object)this.mCurrentState)));
                return;
            }
            Log.i((String)TAG, (String)"compareStop");
            MegviiFaceUnlockImpl.getInstance().reset();
            this.mCurrentState = SERVICE_STATE.IDLE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void saveFeatureStart() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState == SERVICE_STATE.INITING) {
                this.init();
            } else if (this.mCurrentState == SERVICE_STATE.UNLOCKING) {
                Log.e((String)TAG, (String)"save feature, stop unlock");
                this.compareStop();
            }
            if (this.mCurrentState != SERVICE_STATE.IDLE) {
                String string = TAG;
                Log.e((String)string, (String)("saveFeatureStart failed: current state: " + (Object)((Object)this.mCurrentState)));
            }
            Log.i((String)TAG, (String)"saveFeatureStart");
            MegviiFaceUnlockImpl.getInstance().prepare();
            this.mCurrentState = SERVICE_STATE.ENROLLING;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int saveFeature(byte[] byArray, int n, int n2, int n3, boolean bl, byte[] byArray2, byte[] byArray3, int[] nArray) {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState != SERVICE_STATE.ENROLLING) {
                String string = TAG;
                Log.e((String)string, (String)("save feature failed , current state : " + (Object)((Object)this.mCurrentState)));
                return -1;
            }
            Log.i((String)TAG, (String)"saveFeature");
            return MegviiFaceUnlockImpl.getInstance().saveFeature(byArray, n, n2, n3, bl, byArray2, byArray3, nArray);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void saveFeatureStop() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState != SERVICE_STATE.ENROLLING) {
                String string = TAG;
                Log.d((String)string, (String)("saveFeatureStop failed: current state: " + (Object)((Object)this.mCurrentState)));
            }
            Log.i((String)TAG, (String)"saveFeatureStop");
            MegviiFaceUnlockImpl.getInstance().reset();
            this.mCurrentState = SERVICE_STATE.IDLE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setDetectArea(int n, int n2, int n3, int n4) {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            Log.i((String)TAG, (String)"setDetectArea start");
            MegviiFaceUnlockImpl.getInstance().setDetectArea(n, n2, n3, n4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void deleteFeature(int n) {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            String string = TAG;
            Log.i((String)string, (String)"deleteFeature start");
            MegviiFaceUnlockImpl.getInstance().deleteFeature(n);
            Log.i((String)string, (String)"deleteFeature stop");
            this.release();
        }
    }

    @Override
    public int getFeatureCount() {
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void release() {
        FacePPImpl facePPImpl = this;
        synchronized (facePPImpl) {
            if (this.mCurrentState == SERVICE_STATE.INITING) {
                Log.i((String)TAG, (String)"has been released, ignore");
                return;
            }
            String string = TAG;
            Log.i((String)string, (String)"release start");
            MegviiFaceUnlockImpl.getInstance().release();
            this.mCurrentState = SERVICE_STATE.INITING;
            Log.i((String)string, (String)"release stop");
        }
    }

    public static enum SERVICE_STATE {
        INITING,
        IDLE,
        ENROLLING,
        UNLOCKING,
        ERROR;
    }
}
