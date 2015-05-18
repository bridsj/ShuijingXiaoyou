package com.shui.jing.xiao.you.ui;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.constant.Constants;
import com.shui.jing.xiao.you.ui.utils.DoubleKeyUtils;
import com.shui.jing.xiao.you.ui.utils.ToastUtil;


public class MainActivity extends BaseFragmentActivity {
    private Button mTaobaoBtn, mZaixianBtn, mXiaoyoubaBtn, mXiaoyouWeiBtn, mXiayoutuijianBtn;


    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        mTaobaoBtn = (Button) findViewById(R.id.category_taobao_btn);
        mZaixianBtn = (Button) findViewById(R.id.category_zaixian_btn);
        mXiaoyoubaBtn = (Button) findViewById(R.id.category_xiaoyouba_btn);
        mXiaoyouWeiBtn = (Button) findViewById(R.id.category_xiaoyouwei_btn);
        mXiayoutuijianBtn = (Button) findViewById(R.id.category_xiaoyoutuijian_btn);
    }

    @Override
    protected void initWidgetActions() {
        mTaobaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL, "http://shop70278877.m.taobao.com/?t=" + System.currentTimeMillis());
                intent.putExtra(WebViewActivity.KEY_TITLE, getString(R.string.category_taobao_str));
                intent.putExtra(WebViewActivity.IS_SHOW_SHARE, true);
                startActivity(intent);
            }
        });
        mZaixianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ZaixianActivity.class);
                startActivity(intent);
            }
        });

        mXiaoyoubaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL, "http://m.wsq.qq.com/218149417?_wv=1&t=" + System.currentTimeMillis());
                intent.putExtra(WebViewActivity.KEY_TITLE, getString(R.string.category_xiaoyouba_str));
                startActivity(intent);
            }
        });
        mXiaoyouWeiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL, "http://xiaoyoushuijing.bama555.com/?t=" + System.currentTimeMillis());
                intent.putExtra(WebViewActivity.KEY_TITLE, getString(R.string.category_xiaoyouwei_str));
                startActivity(intent);
            }
        });
        mXiayoutuijianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.KEY_URL, "http://xiaoyoushuijing.bama555.com/cate?id=53461760a192f062&wxid=&t=" + System.currentTimeMillis());
                intent.putExtra(WebViewActivity.KEY_TITLE, getString(R.string.category_xiaoyoutuijian_str));
                startActivity(intent);
            }
        });

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                ToastUtil.showToast(getApplicationContext(), getString(R.string.exit_warn));
                exitTime = System.currentTimeMillis();// 更新firstTime
                return true;
            } else {
                Intent intent = new Intent(Constants.FINISH_ACTION_NAME);
                sendBroadcast(intent);
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isFinishAnim() {
        return false;
    }
}
