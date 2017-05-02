package com.bu.zheng.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bu.zheng.R;

import java.util.List;

/**
 * Created by BuZheng on 2017/5/2.
 */

public class SimpleRecyclerAdapter extends BaseRecyclerViewAdapter<String>{

    private final int TYPE_DATA = 1;

    public SimpleRecyclerAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected int getDataNum() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder handleOtherViewType(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DATA:
                return new DataHolder(mInflater.inflate(R.layout.item_view_simple_list_layout, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null && mData.size() > 0) {
            if (position < mData.size()) {
                return TYPE_DATA;
            }
        }
        return TYPE_FOOTER;
    }

    @Override
    protected View getFooterView(ViewGroup parent) {
        return View.inflate(mContext, R.layout.view_bird_loading_layout, null);
    }

    @Override
    protected void setData(RecyclerView.ViewHolder holder, int type, int position) {
        switch (type) {
            case TYPE_DATA:
                ((DataHolder) holder).setData(mData.get(position));
                break;
        }
    }

    private class DataHolder extends RecyclerView.ViewHolder {

        public DataHolder(View itemView) {
            super(itemView);
        }

        public void setData(String text) {
            ((TextView) itemView).setText(text);
        }
    }

    @Override
    protected void startAnim(View view) {

    }

}
