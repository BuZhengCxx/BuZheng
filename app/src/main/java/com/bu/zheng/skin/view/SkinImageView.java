package com.bu.zheng.skin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bu.zheng.skin.SkinManager;
import com.bu.zheng.skin.ViewSkinAttr;

/**
 * Created by chenxiaoxiong on 16/3/29.
 */
public class SkinImageView extends ImageView {

    public SkinImageView(Context context) {
        super(context);
    }

    public SkinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);

        //应对动态掉用setImageResource
        ViewSkinAttr attr = SkinManager.getInstance(getContext()).getViewManager().addImageSrc(this, resId);
        if (attr != null) {
            //应对动态掉用中的初始化皮肤设置
            SkinManager.getInstance(getContext()).getViewManager().setViewSkin(this, attr);
        }
    }
}
