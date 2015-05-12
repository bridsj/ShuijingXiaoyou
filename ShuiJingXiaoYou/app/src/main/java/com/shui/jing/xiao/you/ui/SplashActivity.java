package com.shui.jing.xiao.you.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.shui.jing.xiao.you.R;

/**
 * Created by dengshengjin on 15/4/19.
 */
public class SplashActivity extends Activity {
    private final String FINISH_ACTION = "finish_action";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initData();
        initViews();
        initWidgetActions();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                if (!isFinishing()) {
                    finish();
                }
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                mHandler.sendEmptyMessageAtTime(101, 300);
            }
        }
    };

    protected void initData() {

    }

    protected void initViews() {
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(100, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.sendEmptyMessage(101);
    }

    protected void initWidgetActions() {

    }
}
