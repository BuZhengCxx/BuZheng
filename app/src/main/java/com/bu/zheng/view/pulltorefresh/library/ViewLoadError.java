package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;

/**
 * Created by chenxiaoxiong on 15/3/25.
 */
public class ViewLoadError extends LinearLayout {

    private PullToRefreshBase mPullToRefreshBase;
    private LoadingStateLayout mLoadingStateLayout;

    private TextView mTipText;

    public ViewLoadError(Context context, PullToRefreshBase pullToRefreshBase) {
        super(context);
        mPullToRefreshBase = pullToRefreshBase;
        init(context);
    }

    public ViewLoadError(Context context, LoadingStateLayout loadingStateLayout) {
        super(context);
        mLoadingStateLayout = loadingStateLayout;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.view_pull_load_error_listview, this, true);
        mTipText = (TextView) root.findViewById(R.id.pulllist_error_text2);
        mTipText.setOnClickListener(mOnClickListener);
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

    public void setTipText(int strId) {
        mTipText.setText(strId);
    }

    public void setTipText(String str) {
        mTipText.setText(str);
    }
}
