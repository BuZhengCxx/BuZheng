package com.bu.zheng.skin;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by chenxiaoxiong on 2017/3/14.
 */

public class ViewCompat {

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            SDK16.setBackground(view, background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @TargetApi(16)
    static class SDK16 {
        public static void setBackground(View view, Drawable background) {
            view.setBackground(background);
        }
    }

    public static void loadJavaScript(WebView webView, String jsUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try{
                webView.evaluateJavascript(jsUrl, null);
            }catch (Exception e){
                webView.loadUrl(jsUrl);
            }
        } else {
            webView.loadUrl(jsUrl);
        }
    }
}
