package com.shui.jing.xiao.you.ui.db;

import com.activeandroid.query.Select;
import com.shui.jing.xiao.you.ui.model.ZhanBuModel;

import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class ZhanBuDBUtil {
    public List<ZhanBuModel> queryList(long time, int pageSize) {
        try {
            //4-20 6:30
            // 4-20 11:59
            // 4-20 12:80
            // 4-20 13:01
            return new com.activeandroid.query.Select().from(ZhanBuModel.class).where("createTime < " + time).orderBy("createTime desc").limit(pageSize).execute();

        } catch (Throwable t) {

        }
        return null;
    }

    public int querySize() {
        try {
            return new Select().from(ZhanBuModel.class).execute().size();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0;
    }
}
