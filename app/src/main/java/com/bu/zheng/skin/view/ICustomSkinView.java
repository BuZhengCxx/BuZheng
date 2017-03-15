package com.bu.zheng.skin.view;

import android.util.AttributeSet;

import com.bu.zheng.skin.ViewSkinAttr;

/**
 * Created by chenxiaoxiong on 16/4/14.
 */
public interface ICustomSkinView {

    void addStyledAttributes(ViewSkinAttr viewSkinAttr, AttributeSet attributeSet);

    void setStyledAttributes(ViewSkinAttr viewSkinAttr);
}
