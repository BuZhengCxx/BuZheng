package com.bu.zheng.view.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by BuZheng on 2017/4/1.
 */

public abstract class ShaderImageView extends ImageView {
    //single color drawable will be created of 1x1 pixel
    private final static int COLORDRAWABLE_DIMENSION = 1;
    private ShaderHelper mPathHelper;
    private boolean disposable;
    public ShaderImageView(Context context) {
        super(context);
        setup(context, null, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs, defStyle);
    }

    public void setNeedBord(boolean need){
        getPathHelper().setNeedBord(need);
    }
    protected void setup(Context context, AttributeSet attrs, int defStyle) {
        setScaleType(ScaleType.CENTER_CROP);
        getPathHelper().init(context, attrs, defStyle);
        getPathHelper().setMatrix(getImageMatrix());
    }

    protected ShaderHelper getPathHelper() {
        if(mPathHelper == null) {
            mPathHelper = createImageViewHelper();
        }
        return mPathHelper;
    }

    protected abstract ShaderHelper createImageViewHelper();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getPathHelper().isSquare()) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int dimen = Math.min(width, height);
            setMeasuredDimension(dimen, dimen);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getPathHelper().onImageDrawableReset(null);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        getPathHelper().onImageDrawableReset(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getPathHelper().onSizeChanged(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getPathHelper().getBitmap() == null){
            getPathHelper().onImageDrawableReset(getBitmapFromDrawable(getDrawable()));
        }
        if(getPathHelper().onDraw(canvas)) {
            if (disposable){
                getPathHelper().onImageDrawableReset(null);
            }
        }else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if (drawable == null){
            return null;
        }
        disposable = false;
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }else if (drawable instanceof TransitionDrawable){
            disposable = true;
        }
        try {
            Bitmap bitmap;
            Bitmap.Config config = drawable.getOpacity() == PixelFormat.OPAQUE ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
            if (/*drawable instanceof ColorDrawable*/drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0){
                //getIntrinsicWidth() and getIntrinsicHieght() will return -1 if drawable is a solid color
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,COLORDRAWABLE_DIMENSION, config);
            }else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), config);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }

    public void showShadow(boolean b){
        if(getPathHelper().showShadow(b)){
            invalidate();
        }
    }
}
