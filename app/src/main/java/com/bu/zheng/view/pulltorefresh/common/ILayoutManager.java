package com.bu.zheng.view.pulltorefresh.common;

import android.view.View;

/**
 * Created by chenxiaoxiong on 15/4/17.
 */
public interface ILayoutManager {

    boolean isAtTop();

    boolean isAtBottom();

    boolean isAtTopExtra();

    int findFirstVisibleChildPosition();

    View findChildByPosition(int position);
}
