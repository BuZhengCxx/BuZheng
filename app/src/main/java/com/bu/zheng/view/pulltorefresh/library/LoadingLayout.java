/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.bu.zheng.view.pulltorefresh.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.common.Mode;
import com.bu.zheng.view.pulltorefresh.common.Orientation;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    protected RelativeLayout mInnerLayout;

    protected ImageView mHeaderImage;
    protected ProgressBar mHeaderProgress;

    private boolean mUseIntrinsicAnimation;

    private TextView mHeaderText;
    private TextView mSubHeaderText;

    protected Mode mMode;
    protected Orientation mOrientation;

    private CharSequence mPullLabel;// ex.下拉刷新
    private CharSequence mRefreshingLabel;// ex.正在加载
    private CharSequence mReleaseLabel;// ex.释放刷新

    protected Context mContext;

    public LoadingLayout(Context context, Mode mode, Orientation orientation, TypedArray attrs) {
        super(context);
        mContext = context;
        mMode = mode;
        mOrientation = orientation;

        setBackgroundColor(context.getResources().getColor(R.color.bgcolor2));

        switch (orientation) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(getHorizontalLayoutResId(), this);
                break;

            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(getVerticalLayoutResId(), this);
                break;
        }

        initLayout(context, mode, orientation, attrs);
    }

    protected int getHorizontalLayoutResId() {
        return R.layout.pull_to_refresh_header_horizontal;
    }

    protected int getVerticalLayoutResId() {
        return R.layout.comic_pull_to_refresh_header_vertical;
    }

    protected void initLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        mInnerLayout = (RelativeLayout) findViewById(R.id.fl_inner);
        mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
        mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
        mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
        mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
                mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
                mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
                break;
        }

        Drawable imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        setLoadingDrawable(imageDrawable);

        reset();
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        switch (mOrientation) {
            case HORIZONTAL:
                return mInnerLayout.getWidth();
            case VERTICAL:
            default:
                return mInnerLayout.getHeight();
        }
    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public final void pullToRefresh() {
        if (mHeaderText != null) {
            mHeaderText.setText(mPullLabel);
        }
        pullToRefreshImpl();
    }

    public final void refreshing() {
        if (mHeaderText != null) {
            mHeaderText.setText(mRefreshingLabel);
        }
        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mHeaderImage.getDrawable()).start();
        } else {
            refreshingImpl();
        }

        if (mSubHeaderText != null) {
            mSubHeaderText.setVisibility(View.GONE);
        }
    }

    public final void releaseToRefresh() {
        if (mHeaderText != null) {
            mHeaderText.setText(mReleaseLabel);
        }
        releaseToRefreshImpl();
    }

    public final int refreshComplete() {
        return refreshCompleteImpl();
    }

    public final void reset() {
        if (mHeaderText != null) {
            mHeaderText.setText(mPullLabel);
        }
        mHeaderImage.setVisibility(View.VISIBLE);

        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mHeaderImage.getDrawable()).stop();
        } else {
            resetImpl();
        }

        if (mSubHeaderText != null) {
            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        if (mSubHeaderText != null) {
            if (TextUtils.isEmpty(label)) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setText(label);

                // Only set it to Visible if we're GONE, otherwise VISIBLE will
                // be set soon
                // 但是第一次刷新的时候在回滚的过程中这个会出现，bug
                if (View.GONE == mSubHeaderText.getVisibility()) {
                    mSubHeaderText.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public final void setLoadingDrawable(Drawable imageDrawable) {
        mHeaderImage.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);
        onLoadingDrawableSet(imageDrawable);
    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }


    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract int refreshCompleteImpl();

    protected abstract void resetImpl();
}
