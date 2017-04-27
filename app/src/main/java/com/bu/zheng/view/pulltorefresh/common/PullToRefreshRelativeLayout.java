package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bu.zheng.R;

/**
 * Created by chenxiaoxiong on 15/9/11.
 */
public class PullToRefreshRelativeLayout extends PullToRefreshBase<RelativeLayout> {

    public PullToRefreshRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RelativeLayout createRefreshableView(Context context, AttributeSet attrs) {
        RelativeLayout l = new RelativeLayout(context, attrs);
        l.setId(R.id.relativelayout);
        return l;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        ListView child = (ListView) this.getRefreshableView().getChildAt(0);
        return isFirstItemVisible(child);
    }

    private boolean isFirstItemVisible(ListView listView) {
        final Adapter adapter = listView.getAdapter();
        if (null == adapter) {
            return true;
        } else {
            if (listView.getFirstVisiblePosition() == 0
                    && listView.getChildAt(0) != null
                    && listView.getChildAt(0).getTop() == 0) {
                return true;
            }
        }
        return false;
    }
}
