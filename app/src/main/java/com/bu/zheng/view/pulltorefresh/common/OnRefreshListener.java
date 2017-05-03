package com.bu.zheng.view.pulltorefresh.common;

import android.view.View;

/**
 * Created by BuZheng on 2017/5/3.
 *
 * Simple Listener to listen for any callbacks to Refresh.
 */

public interface OnRefreshListener<V extends View> {
    /**
     * onRefresh will be called for both a Pull from start, and Pull from end
     */
    void onRefresh(final PullToRefreshBase<V> refreshView);

}