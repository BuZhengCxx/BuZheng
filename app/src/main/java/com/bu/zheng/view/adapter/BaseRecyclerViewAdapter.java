package com.bu.zheng.view.adapter;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.bu.zheng.R;

import java.util.List;

/**
 * Created by BuZheng on 2017/5/2.
 */

public abstract class BaseRecyclerViewAdapter<T> extends FooterRecyclerViewAdapter<T> {

    public BaseRecyclerViewAdapter(Context context, List data) {
        this(context,data,false);
    }

    public BaseRecyclerViewAdapter(Context context, List data, boolean animable) {
        super(context, data, animable);
    }

    @Override
    public void hideFooterView() {
        if (mFooterView != null) {
            ImageView loading = (ImageView) mFooterView.findViewById(R.id.loading);
            loading.clearAnimation();
            loading.setImageResource(R.drawable.loading_img_wuyaover);
        }
    }

    @Override
    public void showFooterView() {
        if (mFooterView != null) {
            ImageView loading = (ImageView) mFooterView.findViewById(R.id.loading);
            loading.clearAnimation();
            loading.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bird_loading_anim));
            AnimationDrawable anim = (AnimationDrawable) loading.getDrawable();
            anim.start();
        }
    }
}
