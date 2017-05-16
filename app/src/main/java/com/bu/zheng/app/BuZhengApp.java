package com.bu.zheng.app;

import android.app.Application;

/**
 * Created by BuZheng on 2017/5/14.
 */

public class BuZhengApp extends Application {

    private static BuZhengApp sApplication;

    public static BuZhengApp instance() {
        if (null == sApplication) {
            throw new NullPointerException("app not create should be terminated!");
        }
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
