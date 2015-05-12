package com.shui.jing.xiao.you.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.model.ZhanBuModel;
import com.shui.jing.xiao.you.ui.utils.PhoneUtil;
import com.shui.jing.xiao.you.ui.utils.ResUtil;
import com.shui.jing.xiao.you.ui.utils.TimeUtil;
import com.shui.jing.xiao.you.ui.utils.ToastUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by dengshengjin on 15/4/19.
 */
public class ZaixianDetailActivity extends BaseFragmentActivity {
    private TextView mTitleText, mTitleText2;
    private TextView mBackText;
    private Handler mHandler;
    private ImageView mImageView;
    private ZhanBuModel zhanBuModel;
    public static final String MODEL = "model";
    private TextView mTimeText;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private ResUtil mResUtil;
    private String APP_ID = "wx41b1e946bf84b869";
    protected IWXAPI api;
    private AlertDialog.Builder mBuilder;
    private ViewGroup mWeixinBox;
    private TextView mDescText;

    @Override
    protected void initData() {
        zhanBuModel = (ZhanBuModel) getIntent().getSerializableExtra(MODEL);
        mImageLoader = ImageLoader.getInstance();
        this.mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888).displayer(new SimpleBitmapDisplayer()).build();
        mResUtil = ResUtil.getInstance(getApplicationContext());
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_zaixian_detail);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText2 = (TextView) findViewById(R.id.activity_zaixian_title);
        mWeixinBox = (ViewGroup) findViewById(R.id.weixin_box);
        mTimeText = (TextView) findViewById(R.id.activity_zaixian_time);
        mBackText = (TextView) findViewById(R.id.back_btn);
        mDescText = (TextView) findViewById(R.id.desc_text);
        mImageView = (ImageView) findViewById(R.id.activity_zaixian_img);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
                int imgWidth = (int) (PhoneUtil.getDisplayWidth(getApplicationContext()) - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
                mDescText.setVisibility(View.GONE);
                if (zhanBuModel != null) {
                    if (zhanBuModel.type == 1) {
                        lp.height = getImgHeight(imgWidth, 450, 730);
                        mImageView.requestLayout();
                        if (!TextUtils.isEmpty(zhanBuModel.desc)) {
                            mDescText.setVisibility(View.VISIBLE);
                            mDescText.setText(zhanBuModel.desc);
                        }
                    } else {
                        lp.height = getImgHeight(imgWidth, 500, 832);
                        mImageView.requestLayout();
                    }
                }

            }
        });
        if (zhanBuModel != null) {
            if (zhanBuModel.type == 1) {
                mTitleText.setText(getString(R.string.taluo_title_str));
                mTitleText2.setText(getString(R.string.taluo_title_str));
            } else {
                mTitleText.setText(getString(R.string.shuijing_title_str));
                mTitleText2.setText(getString(R.string.shuijing_title_str));
            }
            mTimeText.setText(TimeUtil.convert2String(zhanBuModel.createTime));
            mImageLoader.displayImage("drawable://" + mResUtil.drawableId(zhanBuModel.drawable), mImageView, mOptions);
        }
        mWeixinBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attationUs();
            }
        });
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

    private int getImgHeight(int imgWidth, int width, int height) {
        return (int) (height * imgWidth * 1.0f / width);
    }

    private void attationUs() {
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(getString(R.string.weixin_name));
        if (!api.isWXAppInstalled()) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.clip_weixin_succ1));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mBuilder = new AlertDialog.Builder(ZaixianDetailActivity.this, R.style.alert_dialog_style);
            } else {
                mBuilder = new AlertDialog.Builder(ZaixianDetailActivity.this);
            }
            mBuilder.setTitle(getString(R.string.warn_title))
                    .setMessage(getString(R.string.clip_weixin_succ2)).setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }

            }).setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    api.openWXApp();


                }
            }).create().show();
        }
    }

}
