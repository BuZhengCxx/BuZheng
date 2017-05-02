package com.bu.zheng.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;
import com.bu.zheng.view.pulltorefresh.common.ILayoutManager;
import com.bu.zheng.view.pulltorefresh.library.IRecyclerFooter;

/**
 * Created by BuZheng on 2017/5/2.
 */

public class OverScrollRecyclerView extends RecyclerView {

    // 滚动状态
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PULLING = 1;
    public static final int STATE_SPRING = 2;
    public static final int STATE_REFRESH = 3;
    private int mState;

    private View mHeader;
    private RelativeLayout mContainer;
    private ValueAnimator mSprinAnimator;

    private LinearLayout.LayoutParams mLp;
    private int mTopMargin;
    private static int MIN_TOP_MARGIN;
    private static int MAX_TOP_MARGIN;
    private static int MAX_PULL_DISTANCE;
    private static final float FACTOR_PULL = 0.4f;
    private static final long SPRIN_DURATION = 300;

    private Context mContext;

    private int mTopWidth;
    private int mTopHeight;

    private ILayoutManager mLayoutManager;
    private boolean mPullToRefreshEnable = true;
    private boolean mPullToLoadMoreEnable = true;
    private boolean mHasMore;

    public OverScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OverScrollRecyclerView, 0, 0);
            MIN_TOP_MARGIN = typedArray.getDimensionPixelSize(R.styleable.OverScrollRecyclerView_top_margin, 0);
            typedArray.recycle();
        }
        mTopWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mTopHeight = (int) (mTopWidth * 0.563f);

        addOnScrollListener(mOnScrollListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        findViews();

        MAX_PULL_DISTANCE = AndroidUtil.dip2px(mContext, 140);

        MAX_TOP_MARGIN = MAX_PULL_DISTANCE + MIN_TOP_MARGIN;

        mSprinAnimator = ValueAnimator.ofFloat(0f, 1f);
        mSprinAnimator.setDuration(SPRIN_DURATION);
        mSprinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = ((Float) animation.getAnimatedValue()).floatValue();
                if (factor == 1) {
                    if (mOnLoadingListener != null && mState == STATE_REFRESH) {
                        mOnLoadingListener.onRefreshing();
                    }
                    mState = STATE_NORMAL;
                }

                int topMargin = (int) (mTopMargin - (mTopMargin - MIN_TOP_MARGIN) * factor);
                doChangeTopMargin(topMargin);
            }
        });
    }

    private void findViews() {
        View header = getChildAt(0);
        if (header != null) {
            mHeader = header.findViewById(R.id.header);
            //mContainer = (RelativeLayout) header.findViewById(R.id.container);
        }
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
        if (mContainer == null || mHeader == null) {
            findViews();
        }

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
        if (mContainer == null) {
            findViews();
        }

        if (mContainer != null) {
            mLp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
            int topMargin = mLp.topMargin;
            int move = (int) (dy * FACTOR_PULL);

            if (topMargin + move > MAX_TOP_MARGIN) {
                mTopMargin = MAX_TOP_MARGIN;

            } else if (topMargin + move < MIN_TOP_MARGIN) {
                mTopMargin = MIN_TOP_MARGIN;

            } else {
                mTopMargin = topMargin + move;
            }
            doChangeTopMargin(mTopMargin);
        }
    }

    private void doChangeTopMargin(int topMargin) {
        if (mContainer == null) {
            findViews();
        }
        if (mContainer != null) {


            if (mOnScaleListener != null) {
                float scale = (topMargin - MIN_TOP_MARGIN) * 1.0f / mTopHeight;
                mOnScaleListener.onScale(scale);
            }

            mLp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
            mLp.topMargin = topMargin;
            mContainer.setLayoutParams(mLp);
        }
    }

    public boolean onTouchUp() {
        if (mState == STATE_PULLING) {

            mLp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
            int topMargin = mLp.topMargin;
            if ((topMargin - MIN_TOP_MARGIN) > MAX_PULL_DISTANCE / 2) {
                mState = STATE_REFRESH;
            } else {
                mState = STATE_SPRING;
            }
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


    public interface OnLoadingListener {
        void onRefreshing();

        void onLoadMore();
    }

    private OnLoadingListener mOnLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mLayoutManager = (ILayoutManager) recyclerView.getLayoutManager();

            if (mPullToLoadMoreEnable && mHasMore && mLayoutManager.isAtBottom() && mOnLoadingListener != null) {
                mOnLoadingListener.onLoadMore();
            }
        }
    };

    public void onRefreshComplete(boolean hasMore) {
        mState = STATE_NORMAL;
        mHasMore = hasMore;
        if (!mHasMore) {
            hideFooterView();
        } else {
            showFooterView();
        }
    }

    public void onRefreshComplete(Object hasMore) {
        if (hasMore == null) {
            onRefreshComplete(false);
            return;
        }

        if (hasMore instanceof Boolean) {
            mHasMore = (Boolean) hasMore;
        }

        if (hasMore instanceof String) {
            mHasMore = !TextUtils.isEmpty((String) hasMore);
        }

        onRefreshComplete(mHasMore);
    }

    public void disablePullToLoadMore() {
        mPullToLoadMoreEnable = false;
        hideFooterView();
    }

    public void disablePullToRefresh() {
        mPullToRefreshEnable = false;
    }

    private void hideFooterView() {
        if (getAdapter() != null) {
            ((IRecyclerFooter) getAdapter()).hideFooterView();
        }
    }

    private void showFooterView() {
        if (getAdapter() != null) {
            ((IRecyclerFooter) getAdapter()).showFooterView();
        }
    }
}
