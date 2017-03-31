package com.bu.zheng.view.richtext;

import android.text.Spannable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class RichTextParser {

    private List<String> mPatterns;

    private OnRichTextMatchListener mOnRichTextMatchListener;

    public RichTextParser() {
        mPatterns = new ArrayList<>();
    }

    public void addMatcher(String regular) {
        if (!TextUtils.isEmpty(regular)) {
            mPatterns.add(regular);
        }
    }

    public void setOnRichTextMatchListener(OnRichTextMatchListener onRichTextMatchListener) {
        this.mOnRichTextMatchListener = onRichTextMatchListener;
    }

    public void parseRichText(Spannable spannable) {
        if (mOnRichTextMatchListener != null && mPatterns.size() > 0) {
            for (String regular : mPatterns) {
                Pattern pattern = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(spannable);
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    mOnRichTextMatchListener.onMatched(regular, start, end);
                }
            }
        }
    }
}
