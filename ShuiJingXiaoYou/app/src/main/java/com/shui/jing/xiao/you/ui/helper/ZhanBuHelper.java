package com.shui.jing.xiao.you.ui.helper;

import android.content.Context;
import android.text.TextUtils;

import com.shui.jing.xiao.you.R;
import com.shui.jing.xiao.you.ui.model.ZhanBuModel;
import com.shui.jing.xiao.you.ui.utils.LibIOUtil;
import com.shui.jing.xiao.you.ui.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class ZhanBuHelper {
    private Context mContext;

    public ZhanBuHelper(Context context) {
        mContext = context;
    }

    public String createDiamondJson() {
        InputStream is = mContext.getResources().openRawResource(R.raw.diamond_data);
        return LibIOUtil.convertStreamToStr(is);
    }

    public String createTaluoCardJson() {
        InputStream is = mContext.getResources().openRawResource(R.raw.taluo_card_data);
        return LibIOUtil.convertStreamToStr(is);
    }

    public List<ZhanBuModel> queryModel(String jsonStr, int type) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            JSONArray arr = new JSONArray(jsonStr);
            if (arr != null) {
                List<ZhanBuModel> list = new LinkedList<>();
                for (int i = 0, len = arr.length(); i < len; i++) {
                    ZhanBuModel model = new ZhanBuModel();
                    JSONObject obj = arr.optJSONObject(i);
                    model.zhanBuId = obj.optInt("id");
                    model.name = obj.optString("name");
                    model.desc = obj.optString("desc");
                    model.drawable = obj.optString("drawable");
                    model.createTime = TimeUtil.getCurrTime();
                    model.updateTime = TimeUtil.getCurrTime();
                    model.type = type;
                    list.add(model);
                }
                return list;
            }
        } catch (Throwable t) {
            t.printStackTrace();

        }
        return null;
    }
}