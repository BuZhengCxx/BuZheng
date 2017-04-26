package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;
import com.bu.zheng.util.ToastUtil;
import com.bu.zheng.view.richtext.BaseRichTextClickListener;
import com.bu.zheng.view.richtext.RichTextView;

/**
 * Created by BuZheng on 2017/3/17.
 */

public class RichTextActivity extends BaseActivity {

    RichTextView richTextView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_rich_text_layout);

        setTitle("RichText");

        initViews();

        onLoadFinished();
    }

    private void initViews() {
        richTextView1 = (RichTextView) findViewById(R.id.richtext1);
        richTextView1.setOnRichTextClickListener(new BaseRichTextClickListener() {
            @Override
            protected void doOther(View view, String richText, int type) {
                ToastUtil.showToast(RichTextActivity.this, richText);
            }
        });
        richTextView1.setRichText(getString(R.string.rich_text_test_str1));
    }

    @Override
    protected void onReloadData() {

    }
}
