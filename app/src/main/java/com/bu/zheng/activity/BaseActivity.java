package com.bu.zheng.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.skin.SkinViewFactory;
import com.bu.zheng.util.AndroidUtil;


/**
 * Created by BuZheng on 2017/3/15.
 */

public abstract class BaseActivity extends Activity {

    private FrameLayout mLayoutRoot;
    private volatile View mLoadingView;
    private volatile View mErrorView;
    private volatile View mNoContentView;

    private volatile View mTitleView;
    protected volatile ImageView mTitleBack;
    protected volatile TextView mTitleRight;
    protected volatile ImageView mIconRight;

    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater.setFactory(new SkinViewFactory(this));
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_base_layout);

        mLayoutRoot = (FrameLayout) findViewById(R.id.layout_root);

        initTitle();
        showLoadingView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, mLayoutRoot, false);
        mLayoutRoot.addView(view, 0);
    }

    @Override
    public void setContentView(View view) {
        mLayoutRoot.addView(view, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    protected abstract void onReloadData();

    public void onLoadFinished() {
        hideErrorView();
        hideLoadingView();
        hideNoContentView();
    }

    private void initTitle() {
        mTitleView = findViewById(R.id.view_title);
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleRight = (TextView) findViewById(R.id.title_right);
        mIconRight = (ImageView) findViewById(R.id.icon_right);
    }

    public void showLoadingView() {
        getLoadingView().setVisibility(View.VISIBLE);
        hideErrorView();
        hideNoContentView();
    }

    public void hideLoadingView() {
        getLoadingView().setVisibility(View.GONE);
    }

    public void showErrorView() {
        hideLoadingView();
        hideNoContentView();
        stopProgressDialog();
        getErrorView().setVisibility(View.VISIBLE);
    }

    public void hideErrorView() {
        getErrorView().setVisibility(View.GONE);
    }

    public void showNoContentView() {
        hideLoadingView();
        hideErrorView();
        getNoContentView().setVisibility(View.VISIBLE);
    }

    public void showNoContentView(int strId) {
        hideLoadingView();
        hideErrorView();
        getNoContentView().setVisibility(View.VISIBLE);
        ((TextView) getNoContentView().findViewById(R.id.no_content_tip)).setText(strId);
    }

    public void hideNoContentView() {
        getNoContentView().setVisibility(View.GONE);
    }

    private View getLoadingView() {
        if (mLoadingView == null) {
            synchronized (BaseActivity.class) {
                if (mLoadingView == null) {
                    ViewStub stub = (ViewStub) findViewById(R.id.view_loading);
                    if (stub != null) {
                        mLoadingView = stub.inflate();
                    }
                }
            }
        }
        return mLoadingView;
    }

    private View getErrorView() {
        if (mErrorView == null) {
            synchronized (BaseActivity.class) {
                if (mErrorView == null) {
                    ViewStub stub = (ViewStub) findViewById(R.id.view_error);
                    if (stub != null) {
                        mErrorView = stub.inflate();
                        mErrorView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showLoadingView();
                                onReloadData();
                            }
                        });
                    }
                }
            }
        }
        return mErrorView;
    }

    private View getNoContentView() {
        if (mNoContentView == null) {
            synchronized ((BaseActivity.class)) {
                if (mNoContentView == null) {
                    ViewStub stub = (ViewStub) findViewById(R.id.view_no_content);
                    mNoContentView = stub.inflate();
                }
            }
        }
        return mNoContentView;
    }

    private View getTitleView() {
        if (mTitleView == null) {
            synchronized ((BaseActivity.class)) {
                if (mTitleView == null) {
                    mTitleView = findViewById(R.id.view_title);
                }
            }
        }
        return mTitleView;
    }

    public void hideTitle() {
        getTitleView().setVisibility(View.GONE);
    }

    public void setTitle(int resId) {
        ((TextView) getTitleView().findViewById(R.id.title_middle)).setText(resId);
    }

    public void setTitle(String title) {
        ((TextView) getTitleView().findViewById(R.id.title_middle)).setText(title);
    }

    public void setTitleRight(int resId) {
        mTitleRight = (TextView) getTitleView().findViewById(R.id.title_right);
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText(resId);
    }

    public void setTitleRight(String title) {
        mTitleRight = (TextView) getTitleView().findViewById(R.id.title_right);
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText(title);
    }

    private AlertDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.view_common_dialog_alert_waiting, null);
            mProgressDialog = builder.setView(view).create();
        }
        mProgressDialog.show();
        mProgressDialog.getWindow().setLayout(AndroidUtil.dip2px(this, 100), AndroidUtil.dip2px(this, 75));
    }

    public void stopProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
