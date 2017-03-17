package com.bu.zheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/3/17.
 */

public class ActivityDemoList extends BaseActivity {

    static final String[] mListItems = {
    };

    static final Class<?>[] mActvities = {

    };

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo_list_layout);

        setTitle("Demoss");

        mListView = (ListView) this.findViewById(R.id.listview);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListItems));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position - ((ListView) parent).getHeaderViewsCount();
                Intent intent = new Intent(ActivityDemoList.this, mActvities[index]);
                startActivity(intent);
            }
        });

        onLoadFinished();
    }

    @Override
    protected void onReloadData() {

    }
}
