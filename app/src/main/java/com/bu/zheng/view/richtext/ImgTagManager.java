package com.bu.zheng.view.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;

import com.bu.zheng.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class ImgTagManager {

    private static ImgTagManager mInstance;

    private Context mContext;

    private static Pattern mEmoticonPattern = Pattern.compile(TagImgSpan.REGULAR);

    public static ImgTagManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImgTagManager(context);
        }
        return mInstance;
    }

    private ImgTagManager(Context context) {
        mContext = context;
    }

    private static HashMap<String, Integer> mTagMap;

    static {
        mTagMap = new HashMap<>();
        mTagMap.put("[vip1]", R.drawable.pub_tag_vip1);
        mTagMap.put("[vip2]", R.drawable.pub_tag_vip2);
    }

    public int setEmoticonSpan(Spannable sp, float textHeight) {
        return setEmoticonSpan(sp, null, textHeight);
    }

    public int setEmoticonSpan(Spannable sp, int[] select, float textHeight) {
        int spanCount = 0;
        if (sp == null || sp.length() < 2) {
            return spanCount;
        }

        Matcher emoMatcher = mEmoticonPattern.matcher(sp);

        TagImgSpan[] spans = sp.getSpans(0, sp.length(), TagImgSpan.class);

        while (true) {
            if (!emoMatcher.find()) {
                break;
            }

            int begin = emoMatcher.start();
            int end = emoMatcher.end();
            if (begin < end) {
                String phrase = sp.subSequence(begin, end).toString();
                if (isSpanExist(spans, sp, phrase, begin, end)) {
                    continue;
                }
                int resId = 0;
                if (mTagMap.containsKey(phrase)) {
                    resId = mTagMap.get(phrase);
                }
                if (resId > 0) {
                    /* 去掉背景Span */
                    BackgroundColorSpan[] bgcSpns = sp.getSpans(begin, end, BackgroundColorSpan.class);
                    if (bgcSpns != null) {
                        for (int i = 0; i < bgcSpns.length; i++) {
                            sp.removeSpan(bgcSpns[i]);
                        }
                    }

                    sp.setSpan(new TagImgSpan(mContext, resId, ImageSpan.ALIGN_BASELINE, phrase, (int) (textHeight)),
                            begin,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanCount++;
                }
            }
        }
        return spanCount + spans.length;
    }

    private boolean isSpanExist(TagImgSpan[] spans, Spannable sp, String phrase, int begin, int end) {
        if (spans == null || spans.length == 0) {
            return false;
        }
        int count = spans.length;
        for (int i = 0; i < count; i++) {
            int spanStart = sp.getSpanStart(spans[i]);
            int spanEnd = sp.getSpanEnd(spans[i]);

            if (spanStart != begin || spanEnd != end) {
                continue;
            }

            if (spanStart == begin && spanEnd == end && spans[i].mPhrase.equals(phrase)) {
                return true;
            }
        }
        return false;
    }
}
