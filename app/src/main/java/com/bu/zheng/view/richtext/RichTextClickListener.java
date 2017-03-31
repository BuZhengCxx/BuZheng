package com.bu.zheng.view.richtext;

import android.view.View;

/**
 * Created by BuZheng on 2017/3/31.
 */

public interface RichTextClickListener {

    int RICHTEXT_TYPE_TOPIC_TAG = 1;

    /**
     * @param view
     * @param richText
     * @param type
     * @return
     */
    boolean onRichTextClick(View view, String richText, int type);
}
