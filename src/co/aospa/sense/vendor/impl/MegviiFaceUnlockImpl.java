/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.megvii.facepp.sdk.Lite
 */
package co.aospa.sense.vendor.impl;

import com.megvii.facepp.sdk.Lite;

public class MegviiFaceUnlockImpl
extends Lite {
    private static MegviiFaceUnlockImpl sInstance;

    private MegviiFaceUnlockImpl() {
    }

    public static MegviiFaceUnlockImpl getInstance() {
        if (sInstance == null) {
            sInstance = new MegviiFaceUnlockImpl();
        }
        return sInstance;
    }
}
