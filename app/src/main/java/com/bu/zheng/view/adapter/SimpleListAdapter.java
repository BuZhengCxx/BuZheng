package com.bu.zheng.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bu.zheng.R;

import java.util.List;

/**
 * Created by BuZheng on 2017/4/1.
 */

public class SimpleListAdapter extends BaseListAdapter<String> {

    public SimpleListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_view_simple_list_layout, null);
        }
        ((TextView) convertView).setText(mData.get(position));
        return convertView;
    }
}
