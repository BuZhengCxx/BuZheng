package com.bu.zheng.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public interface IResources {

    Drawable getDrawable(int id) throws Resources.NotFoundException;

    int getColor(int resId) throws Resources.NotFoundException;

    ColorStateList getColorStateList(int resId) throws Resources.NotFoundException;

    float getDimension(int resId) throws Resources.NotFoundException;

    int getIdentifier(int resId);
}
