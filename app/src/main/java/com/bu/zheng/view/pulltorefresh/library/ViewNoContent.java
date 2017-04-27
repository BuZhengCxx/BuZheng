package com.bu.zheng.view.pulltorefresh.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bu.zheng.R;


/**
 * Created by chenxiaoxiong on 15/3/27.
 */
public class ViewNoContent extends LinearLayout {

    public ViewNoContent(Context context) {
        super(context);
        init(context);
    }

    private ImageView mNoContentImg;
    private TextView mErrorTipTxt;
    private TextView mNoContentTxt;

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.view_pull_no_content_layout_listview,this,true);

        mNoContentImg = (ImageView)root.findViewById(R.id.no_content_img);
        mErrorTipTxt = (TextView)root.findViewById(R.id.pulllist_error_text);
        mNoContentTxt = (TextView) root.findViewById(R.id.no_content);
    }

    public void withNoRetryTip(int strId){
        mNoContentImg.setVisibility(View.VISIBLE);
        mNoContentTxt.setVisibility(View.GONE);
        mErrorTipTxt.setText(strId);
    }
}
