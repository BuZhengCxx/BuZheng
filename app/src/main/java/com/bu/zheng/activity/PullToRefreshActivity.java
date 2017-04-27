package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/4/27.
 */

public class PullToRefreshActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pull_to_refresh_layout);

        initViews();
    }

    private void initViews() {

    }
}
