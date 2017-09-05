package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.Mode;
import com.bu.zheng.view.pulltorefresh.common.Orientation;

/**
 * Created by chenxiaoxiong on 16/7/8.
 */
public class CustomLoadingLayout3 extends LoadingLayout {

    private RelativeLayout mBannerLayout;
    private ImageView mBannerImage;
    private TextView mPullTip;
    private boolean mHasBanner;

    public CustomLoadingLayout3(Context context, Mode mode,
                                Orientation scrollDirection,
                                TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    protected void initLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {

        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

        //extra
        mBannerLayout = (RelativeLayout) mInnerLayout.findViewById(R.id.banner_layout);
        mBannerImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image_2);
        mPullTip = (TextView) mInnerLayout.findViewById(R.id.pull_tip);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
                break;
        }

        Drawable imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        setLoadingDrawable(imageDrawable);
    }

    public void setBanner(String url) {
        this.mHasBanner = true;
        //if (!TextUtils.isEmpty(url)) {
        ViewGroup.LayoutParams layoutParams = mInnerLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels * 9 / 16;
        mInnerLayout.requestLayout();
        //}
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
        if (imageDrawable != null) {
            ViewGroup.LayoutParams layoutParams = mInnerLayout.getLayoutParams();
            layoutParams.height = imageDrawable.getIntrinsicHeight();
            mInnerLayout.requestLayout();
        }
    }

    private int mPullState;

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        if (scaleOfLayout > 0 && mPullTip != null) {
            if (scaleOfLayout > 0.7f) {
                mPullTip.setText("有彩蛋呐 ╰(*°▽°*)╯快放开我吧~");
                mPullState = 3;

            } else if (scaleOfLayout > 0.4f) {
                mPullTip.setText("可以刷新啦快放开我~");
                mPullState = 2;

            } else {
                mPullTip.setText("拉我刷新呀~");
                mPullState = 1;
            }
        }
    }

    public int getPullState() {
        return mPullState;
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
