package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.library.IRecyclerFooter;

/**
 * Created by chenxiaoxiong on 15/3/5.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    private ILayoutManager mLayoutManager;

    private boolean mPullToRefreshEnable = true;
    private boolean mPullToLoadMoreEnable = true;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRefreshableView.addOnScrollListener(mOnScrollListener);
        //set the pull listener
        setOnRefreshListener(new OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (mOnLoadingListener != null) {
                    mOnLoadingListener.onRefreshing();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerview = new RecyclerView(context, attrs);
        recyclerview.setId(R.id.recyclerview);
        return recyclerview;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        if (!mPullToRefreshEnable) {
            return false;
        }
        if (mLayoutManager == null) {
            return true;
        }
        return mLayoutManager.isAtTop();
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //ImageDisplayer.resumeRequests(recyclerView.getContext());
                    break;
                default:
                    //ImageDisplayer.pauseRequests(recyclerView.getContext());
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

            handleTagScroll();
        }
    };

    protected void handleTagScroll() {
    }

    public void load() {
        if (mOnLoadingListener != null) {
            mOnLoadingListener.onLoading();
        }
    }

    private boolean mHasMore;

    public void onRefreshComplete(boolean hasMore) {
        onRefreshComplete();
        mHasMore = hasMore;
        setHasMore(hasMore);
        if (!mHasMore) {
            hideFooterView();
        } else {
            showFooterView();
        }
    }

    public void onLoadingComplete(boolean hasMore) {
        mHasMore = hasMore;
        setHasMore(hasMore);
        if (!mHasMore) {
            hideFooterView();
        } else {
            showFooterView();
        }
    }

    public void disablePullToLoadMore() {
        mPullToLoadMoreEnable = false;
        hideFooterView();
    }

    public void disablePullToRefresh() {
        mPullToRefreshEnable = false;
    }

    private void setHasMore(boolean hasMore){
        if(getAdapter() != null){
            getAdapter().setHasMore(hasMore);
        }
    }

    private void hideFooterView() {
        if (getAdapter() != null) {
            getAdapter().hideFooterView();
        }
    }

    private void showFooterView() {
        if (getAdapter() != null) {
            getAdapter().showFooterView();
        }
    }

    private IRecyclerFooter getAdapter() {
        if (getRefreshableView().getAdapter() instanceof IRecyclerFooter) {
            return ((IRecyclerFooter) getRefreshableView().getAdapter());
        }
        return null;
    }

    public interface OnLoadingListener {
        void onLoading();

        void onRefreshing();

        void onLoadMore();
    }

    private OnLoadingListener mOnLoadingListener;

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRefreshableView.setHasFixedSize(hasFixedSize);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRefreshableView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRefreshableView.setAdapter(adapter);
    }
}
