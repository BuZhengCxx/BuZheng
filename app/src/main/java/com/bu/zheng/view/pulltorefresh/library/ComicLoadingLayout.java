package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;

/**
 * Created by chenxiaoxiong on 15/5/12.
 */
public class ComicLoadingLayout extends LoadingLayout {

    public ComicLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    protected void initLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
                break;
        }

        Drawable imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        setLoadingDrawable(imageDrawable);
    }

    protected int getVerticalLayoutResId() {
        return R.layout.comic_pull_to_refresh_header_vertical_version_b;
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.img_pull1;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {
        mHeaderImage.clearAnimation();
        mHeaderImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.list_release_anim));
        AnimationDrawable anim = (AnimationDrawable) mHeaderImage.getDrawable();
        anim.start();
    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected int refreshCompleteImpl() {
        return 400;
    }

    @Override
    protected void resetImpl() {
        mHeaderImage.clearAnimation();
        mHeaderImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_pull1));
    }
}
