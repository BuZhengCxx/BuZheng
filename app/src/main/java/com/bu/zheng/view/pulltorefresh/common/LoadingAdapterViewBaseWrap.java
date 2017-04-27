package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.library.ViewLoadError;
import com.bu.zheng.view.pulltorefresh.library.ViewLoadingFooter;
import com.bu.zheng.view.pulltorefresh.library.ViewLoadingState;
import com.bu.zheng.view.pulltorefresh.library.ViewNoContent;
import com.bu.zheng.view.pulltorefresh.library.ViewNoNetwork;

public abstract class LoadingAdapterViewBaseWrap<T extends AbsListView> extends PullToRefreshAdapterViewBase<T> {

    public static final int STATE_IDLE = 0x01;
    public static final int STATE_LOADING = 0x02;
    public static final int STATE_LOAD_ERROR = 0x03;
    public static final int STATE_NO_NETWORK = 0x04;
    public static final int STATE_NO_CONTENT = 0x05;
    public static final int STATE_LOADINGMORE = 0x06;

    // ---------------Data-----------
    private Context mContext;

    private int mState = STATE_IDLE;
    private boolean mHasMore;
    private boolean mIsLoadingMoreEnable = true;
    private boolean mIsPullToRefreshEnable = true;
    private boolean mMoreThanOneScreen;
    private boolean mLoadMoreVisible = true;

    private OnLoadingListener mLoadingListener;

    // ---------------View-----------
    private AutoFillListLinearLayout mLoadingContainer;
    private ViewLoadingState mLoadingView;
    private ViewLoadingFooter mLoadingFooterView;
    private ViewNoNetwork mNoNetworkView;
    private ViewLoadError mLoadingErrorView;

    private ViewNoContent mNoContentView;
    private boolean mWithNoRetry;
    private int mTipId;

    public LoadingAdapterViewBaseWrap(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LoadingAdapterViewBaseWrap(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LoadingAdapterViewBaseWrap(Context context, Mode mode) {
        super(context, mode);
        mContext = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        if (mRefreshableView instanceof ListView) {
            mRefreshableView.setScrollingCacheEnabled(false);
            mRefreshableView.setAnimationCacheEnabled(false);
            mLoadingContainer = new AutoFillListLinearLayout(mContext);
            mLoadingContainer.setOrientation(VERTICAL);
            mLoadingContainer.setGravity(Gravity.CENTER);

            setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (!mHasMore && totalItemCount > 2 && visibleItemCount == totalItemCount) {
                        //System.out.println("firstVisibleItem:" + firstVisibleItem + "; visibleItemCount:" + visibleItemCount + "; totalItemCount:" + totalItemCount);
                        mMoreThanOneScreen = false;
                        setFooterLoadEndState();
                        resetLoadingView();
                    }
                }
            });

            ((ListView) mRefreshableView).addFooterView(mLoadingContainer);
            addAllStateViews();
        }

        reset();

        setOnRefreshListener(new OnRefreshListener<T>() {
            @Override
            public void onRefresh(PullToRefreshBase<T> refreshView) {
                if (mLoadingListener != null)
                    mLoadingListener.onRefreshing();
            }
        });

        setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (mIsLoadingMoreEnable && mHasMore && mState == STATE_IDLE) {
                    mState = STATE_LOADINGMORE;
                    //resetLoadingView();
                    if (mLoadingListener != null) {
                        mLoadingListener.onLoadingMore();
                    }
                    setFooterLoadingState();
                } else {
                    setFooterLoadEndState();
                }
            }
        });
    }

    public void disableLoadMoreVisible() {
        mLoadMoreVisible = false;
    }

    private void setFooterLoadingState() {
        if (mLoadingFooterView != null) {
            ImageView loading = (ImageView) mLoadingFooterView.findViewById(R.id.loading);
            loading.clearAnimation();
            loading.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bird_loading_anim));
            AnimationDrawable anim = (AnimationDrawable) loading.getDrawable();
            anim.start();
        }
    }

    private void setFooterLoadEndState() {
        if (mLoadingFooterView != null) {
            ImageView loading = (ImageView) mLoadingFooterView.findViewById(R.id.loading);
            loading.clearAnimation();
            loading.setImageResource(R.drawable.loading_img_wuyaover);
        }
    }

    private void addAllStateViews() {
        mLoadingView = new ViewLoadingState(mContext, this);
        mLoadingContainer.addView(mLoadingView);

        mLoadingFooterView = new ViewLoadingFooter(mContext, this);
        mLoadingContainer.addView(mLoadingFooterView);

        mNoNetworkView = new ViewNoNetwork(mContext, this);
        mLoadingContainer.addView(mNoNetworkView);

        mLoadingErrorView = new ViewLoadError(mContext, this);
        mLoadingContainer.addView(mLoadingErrorView);

        mNoContentView = new ViewNoContent(mContext);
        mLoadingContainer.addView(mNoContentView);
    }

    private void hideAllStateViews() {
        if (mLoadingFooterView != null) {
            mLoadingFooterView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mNoNetworkView != null) {
            mNoNetworkView.setVisibility(View.GONE);
        }
        if (mLoadingErrorView != null) {
            mLoadingErrorView.setVisibility(View.GONE);
        }
        if (mNoContentView != null) {
            mNoContentView.setVisibility(View.GONE);
        }
    }

    private void resetLoadingView() {

        hideAllStateViews();

        switch (mState) {
            case STATE_LOADING:
                mLoadingContainer.setAutoFillState();
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mLoadingView.startLoadingAnim();
                }
                break;

            case STATE_LOADINGMORE:
                mLoadingContainer.setWrapState();
                if (mLoadingFooterView != null) {
                    mLoadingFooterView.setVisibility(View.VISIBLE);
                }
                break;

            case STATE_NO_NETWORK:
                mLoadingContainer.setAutoFillState();
                if (mNoNetworkView != null) {
                    mNoNetworkView.setVisibility(View.VISIBLE);
                }
                break;

            case STATE_LOAD_ERROR:
                mLoadingContainer.setAutoFillState();
                if (mLoadingErrorView != null) {
                    mLoadingErrorView.setVisibility(View.VISIBLE);
                }
                break;

            case STATE_NO_CONTENT:
                mLoadingContainer.setAutoFillState();
                if (mNoContentView != null) {
                    mNoContentView.setVisibility(View.VISIBLE);
                    if (mWithNoRetry) {
                        mNoContentView.withNoRetryTip(mTipId);
                    }
                }
                break;

            case STATE_IDLE:
            default:
                if (mHasMore || mMoreThanOneScreen) {
                    mLoadingContainer.setWrapState();

                    if (mLoadMoreVisible) {
                        mLoadingFooterView.setVisibility(View.VISIBLE);
                    }

                } else if (!mMoreThanOneScreen) {
                    mLoadingContainer.setWrapState();
                    mLoadingFooterView.setVisibility(View.GONE);

                } else {
                    mLoadingContainer.setEmptyState();
                    mLoadingFooterView.setVisibility(View.GONE);
                }
        }

        ListAdapter adapter = mRefreshableView.getAdapter();
        if (adapter != null && adapter instanceof BaseAdapter) {
            ((BaseAdapter) adapter).notifyDataSetChanged();
        }
        mLoadingContainer.measure(0, 0);
    }

    public BaseAdapter getAdapter() {
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (adapter != null && adapter instanceof BaseAdapter) {
            return (BaseAdapter) adapter;
        }
        return null;
    }

    public void notifyDataChange() {
        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public void load() {
        if (mState == STATE_IDLE
                || mState == STATE_NO_NETWORK
                || mState == STATE_LOAD_ERROR
                || mState == STATE_NO_CONTENT) {
            mState = STATE_LOADING;
            resetLoadingView();
            if (mLoadingListener != null) {
                mLoadingListener.onLoading();
            }
        }
    }

    public void reLoad() {
        mState = STATE_IDLE;
        mHasMore = false;
        load();
    }

    public void onNoNetwork() {
        setMode(Mode.DISABLED);
        mState = STATE_NO_NETWORK;
        resetLoadingView();
    }

    public void onLoadingError() {
        setMode(Mode.DISABLED);
        mState = STATE_LOAD_ERROR;
        resetLoadingView();
    }

    public void onLoadingError(int tipStrId) {
        setMode(Mode.DISABLED);
        mState = STATE_LOAD_ERROR;
        mLoadingErrorView.setTipText(tipStrId);
        resetLoadingView();
    }

    public void onNoContent() {
        setMode(Mode.DISABLED);
        mState = STATE_NO_CONTENT;
        resetLoadingView();
    }

    public void onNoContentNoRetryTip(int tipId) {
        setMode(Mode.DISABLED);
        mState = STATE_NO_CONTENT;
        mWithNoRetry = true;
        mTipId = tipId;
        resetLoadingView();
    }

    public void onLoadingComplete(boolean hasMore) {
        if (mIsPullToRefreshEnable) {
            setMode(Mode.PULL_FROM_START);
        }
        mHasMore = hasMore;
        mState = STATE_IDLE;

        if (!mHasMore) {
            setFooterLoadEndState();
        } else {
            mMoreThanOneScreen = true;
        }
        resetLoadingView();
    }

    public void onLoadingComplete(Object hasMore) {
        mHasMore = false;
        if (hasMore instanceof Boolean) {
            mHasMore = (Boolean) hasMore;
        }
        if (hasMore instanceof String) {
            mHasMore = !TextUtils.isEmpty((String) hasMore);
        }
        onLoadingComplete(mHasMore);
    }

    public void onRefreshComplete(Object hasMore) {
        mHasMore = false;
        if (hasMore instanceof Boolean) {
            mHasMore = (Boolean) hasMore;
        }
        if (hasMore instanceof String) {
            mHasMore = !TextUtils.isEmpty((String) hasMore);
        }
        onRefreshComplete();
        onLoadingComplete(mHasMore);
    }

    public void reset() {
        mState = STATE_IDLE;
        mHasMore = false;
        setMode(Mode.DISABLED);
        resetLoadingView();
    }

    public void disablePullRefresh() {
        mIsPullToRefreshEnable = false;
    }

    public void setOnLoadingListener(OnLoadingListener loadingListener) {
        mLoadingListener = loadingListener;
    }

    public interface OnLoadingListener {
        void onRefreshing();

        void onLoading();

        void onLoadingMore();
    }
}
