package com.bu.zheng.util;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AndroidUtil {

    public static boolean isHuaweiPhone() {
        String systemModel = android.os.Build.MODEL;

        if (systemModel.startsWith("HUAWEI")
                || systemModel.startsWith("huawei")) {
            return true;
        }

        return false;
    }

    public static boolean isMxPhone() {
        String model = android.os.Build.BRAND;

        if (!TextUtils.isEmpty(model)) {
            return model.equals("Meizu");
        }

        return false;
    }

    public static boolean isMx3Phone() {

        String model = android.os.Build.MODEL;

        if (!TextUtils.isEmpty(model)) {
            return model.equals("M351");
        }

        return false;
    }

    public static boolean isMIOSPhone() {
        String model = android.os.Build.MODEL;

        boolean isMi = false;
        if (!TextUtils.isEmpty(model)) {
            isMi = model.startsWith("Mi");
        }

        if (!isMi) {
            String manu = android.os.Build.MANUFACTURER;
            isMi = (manu != null) ? "Xiaomi".equalsIgnoreCase(manu) : false;
        }

        return isMi;
    }

    private static boolean mIsCheckedMIUI = false;
    private static boolean mIsMiui = false;

    public synchronized static boolean isMIUI() {
        if (!mIsCheckedMIUI) {
            String miuiName = getSystemProperty("ro.miui.ui.version.name");
            mIsMiui = !TextUtils.isEmpty(miuiName);
            mIsCheckedMIUI = true;
        }

        return mIsMiui;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int[] getScreenWH(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return new int[]{size.x, size.y};
    }
}
