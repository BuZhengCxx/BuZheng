package com.bu.zheng.view.shape;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by BuZheng on 2017/4/1.
 */

public class CircularImageView extends ShaderImageView {

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new CircleShader();
    }
}