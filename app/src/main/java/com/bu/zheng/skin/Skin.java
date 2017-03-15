package com.bu.zheng.skin;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public class Skin {
    public String id;//皮肤id
    public String title;
    public String img;
    public int version;
    public String skinUrl;

    private String filePath;//下载后的本地文件路径
    private String apkPkgName;//apk包名

    private Context mContext;
    private IResources mISkinRes;

    public Skin() {
    }

    public Skin(String id) {
        this.id = id;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public IResources getResources() {
        if (mISkinRes != null) {
            return mISkinRes;
        }
        if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(apkPkgName)) {
            mISkinRes = new ApkSkinRes(mContext, filePath, apkPkgName);
        }
        return mISkinRes;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setApkPkgName(String apkPkgName) {
        this.apkPkgName = apkPkgName;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
