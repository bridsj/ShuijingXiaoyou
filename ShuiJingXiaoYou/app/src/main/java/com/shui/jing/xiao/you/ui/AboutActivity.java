package com.shui.jing.xiao.you.ui;

import android.view.View;
import android.widget.TextView;

import com.shui.jing.xiao.you.R;

/**
 * Created by dengshengjin on 15/4/19.
 */
public class AboutActivity extends BaseFragmentActivity {

    private TextView mBackText;

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_about);
        mBackText = (TextView) findViewById(R.id.back_btn);
    }

    @Override
    protected void initWidgetActions() {
        mBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
