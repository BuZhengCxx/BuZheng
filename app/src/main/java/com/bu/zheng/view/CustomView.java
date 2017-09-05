package com.bu.zheng.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by BuZheng on 2017/5/9.
 */

public class CustomView extends View {

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //int width = measureDimension(100, widthMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = measureDimension(100, heightMeasureSpec);
//        setMeasuredDimension(width, height);
//    }

    private int measureDimension(int defauleSize, int measureSpec) {
        int result = 0;
        final int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.EXACTLY:
                result = 100;
                break;

            case MeasureSpec.AT_MOST:
                result = defauleSize;
                break;

            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

}
