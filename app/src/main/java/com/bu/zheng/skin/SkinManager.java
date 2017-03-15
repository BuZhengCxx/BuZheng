package com.bu.zheng.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.bu.zheng.util.ToastUtil;

import java.io.File;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public class SkinManager {

    private static SkinManager mSkinManager;

    synchronized public static SkinManager getInstance(Context context) {
        if (mSkinManager == null) {
            mSkinManager = new SkinManager(context.getApplicationContext());
        }
        return mSkinManager;
    }

    private ViewManager mViewManager;
    private PackageManager mPackageManager;
    private Context mContext;
    private Skin mSkin;

    private SkinManager(Context context) {
        mContext = context;
        mViewManager = new ViewManager(context, context.getResources(), null);
        mPackageManager = context.getPackageManager();
    }

    public ViewManager getViewManager() {
        return mViewManager;
    }

    public void addView(View view, AttributeSet attributeSet) {
        if (mViewManager != null) {
            ViewSkinAttr viewSkinAttr = mViewManager.addView(view, attributeSet);
            if (viewSkinAttr != null) {
                mViewManager.setViewSkin(view, viewSkinAttr);
            }
        }
    }

    public boolean setSkin(Skin skin) {

        String filePath = SkinUtils.getSkinFilePath(skin);

        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        PackageInfo packageInfo = mPackageManager.getPackageArchiveInfo(filePath, PackageManager.PERMISSION_GRANTED);
        if (packageInfo == null) {
            return false;
        }

        int version = packageInfo.versionCode;
        if (!SkinUtils.skinUsable(version)) {
            ToastUtil.showToast(mContext, "换肤失败");
            return false;
        }

        skin.setContext(mContext);
        skin.setFilePath(filePath);
        skin.setApkPkgName(packageInfo.packageName);
        skin.setVersion(version);

        mSkin = skin;

        if (mViewManager != null) {
            mViewManager.changeSkin(mSkin);
        }
        return true;
    }


    //for custom view
    public int getColor(int resId) {
        if (mViewManager != null) {
            return mViewManager.getColor(resId);
        }
        return -1;
    }

    public ColorStateList getColorStateList(int resId) {
        if (mViewManager != null) {
            return mViewManager.getColorStateList(resId);
        }
        return null;
    }

    public Drawable getDrawable(int resId) {
        if (mViewManager != null) {
            return mViewManager.getDrawable(resId);
        }
        return null;
    }

    public void addBgColorResId(View view, int resId) {
        ViewSkinAttr attr = getViewManager().addBgColor(view, resId);
        if (attr != null) {
            getViewManager().setViewSkin(view, attr);
        }
    }
}
