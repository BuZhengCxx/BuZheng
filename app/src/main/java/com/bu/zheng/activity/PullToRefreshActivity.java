package com.bu.zheng.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bu.zheng.R;
import com.bu.zheng.util.DataUtil;
import com.bu.zheng.view.adapter.SimpleRecyclerAdapter;
import com.bu.zheng.view.pulltorefresh.common.PullLinearLayoutManager;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created by BuZheng on 2017/4/27.
 */

public class PullToRefreshActivity extends BaseActivity {

    private PullToRefreshRecyclerView mRecyclerView;

    private SimpleRecyclerAdapter mAdapter;

    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pull_to_refresh_layout);

        initViews();
    }

    private void initViews() {
        mData = DataUtil.getStringListData(20, 0);

        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerview);
        initRecyclerView();
    }

    private void initRecyclerView() {
        PullLinearLayoutManager layoutManager = new PullLinearLayoutManager(this, mRecyclerView.getRefreshableView());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.getRefreshableView().addItemDecoration(new DividerItemDecorator());
        mAdapter = new SimpleRecyclerAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.onRefreshComplete(false);
    }

    class DividerItemDecorator extends RecyclerView.ItemDecoration {

        private Drawable divider;
        private int height;

        public DividerItemDecorator() {
            divider = new ColorDrawable(getResources().getColor(R.color.bg_color_e9e9e9));
            height = 1;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildLayoutPosition(view);
            int count = mData.size() + 1;
            if (position == count - 1 || position == count - 2) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, height);
            }
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            View child;
            RecyclerView.LayoutParams layoutParams;

            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(child);

                if (position != mData.size() && position != mData.size() - 1) {
                    layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

                    int top = child.getBottom() + layoutParams.bottomMargin;
                    int bottom = top + height;
                    divider.setBounds(left, top, right, bottom);
                    divider.draw(canvas);
                }
            }
        }
    }
}
