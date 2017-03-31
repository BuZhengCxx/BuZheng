package com.bu.zheng.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class RichTextView extends BaseRichTextView {

    private Context context;

    RichTextClickListener mRichTextClickListener;

    /**
     * do this before setRichText
     * @param listener
     */
    public void setOnRichTextClickListener(RichTextClickListener listener) {
        mRichTextClickListener = listener;
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        addMatcher(TagImgSpan.REGULAR);
        addMatcher(TopicTagSpan.REGULAR);
    }

    @Override
    public void onMatched(String regular, int start, int end) {
        Spannable sp = (Spannable) getText();
        if (TextUtils.equals(regular, TagImgSpan.REGULAR)) {
            ImgTagManager.getInstance(context).setEmoticonSpan(sp, getTextSize());

        } else if (TextUtils.equals(regular, TopicTagSpan.REGULAR)) {
            new TopicTagSpan(getContext(),sp.subSequence(start, end).toString(), mRichTextClickListener).setSpan(sp, start, end);
        }
    }
}
