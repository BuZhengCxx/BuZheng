package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by chenxiaoxiong on 15/3/26.
 */
public class LoadingStateLayout extends RelativeLayout {

    public LoadingStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LoadingStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private ViewLoadingState mViewLoadingState ;
    private ViewNoNetwork mViewNoNetwork ;
    private ViewLoadError mViewLoadError ;
    private ViewNoContent mViewNoContent ;
    private LayoutParams mLp ;

    private void init(Context context){
        mViewLoadingState = new ViewLoadingState(context,this);
        mViewNoNetwork = new ViewNoNetwork(context,this);
        mViewLoadError = new ViewLoadError(context,this);
        mViewNoContent = new ViewNoContent(context);

        mLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mLp.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(mViewLoadingState,mLp);
        addView(mViewNoNetwork,mLp);
        addView(mViewLoadError,mLp);
        addView(mViewNoContent,mLp);
    }

    public void load(){
        mViewLoadingState.setVisibility(View.VISIBLE);
        mViewLoadingState.startLoadingAnim();
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.GONE);
        mViewNoContent.setVisibility(View.GONE);
        if(mOnLoadingListener != null){
            mOnLoadingListener.onLoading();
        }
    }

    public void reLoad(){
        mViewLoadingState.setVisibility(View.VISIBLE);
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.GONE);
        mViewNoContent.setVisibility(View.GONE);
        if(mOnLoadingListener != null){
            mOnLoadingListener.onReLoading();
        }
    }

    public void onLoadingComplete(){
        mViewLoadingState.setVisibility(View.GONE);
        mViewLoadingState.stopLoadingAnim();
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.GONE);
        mViewNoContent.setVisibility(View.GONE);
    }

    public void onNoNetwork(){
        mViewLoadingState.setVisibility(View.GONE);
        mViewLoadingState.stopLoadingAnim();
        mViewNoNetwork.setVisibility(View.VISIBLE);
        mViewLoadError.setVisibility(View.GONE);
        mViewNoContent.setVisibility(View.GONE);
    }

    public void onLoadError(){
        mViewLoadingState.setVisibility(View.GONE);
        mViewLoadingState.stopLoadingAnim();
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.VISIBLE);
        mViewNoContent.setVisibility(View.GONE);
    }

    public void onLoadError(int tipStrId){
        mViewLoadingState.setVisibility(View.GONE);
        mViewLoadingState.stopLoadingAnim();
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.VISIBLE);
        mViewNoContent.setVisibility(View.GONE);

        mViewLoadError.setTipText(tipStrId);
    }

    public void onNoContent(){
        mViewLoadingState.setVisibility(View.GONE);
        mViewLoadingState.stopLoadingAnim();
        mViewNoNetwork.setVisibility(View.GONE);
        mViewLoadError.setVisibility(View.GONE);
        mViewNoContent.setVisibility(View.VISIBLE);
    }


    public interface OnLoadingListener{
        public void onLoading();
        public void onReLoading();
    }

    private OnLoadingListener mOnLoadingListener ;
    public void setOnLoadingListener(OnLoadingListener listener){
        mOnLoadingListener = listener ;
    }

}
