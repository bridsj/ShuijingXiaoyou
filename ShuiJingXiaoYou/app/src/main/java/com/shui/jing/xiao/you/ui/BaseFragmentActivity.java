package com.shui.jing.xiao.you.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.constant.Constants;


/**
 * @author deng.shengjin
 * @version create_time:2014-3-27_上午11:01:46
 * @Description 父类
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    protected FinishBroadcastReceiver finishReceiver;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initData();
        initViews();
        initWidgetActions();
        try {
            if (finishReceiver == null) {
                finishReceiver = new FinishBroadcastReceiver();
            }
            IntentFilter finishIntent = new IntentFilter();
            finishIntent.addAction(Constants.FINISH_ACTION_NAME);
            registerReceiver(finishReceiver, finishIntent);
        } catch (Exception e) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (finishReceiver != null) {
                unregisterReceiver(finishReceiver);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.no_anim);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.no_anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishAnim()) {
            overridePendingTransition(R.anim.no_anim, R.anim.push_right_out);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isFinishAnim()) {
            overridePendingTransition(R.anim.no_anim, R.anim.push_right_out);
        }
    }

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initWidgetActions();

    final class FinishBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.FINISH_ACTION_NAME + getPackageName())) {
                finish();
            }
        }
    }

    protected boolean isFinishAnim() {
        return true;
    }

    protected void setVisible(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    protected void setInVisible(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.INVISIBLE);
    }

    protected void setGone(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.GONE);
    }

}
