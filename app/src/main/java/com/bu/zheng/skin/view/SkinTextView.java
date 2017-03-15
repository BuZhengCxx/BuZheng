package com.bu.zheng.skin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bu.zheng.skin.SkinManager;
import com.bu.zheng.skin.ViewSkinAttr;


/**
 * Created by chenxiaoxiong on 16/3/29.
 */
public class SkinTextView extends TextView {
    public SkinTextView(Context context) {
        super(context);
    }

    public SkinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 应对动态设置文字颜色
     *
     * @param resId 文字颜色的res id
     */
    public void setTextSkinColorResId(int resId) {
        ViewSkinAttr attr = SkinManager.getInstance(getContext()).getViewManager().addTextColor(this, resId);
        if (attr != null) {
            //应对动态调用中的初始化皮肤设置
            SkinManager.getInstance(getContext()).getViewManager().setViewSkin(this, attr);
        }
    }

    public void setTextSkinDrawableLeftId(int resId) {
        ViewSkinAttr attr = SkinManager.getInstance(getContext()).getViewManager().addTextDrawableLeft(this, resId);
        if (attr != null) {
            SkinManager.getInstance(getContext()).getViewManager().setViewSkin(this, attr);
        }
    }

    public void setTextSkinDrawableRightId(int resId) {
        ViewSkinAttr attr = SkinManager.getInstance(getContext()).getViewManager().addTextDrawableRight(this, resId);
        if (attr != null) {
            SkinManager.getInstance(getContext()).getViewManager().setViewSkin(this, attr);
        }
    }
}
