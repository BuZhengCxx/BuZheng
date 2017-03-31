package com.bu.zheng.view.richtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class TagImgSpan extends ImageSpan {

    public static final String REGULAR = "\\[(\\S+?)\\]";
    String mPhrase;
    int mSize;
    int resourceId;

    public TagImgSpan(Context context, int resourceId, int verticalAlignment, String phrase, int height) {
        super(context, resourceId, verticalAlignment);
        mPhrase = phrase;
        mSize = height;
        this.resourceId = resourceId;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
