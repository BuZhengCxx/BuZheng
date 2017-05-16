package com.bu.zheng.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bu.zheng.R;

import java.util.Locale;
import java.util.Random;

/**
 * Created by BuZheng on 2017/5/14.
 */

public class PlatformUtil {

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取网络类型 2g 3g 4g
     *
     * @param context
     * @return
     */
    public static String getMobileType(Context context) {

        NetworkInfo info = ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "WIFI";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int sub = info.getSubtype();
                switch (sub) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2G";
                    default:
                        return "3G+";
                }
            }
        }
        return "UNKNOW";
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {

        String imei = null;
        String androidId = null;
        try {
            imei = getPhoneIMEI(context);
            if (TextUtils.isEmpty(imei)) {
                imei = getWifiMacAddress(context);
            }

            androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(androidId)) {
                imei += "_" + androidId;
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                if (!TextUtils.isEmpty(androidId)) {
                    imei = "_" + androidId;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(imei)) {
            Random rd = new Random(999999999);
            imei = "random_" + rd.nextInt();
        }

        String deviceId = imei.toLowerCase();

        return deviceId;
    }

    public static String getPhoneIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        return imei;
    }

    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    public static String getResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(dm);
        StringBuffer buffer = new StringBuffer();
        buffer.append(dm.widthPixels).append('x').append(dm.heightPixels);
        return buffer.toString();
    }

    /**
     * 获取OS版本
     *
     * @return
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * return en-us
     *
     * @param context
     * @return
     */
    static public String getLocalLanguage(Context context) {
        Locale local = Locale.getDefault();
        return local.getLanguage() + "-" + local.getCountry();
    }

    /**
     * 获取设备名字
     *
     * @return
     */
    public static String getMobileName() {
        return android.os.Build.MODEL;
    }
}
