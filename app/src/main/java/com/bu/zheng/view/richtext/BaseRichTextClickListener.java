package com.bu.zheng.view.richtext;

import android.view.View;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class BaseRichTextClickListener implements RichTextClickListener {

    @Override
    public boolean onRichTextClick(View view, String richText, int type) {
        doOther(view, richText, type);
        switch (type) {
            case RICHTEXT_TYPE_TOPIC_TAG:
                break;
        }
        return true;
    }

    protected void doOther(View view, String richText, int type) {

    }
}
