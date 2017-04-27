package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenxiaoxiong on 15/3/5.
 */
public class PullLinearLayoutManager extends LinearLayoutManager implements ILayoutManager {

    private RecyclerView mRecyclerView;

    public PullLinearLayoutManager(Context context, RecyclerView recylerView) {
        super(context);
        mRecyclerView = recylerView;
    }

    @Override
    public boolean isAtTop() {
        if (getItemCount() == 0) {
            return true;
        }
        //the recyclerview is at top position or not
        if (findViewByPosition(0) != null
                && getChildAt(0) != null
                && findViewByPosition(0) == getChildAt(0)
                && getChildAt(0).getTop() == getTopDecorationHeight(getChildAt(0))//for top decoration
                && isAtTopExtra()) {//for extra condition
            return true;
        }
        return false;
    }

    @Override
    public boolean isAtBottom() {
        if (getItemCount() == 0) {
            return false;
        }

        //the recyclerview is at bottom or not
        if (findViewByPosition(getItemCount() - 1) != null
                && getChildAt(getChildCount() - 1) != null
                && findViewByPosition(getItemCount() - 1) == getChildAt(getChildCount() - 1)
                && getChildAt(getChildCount() - 1).getBottom() + getPaddingBottom() <= mRecyclerView.getBottom()) {
            //getPaddingBottom() for e.g. home rec paddingbottom
            return true;
        }
        return false;
    }

    @Override
    public boolean isAtTopExtra() {
        return true;
    }

    @Override
    public int findFirstVisibleChildPosition() {
        return findFirstVisibleItemPosition();
    }

    @Override
    public View findChildByPosition(int position) {
        return findChildByPosition(position);
    }
}
