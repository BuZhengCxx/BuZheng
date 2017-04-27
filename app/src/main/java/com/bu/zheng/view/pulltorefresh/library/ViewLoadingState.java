package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;

/**
 * Created by chenxiaoxiong on 15/3/25.
 */
public class ViewLoadingState extends LinearLayout {

    private PullToRefreshBase mPullToRefreshBase;

    private LoadingStateLayout mLoadingStateLayout;

    private ImageView mLoadingAnim;

    public ViewLoadingState(Context context, PullToRefreshBase pullToRefreshBase) {
        super(context);
        mPullToRefreshBase = pullToRefreshBase;
        init(context);
    }

    public ViewLoadingState(Context context, LoadingStateLayout loadingStateLayout) {
        super(context);
        mLoadingStateLayout = loadingStateLayout;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.view_pull_loading_listview, this, true);
        mLoadingAnim = (ImageView) root.findViewById(R.id.anim);

        root.setOnClickListener(mOnClickListener);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPullToRefreshBase != null) {
                mPullToRefreshBase.reLoad();
            }

            if (mLoadingStateLayout != null) {
                mLoadingStateLayout.reLoad();
            }
        }
    };

    public void startLoadingAnim() {
        if (mLoadingAnim != null) {
            mLoadingAnim.clearAnimation();
            mLoadingAnim.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_loading_anim));
            AnimationDrawable anim = (AnimationDrawable) mLoadingAnim.getDrawable();
            anim.start();
        }
    }

    public void stopLoadingAnim() {
        if (mLoadingAnim != null) {
            mLoadingAnim.clearAnimation();
        }
    }
}
