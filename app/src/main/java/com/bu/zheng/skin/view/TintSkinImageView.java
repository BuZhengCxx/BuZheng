package com.bu.zheng.skin.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bu.zheng.R;
import com.bu.zheng.skin.SkinManager;
import com.bu.zheng.skin.SkinUtils;
import com.bu.zheng.skin.ViewSkinAttr;

/**
 * Created by chenxiaoxiong on 16/4/20.
 */
public class TintSkinImageView extends ImageView implements ITintSkinView {

    public TintSkinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TintSkinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTintColor(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintColor);
        if (typedArray != null) {
            int resId;
            TypedValue typedValue = typedArray.peekValue(R.styleable.TintColor_tint_color);
            if (typedValue != null) {
                resId = typedValue.resourceId;
                viewSkinAttr.setTintColorResId(resId);

                typedValue = typedArray.peekValue(R.styleable.TintColor_tint_drawable);
                if (typedValue != null) {
                    resId = typedValue.resourceId;
                    viewSkinAttr.setTintDrawableResId(resId);
                }
            }
            typedArray.recycle();
        }
    }

    @Override
    public void setTintColor(ViewSkinAttr viewSkinAttr) {
        int resId = viewSkinAttr.getTintColorResId();
        if (resId != -1) {
            Drawable drawable = SkinManager.getInstance(getContext()).getViewManager().getDrawable(viewSkinAttr.getTintDrawableResId());
            int tint = SkinManager.getInstance(getContext()).getViewManager().getColor(resId);
            SkinUtils.tintColor(this, drawable, tint);
        }
    }

    @Override
    public void addTintColorStateList(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintColor);
        if (typedArray != null) {
            int resId;
            TypedValue typedValue = typedArray.peekValue(R.styleable.TintColor_tint_color_list);
            if (typedValue != null) {
                resId = typedValue.resourceId;
                viewSkinAttr.setTintColorListResId(resId);

                typedValue = typedArray.peekValue(R.styleable.TintColor_tint_drawable);
                if (typedValue != null) {
                    resId = typedValue.resourceId;
                    viewSkinAttr.setTintDrawableResId(resId);
                }
            }
            typedArray.recycle();
        }
    }

    @Override
    public void setTintColorStateList(ViewSkinAttr viewSkinAttr) {
        int resId = viewSkinAttr.getTintColorListResId();
        if (resId != -1) {
            Drawable drawable = SkinManager.getInstance(getContext()).getViewManager().getDrawable(viewSkinAttr.getTintDrawableResId());
            ColorStateList tint = SkinManager.getInstance(getContext()).getViewManager().getColorStateList(resId);
            SkinUtils.tintColorStateList(this, drawable, tint);
        }
    }
}
