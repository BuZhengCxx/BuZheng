package com.bu.zheng.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bu.zheng.R;
import com.bu.zheng.skin.ViewCompat;

import java.lang.reflect.Field;

public class ToastUtil {

    public static Toast showToast(Context context, int resId) {
        return showToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static Toast showToast(Context context, String content) {
        return showToast(context, content, Toast.LENGTH_SHORT);
    }

    public static Toast showToast(Context context, int resId, int duration) {
        Toast toast = Toast.makeText(context, getShowText(context.getResources().getString(resId)), duration);
        try {
            View v = toast.getView();
            if (v != null) {
                ViewCompat.setBackground(v, null);
                TextView tv = (TextView) v.findViewById(android.R.id.message);
                if (tv != null) {
                    ViewCompat.setBackground(tv, context.getResources().getDrawable(R.drawable.pub_img_tips));
                    tv.setTextColor(context.getResources().getColor(R.color.txtcolor8));
                    tv.setGravity(Gravity.CENTER);
                }
            }
        } catch (Exception e) {

        }
        toast.show();
        return toast;
    }

    public static Toast showToast(Context context, String content, int duration) {
        Toast toast = Toast.makeText(context, getShowText(content), duration);
        try {
            View v = toast.getView();
            if (v != null) {
                ViewCompat.setBackground(v, null);
                TextView tv = (TextView) v.findViewById(android.R.id.message);
                if (tv != null) {
                    ViewCompat.setBackground(tv, context.getResources().getDrawable(R.drawable.pub_img_tips));
                    tv.setTextColor(context.getResources().getColor(R.color.txtcolor8));
                    tv.setGravity(Gravity.CENTER);
                }
            }
        } catch (Exception e) {

        }
        toast.show();
        return toast;
    }

    public static Toast showContentCenterToast(Context context, int resId, int duration) {
        Toast toast = Toast.makeText(context, getShowText(context.getResources().getString(resId)), duration);
        try {
            View v = toast.getView();
            if (v != null) {
                ViewCompat.setBackground(v, null);
                TextView tv = (TextView) v.findViewById(android.R.id.message);
                if (tv != null) {
                    ViewCompat.setBackground(tv, context.getResources().getDrawable(R.drawable.pub_img_tips));
                    tv.setTextColor(context.getResources().getColor(R.color.txtcolor8));
                    tv.setGravity(Gravity.CENTER);
                }
            }
        } catch (Exception e) {

        }

        toast.show();
        return toast;
    }

    public static Toast showContentCenterToast(Context context, int resId) {
        return showContentCenterToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static Toast showContentCenterToast(Context context, String str, int duration) {
        Toast toast = Toast.makeText(context, getShowText(str), duration);
        try {
            View v = toast.getView();
            if (v != null) {
                ViewCompat.setBackground(v, null);
                TextView tv = (TextView) v.findViewById(android.R.id.message);
                if (tv != null) {
                    ViewCompat.setBackground(tv, context.getResources().getDrawable(R.drawable.pub_img_tips));
                    tv.setTextColor(context.getResources().getColor(R.color.txtcolor8));
                    tv.setGravity(Gravity.CENTER);
                }
            }
        } catch (Exception e) {

        }

        toast.show();
        return toast;
    }

    public static Toast showContentCenterAnimToast(Context context, String str, int duration, int animId) {
        Toast toast = Toast.makeText(context, getShowText(str), duration);
        try {
            View v = toast.getView();
            if (v != null) {
                ViewCompat.setBackground(v, null);
                TextView tv = (TextView) v.findViewById(android.R.id.message);
                if (tv != null) {
                    ViewCompat.setBackground(tv, context.getResources().getDrawable(R.drawable.pub_img_tips));
                    tv.setTextColor(context.getResources().getColor(R.color.txtcolor8));
                    tv.setGravity(Gravity.CENTER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Object mTN = getField(toast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams p = (WindowManager.LayoutParams) mParams;
                    p.windowAnimations = animId;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        toast.show();
        return toast;
    }

    public static Toast showContentCenterToast(Context context, String str) {
        return showContentCenterToast(context, str, Toast.LENGTH_SHORT);
    }

    private static Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (object != null && fieldName != null) {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }

    private static CharSequence getShowText(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
}
