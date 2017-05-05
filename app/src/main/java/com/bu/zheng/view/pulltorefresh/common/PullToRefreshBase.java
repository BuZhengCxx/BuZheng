/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.bu.zheng.view.pulltorefresh.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bu.zheng.R;
import com.bu.zheng.view.pulltorefresh.library.CustomLoadingLayout3;
import com.bu.zheng.view.pulltorefresh.library.FlipLoadingLayout;
import com.bu.zheng.view.pulltorefresh.library.ILoadingLayout;
import com.bu.zheng.view.pulltorefresh.library.IPullToRefresh;
import com.bu.zheng.view.pulltorefresh.library.LoadingLayout;
import com.bu.zheng.view.pulltorefresh.library.LoadingLayoutProxy;
import com.bu.zheng.view.pulltorefresh.library.Utils;
import com.bu.zheng.view.pulltorefresh.library.ViewCompat;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {

    static final boolean DEBUG = true;
    static final boolean USE_HW_LAYERS = false;
    static final String LOG_TAG = "PullToRefreshBase";
    static final float FRICTION = 2.0f;

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;
    static final int DEMO_SCROLL_INTERVAL = 225;

    static final String STATE_STATE = "ptr_state";
    static final String STATE_MODE = "ptr_mode";
    static final String STATE_CURRENT_MODE = "ptr_current_mode";
    static final String STATE_SCROLLING_REFRESHING_ENABLED = "ptr_disable_scrolling";
    static final String STATE_SHOW_REFRESHING_VIEW = "ptr_show_refreshing_view";
    static final String STATE_SUPER = "ptr_super";

    private int mTouchSlop;
    private float mLastMotionX, mLastMotionY;
    private float mInitialMotionX, mInitialMotionY;

    private boolean mIsBeingDragged = false;
    private State mState = State.RESET;
    private Mode mMode = Mode.getDefault();

    private Mode mCurrentMode;
    T mRefreshableView;
    private FrameLayout mRefreshableViewWrapper;

    private boolean mShowViewWhileRefreshing = true;
    private boolean mScrollingWhileRefreshingEnabled = false;
    private boolean mFilterTouchEvents = true;
    private boolean mOverScrollEnabled = true;
    private boolean mLayoutVisibilityChangesEnabled = true;

    private Interpolator mScrollAnimationInterpolator;

    private LoadingLayout mHeaderLayout;
    private LoadingLayout mFooterLayout;

    private OnRefreshListener<T> mOnRefreshListener;
    private OnRefreshListener2<T> mOnRefreshListener2;
    private OnPullEventListener<T> mOnPullEventListener;

    private SmoothScrollRunnable mSmoothScrollRunnable;

    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullToRefreshBase(Context context, Mode mode) {
        super(context);
        mMode = mode;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                setOrientation(LinearLayout.HORIZONTAL);
                break;

            case VERTICAL:
            default:
                setOrientation(LinearLayout.VERTICAL);
                break;
        }

        setGravity(Gravity.CENTER);

        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

        if (a.hasValue(R.styleable.PullToRefresh_ptrMode)) {
            mMode = Mode.mapIntToValue(a.getInteger(R.styleable.PullToRefresh_ptrMode, 0));
        }

        mRefreshableView = createRefreshableView(context, attrs);
        addRefreshableView(context, mRefreshableView);

        mHeaderLayout = createLoadingLayout(context, Mode.PULL_FROM_START, a);
        mFooterLayout = createLoadingLayout(context, Mode.PULL_FROM_END, a);

        if (a.hasValue(R.styleable.PullToRefresh_ptrOverScroll)) {
            mOverScrollEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrOverScroll, true);
        }

        if (a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
            mScrollingWhileRefreshingEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled, false);
        }

        // Let the derivative classes have a go at handling attributes, then recycle them...
        handleStyledAttributes(a);
        a.recycle();

        // Finally update the UI for the modes
        updateUIForMode();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {

        final T refreshableView = getRefreshableView();

        if (refreshableView instanceof ViewGroup) {
            ((ViewGroup) refreshableView).addView(child, index, params);
        } else {
            throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
        }
    }

    @Override
    public final boolean demo() {
        if (mMode.showHeaderLoadingLayout() && isReadyForPullStart()) {
            smoothScrollToAndBack(-getHeaderSize() * 2);
            return true;
        } else if (mMode.showFooterLoadingLayout() && isReadyForPullEnd()) {
            smoothScrollToAndBack(getFooterSize() * 2);
            return true;
        }
        return false;
    }

    public final void autoFresh() {
        if (mMode.showHeaderLoadingLayout() && isReadyForPullStart()) {
            smoothScrollTo(-getHeaderSize(), getPullToRefreshScrollDuration(), 0, new OnSmoothScrollFinishedListener() {
                @Override
                public void onSmoothScrollFinished() {
                    if (Utils.getActiveNetworkInfo(getContext()) == null) {
                        Toast.makeText(getContext(), "没有网络，请检查网络设置", Toast.LENGTH_SHORT).show();
                        onRefreshComplete();
                        return;
                    }

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh(PullToRefreshBase.this);
                    }
                    if (mOnRefreshListener2 != null) {
                        mOnRefreshListener2.onPullDownToRefresh(PullToRefreshBase.this);
                    }
                }
            });
            mHeaderLayout.refreshing();
            mState = State.REFRESHING;

        } else if (mMode.showFooterLoadingLayout() && isReadyForPullEnd()) {
            smoothScrollTo(getFooterSize(), getPullToRefreshScrollDuration(), 0, new OnSmoothScrollFinishedListener() {
                @Override
                public void onSmoothScrollFinished() {
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh(PullToRefreshBase.this);
                    }
                    if (mOnRefreshListener2 != null) {
                        mOnRefreshListener2.onPullUpToRefresh(PullToRefreshBase.this);
                    }
                }
            });
            mFooterLayout.refreshing();
            mState = State.REFRESHING;
        }
    }

    public void load() {
    }

    public void reLoad() {
    }

    @Override
    public final Mode getCurrentMode() {
        return mCurrentMode;
    }

    @Override
    public final boolean getFilterTouchEvents() {
        return mFilterTouchEvents;
    }

    @Override
    public final ILoadingLayout getLoadingLayoutProxy() {
        return getLoadingLayoutProxy(true, true);
    }

    @Override
    public final ILoadingLayout getLoadingLayoutProxy(boolean includeStart, boolean includeEnd) {
        return createLoadingLayoutProxy(includeStart, includeEnd);
    }

    @Override
    public final Mode getMode() {
        return mMode;
    }

    @Override
    public final T getRefreshableView() {
        return mRefreshableView;
    }

    @Override
    public final boolean getShowViewWhileRefreshing() {
        return mShowViewWhileRefreshing;
    }

    @Override
    public final State getState() {
        return mState;
    }

    @Override
    public final boolean isPullToRefreshEnabled() {
        return mMode.permitsPullToRefresh();
    }

    @Override
    public final boolean isPullToRefreshOverScrollEnabled() {
        return VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD && mOverScrollEnabled
                && OverscrollHelper.isAndroidOverScrollEnabled(mRefreshableView);
    }

    @Override
    public final boolean isRefreshing() {
        return mState == State.REFRESHING || mState == State.MANUAL_REFRESHING;
    }

    @Override
    public final boolean isScrollingWhileRefreshingEnabled() {
        return mScrollingWhileRefreshingEnabled;
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isPullToRefreshEnabled()) {
            return false;
        }
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                //If we're refreshing, and the flag is set. Eat all MOVE events
                if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
                    return true;
                }

                if (isReadyForPull()) {
                    final float y = event.getY(), x = event.getX();
                    final float diff, oppositeDiff, absDiff;

                    // We need to use the correct values, based on scroll direction
                    switch (getPullToRefreshScrollDirection()) {
                        case HORIZONTAL:
                            diff = x - mLastMotionX;
                            oppositeDiff = y - mLastMotionY;
                            break;
                        case VERTICAL:
                        default:
                            diff = y - mLastMotionY;
                            oppositeDiff = x - mLastMotionX;
                            break;
                    }
                    absDiff = Math.abs(diff);

                    if (absDiff > mTouchSlop && (!mFilterTouchEvents || absDiff > Math.abs(oppositeDiff))) {
                        if (mMode.showHeaderLoadingLayout() && diff >= 1f && isReadyForPullStart()) {
                            mLastMotionX = x;
                            mLastMotionY = y;
                            mIsBeingDragged = true;
                            if (mMode == Mode.BOTH) {
                                mCurrentMode = Mode.PULL_FROM_START;
                            }
                        } else if (mMode.showFooterLoadingLayout() && diff <= -1f && isReadyForPullEnd()) {
                            mLastMotionX = x;
                            mLastMotionY = y;
                            mIsBeingDragged = true;
                            if (mMode == Mode.BOTH) {
                                mCurrentMode = Mode.PULL_FROM_END;
                            }
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPull()) {
                    mLastMotionX = mInitialMotionX = event.getX();
                    mLastMotionY = mInitialMotionY = event.getY();
                    mIsBeingDragged = false;
                }
                break;
            }
        }
        return mIsBeingDragged;
    }

    @Override
    public final void onRefreshComplete() {
        if (isRefreshing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setState(State.RESET);
                }
            }, mHeaderLayout.refreshComplete());
        }
    }

    public final void onNoNetworkComplete() {
        if (mState == State.MANUAL_REFRESHING || mState == State.REFRESHING || mState == State.RELEASE_TO_REFRESH) {
            setState(State.RESET);
        }
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        if (!isPullToRefreshEnabled()) {
            return false;
        }

        // If we're refreshing, and the flag is set. Eat the event
        if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    pullEvent();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPull()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = mInitialMotionX = event.getX();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;

                    if (mState == State.RELEASE_TO_REFRESH) {
                        if (mOnRefreshListener != null) {
                            if (Utils.getActiveNetworkInfo(getContext()) == null) {
                                Toast.makeText(getContext(), "没有网络，请检查网络设置", Toast.LENGTH_SHORT).show();
                                onNoNetworkComplete();
                                return true;
                            }
                            setState(State.REFRESHING, true);
                            mOnRefreshListener.onRefresh(this);
                            return true;

                        } else if (mOnRefreshListener2 != null) {
                            if (Utils.getActiveNetworkInfo(getContext()) == null) {
                                Toast.makeText(getContext(), "没有网络，请检查网络设置", Toast.LENGTH_SHORT).show();
                                onNoNetworkComplete();
                                return true;
                            }
                            setState(State.REFRESHING, true);
                            if (mCurrentMode == Mode.PULL_FROM_START) {
                                mOnRefreshListener2.onPullDownToRefresh(this);
                            } else if (mCurrentMode == Mode.PULL_FROM_END) {
                                mOnRefreshListener2.onPullUpToRefresh(this);
                            }
                            return true;
                        }
                    }

                    // If we're already refreshing, just scroll back to the top
                    if (isRefreshing()) {
                        smoothScrollTo(0);
                        return true;
                    }

                    // If we haven't returned by here, then we're not in a state
                    // to pull, so just reset
                    setState(State.RESET);

                    return true;
                }
                break;
            }
        }

        return false;
    }

    @Override
    public final void setScrollingWhileRefreshingEnabled(boolean allowScrollingWhileRefreshing) {
        mScrollingWhileRefreshingEnabled = allowScrollingWhileRefreshing;
    }

    @Override
    public final void setFilterTouchEvents(boolean filterEvents) {
        mFilterTouchEvents = filterEvents;
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        getRefreshableView().setLongClickable(longClickable);
    }

    @Override
    public final void setMode(Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            updateUIForMode();
        }
    }

    @Override
    public void setOnPullEventListener(OnPullEventListener<T> listener) {
        mOnPullEventListener = listener;
    }

    @Override
    public final void setOnRefreshListener(OnRefreshListener<T> listener) {
        mOnRefreshListener = listener;
        mOnRefreshListener2 = null;
    }

    @Override
    public final void setOnRefreshListener(OnRefreshListener2<T> listener) {
        mOnRefreshListener2 = listener;
        mOnRefreshListener = null;
    }

    @Override
    public final void setPullToRefreshOverScrollEnabled(boolean enabled) {
        mOverScrollEnabled = enabled;
    }

    @Override
    public final void setRefreshing() {
        setRefreshing(true);
    }

    @Override
    public final void setRefreshing(boolean doScroll) {
        if (!isRefreshing()) {
            setState(State.MANUAL_REFRESHING, doScroll);
        }
    }

    @Override
    public void setScrollAnimationInterpolator(Interpolator interpolator) {
        mScrollAnimationInterpolator = interpolator;
    }

    @Override
    public final void setShowViewWhileRefreshing(boolean showView) {
        mShowViewWhileRefreshing = showView;
    }

    public abstract Orientation getPullToRefreshScrollDirection();

    final void setState(State state, final boolean... params) {
        mState = state;
        if (DEBUG) {
            Log.d(LOG_TAG, "State: " + mState.name());
        }

        switch (mState) {
            case RESET:
                onReset();
                break;
            case PULL_TO_REFRESH:
                onPullToRefresh();
                break;
            case RELEASE_TO_REFRESH:
                onReleaseToRefresh();
                break;
            case REFRESHING:
            case MANUAL_REFRESHING:
                // doScroll ： true
                onRefreshing(params[0]);
                break;
            case OVERSCROLLING:
                // NO-OP
                break;
        }

        // Call OnPullEventListener
        if (mOnPullEventListener != null) {
            mOnPullEventListener.onPullEvent(this, mState, mCurrentMode);
        }
    }

    /**
     * Used internally for adding view. Need because we override addView to
     * pass-through to the Refreshable View
     */
    protected final void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    /**
     * Used internally for adding view. Need because we override addView to
     * pass-through to the Refreshable View
     */
    protected final void addViewInternal(View child, ViewGroup.LayoutParams params) {
        super.addView(child, -1, params);
    }

    protected LoadingLayout createLoadingLayout(Context context, Mode mode, TypedArray attrs) {
        //LoadingLayout layout = new FlipLoadingLayout(context, mode, getPullToRefreshScrollDirection(), attrs);
        LoadingLayout layout = new CustomLoadingLayout3(context, mode, getPullToRefreshScrollDirection(), attrs);
        layout.setVisibility(View.INVISIBLE);
        return layout;
    }

    /**
     * Used internally for {@link #getLoadingLayoutProxy(boolean, boolean)}.
     * Allows derivative classes to include any extra LoadingLayouts.
     */
    protected LoadingLayoutProxy createLoadingLayoutProxy(final boolean includeStart, final boolean includeEnd) {
        LoadingLayoutProxy proxy = new LoadingLayoutProxy();

        if (includeStart && mMode.showHeaderLoadingLayout()) {
            proxy.addLayout(mHeaderLayout);
        }
        if (includeEnd && mMode.showFooterLoadingLayout()) {
            proxy.addLayout(mFooterLayout);
        }

        return proxy;
    }

    /**
     * This is implemented by derived classes to return the created View. If you
     * need to use a custom View (such as a custom ListView), override this
     * method and return an instance of your custom class.
     * <p/>
     * Be sure to set the ID of the view in this method, especially if you're
     * using a ListActivity or ListFragment.
     */
    protected abstract T createRefreshableView(Context context, AttributeSet attrs);

    protected final void disableLoadingLayoutVisibilityChanges() {
        mLayoutVisibilityChangesEnabled = false;
    }

    protected final LoadingLayout getFooterLayout() {
        return mFooterLayout;
    }

    protected final int getFooterSize() {
        return mFooterLayout.getContentSize();
    }

    protected final LoadingLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    protected final int getHeaderSize() {
        return mHeaderLayout.getContentSize();
    }

    protected int getPullToRefreshScrollDuration() {
        return SMOOTH_SCROLL_DURATION_MS;
    }

    protected FrameLayout getRefreshableViewWrapper() {
        return mRefreshableViewWrapper;
    }

    /**
     * Allows Derivative classes to handle the XML Attrs without creating a TypedArray themsevles
     */
    protected void handleStyledAttributes(TypedArray a) {
    }

    protected abstract boolean isReadyForPullEnd();

    protected abstract boolean isReadyForPullStart();

    /**
     * Called by {@link #onRestoreInstanceState(android.os.Parcelable)} so that derivative
     * classes can handle their saved instance state.
     *
     * @param savedInstanceState - Bundle which contains saved instance state.
     */
    protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
    }

    /**
     * Called by {@link #onSaveInstanceState()} so that derivative classes can
     * save their instance state.
     *
     * @param saveState - Bundle to be updated with saved state.
     */
    protected void onPtrSaveInstanceState(Bundle saveState) {
    }

    protected void onPullToRefresh() {
        switch (mCurrentMode) {
            case PULL_FROM_END:
                mFooterLayout.pullToRefresh();
                break;
            case PULL_FROM_START:
                mHeaderLayout.pullToRefresh();
                break;
            default:
                break;
        }
    }

    protected void onRefreshing(final boolean doScroll) {
        if (mMode.showHeaderLoadingLayout()) {
            mHeaderLayout.refreshing();
        }
        if (mMode.showFooterLoadingLayout()) {
            mFooterLayout.refreshing();
        }

        if (doScroll) {
            if (mShowViewWhileRefreshing) {
                switch (mCurrentMode) {
                    case MANUAL_REFRESH_ONLY:
                    case PULL_FROM_END:
                        smoothScrollTo(getFooterSize());
                        break;
                    default:
                    case PULL_FROM_START:
                        smoothScrollTo(-getHeaderSize());
                        break;
                }
            } else {
                smoothScrollTo(0);
            }
        }
    }

    protected void onReleaseToRefresh() {
        switch (mCurrentMode) {
            case PULL_FROM_END:
                mFooterLayout.releaseToRefresh();
                break;
            case PULL_FROM_START:
                mHeaderLayout.releaseToRefresh();
                break;
            default:
                break;
        }
    }

    protected void onReset() {
        mIsBeingDragged = false;
        mLayoutVisibilityChangesEnabled = true;

        // Always reset both layouts, just in case...
        smoothScrollTo(0, getPullToRefreshScrollDuration(), 0, new OnSmoothScrollFinishedListener() {
            @Override
            public void onSmoothScrollFinished() {
                mHeaderLayout.reset();
                mFooterLayout.reset();
            }
        });
    }

    @Override
    protected final void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            setMode(Mode.mapIntToValue(bundle.getInt(STATE_MODE, 0)));
            mCurrentMode = Mode.mapIntToValue(bundle.getInt(STATE_CURRENT_MODE, 0));

            mScrollingWhileRefreshingEnabled = bundle.getBoolean(STATE_SCROLLING_REFRESHING_ENABLED, false);
            mShowViewWhileRefreshing = bundle.getBoolean(STATE_SHOW_REFRESHING_VIEW, true);

            // Let super Restore Itself
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));

            State viewState = State.mapIntToValue(bundle.getInt(STATE_STATE, 0));
            if (viewState == State.REFRESHING || viewState == State.MANUAL_REFRESHING) {
                setState(viewState, true);
            }

            // Now let derivative classes restore their state
            onPtrRestoreInstanceState(bundle);
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        // Let derivative classes get a chance to save state first, that way we
        // can make sure they don't overrite any of our values
        onPtrSaveInstanceState(bundle);

        bundle.putInt(STATE_STATE, mState.getIntValue());
        bundle.putInt(STATE_MODE, mMode.getIntValue());
        bundle.putInt(STATE_CURRENT_MODE, mCurrentMode.getIntValue());
        bundle.putBoolean(STATE_SCROLLING_REFRESHING_ENABLED, mScrollingWhileRefreshingEnabled);
        bundle.putBoolean(STATE_SHOW_REFRESHING_VIEW, mShowViewWhileRefreshing);
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (DEBUG) {
            Log.d(LOG_TAG, String.format("onSizeChanged. W: %d, H: %d", w, h));
        }

        super.onSizeChanged(w, h, oldw, oldh);

        // We need to update the header/footer when our size changes
        refreshLoadingViewsSize();

        // Update the Refreshable View layout
        refreshRefreshableViewSize(w, h);

        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    /**
     * Re-measure the Loading Views height, and adjust internal padding as necessary
     */
    protected final void refreshLoadingViewsSize() {
        final int maximumPullScroll = (int) (getMaximumPullScroll() * 1.2f);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                if (mMode.showHeaderLoadingLayout()) {
                    mHeaderLayout.setWidth(maximumPullScroll);
                    paddingLeft = -maximumPullScroll;
                } else {
                    paddingLeft = 0;
                }

                if (mMode.showFooterLoadingLayout()) {
                    mFooterLayout.setWidth(maximumPullScroll);
                    paddingRight = -maximumPullScroll;
                } else {
                    paddingRight = 0;
                }
                break;

            case VERTICAL:
                if (mMode.showHeaderLoadingLayout()) {
                    mHeaderLayout.setHeight(maximumPullScroll);
                    paddingTop = -maximumPullScroll;
                } else {
                    paddingTop = 0;
                }

                if (mMode.showFooterLoadingLayout()) {
                    mFooterLayout.setHeight(maximumPullScroll);
                    paddingBottom = -maximumPullScroll;
                } else {
                    paddingBottom = 0;
                }
                break;
        }

        if (DEBUG) {
            Log.d(LOG_TAG, String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", paddingLeft, paddingTop, paddingRight, paddingBottom));
        }

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    protected final void refreshRefreshableViewSize(int width, int height) {
        // We need to set the Height of the Refreshable View to the same as this layout
        LayoutParams lp = (LayoutParams) mRefreshableViewWrapper.getLayoutParams();

        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                if (lp.width != width) {
                    lp.width = width;
                    mRefreshableViewWrapper.requestLayout();
                }
                break;
            case VERTICAL:
                if (lp.height != height) {
                    lp.height = height;
                    mRefreshableViewWrapper.requestLayout();
                }
                break;
        }
    }

    /**
     * Helper method which just calls scrollTo() in the correct scrolling direction.
     *
     * @param value - New Scroll value
     */
    protected final void setHeaderScroll(final int value) {
        if (DEBUG) {
            Log.d(LOG_TAG, "setHeaderScroll: " + value);
        }

        if (mLayoutVisibilityChangesEnabled) {
            if (value < 0) {
                mHeaderLayout.setVisibility(View.VISIBLE);
            } else if (value > 0) {
                mFooterLayout.setVisibility(View.VISIBLE);
            } else {
                mHeaderLayout.setVisibility(View.INVISIBLE);
                mFooterLayout.setVisibility(View.INVISIBLE);
            }
        }

        if (USE_HW_LAYERS) {
            /**
             * Use a Hardware Layer on the Refreshable View if we've scrolled at
             * all. We don't use them on the Header/Footer Views as they change
             * often, which would negate any HW layer performance boost.
             */
            ViewCompat.setLayerType(mRefreshableViewWrapper, value != 0 ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE);
        }

        switch (getPullToRefreshScrollDirection()) {
            case VERTICAL:
                scrollTo(0, value);
                break;

            case HORIZONTAL:
                scrollTo(value, 0);
                break;
        }
    }

    protected final void smoothScrollTo(int scrollValue) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDuration());
    }

    /**
     * Updates the View State when the mode has been set. This does not do any
     * checking that the mode is different to current state so always updates.
     * add Header and Footer
     */
    protected void updateUIForMode() {
        // We need to use the correct LayoutParam values, based on scroll direction
        final LayoutParams lp = getLoadingLayoutLayoutParams();

        // Remove Header, and then add Header Loading View again if needed
        if (this == mHeaderLayout.getParent()) {
            removeView(mHeaderLayout);
        }
        if (mMode.showHeaderLoadingLayout()) {
            addViewInternal(mHeaderLayout, 0, lp);
        }

        // Remove Footer, and then add Footer Loading View again if needed
        if (this == mFooterLayout.getParent()) {
            removeView(mFooterLayout);
        }
        if (mMode.showFooterLoadingLayout()) {
            addViewInternal(mFooterLayout, lp);
        }

        // Hide Loading Views
        refreshLoadingViewsSize();

        // If we're not using Mode.BOTH, set mCurrentMode to mMode, otherwise
        // set it to pull down
        mCurrentMode = (mMode != Mode.BOTH) ? mMode : Mode.PULL_FROM_START;
    }

    /**
     * add refreshableView to a FrameLayout，then set the FrameLayout as the child of LinearLayout
     */
    private void addRefreshableView(Context context, T refreshableView) {
        mRefreshableViewWrapper = new FrameLayout(context);
        mRefreshableViewWrapper.addView(refreshableView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //add mRefreshableViewWrapper to the last position
        addViewInternal(mRefreshableViewWrapper, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private boolean isReadyForPull() {
        switch (mMode) {
            case PULL_FROM_START:
                return isReadyForPullStart();
            case PULL_FROM_END:
                return isReadyForPullEnd();
            case BOTH:
                return isReadyForPullEnd() || isReadyForPullStart();
            default:
                return false;
        }
    }

    /**
     * Actions a Pull Event
     *
     * @return true if the Event has been handled, false if there has been no change
     */
    private void pullEvent() {
        final int newScrollValue;
        final int itemDimension;
        final float initialMotionValue, lastMotionValue;

        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                initialMotionValue = mInitialMotionX;
                lastMotionValue = mLastMotionX;
                break;
            case VERTICAL:
            default:
                initialMotionValue = mInitialMotionY;
                lastMotionValue = mLastMotionY;
                break;
        }

        switch (mCurrentMode) {
            case PULL_FROM_END:
                newScrollValue = Math.round(Math.max(initialMotionValue - lastMotionValue, 0) / FRICTION);
                itemDimension = getFooterSize();
                break;

            case PULL_FROM_START:
            default:
                newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);
                itemDimension = getHeaderSize();
                break;
        }

        setHeaderScroll(newScrollValue);

        if (newScrollValue != 0 && !isRefreshing()) {
            float scale = Math.abs(newScrollValue) / (float) itemDimension;
            switch (mCurrentMode) {
                case PULL_FROM_END:
                    mFooterLayout.onPull(scale);
                    break;
                case PULL_FROM_START:
                default:
                    mHeaderLayout.onPull(scale);
                    if (mOnPullListener != null) {
                        mOnPullListener.onScaleOfLayout(scale);
                    }
                    break;
            }

            if (mState != State.PULL_TO_REFRESH && itemDimension >= Math.abs(newScrollValue)) {
                setState(State.PULL_TO_REFRESH);

            } else if (mState == State.PULL_TO_REFRESH && itemDimension < Math.abs(newScrollValue)) {
                setState(State.RELEASE_TO_REFRESH);
            }
        }
    }

    private LayoutParams getLoadingLayoutLayoutParams() {
        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            case VERTICAL:
            default:
                return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
    }

    private int getMaximumPullScroll() {
        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                return Math.round(getWidth() / FRICTION);
            case VERTICAL:
            default:
                return Math.round(getHeight() / FRICTION);
        }
    }

    private final void smoothScrollTo(int newScrollValue, long duration) {
        smoothScrollTo(newScrollValue, duration, 0, null);
    }

    private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis, OnSmoothScrollFinishedListener listener) {
        if (mSmoothScrollRunnable != null) {
            mSmoothScrollRunnable.stop();
        }

        final int oldScrollValue;
        switch (getPullToRefreshScrollDirection()) {
            case HORIZONTAL:
                oldScrollValue = getScrollX();
                break;
            case VERTICAL:
            default:
                oldScrollValue = getScrollY();
                break;
        }

        if (oldScrollValue != newScrollValue) {
            if (mScrollAnimationInterpolator == null) {
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            } else {
                post(mSmoothScrollRunnable);
            }
        }
    }

    private final void smoothScrollToAndBack(int y) {
        smoothScrollTo(y, SMOOTH_SCROLL_DURATION_MS, 0, new OnSmoothScrollFinishedListener() {
            @Override
            public void onSmoothScrollFinished() {
                smoothScrollTo(0, SMOOTH_SCROLL_DURATION_MS, DEMO_SCROLL_INTERVAL, null);
            }
        });
    }


    // ===========================================================
    // Inner, Anonymous Classes, and Enumerations
    // ===========================================================

    public interface OnLastItemVisibleListener {
        /**
         * Called when the user has scrolled to the end of the list
         */
        void onLastItemVisible();
    }

    /**
     * Listener that allows you to be notified when the user has started or
     * finished a pull event. Useful when you want to append extra UI events
     *
     * @author Chris Banes
     */
    public interface OnPullEventListener<V extends View> {

        /**
         * Called when the internal state has been changed, usually by the user
         * pulling.
         *
         * @param refreshView - View which has had it's state change.
         * @param state       - The new state of View.
         * @param direction   - One of {@link com.bu.zheng.view.pulltorefresh.common.Mode#PULL_FROM_START} or
         *                    {@link com.bu.zheng.view.pulltorefresh.common.Mode#PULL_FROM_END} depending on which direction
         *                    the user is pulling. Only useful when <var>state</var> is
         *                    {@link com.bu.zheng.view.pulltorefresh.common.State#PULL_TO_REFRESH} or
         *                    {@link com.bu.zheng.view.pulltorefresh.common.State#RELEASE_TO_REFRESH}.
         */
        void onPullEvent(final PullToRefreshBase<V> refreshView, State state, Mode direction);

    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {
                /**
                 * We do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;// 0-1000-。。。
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);// 0-1000

                final int deltaY = Math.round((mScrollFromY - mScrollToY) * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                setHeaderScroll(mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(PullToRefreshBase.this, this);
            } else {
                if (mListener != null) {
                    mListener.onSmoothScrollFinished();
                }
            }
        }

        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    public interface OnPullListener {
        void onScaleOfLayout(float scaleOfLayout);
    }

    private OnPullListener mOnPullListener;

    public void setOnPullListener(OnPullListener listener) {
        this.mOnPullListener = listener;
    }
}
