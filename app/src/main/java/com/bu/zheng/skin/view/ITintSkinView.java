package com.bu.zheng.skin.view;

import android.util.AttributeSet;
import android.view.View;

import com.bu.zheng.skin.ViewSkinAttr;

/**
 * Created by chenxiaoxiong on 16/4/20.
 */
public interface ITintSkinView {

    void addTintColor(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet);

    void setTintColor(ViewSkinAttr viewSkinAttr);

    void addTintColorStateList(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet);

    void setTintColorStateList(ViewSkinAttr viewSkinAttr);
}
