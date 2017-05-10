package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/5/9.
 */

public class MyScrollViewActivity extends BaseActivity {

    private ImageView mTitleBack;
    private TextView mTitleLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_scrollview_layout);

        initViews();
    }

    private void initViews() {
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("MyScrollView");
        mTitleBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
