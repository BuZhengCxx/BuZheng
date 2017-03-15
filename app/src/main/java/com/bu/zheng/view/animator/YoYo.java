package com.bu.zheng.view.animator;

import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by BuZheng on 2017/3/15.
 */

public class YoYo {

    private BaseViewAnimator animator;
    private long duration;
    private View target;
    private AnimatorListenerAdapter adapter;

    public YoYo(BaseViewAnimator animator) {
        this.animator = animator;
    }

    public YoYo duration(long duration) {
        this.duration = duration;
        return this;
    }

    public YoYo setListener(AnimatorListenerAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public void playOn(View target) {
        this.target = target;
        play();
    }

    private BaseViewAnimator play() {
        animator.setTarget(target);

        if (duration > 0) {
            animator.setDuration(duration);
        }

        if (adapter != null) {
            animator.addAnimatorListener(adapter);
        }

        animator.start();
        return animator;
    }
}
