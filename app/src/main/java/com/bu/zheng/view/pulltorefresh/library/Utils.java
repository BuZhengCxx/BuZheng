package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

    static final String LOG_TAG = "PullToRefresh";

    public static void warnDeprecation(String depreacted, String replacement) {
        Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        NetworkInfo info = null;

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return info;
        }

        NetworkInfo[] infos = connectivity.getAllNetworkInfo();
        if (infos == null) {
            return info;
        }

        for (int i = 0; i < infos.length; i++) {
            NetworkInfo tmp = infos[i];
            if (null != tmp && tmp.isAvailable()
                    && tmp.isConnectedOrConnecting()) {
                info = tmp;
                break;
            }
        }

        return info;
    }
}
