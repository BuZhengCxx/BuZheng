package com.bu.zheng.view.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/4/1.
 */

public abstract class ShaderHelper {

//    private final static int ALPHA_MAX = 255;

    protected int mViewWidth;
    protected int mViewHeight;

    protected int mBorderColor = Color.BLACK;
    protected int mBorderWidth = 1;
    //    protected float mBorderAlpha = 1f;
    protected boolean mSquare = false;

    protected final Paint mBorderPaint;
    protected final Paint mShadowPaint;
    protected final Paint mImagePaint;
    protected BitmapShader mShader;
    //    protected Drawable mDrawable;
    protected Bitmap mBitmap;
    protected Matrix mMatrix = new Matrix();
    protected boolean mNeedBord = false;
    protected boolean mNeedShadow = false;

    public ShaderHelper() {

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);

        mImagePaint = new Paint();
        mImagePaint.setAntiAlias(true);

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
    }

    public abstract void draw(Canvas canvas, Paint imagePaint, Paint borderPaint, Paint shadowPaint);
    public abstract void reset();

    public abstract void calculate(int bitmapWidth, int bitmapHeight, float width, float height, float scale, float translateX, float translateY);

    /**
     *
     * @param array this array will be recycled in the super class.
     */
    protected abstract void onInit(TypedArray array);

    public boolean isSquare() {
        return mSquare;
    }

    public void setNeedBord(boolean need){
        mNeedBord = need;
    }

    private void setBorder(int width,int color){
        if (width > 0){
            mNeedBord = true;
            mBorderWidth = width;
            mBorderColor = color;
//            mBorderAlpha = alpha;
            mBorderPaint.setColor(mBorderColor);
//            mBorderPaint.setAlpha(Float.valueOf(mBorderAlpha * ALPHA_MAX).intValue());
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    private void setShadow(int color){
        if (color > 0){
            mShadowPaint.setColor(color);
        }
    }

    public final void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView, defStyle, 0);
        int width = a.getDimensionPixelSize(R.styleable.ShaderImageView_shader_border_width, 0);
        int borderColor = a.getColor(R.styleable.ShaderImageView_shader_border_color, Color.BLACK);
        int shadowColor = a.getColor(R.styleable.ShaderImageView_shader_shadow_color, 0);
//        float alpha = a.getFloat(R.styleable.ShaderImageView_shader_border_alpha,1.0f);
        onInit(a);
        a.recycle();
        setBorder(width, borderColor);
        setShadow(shadowColor);
    }

    public void setMatrix(Matrix matrix){
        mMatrix = matrix;
    }


    public boolean onDraw(Canvas canvas) {
        if (mShader == null) {
            mShader = createShader();
        }
        if (mShader != null && mViewWidth > 0 && mViewHeight > 0) {
            mImagePaint.setShader(mShader);
            mShader.setLocalMatrix(mMatrix);
            draw(canvas, mImagePaint, mBorderPaint,mShadowPaint);
            return true;
        }

        return false;
    }

    public void onSizeChanged(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
        if(isSquare()) {
            mViewWidth = mViewHeight = Math.min(width, height);
        }
        if(mShader != null) {
            calculateDrawableSizes();
        }
    }

    public Bitmap calculateDrawableSizes() {
        Bitmap bitmap = getBitmap();
        if(bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            if(bitmapWidth > 0 && bitmapHeight > 0) {
                mMatrix.reset();
                int borderWidth = mNeedBord ? mBorderWidth : 0;
                float width = mViewWidth - 2f * borderWidth;
                float height = mViewHeight - 2f * borderWidth;

                float scale;
                float translateX = 0;
                float translateY = 0;

                if (bitmapWidth * height > width * bitmapHeight) {
                    scale = height / bitmapHeight;
                    translateX = (width - bitmapWidth * scale) / 2f;
                } else {
                    scale = width / bitmapWidth;
                    translateY = (height - bitmapHeight * scale) / 2f;
                }

                mMatrix.setScale(scale, scale);
                mMatrix.postTranslate((int)(translateX + 0.5) + (mNeedBord ? mBorderWidth : 0), (int)(translateY + 0.5) + (mNeedBord ? mBorderWidth : 0));
                float[] value = new float[9];
                mMatrix.getValues(value);
                scale = value[Matrix.MSCALE_X];
                translateX = value[Matrix.MTRANS_X];
                translateY = value[Matrix.MTRANS_Y];
                calculate(bitmapWidth, bitmapHeight, width, height, scale, translateX, translateY);

                return bitmap;
            }
        }

        reset();
        return null;
    }

    public final void onImageDrawableReset(Bitmap bitmap) {
//        mDrawable = drawable;
        mBitmap = bitmap;
        mShader = null;
        mImagePaint.setShader(null);
    }

    protected BitmapShader createShader() {
        Bitmap bitmap = calculateDrawableSizes();
        if(bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            return new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        return null;
    }

    public Bitmap getBitmap() {
//        Bitmap bitmap = null;
//        if(mDrawable != null) {
//            if(mDrawable instanceof BitmapDrawable) {
//                bitmap = ((BitmapDrawable) mDrawable).getBitmap();
//            }
//        }

        return mBitmap;
    }

    public boolean showShadow(boolean show){

        if(show == mNeedShadow){
            return false;
        }

        mNeedShadow = show;

        return true;
    }

}


