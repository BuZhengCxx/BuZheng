package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/5/26.
 */

public class CoordinatorLayoutActivity extends BaseActivity{

    private RecyclerView mRecyclerView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coordinator_demos_layout);

        initViews();
    }

    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);

    }
}
