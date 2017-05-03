package com.bu.zheng.view.pulltorefresh.common;

/**
 * Created by BuZheng on 2017/5/2.
 */

public enum Mode {
    /**
     * Disable all Pull-to-Refresh gesture and Refreshing handling
     */
    DISABLED(0x0),

    /**
     * Only allow the user to Pull from the start of the Refreshable View to
     * refresh. The start is either the Top or Left, depending on the
     * scrolling direction.
     */
    PULL_FROM_START(0x1),

    /**
     * Only allow the user to Pull from the end of the Refreshable View to
     * refresh. The start is either the Bottom or Right, depending on the
     * scrolling direction.
     */
    PULL_FROM_END(0x2),

    /**
     * Allow the user to both Pull from the start, from the end to refresh.
     */
    BOTH(0x3),

    /**
     * Disables Pull-to-Refresh gesture handling, but allows manually
     * setting the Refresh state via
     * {@link com.bu.zheng.view.pulltorefresh.common.PullToRefreshBase#setRefreshing() setRefreshing()}.
     */
    MANUAL_REFRESH_ONLY(0x4);

    static Mode mapIntToValue(final int modeInt) {
        for (Mode value : Mode.values()) {
            if (modeInt == value.getIntValue()) {
                return value;
            }
        }
        return getDefault();
    }

    static Mode getDefault() {
        return PULL_FROM_START;
    }

    private int mIntValue;

    Mode(int modeInt) {
        mIntValue = modeInt;
    }

    boolean permitsPullToRefresh() {
        return !(this == DISABLED || this == MANUAL_REFRESH_ONLY);
    }

    /**
     * @return true if this mode wants the Loading Layout Header to be shown
     */
    public boolean showHeaderLoadingLayout() {
        return this == PULL_FROM_START || this == BOTH;
    }

    /**
     * @return true if this mode wants the Loading Layout Footer to be shown
     */
    public boolean showFooterLoadingLayout() {
        return this == PULL_FROM_END || this == BOTH || this == MANUAL_REFRESH_ONLY;
    }

    int getIntValue() {
        return mIntValue;
    }
}