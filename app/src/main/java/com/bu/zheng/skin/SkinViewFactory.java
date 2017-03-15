package com.bu.zheng.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bu.zheng.skin.view.ICustomSkinView;
import com.bu.zheng.skin.view.ISkinView;
import com.bu.zheng.skin.view.ITintSkinView;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public class SkinViewFactory implements LayoutInflater.Factory {

    private LayoutInflater mLayoutInflater;
    private SkinManager mSkinManager;

    public SkinViewFactory(Context context) {
        mSkinManager = SkinManager.getInstance(context.getApplicationContext());
        mLayoutInflater = LayoutInflater.from(context);
    }

    private boolean canChangeSkin(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (name.startsWith(SkinConfig.SKIN_CLASS_NAME_PREFIX)) {
            return true;
        }
        try {
            Class cls = Class.forName(name);
            Class[] clss = cls.getInterfaces();
            if (clss.length > 0) {
                String clsName;
                for (Class item : clss) {
                    clsName = item.getName();
                    if (TextUtils.equals(clsName, ICustomSkinView.class.getName())
                            || TextUtils.equals(clsName, ISkinView.class.getName())
                            || TextUtils.equals(clsName, ITintSkinView.class.getName())) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        if (!canChangeSkin(name)) {
            return null;
        }

        View view;
        try {
            view = mLayoutInflater.createView(name, null, attrs);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InflateException e) {
            return null;
        }

        if (view == null) {
            return null;
        }

        if (mSkinManager != null) {
            mSkinManager.addView(view, attrs);
        }
        return view;
    }
}
