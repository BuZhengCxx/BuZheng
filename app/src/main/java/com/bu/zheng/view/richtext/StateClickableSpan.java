package com.bu.zheng.view.richtext;


import android.view.View;

/**
 * Created by BuZheng on 2017/3/31.
 */

public interface StateClickableSpan {

    int getStateNormalColor();

    int getStatePressedColor();

    void onClickSpan(View widget);
}
