package com.bu.zheng.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.log.Logger;
import com.bu.zheng.util.AndroidUtil;
import com.bu.zheng.util.DataUtil;
import com.bu.zheng.view.adapter.SimpleRecyclerAdapter;
import com.bu.zheng.view.pulltorefresh.common.PullLinearLayoutManager;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created by BuZheng on 2017/4/27.
 */

public class PullToRefreshRecyclerViewActivity extends BaseActivity {

    private ImageView mTitleBack;
    private TextView mTitleLeft;
    private ImageView mTitleRight;

    private PullToRefreshRecyclerView mRecyclerView;
    private TextView mPullText;


    private SimpleRecyclerAdapter mAdapter;

    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_pull_to_refresh_recyclerview_layout);

        initViews();
    }

    private int mPullState;

    private void initViews() {
        mData = DataUtil.getStringListData(40, 0);

        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("PullToRefreshRecyclerView");
        mTitleBack.setOnClickListener(mOnClickListener);
        mTitleRight = (ImageView) findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(mOnClickListener);

        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerview);
        initRecyclerView();

        mPullText = (TextView) mRecyclerView.findViewById(R.id.pull_tip);

        mRecyclerView.setOnPullListener(new PullToRefreshBase.OnPullListener() {
            @Override
            public void onScaleOfLayout(float scaleOfLayout) {
            }
        });
    }

    private void initRecyclerView() {
        try {
            PullLinearLayoutManager layoutManager = new PullLinearLayoutManager(this, mRecyclerView.getRefreshableView());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.getRefreshableView().addItemDecoration(new DividerItemDecorator());
            mAdapter = new SimpleRecyclerAdapter(this, mData);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.onRefreshComplete(false);

            mRecyclerView.setBanner(null);

        } catch (Exception e) {
            Logger.e("Topic", e, "这是一条异常日志");
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;

                case R.id.title_right:
                    mRecyclerView.autoFresh();
                    //Logger.d("Topic", "这是日志message");
                    //Logger.logAppStart();
                    //Logger.logAppExit();
                    //Logger.logChangeAccount();
                    //Logger.logDebug();
                    break;
            }
        }
    };

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
