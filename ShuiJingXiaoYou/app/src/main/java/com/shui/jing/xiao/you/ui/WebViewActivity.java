package com.shui.jing.xiao.you.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.utils.AppUtil;
import com.shui.jing.xiao.you.ui.utils.DoubleKeyUtils;
import com.shui.jing.xiao.you.ui.utils.KeyboardUtil;
import com.shui.jing.xiao.you.ui.widget.ZMProgressBar;

import java.io.File;

/**
 * @author deng.shengjin
 * @version create_time:2014-7-18_下午5:55:19
 * @Description TODO
 */
public class WebViewActivity extends BaseFragmentActivity {
    private ZMProgressBar mProgressBar;
    private WebView mWebView;
    private View mBrowserBack, mBrowserForward, mBrowserRefresh;

    private String mUrl;
    private String mData;
    private String mTitle;
    private boolean isShowShare;

    public static final String KEY_URL = "URL";
    public static final String KEY_DATA = "Data";
    public static final String KEY_TITLE = "Title";
    public static final String IS_SHOW_SHARE = "ShowShare";


    private TextView backBtn;
    private TextView titleText;
    private ImageView shareBtn;


    @Override
    protected void onResume() {
        super.onResume();
    }

    ;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        mData = getIntent().getStringExtra(KEY_DATA);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        isShowShare = getIntent().getBooleanExtra(IS_SHOW_SHARE, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_web_view);
        backBtn = (TextView) findViewById(R.id.back_btn);
        titleText = (TextView) findViewById(R.id.title_text);
        mProgressBar = (ZMProgressBar) findViewById(R.id.progress_bar);

        if (!TextUtils.isEmpty(mTitle)) {
            titleText.setText(mTitle);
        } else {
            titleText.setText(getString(R.string.detail_title));
        }

        shareBtn = (ImageView) findViewById(R.id.share_btn);
        if (isShowShare && AppUtil.isAppInstalled(getApplicationContext(), "com.taobao.taobao")) {
            shareBtn.setVisibility(View.VISIBLE);
        } else {
            shareBtn.setVisibility(View.GONE);
        }
        mBrowserBack = findViewById(R.id.btn_browser_back);
        mBrowserForward = findViewById(R.id.btn_browser_forward);
        mBrowserRefresh = findViewById(R.id.btn_browser_refresh);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);


        mWebView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setDownloadListener(new WebViewDownLoadListener());
        mWebView.setHorizontalScrollBarEnabled(false);

//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        mWebView.getSettings().setAppCachePath(appCachePath);
//        mWebView.getSettings().setAllowFileAccess(true);
        CookieManager.getInstance().setAcceptCookie(true);


        shareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DoubleKeyUtils.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(mWebView.getUrl()) || TextUtils.isEmpty(mWebView.getTitle())) {
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://shop.m.taobao.com/shop/shop_index.htm?shop_id=70278877"); // 淘宝商品的地址
                    intent.setData(content_url);
                    intent.setClassName("com.taobao.taobao", "com.taobao.tao.shop.router.ShopUrlRouterActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Throwable t) {
                    t.printStackTrace();
                    attationUs();
                }


            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                setTopProgress(newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("mailto:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                        startActivity(intent);
                    } catch (Throwable e) {

                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setTopProgress(2);// 防止进度加载慢的情况下长时间看不到进度条
                setTopProgressBarVisibility(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isFinishing()) {
                    return;
                }
                setTopProgress(100);
                setTopProgressBarVisibility(false);

                updateBtnStatus();
            }

        });
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        } else if (!TextUtils.isEmpty(mData)) {
            mWebView.loadDataWithBaseURL(null, mData, "text/html", "UTF-8", null);
        }
//        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initWidgetActions() {
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mBrowserBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                updateBtnStatus();
            }
        });

        mBrowserForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                updateBtnStatus();
            }
        });

        mBrowserRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUrl) && !TextUtils.isEmpty(mData)) {
                    mWebView.loadDataWithBaseURL(null, mData, "text/html", "UTF-8", null);
                } else {
                    mWebView.reload();
                }

            }
        });
    }

    protected void setTopProgressBarVisibility(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setTopProgress(float progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            updateBtnStatus();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        KeyboardUtil.hideSoftKeyboard(WebViewActivity.this);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            if (this.mWebView != null) {
                ((RelativeLayout) findViewById(R.id.web_view_root_box)).removeView(this.mWebView);
                this.mWebView.stopLoading();
                this.mWebView.removeAllViews();
                this.mWebView.destroy();
                this.mWebView = null;
                System.gc();
            }
        } catch (Throwable t) {

        }
        new Thread() {
            @Override
            public void run() {
                try {
                    clearWebViewCache();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }.start();
    }

    private void updateBtnStatus() {
        mBrowserBack.setEnabled(mWebView.canGoBack());
        mBrowserForward.setEnabled(mWebView.canGoForward());
    }

    class WebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e1) {
                }
            }
        }
    }

    private void attationUs() {
        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(getString(R.string.taobao_warn_str));
        AlertDialog.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mBuilder = new AlertDialog.Builder(WebViewActivity.this, R.style.alert_dialog_style);
        } else {
            mBuilder = new AlertDialog.Builder(WebViewActivity.this);
        }
        mBuilder.setTitle(getString(R.string.warn_title))
                .setMessage(getString(R.string.clip_taobao)).setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                try {
                    intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                    startActivity(intent);
                } catch (Throwable e) {
                }


            }
        }).create().show();
    }


    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {


        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

}