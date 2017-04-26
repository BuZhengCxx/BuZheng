package com.bu.zheng.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;

/**
 * Created by BuZheng on 2017/3/17.
 */

public class DemoListActivity extends BaseActivity {

    static final String[] mListItems = {
            "RichText",
            "OverScrollListView"
    };

    static final Class<?>[] mActvities = {
            RichTextActivity.class,
            OverScrollListActivity.class
    };

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_demo_list_layout);

        setTitle("Demos");

        mListView = (ListView) this.findViewById(R.id.listview);
        mListView.setSelector(new BitmapDrawable(getResources()));
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListItems));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position - ((ListView) parent).getHeaderViewsCount();
                if (index >= 0 && index < mActvities.length) {
                    Intent intent = new Intent(DemoListActivity.this, mActvities[index]);
                    startActivity(intent);
                }
            }
        });

        onLoadFinished();
    }

    @Override
    protected void onReloadData() {

    }
}
