package com.bu.zheng.skin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bu.zheng.skin.SkinManager;
import com.bu.zheng.skin.ViewSkinAttr;


/**
 * Created by chenxiaoxiong on 16/3/29.
 */
public class SkinRelativeLayout extends RelativeLayout {
    public SkinRelativeLayout(Context context) {
        super(context);
    }

    public SkinRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 应对动态设置背景
     * @param resId 颜色的res id
     */
    public void setBgColorResId(int resId){
        ViewSkinAttr attr = SkinManager.getInstance(getContext()).getViewManager().addBgColor(this, resId);
        if (attr != null) {
            //应对动态调用中的初始化皮肤设置
            SkinManager.getInstance(getContext()).getViewManager().setViewSkin(this, attr);
        }
    }
}
