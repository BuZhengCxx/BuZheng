package com.bu.zheng.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.bu.zheng.R;
import com.bu.zheng.skin.SkinViewFactory;
import com.bu.zheng.util.AndroidUtil;

import java.util.ArrayList;


/**
 * Created by BuZheng on 2017/3/15.
 */

public abstract class BaseActivity extends Activity {

    private LayoutInflater mLayoutInflater;

    private String mTestTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater.setFactory(new SkinViewFactory(this));
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected <T extends Parcelable> T getParcelableExtra(String name) {
        try {
            return getIntent().getParcelableExtra(name);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return null;
    }

    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        try {
            return getIntent().getParcelableArrayListExtra(name);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return null;
    }

    protected int getIntExtra(String name, int defaultValue) {
        try {
            return getIntent().getIntExtra(name, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return defaultValue;
    }

    protected long getLongExtra(String name, long defaultValue) {
        try {
            return getIntent().getLongExtra(name, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return defaultValue;
    }

    public String getStringExtra(String name) {
        try {
            return getIntent().getStringExtra(name);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return null;
    }

    public boolean getBooleanExtra(String name, boolean defaultValue) {
        try {
            return getIntent().getBooleanExtra(name, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        return defaultValue;
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
