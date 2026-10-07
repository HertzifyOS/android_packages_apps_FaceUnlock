/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.Image
 *  android.media.Image$Plane
 *  android.os.Environment
 *  android.os.StatFs
 *  android.util.Log
 *  co.aospa.sense.vendor.util.UnlockEncryptor
 */
package com.megvii.facepp.sdk;

import android.media.Image;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import co.aospa.sense.vendor.util.UnlockEncryptor;
import com.megvii.facepp.sdk.FeatureRestoreHelper;
import com.megvii.facepp.sdk.jni.LiteApi;
import java.nio.ByteBuffer;

public class Lite {
    public static final int FEATURE_SIZE = 10000;
    public static final int IMAGE_SIZE = 40000;
    public static final int RESULT_SIZE = 20;
    private static Lite sInstance;
    private long handle = 0L;
    private final FeatureRestoreHelper mFeatureRestoreHelper = new FeatureRestoreHelper();
    private String mPath;

    public int setConfig(float f, float f2, float f3) {
        return 0;
    }

    public static Lite getInstance() {
        if (sInstance == null) {
            sInstance = new Lite();
        }
        return sInstance;
    }

    public void initHandle(String string, UnlockEncryptor unlockEncryptor) {
        this.initHandle(string);
        this.mFeatureRestoreHelper.setUnlockEncryptor(unlockEncryptor);
    }

    public void initHandle(String string) {
        if (this.handle == 0L) {
            this.handle = LiteApi.nativeInitHandle(string);
            this.mPath = string;
        }
    }

    public int initAll(String string, String string2, byte[] byArray) {
        return (int)LiteApi.nativeInitAll(this.handle, string, string2, byArray);
    }

    public int initAllWithPath(String string, String string2, String string3) {
        return (int)LiteApi.nativeInitAllWithPath(this.handle, string, string2, string3);
    }

    public int initLive(String string, String string2) {
        return (int)LiteApi.nativeInitLive(this.handle, string, string2);
    }

    public int initDetect(byte[] byArray) {
        return (int)LiteApi.nativeInitDetect(this.handle, byArray);
    }

    public int initDetectWithPath(String string) {
        return (int)LiteApi.nativeInitDetectWithPath(this.handle, string);
    }

    public int releaseLive() {
        return (int)LiteApi.nativeReleaseLive(this.handle);
    }

    public int releaseDetect() {
        return (int)LiteApi.nativeReleaseDetect(this.handle);
    }

    public void release() {
        LiteApi.nativeRelease(this.handle);
        this.handle = 0L;
    }

    public int compare(byte[] byArray, int n, int n2, int n3, boolean bl, boolean bl2, int[] nArray) {
        if (nArray.length < 20) {
            return 1;
        }
        return LiteApi.nativeCompare(this.handle, byArray, n, n2, n3, bl, bl2, nArray);
    }

    public int compare(byte[] byArray, int n, int n2, int n3, int[] nArray) {
        if (nArray.length < 20) {
            return 1;
        }
        return LiteApi.nativeCompare(this.handle, byArray, n, n2, n3, false, false, nArray);
    }

    public int compareMultiImages(MGULKImage[] mGULKImageArray, int[] nArray) {
        if (nArray.length < 20) {
            return 1;
        }
        return LiteApi.nativeCompareMultiImages(this.handle, mGULKImageArray, nArray);
    }

    public int saveFeature(byte[] byArray, int n, int n2, int n3, boolean bl, byte[] byArray2, byte[] byArray3) {
        return this.updateFeature(byArray, n, n2, n3, bl, byArray2, byArray3, 0);
    }

    public int saveFeature(byte[] byArray, int n, int n2, int n3, byte[] byArray2, byte[] byArray3) {
        return this.updateFeature(byArray, n, n2, n3, true, byArray2, byArray3, 0);
    }

    public int saveFeature(byte[] byArray, int n, int n2, int n3, boolean bl, byte[] byArray2, byte[] byArray3, int[] nArray) {
        if (new StatFs(Environment.getDataDirectory().getPath()).getAvailableBlocksLong() < 256L) {
            return 33;
        }
        if (byArray3.length < 40000 || byArray2.length < 10000) {
            return 1;
        }
        int n4 = LiteApi.nativeSaveFeature(this.handle, byArray, n, n2, n3, bl ? 1 : 0, byArray2, byArray3, nArray);
        if (n4 == 0) {
            this.mFeatureRestoreHelper.saveRestoreImage(byArray3, this.mPath, nArray[0]);
        }
        return n4;
    }

    public int saveFeature(byte[] byArray, int n, int n2, int n3, byte[] byArray2, byte[] byArray3, int[] nArray) {
        if (byArray3.length < 40000 || byArray2.length < 10000) {
            return 1;
        }
        int n4 = LiteApi.nativeSaveFeature(this.handle, byArray, n, n2, n3, 1, byArray2, byArray3, nArray);
        if (n4 == 0) {
            this.mFeatureRestoreHelper.saveRestoreImage(byArray3, this.mPath, nArray[0]);
        }
        return n4;
    }

    public int saveFeatureMultiImages(MGULKImage[] mGULKImageArray, byte[] byArray, byte[] byArray2, int[] nArray) {
        int n = LiteApi.nativeSaveFeatureMultiImages(this.handle, mGULKImageArray, byArray, byArray2, nArray);
        if (n == 0) {
            this.mFeatureRestoreHelper.saveRestoreImage(byArray2, this.mPath, nArray[0]);
        }
        return n;
    }

    public int updateFeature(byte[] byArray, int n, int n2, int n3, boolean bl, byte[] byArray2, byte[] byArray3, int n4) {
        if (byArray3.length < 40000 || byArray2.length < 10000) {
            return 1;
        }
        int n5 = LiteApi.nativeUpdateFeature(this.handle, byArray, n, n2, n3, bl ? 1 : 0, byArray2, byArray3, n4);
        if (n5 == 0) {
            this.mFeatureRestoreHelper.saveRestoreImage(byArray3, this.mPath, n4);
        }
        return n5;
    }

    public int deleteFeature() {
        return this.deleteFeature(0);
    }

    public int deleteFeature(int n) {
        int n2 = LiteApi.nativeDeleteFeature(this.handle, n);
        this.mFeatureRestoreHelper.deleteRestoreImage(this.mPath, n);
        return n2;
    }

    public int restoreFeature() {
        return this.mFeatureRestoreHelper.restoreAllFeature(this.mPath);
    }

    public int setConfig(float f, float f2, float f3, float f4, boolean bl, boolean bl2) {
        return LiteApi.nativeSetConfig(this.handle, f, f2, f3, f4, bl, bl2);
    }

    public int setConfig(float f, float f2, float f3, float f4) {
        return LiteApi.nativeSetConfig(this.handle, f, f2, f3, f4, false, false);
    }

    public int setConfig(LiteConfig liteConfig) {
        if (liteConfig == null) {
            return -1;
        }
        return LiteApi.nativeSetConfigV2(this.handle, liteConfig);
    }

    public int reset() {
        return LiteApi.nativeReset(this.handle);
    }

    public int prepare(MGULKPowerMode mGULKPowerMode) {
        int n = mGULKPowerMode.ordinal();
        int n2 = 2;
        if (n != 1) {
            if (n == 2) {
                n2 = 1;
            }
            return LiteApi.nativePrepareWithPower(this.handle, n2);
        }
        n2 = 0;
        return LiteApi.nativePrepareWithPower(this.handle, n2);
    }

    public int prepare() {
        MGULKPowerMode.MG_UNLOCK_POWER_HIGH.ordinal();
        return LiteApi.nativePrepare(this.handle);
    }

    public int setDetectArea(int n, int n2, int n3, int n4) {
        return LiteApi.nativeSetDetectArea(this.handle, n, n2, n3, n4);
    }

    public String getVersion() {
        return LiteApi.nativeGetVersion(this.handle);
    }

    public int getFeature(byte[] byArray, int n, int n2, int n3, byte[] byArray2) {
        if (byArray2.length < 10000) {
            return 1;
        }
        return LiteApi.nativeGetFeature(this.handle, byArray, n, n2, n3, byArray2);
    }

    public int compareFeatures(byte[] byArray, float[] fArray, int n, boolean bl) {
        return LiteApi.nativeCompareFeatures(this.handle, byArray, fArray, n, bl);
    }

    public int checkFeatureValid(int n) {
        return LiteApi.nativeCheckFeatureValid(this.handle, n);
    }

    public int getFeatureCount() {
        return LiteApi.nativeGetFeatureCount();
    }

    public long setLogLevel(int n) {
        return LiteApi.nativeSetLogLevel(n);
    }

    public LiteConfig getConfig() {
        LiteConfig liteConfig = new LiteConfig(this, this, null);
        LiteApi.nativeGetConfig(this.handle, liteConfig);
        return liteConfig;
    }

    public static int image2NV21(Image image, byte[] byArray) {
        int n = Lite.readImageIntoBuffer(image, byArray);
        if (n == 1) {
            return 1;
        }
        Lite.revertHalf(byArray);
        return n;
    }

    private static int readImageIntoBuffer(Image image, byte[] byArray) {
        if (image == null) {
            Log.e((String)"NULL Image", (String)"image is null");
            return 1;
        }
        int n = image.getWidth();
        int n2 = image.getHeight();
        Image.Plane[] planeArray = image.getPlanes();
        int n3 = 0;
        for (int i = 0; i < planeArray.length; ++i) {
            int n4;
            ByteBuffer byteBuffer = planeArray[i].getBuffer();
            int n5 = planeArray[i].getRowStride();
            int n6 = planeArray[i].getPixelStride();
            int n7 = i == 0 ? n : n / 2;
            int n8 = i == 0 ? n2 : n2 / 2;
            if (n6 == 1 && n5 == n7) {
                int n9 = n7 * n8;
                byteBuffer.get(byArray, n3, n9);
                n3 += n9;
                continue;
            }
            byte[] byArray2 = new byte[n5];
            for (n4 = 0; n4 < n8 - 1; ++n4) {
                byteBuffer.get(byArray2, 0, n5);
                int n10 = 0;
                while (n10 < n7) {
                    byArray[n3] = byArray2[n10 * n6];
                    ++n10;
                    ++n3;
                }
            }
            byteBuffer.get(byArray2, 0, Math.min(n5, byteBuffer.remaining()));
            n4 = 0;
            while (n4 < n7) {
                byArray[n3] = byArray2[n4 * n6];
                ++n4;
                ++n3;
            }
        }
        return 0;
    }

    private static void revertHalf(byte[] byArray) {
        int n = byArray.length;
        int n2 = n / 3;
        byte[] byArray2 = new byte[n2];
        int n3 = n / 6;
        int n4 = n3 * 4;
        int n5 = n3 * 5;
        int n6 = 0;
        while (n6 < n2 - 1) {
            byArray2[n6] = byArray[n5];
            byArray2[n6 + 1] = byArray[n4];
            n6 += 2;
            ++n5;
            ++n4;
        }
        int n7 = n2 * 2;
        int n8 = n - n7;
        if (n8 >= 0) {
            System.arraycopy(byArray2, 0, byArray, n7, n8);
        }
    }

    public class LiteConfig {
        public static final int MG_UNLOCK_BIG_CPU_CORE_HIGH = 4;
        public static final int MG_UNLOCK_BIG_CPU_CORE_LOW = 0;
        public static final int MG_UNLOCK_COMPARE_ALL = 0;
        public static final int MG_UNLOCK_COMPARE_LIVE = 1;
        public static final int MG_UNLOCK_COMP_DEVICE_CPU = 1;
        public static final int MG_UNLOCK_COMP_DEVICE_NONE = 0;
        public static final int MG_UNLOCK_COMP_DEVICE_OPENCL = 3;
        public static final int MG_UNLOCK_COMP_DEVICE_SNPE = 2;
        public static final int MG_UNLOCK_EXTRACT_APU = 4;
        public static final int MG_UNLOCK_EXTRACT_DOUBLE_CORE_NORMAL = 1;
        public static final int MG_UNLOCK_EXTRACT_DSP = 3;
        public static final int MG_UNLOCK_EXTRACT_OPENCL = 2;
        public static final int MG_UNLOCK_EXTRACT_SINALE_CORE_NORMAL = 0;
        public static final int MG_UNLOCK_STORE_DEBUG_IMAGE_NONE = 0;
        public static final int MG_UNLOCK_STORE_DEBUG_IMAGE_NV21 = 1;
        public static final int MG_UNLOCK_STORE_DEBUG_IMAGE_NV21_LANDMARK = 2;
        public float ComparePitchDownThreshold;
        public float ComparePitchTopThreshold;
        public float CompareYawLeftThreshold;
        public float CompareYawRightThreshold;
        public int bigCpuCore;
        public boolean blurness;
        public int compDeviceType;
        public boolean compareBlurness;
        public int compareType;
        public int extractConfig;
        public boolean eyeOcclusion;
        public boolean eyeStatus;
        public boolean faceIntact;
        public boolean light;
        public boolean mouthOcclusion;
        public String nativeLibraryPath;
        public String openclCachePath;
        public float pitchDownThreshold;
        public float pitchTopThreshold;
        public int rectBottom;
        public int rectLeft;
        public int rectRight;
        public int rectTop;
        public String saveImagePath;
        public String snpeCachePath;
        public int storeDebugImgMode;
        public boolean useModelToCheck3dPose;
        public float yawLeftThreshold;
        public float yawRightThreshold;

        LiteConfig(Lite lite2, Lite lite3, MGULKPowerMode mGULKPowerMode) {
            this();
        }

        private LiteConfig() {
        }

        public String toString() {
            return "LiteConfig{compDeviceType=" + this.compDeviceType + ", bigCpuCore=" + this.bigCpuCore + ", useModelToCheck3dPose=" + this.useModelToCheck3dPose + ", eyeOcclusion=" + this.eyeOcclusion + ", mouthOcclusion=" + this.mouthOcclusion + ", eyeStatus=" + this.eyeStatus + ", light=" + this.light + ", blurness=" + this.blurness + ", compareBlurness=" + this.compareBlurness + ", faceIntact=" + this.faceIntact + ", yawLeftThreshold=" + this.yawLeftThreshold + ", yawRightThreshold=" + this.yawRightThreshold + ", pitchTopThreshold=" + this.pitchTopThreshold + ", pitchDownThreshold=" + this.pitchDownThreshold + ", CompareYawLeftThreshold=" + this.CompareYawLeftThreshold + ", CompareYawRightThreshold=" + this.CompareYawRightThreshold + ", ComparePitchTopThreshold=" + this.ComparePitchTopThreshold + ", ComparePitchDownThreshold=" + this.ComparePitchDownThreshold + ", rectLeft=" + this.rectLeft + ", rectTop=" + this.rectTop + ", rectRight=" + this.rectRight + ", rectBottom=" + this.rectBottom + ", storeDebugImgMode=" + this.storeDebugImgMode + ", saveImagePath='" + this.saveImagePath + "', compareType=" + this.compareType + ", extractConfig=" + this.extractConfig + ", nativeLibraryPath='" + this.nativeLibraryPath + "', openclCachePath='" + this.openclCachePath + "', snpeCachePath='" + this.snpeCachePath + "'}";
        }
    }

    public static class MGULKImage {
        public static int MG_UNLOCK_IMG_2PD = 1;
        public static int MG_UNLOCK_IMG_BGR = 2;
        public static int MG_UNLOCK_IMG_DEPTH = 5;
        public static int MG_UNLOCK_IMG_IR = 3;
        public static int MG_UNLOCK_IMG_IR_PATTERN = 4;
        public static int MG_UNLOCK_IMG_NV21;
        int angle;
        int height;
        byte[] imageData;
        int imageSize;
        int imageType;
        int width;

        public MGULKImage(int n, byte[] byArray, int n2, int n3, int n4, int n5) {
            this.imageType = n;
            this.imageData = byArray;
            this.imageSize = n2;
            this.width = n3;
            this.height = n4;
            this.angle = n5;
        }
    }

    public static enum MGULKPowerMode {
        MG_UNLOCK_POWER_NONE,
        MG_UNLOCK_POWER_LOW,
        MG_UNLOCK_POWER_HIGH;
    }
}
