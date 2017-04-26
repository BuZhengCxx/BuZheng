package com.bu.zheng.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;

/**
 * Created by BuZheng on 2017/4/1.
 */

public class OverScrollListView extends ListView {

    // 滚动状态
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PULLING = 1;
    public static final int STATE_SPRING = 2;
    public static final int STATE_REFRESH = 3;
    private int mState;

    private View mHeader;
    private LinearLayout mContainer;
    private ValueAnimator mSprinAnimator;

    private RelativeLayout.LayoutParams mLp;
    private int mTopMargin;
    private static int MIN_TOP_MARGIN;
    private static int MAX_TOP_MARGIN;
    private static final float FACTOR_PULL = 0.3f;
    private static final long SPRIN_DURATION = 300;
    private static final float FACTOR_SCALE = 0.3f;

    private Context mContext;

    public OverScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OverScrollListView, 0, 0);
            MIN_TOP_MARGIN = typedArray.getDimensionPixelSize(R.styleable.OverScrollListView_rank_top_margin,0);
            typedArray.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mHeader = findViewById(R.id.header);
        mContainer = (LinearLayout) findViewById(R.id.top_container);

        MAX_TOP_MARGIN = AndroidUtil.dip2px(mContext, 100) + MIN_TOP_MARGIN;

        mSprinAnimator = ValueAnimator.ofFloat(0f, 1f);
        mSprinAnimator.setDuration(SPRIN_DURATION);
        mSprinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                int topMargin = (int) (mTopMargin - (mTopMargin - MIN_TOP_MARGIN) * value.floatValue());
                doChangeTopMargin(topMargin);
            }
        });
        mSprinAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mState = STATE_SPRING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = STATE_NORMAL;
            }
        });
    }

    private int mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        boolean handled = false;

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                handled = false;
                break;

            case MotionEvent.ACTION_MOVE:
                handled = onTouchMove(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handled = onTouchUp();
                break;
        }
        mLastY = (int) ev.getY();

        if (handled) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private boolean onTouchMove(MotionEvent event) {
        int dy = (int) (event.getY() - mLastY);
        switch (mState) {
            case STATE_NORMAL:
                if (mHeader != null && mHeader.getTop() == 0 && dy > 0) {
                    mState = STATE_PULLING;
                    changeTopMargin(dy);
                    return true;
                } else {
                    return false;
                }

            case STATE_PULLING:
                changeTopMargin(dy);
                break;
        }
        return false;
    }

    private void changeTopMargin(int dy) {
        mLp = (RelativeLayout.LayoutParams) mContainer.getLayoutParams();
        int topMargin = mLp.topMargin;
        int move = (int) (dy * FACTOR_PULL);

        if (topMargin + move > MAX_TOP_MARGIN) {
            mTopMargin = MAX_TOP_MARGIN;
        } else {
            mTopMargin = topMargin + move;
        }
        doChangeTopMargin(mTopMargin);
    }

    private void doChangeTopMargin(int topMargin) {
        mLp = (RelativeLayout.LayoutParams) mContainer.getLayoutParams();
        mLp.topMargin = topMargin;
        mContainer.setLayoutParams(mLp);
        requestLayout();

        if (topMargin > MIN_TOP_MARGIN && topMargin < MAX_TOP_MARGIN) {
            if (mOnScaleListener != null) {
                float scale = (topMargin - MIN_TOP_MARGIN) * 1.0f / (MAX_TOP_MARGIN - MIN_TOP_MARGIN);
                mOnScaleListener.onScale(scale * FACTOR_SCALE);
            }
        }
    }

    public boolean onTouchUp() {
        if (mState == STATE_PULLING) {
            mState = STATE_SPRING;
            mSprinAnimator.cancel();
            mSprinAnimator.start();
            return true;
        }
        return false;
    }

    private OnScaleListener mOnScaleListener;

    public interface OnScaleListener {
        void onScale(float scale);
    }

    public void setOnScaleListener(OnScaleListener listener) {
        mOnScaleListener = listener;
    }
}
