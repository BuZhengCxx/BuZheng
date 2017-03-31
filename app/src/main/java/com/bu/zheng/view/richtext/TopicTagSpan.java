package com.bu.zheng.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bu.zheng.R;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class TopicTagSpan extends ClickableSpan implements StateClickableSpan {

    public static final String REGULAR = "\\#(\\S+?)\\#";

    private Context context;
    private String value;
    private RichTextClickListener richTextClickListener;

    public TopicTagSpan(Context context, String value, RichTextClickListener richTextClickListener) {
        this.context = context;
        this.value = value;
        this.richTextClickListener = richTextClickListener;
    }

    public void setSpan(Spannable sp, int start, int end) {
        sp.setSpan(this, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onClickSpan(View widget) {
        onClick(widget);
    }

    @Override
    public void onClick(View widget) {
        if (richTextClickListener != null) {
            richTextClickListener.onRichTextClick(widget, value, RichTextClickListener.RICHTEXT_TYPE_TOPIC_TAG);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
        ds.setColor(getStateNormalColor());
    }

    @Override
    public int getStateNormalColor() {
        return context.getResources().getColor(R.color.txtcolor1);
    }

    @Override
    public int getStatePressedColor() {
        return context.getResources().getColor(R.color.txtcolor7);
    }
}
