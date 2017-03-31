package com.bu.zheng.view.richtext;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

/**
 * Created by BuZheng on 2017/3/31.
 */

public interface IRichText {

    /**
     * 设置富文本
     *
     * @param cs
     */
    void setRichText(CharSequence cs);

    /**
     * @param spannableStringBuilder
     */
    void setRichSpan(SpannableStringBuilder spannableStringBuilder);

    /**
     * 添加正则匹配
     *
     * @param regular
     */
    void addMatcher(String regular);


    /**
     * 将显示效果转换为富文本
     */
    void toRichText(Spannable sp);
}
