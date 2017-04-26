package com.bu.zheng.view.richtext;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by BuZheng on 2017/3/31.
 */

public class ClickMovementMethod extends LinkMovementMethod {

    private static ClickMovementMethod mInstance;

    public static ClickMovementMethod getInstance() {
        if (mInstance == null) {
            mInstance = new ClickMovementMethod();
        }
        return mInstance;
    }

    private ClickMovementMethod() {
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            StateClickableSpan[] spans = buffer.getSpans(off, off, StateClickableSpan.class);

            if (spans.length != 0) {

                StateClickableSpan span = spans[0];
                int start = buffer.getSpanStart(span);
                int end = buffer.getSpanEnd(span);

                switch (action) {
                    case MotionEvent.ACTION_DOWN://选中（高亮）字段文本
                        //Selection.setSelection(buffer, buffer.getSpanStart(spans[0]), buffer.getSpanEnd(spans[0]));
                        buffer.setSpan(new ForegroundColorSpan(span.getStatePressedColor()),
                                start,
                                end,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;

                    case MotionEvent.ACTION_UP://触发点击事件
                        spans[0].onClickSpan(widget);
                        buffer.setSpan(new ForegroundColorSpan(span.getStateNormalColor()),
                                start,
                                end,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;

                    default:
                        buffer.setSpan(new ForegroundColorSpan(span.getStateNormalColor()),
                                start,
                                end,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                }
                return true;

            } else if(action == MotionEvent.ACTION_UP){
                Selection.removeSelection(buffer); // 取消选中效果
                //响应点击事件
            }
        }
        return false;
    }
}