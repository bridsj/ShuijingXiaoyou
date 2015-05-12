package com.shui.jing.xiao.you.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.shui.jing.xiao.you.R;


public class ZMProgressBar extends RelativeLayout {

    private float mProgress = 0;
    private float mMaxProgress = 100f;

    private int mWidth;
    private ImageView mProgressView;

    public ZMProgressBar(Context context) {
        super(context);
        init();
    }

    public ZMProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZMProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mProgressView = new ImageView(getContext());
        mProgressView.setScaleType(ScaleType.FIT_XY);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mProgressView, params);
        mProgressView.setImageResource(R.drawable.browser_progress);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mWidth = getWidth();
        updateProgress();
    }

    public void setProgress(float progress) {
        mProgress = validate(progress);
        if (mWidth > 0) {
            updateProgress();
        }
    }

    public float getProgress() {
        return mProgress;
    }

    private float validate(float progress) {
        progress = Math.max(progress, 0);
        progress = Math.min(progress, mMaxProgress);

        return progress;
    }

    private void updateProgress() {
        LayoutParams params = (LayoutParams) mProgressView.getLayoutParams();
        params.width = (int) (mProgress / mMaxProgress * mWidth);
        mProgressView.setLayoutParams(params);
    }
}
