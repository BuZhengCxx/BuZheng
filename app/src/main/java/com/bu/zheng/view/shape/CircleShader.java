package com.bu.zheng.view.shape;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by BuZheng on 2017/4/1.
 */

public class CircleShader extends ShaderHelper {

    //    private float mCenter;
//    private float mBitmapCenterX;
//    private float mBitmapCenterY;
    private float mBorderRadius;
    private float mBitmapRadius;

    @Override
    protected void onInit(TypedArray array) {
        mSquare = true;
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint, Paint shadowPaint) {
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mBitmapRadius, imagePaint);
        if(mNeedBord){
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mBorderRadius, borderPaint);
        }

        if(mNeedShadow){
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mBitmapRadius, shadowPaint);
        }

    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
//        mCenter = Math.min(mViewWidth / 2f,mViewHeight / 2f);
    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight,
                          float width, float height,
                          float scale,
                          float translateX, float translateY) {

//        mBitmapCenterX = Math.round(bitmapWidth / 2f);
//        mBitmapCenterY = Math.round(bitmapHeight / 2f);
//        mBitmapRadius = Math.round(width / scale / 2f );
        mBitmapRadius = Math.min(width / 2,height / 2);
        mBorderRadius = (Math.min(mViewWidth,mViewHeight) - mBorderWidth) / 2f;
    }

    @Override
    public void reset() {
        mBitmapRadius = 0f;
        mBorderRadius = 0f;
//        mBitmapCenterX = 0;
//        mBitmapCenterY = 0;
    }
}
