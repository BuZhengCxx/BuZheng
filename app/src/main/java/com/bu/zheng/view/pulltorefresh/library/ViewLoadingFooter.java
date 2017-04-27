package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;

/**
 * Created by chenxiaoxiong on 15/3/25.
 */
public class ViewLoadingFooter extends LinearLayout {

    private PullToRefreshBase mPullToRefreshBase ;

    public ViewLoadingFooter(Context context, PullToRefreshBase pullToRefreshBase) {
        super(context);
        mPullToRefreshBase = pullToRefreshBase ;
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_comic_pull_listview_load_more_footer,this,true);
    }
}
