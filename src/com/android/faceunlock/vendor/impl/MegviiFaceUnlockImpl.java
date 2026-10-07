/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.megvii.facepp.sdk.Lite
 */
package com.android.faceunlock.vendor.impl;

import com.megvii.facepp.sdk.Lite;

import com.android.faceunlock.vendor.impl.MegviiFaceUnlockImpl;

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
