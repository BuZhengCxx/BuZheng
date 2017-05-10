package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;

/**
 * Created by BuZheng on 2017/5/8.
 */

public class PullToRefreshScrollViewActivity extends BaseActivity {

    private ImageView mTitleBack;
    private TextView mTitleLeft;
    private ImageView mTitleRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_pull_to_refresh_scrollview_layout);

        initViews();
    }

    private void initViews() {
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("PullToRefreshScrollView");
        mTitleBack.setOnClickListener(mOnClickListener);
        mTitleRight = (ImageView) findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
            }
        }
    };
}
