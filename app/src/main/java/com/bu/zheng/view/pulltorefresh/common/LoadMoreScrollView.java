package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by chenxiaoxiong on 15/9/3.
 */
public class LoadMoreScrollView extends ScrollView {

    public LoadMoreScrollView(Context context) {
        super(context);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {

        if (mOnLastVisibleListener != null && scrollY != 0 && clampedY) {
            mOnLastVisibleListener.onLostItemVisible();
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    public interface OnLastVisibleListener {
        void onLostItemVisible();
    }

    private OnLastVisibleListener mOnLastVisibleListener;

    public void setOnLastItemVisibleListener(OnLastVisibleListener listener) {
        mOnLastVisibleListener = listener;
    }
}
