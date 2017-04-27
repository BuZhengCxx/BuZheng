package com.bu.zheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bu.zheng.R;
import com.bu.zheng.util.AndroidUtil;
import com.bu.zheng.util.ToastUtil;
import com.bu.zheng.view.richtext.BaseRichTextClickListener;
import com.bu.zheng.view.richtext.RichTextView;

/**
 * Created by BuZheng on 2017/3/17.
 */

public class RichTextActivity extends BaseActivity {

    private ImageView mTitleBack;
    private TextView mTitleLeft;

    RichTextView richTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidUtil.transparentStatusBar(this);

        setContentView(R.layout.activity_rich_text_layout);

        initViews();
    }

    private void initViews() {
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleLeft = (TextView) findViewById(R.id.title_left);
        mTitleLeft.setText("RichText");
        mTitleBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        richTextView = (RichTextView) findViewById(R.id.richtext1);
        richTextView.setOnRichTextClickListener(new BaseRichTextClickListener() {
            @Override
            protected void doOther(View view, String richText, int type) {
                ToastUtil.showToast(RichTextActivity.this, richText);
            }
        });
        richTextView.setRichText(getString(R.string.rich_text_test_str1));
    }
}
