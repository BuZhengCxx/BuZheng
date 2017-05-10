package com.bu.zheng.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;

/**
 * Created by BuZheng on 2017/5/8.
 */

public class PullToRefreshDemosActivity extends BaseActivity {

    static final String[] mPullDemos = {
            "PullToRefreshRecyclerView",
            "PullToRefreshScrollView",
            "MyScrollView"
    };

    static final Class<?>[] mPullActivitys = {
            PullToRefreshRecyclerViewActivity.class,
            PullToRefreshScrollViewActivity.class,
            MyScrollViewActivity.class
    };

    private ImageView mTitleBack;
    private TextView mTitleLeft;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_demo_list_layout);

        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("PullToRefreshDemos");
        mTitleBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView = (ListView) this.findViewById(R.id.listview);
        mListView.setSelector(new BitmapDrawable(getResources()));
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mPullDemos));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position - ((ListView) parent).getHeaderViewsCount();
                if (index >= 0 && index < mPullActivitys.length) {
                    Intent intent = new Intent(PullToRefreshDemosActivity.this, mPullActivitys[index]);
                    startActivity(intent);
                }
            }
        });
    }
}
