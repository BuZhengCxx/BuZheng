package com.bu.zheng.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.bu.zheng.view.pulltorefresh.library.IRecyclerFooter;

import java.util.List;

/**
 * Created by BuZheng on 2017/5/2.
 */

public abstract class FooterRecyclerViewAdapter<T> extends RecyclerView.Adapter
        implements IRecyclerFooter {

    protected View mFooterView;
    protected boolean mHasMore;

    protected List<T> mData;
    protected Context mContext;

    protected final int TYPE_FOOTER = 0;

    private boolean mAnimable = false;
    protected int mLastPosition = -1;
    protected LinearInterpolator mInterpolator;
    protected int mDuration = 500;

    protected LayoutInflater mInflater;

    public FooterRecyclerViewAdapter(Context context, List<T> data) {
        this(context, data, false);
    }

    public FooterRecyclerViewAdapter(Context context, List<T> data, boolean animable) {
        mContext = context;
        mData = data;
        mAnimable = animable;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOTER:
                return new FooterViewHolder(getFooterView(parent));

            default:
                return handleOtherViewType(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_FOOTER:
                mFooterView = ((FooterViewHolder) holder).getView();
                if (mHasMore) {
                    showFooterView();
                } else {
                    hideFooterView();
                }
                break;

            default:
                setData(holder, type, position);
                break;
        }
        if (mAnimable) {
            if (position > mLastPosition) {
                startAnim(holder.itemView);
            } else {
                clear(holder.itemView);
            }
            mLastPosition = position;
        }
    }

    @Override
    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.size() == 0) {
            return 0;
        }
        return getDataNum() + 1;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private View footerView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footerView = itemView;
        }

        public View getView() {
            return footerView;
        }
    }

    private void clear(View view) {
        view.setTranslationX(0);
        view.setTranslationY(0);
        view.setScaleX(1);
        view.setScaleY(1);
    }

    protected abstract int getDataNum();

    protected abstract RecyclerView.ViewHolder handleOtherViewType(ViewGroup parent, int viewType);

    protected abstract View getFooterView(ViewGroup parent);

    protected abstract void setData(RecyclerView.ViewHolder holder, int type, int position);

    protected abstract void startAnim(View view);
}
