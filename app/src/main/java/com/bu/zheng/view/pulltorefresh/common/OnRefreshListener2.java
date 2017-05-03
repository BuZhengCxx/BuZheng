package com.bu.zheng.view.pulltorefresh.common;

import android.view.View;

/**
 * Created by BuZheng on 2017/5/3.
 * An advanced version of the Listener to listen for callbacks to Refresh.
 * This listener is different as it allows you to differentiate between Pull Ups, and Pull Downs.
 */

public interface OnRefreshListener2<V extends View> {
    /**
     * onPullDownToRefresh will be called only when the user has Pulled from
     * the start, and released.
     */
    void onPullDownToRefresh(final PullToRefreshBase<V> refreshView);

    /**
     * onPullUpToRefresh will be called only when the user has Pulled from
     * the end, and released.
     */
    void onPullUpToRefresh(final PullToRefreshBase<V> refreshView);

}