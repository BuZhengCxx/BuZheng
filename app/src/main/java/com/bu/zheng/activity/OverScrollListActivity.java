package com.bu.zheng.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.skin.ViewCompat;
import com.bu.zheng.util.AndroidUtil;
import com.bu.zheng.view.OverScrollListView;
import com.bu.zheng.view.adapter.SimpleListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BuZheng on 2017/4/1.
 */

public class OverScrollListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_over_scroll_listview_layout);

        initViews();
    }

    private RelativeLayout mTitleBar;
    private ImageView mTitleBack;
    private TextView mTitleLeft;
    private View mTitleBackView;

    private ImageView mHeaderImage;
    private View mListHeader;
    private OverScrollListView mOverScrollListView;

    private int mTopWidth;
    private int mTopHeight;

    private SimpleListAdapter mAdapter;

    private static List<String> mData = new ArrayList<>();

    static {
        int count = 20;
        for (int index = 0; index < count; index++) {
            mData.add("Test Data Item " + index);
        }
    }

    private void initViews() {
        mTopWidth = getResources().getDisplayMetrics().widthPixels;
        //mTopHeight = getResources().getDimensionPixelSize(R.dimen.over_scroll_listview_header_image_height);

        mTitleBar = (RelativeLayout) findViewById(R.id.top_title);
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("OverScrollListView");

        mTitleBackView = findViewById(R.id.title_back_view);

        mHeaderImage = (ImageView) findViewById(R.id.top_header_image);
        ViewCompat.setBackground(mHeaderImage, getResources().getDrawable(R.drawable.test));

        mOverScrollListView = (OverScrollListView) findViewById(R.id.listview);
        mOverScrollListView.setSelector(new BitmapDrawable(getResources()));
        mOverScrollListView.setOnScrollListener(mOnScrollListener);
        mOverScrollListView.setOnScaleListener(mOnScaleListener);

        mListHeader = View.inflate(this, R.layout.view_over_scroll_listview_header_layout, null);
        mOverScrollListView.addHeaderView(mListHeader);

        mAdapter = new SimpleListAdapter(this, mData);
        mOverScrollListView.setAdapter(mAdapter);
    }

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mListHeader != null && mAdapter != null && mAdapter.getCount() > 1) {
                int height = mTitleBar.getHeight();
                int headerBottom = mListHeader.getBottom();
                if (headerBottom >= 2 * height) {
                    mTitleBackView.setAlpha(0);
                } else if (headerBottom <= height) {
                    mTitleBackView.setAlpha(1);
                } else {
                    float alpha = Math.abs(headerBottom - height) * 1.0f / height;
                    mTitleBackView.setAlpha(1 - alpha);
                }
            }
        }
    };

    private OverScrollListView.OnScaleListener mOnScaleListener = new OverScrollListView.OnScaleListener() {
        @Override
        public void onScale(float scale) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHeaderImage.getLayoutParams();
            if (mTopHeight == 0) {
                mTopHeight = mHeaderImage.getHeight();
            }
            lp.width = (int) (mTopWidth * (1 + scale));
            lp.height = (int) (mTopHeight * (1 + scale));
            lp.leftMargin = (int) (-mTopWidth * scale * 0.5f);
            lp.topMargin = (int) (-mTopHeight * scale * 0.5f);
            mHeaderImage.setLayoutParams(lp);
        }
    };
}
