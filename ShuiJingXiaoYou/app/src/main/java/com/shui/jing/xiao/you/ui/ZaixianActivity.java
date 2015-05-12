package com.shui.jing.xiao.you.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.adapter.ZaixianAdapter;
import com.shui.jing.xiao.you.ui.db.ZhanBuDBUtil;
import com.shui.jing.xiao.you.ui.helper.ZhanBuHelper;
import com.shui.jing.xiao.you.ui.model.ZhanBuModel;
import com.shui.jing.xiao.you.ui.utils.TimeUtil;
import com.shui.jing.xiao.you.ui.widget.MyProgressBar;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/4/19.
 */
public class ZaixianActivity extends BaseFragmentActivity {
    private TextView mTitleText;
    private TextView mBackText;
    private TextView mStartText;
    private AlertDialog.Builder mBuilder;
    private ListView mListView;
    private ZaixianAdapter mAdapter;
    private List<ZhanBuModel> mList;
    private Handler mHandler;

    private List<ZhanBuModel> mTaluoList, mShuiJingList;
    private ZhanBuHelper mZhanBuHelper;
    private ZhanBuDBUtil mZhanBuDBUtil;
    private Executor mExecutor = Executors.newCachedThreadPool();
    private final int PAGE_SIZE = 30;
    private View mHeaderBox, mFooterBox;
    private MyProgressBar mHeaderProBar, mFooterProBar;

    protected boolean mIsScrollToTop = false;
    private boolean mIsLoading;
    private boolean mHasMore;
    private TextView mEmptyView;

    @Override
    protected void initData() {
        mIsLoading = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mBuilder = new AlertDialog.Builder(ZaixianActivity.this, R.style.alert_dialog_style);
        } else {
            mBuilder = new AlertDialog.Builder(ZaixianActivity.this);
        }
        mBuilder.setTitle(getString(R.string.zaixian_content));
        mAdapter = new ZaixianAdapter(getApplicationContext());
        mZhanBuHelper = new ZhanBuHelper(getApplicationContext());
        mZhanBuDBUtil = new ZhanBuDBUtil();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTaluoList = mZhanBuHelper.queryModel(mZhanBuHelper.createTaluoCardJson(), 1);
                mShuiJingList = mZhanBuHelper.queryModel(mZhanBuHelper.createDiamondJson(), 2);
            }
        });
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_zaixian);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        mBackText = (TextView) findViewById(R.id.back_btn);
        mStartText = (TextView) findViewById(R.id.start_btn);
        View headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_progress, null);
        mHeaderProBar = (MyProgressBar) headerView.findViewById(R.id.progress_bar);
        mHeaderBox = headerView.findViewById(R.id.loading_progress_box);
        mHeaderBox.setVisibility(View.GONE);
        mListView.addHeaderView(headerView);
        View footerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_progress, null);
        mFooterProBar = (MyProgressBar) footerView.findViewById(R.id.progress_bar);
        mFooterBox = footerView.findViewById(R.id.loading_progress_box);
        mFooterBox.setVisibility(View.GONE);
        mListView.addFooterView(footerView);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
    }


    @Override
    protected void initWidgetActions() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE && mIsScrollToTop && !mIsLoading && mHasMore) {
                    mHeaderBox.setVisibility(View.VISIBLE);
                    mHeaderProBar.show();

                    loadData(false, new Callback() {
                        @Override
                        public void callback() {
                            mHeaderBox.setVisibility(View.GONE);
                            mHeaderProBar.stop();
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mIsScrollToTop = (firstVisibleItem <= 1);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ZhanBuModel model = mAdapter.getList().get(position - mListView.getHeaderViewsCount());
                    Intent intent = new Intent(ZaixianActivity.this, ZaixianDetailActivity.class);
                    intent.putExtra(ZaixianDetailActivity.MODEL, model);
                    startActivity(intent);
                } catch (Throwable t) {

                }
            }
        });
        mBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] items = new String[]{getString(R.string.category_taluo_str), getString(R.string.category_shuijing_str)};
        mBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (mIsLoading) {
                            return;
                        }
                        mListView.setSelection(mAdapter.getCount() + mListView.getHeaderViewsCount() - 1);
                        mFooterBox.setVisibility(View.VISIBLE);
                        mFooterProBar.show();
                        mIsLoading = true;
                        mExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mTaluoList != null && !mTaluoList.isEmpty()) {
                                    int position = new Random().nextInt(mTaluoList.size());
                                    final ZhanBuModel model = mTaluoList.get(position);
                                    model.createTime = System.currentTimeMillis();
                                    model.type = 1;
                                    model.save();

                                    getHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mIsLoading = false;
                                            mFooterBox.setVisibility(View.GONE);
                                            mFooterProBar.stop();
                                            mAdapter.notifyDataSetChanged(model);
                                        }
                                    }, 800);
                                }
                            }
                        });
                        break;
                    case 1:
                        if (mIsLoading) {
                            return;
                        }
                        mListView.setSelection(mAdapter.getCount() + mListView.getHeaderViewsCount() - 1);
                        mFooterBox.setVisibility(View.VISIBLE);
                        mFooterProBar.show();
                        mIsLoading = true;
                        mExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mShuiJingList != null && !mShuiJingList.isEmpty()) {
                                    int position = new Random().nextInt(mShuiJingList.size());
                                    final ZhanBuModel model = mShuiJingList.get(position);
                                    model.createTime = System.currentTimeMillis();
                                    Log.e("", "createTime=" + TimeUtil.convert2String(model.createTime));
                                    model.type = 2;
                                    model.save();
                                    getHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mIsLoading = false;
                                            mFooterBox.setVisibility(View.GONE);
                                            mFooterProBar.stop();
                                            mAdapter.notifyDataSetChanged(model);
                                        }
                                    }, 800);
                                }
                            }
                        });
                        break;
                }
            }
        });
        mStartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBuilder.create().show();
            }
        });
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(true, null);
            }
        },100);

    }

    private void loadData(final boolean isMoveToBottom, final Callback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mIsLoading = true;
                long time = 0;
                if (!isMoveToBottom) {
                    time = mAdapter.getList().get(0).createTime;
                } else {
                    time = TimeUtil.getCurrTime();
                }
                mList = mZhanBuDBUtil.queryList(time, PAGE_SIZE);
                int mSize = mZhanBuDBUtil.querySize();
                if (mList != null) {
                    Collections.sort(mList);
                    int mListSize = mList.size() + mAdapter.getCount();
                    if (mListSize >= mSize) {
                        mHasMore = false;
                    } else {
                        mHasMore = true;
                    }
                    Log.e("", "createTime mHasMore=" + mHasMore + "," + mListSize + "," + mSize + "," + mList.size() + "," + mAdapter.getCount() + "," + TimeUtil.convert2String(time));
                    if (isMoveToBottom) {
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIsLoading = false;
                                mAdapter.notifyDataSetChanged(mList);
                                mListView.setSelection(mAdapter.getCount() + mListView.getHeaderViewsCount() - 1);
                            }
                        }, 100);
                    } else {
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIsLoading = false;
                                if (callback != null) {
                                    callback.callback();
                                }
                                int size = mList.size();
                                mAdapter.notifyDataSetChanged(mList);
                                mListView.setSelection(size + mListView.getHeaderViewsCount());
                            }
                        }, 800);
                    }
                }


            }
        });

    }

    public interface Callback {
        void callback();
    }
}
