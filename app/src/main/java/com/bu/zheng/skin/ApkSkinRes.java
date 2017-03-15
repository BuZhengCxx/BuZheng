package com.bu.zheng.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public class ApkSkinRes implements IResources {

    private Resources mDefaultRes;
    public Resources mSkinApkRes;
    private String mSkinApkPkgName;

    public ApkSkinRes(Context context, String skinApkPath, String skinApkPkgName) throws IllegalArgumentException {

        Context appContext = context.getApplicationContext();
        mDefaultRes = appContext.getResources();
        mSkinApkPkgName = skinApkPkgName;

        mSkinApkRes = SkinUtils.getResFromSkinApk(appContext, skinApkPath);
        if (mSkinApkRes == null) {
            throw new IllegalArgumentException("SkinApkRes can not be null");
        }
    }

    @Override
    public Drawable getDrawable(int resId) throws Resources.NotFoundException {
        int parsedId = getIdentifier(resId);
        if (parsedId <= 0) {
            return null;
        }
        return mSkinApkRes.getDrawable(parsedId);
    }

    @Override
    public int getColor(int resId) throws Resources.NotFoundException {
        int parsedId = getIdentifier(resId);
        if (parsedId <= 0) {
            return -1;
        }
        return mSkinApkRes.getColor(parsedId);
    }

    @Override
    public ColorStateList getColorStateList(int resId) throws Resources.NotFoundException {
        int parsedId = getIdentifier(resId);
        if (parsedId <= 0) {
            return null;
        }
        return mSkinApkRes.getColorStateList(parsedId);
    }

    @Override
    public float getDimension(int resId) throws Resources.NotFoundException {
        int parsedId = getIdentifier(resId);
        if (parsedId <= 0) {
            return -1;
        }
        return mSkinApkRes.getDimension(parsedId);
    }

    @Override
    public int getIdentifier(int resId) {
        return getParsedId(resId);
    }

    public int getParsedId(int resId) {
        if (mSkinApkRes == null) {
            return -1;
        }

        String resTypeName = mDefaultRes.getResourceTypeName(resId);
        String resEntryName = mDefaultRes.getResourceEntryName(resId);

        return mSkinApkRes.getIdentifier(resEntryName, resTypeName, mSkinApkPkgName);
    }
}
