package com.shui.jing.xiao.you.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.model.ZhanBuModel;
import com.shui.jing.xiao.you.ui.utils.ResUtil;
import com.shui.jing.xiao.you.ui.utils.TimeUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class ZaixianAdapter extends BaseAdapter {
    private Context mContext;
    private List<ZhanBuModel> mList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private ResUtil mResUtil;

    public ZaixianAdapter(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        this.mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888).displayer(new SimpleBitmapDisplayer()).build();
        mResUtil = ResUtil.getInstance(context);
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.activity_zaixian_item, null);
            holder.mTitleText = (TextView) convertView.findViewById(R.id.activity_zaixian_title);
            holder.mTimeText = (TextView) convertView.findViewById(R.id.activity_zaixian_time);
            holder.mContentImg = (ImageView) convertView.findViewById(R.id.activity_zaixian_img);
            holder.mSpaceView = convertView.findViewById(R.id.space_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ZhanBuModel model = mList.get(position);
        if (model.type == 1) {
            holder.mTitleText.setText(mContext.getString(R.string.taluo_title_str));
        } else {
            holder.mTitleText.setText(mContext.getString(R.string.shuijing_title_str));
        }
        holder.mTimeText.setText(TimeUtil.convert2String(model.createTime));
        mImageLoader.displayImage("drawable://" + mResUtil.drawableId(model.drawable), holder.mContentImg, mOptions);
        if (position == (mList.size() - 1)) {
            holder.mSpaceView.setVisibility(View.GONE);
        } else {
            holder.mSpaceView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    final class ViewHolder {
        public TextView mTitleText;
        public TextView mTimeText;
        public ImageView mContentImg;
        public View mSpaceView;
    }

    public void notifyDataSetChanged(ZhanBuModel model) {
        if (mList == null) {
            mList = new LinkedList<>();
        }
        if (model != null) {
            mList.add(model);
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<ZhanBuModel> list) {
        if (mList == null) {
            mList = new LinkedList<>();
        }

        if (list != null) {
            list.addAll(mList);
        }
        mList = list;
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public List<ZhanBuModel> getList() {
        return mList;
    }
}
