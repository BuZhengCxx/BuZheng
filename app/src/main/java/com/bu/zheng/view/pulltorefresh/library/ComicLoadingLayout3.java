package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase;

/**
 * Created by chenxiaoxiong on 16/7/8.
 */
public class ComicLoadingLayout3 extends LoadingLayout {

    private RelativeLayout mBannerLayout;
    private ImageView mBannerImage;
    private TextView mPullTip;
    private boolean mHasBanner;

    public ComicLoadingLayout3(Context context, PullToRefreshBase.Mode mode,
                               PullToRefreshBase.Orientation scrollDirection,
                               TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    protected void initLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {

        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

        //extra
        mBannerLayout = (RelativeLayout)mInnerLayout.findViewById(R.id.banner_layout);
        mBannerImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image_2);
        mPullTip = (TextView)mInnerLayout.findViewById(R.id.pull_tip);

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

    public void setHasBanner(boolean hasBanner, String url) {
        this.mHasBanner = hasBanner;
        if (!TextUtils.isEmpty(url)) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBannerLayout.getLayoutParams();
            layoutParams.height = getResources().getDisplayMetrics().widthPixels * 9 / 21;
            //ImageDisplayer.display(mBannerImage, url, R.drawable.pub_imgempty_logo96);
        }
    }

    private void initBanner() {
        if (mHasBanner) {
            mHeaderImage.setVisibility(View.GONE);
            mBannerLayout.setVisibility(View.VISIBLE);

        } else {
            mHeaderImage.setVisibility(View.VISIBLE);
            mBannerLayout.setVisibility(View.GONE);
        }
    }

    protected int getVerticalLayoutResId() {
        return R.layout.comic_pull_to_refresh_header_vertical_version_c;
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
        initBanner();
    }

    @Override
    protected void refreshingImpl() {
        if (!mHasBanner) {
            mHeaderImage.clearAnimation();
            mHeaderImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.list_release_anim));
            AnimationDrawable anim = (AnimationDrawable) mHeaderImage.getDrawable();
            anim.start();
        }
    }

    @Override
    protected void releaseToRefreshImpl() {
    }

    @Override
    protected int refreshCompleteImpl() {
        if (mHasBanner) {
            return 0;
        }
        return 400;
    }

    @Override
    protected void resetImpl() {
        if (!mHasBanner) {
            mHeaderImage.clearAnimation();
            mHeaderImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_pull1));
        }
    }
}
