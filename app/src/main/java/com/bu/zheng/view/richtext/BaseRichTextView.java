package com.bu.zheng.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by BuZheng on 2017/3/31.
 */

public abstract class BaseRichTextView extends TextView implements IRichText, OnRichTextMatchListener {

    private RichTextParser mParser;

    public BaseRichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mParser = new RichTextParser();
        mParser.setOnRichTextMatchListener(this);
    }

    @Override
    public void setRichText(CharSequence cs) {
        Spannable sp;
        if (cs instanceof Spannable) {
            sp = (Spannable) cs;
        } else {
            sp = new SpannableStringBuilder(cs);
        }

        toRichText(sp);

        setMovementMethod(ClickMovementMethod.getInstance());

        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
    }

    @Override
    public void setRichSpan(SpannableStringBuilder ssb) {
        if (ssb == null) {
            return;
        }
        toRichText(ssb);

        setMovementMethod(ClickMovementMethod.getInstance());

        setFocusable(true);
        setClickable(true);
        setLongClickable(true);
    }

    @Override
    public void addMatcher(String regular) {
        mParser.addMatcher(regular);
    }

    @Override
    public void toRichText(Spannable sp) {
        setText(sp, BufferType.SPANNABLE);
        mParser.parseRichText(sp);
    }
}
