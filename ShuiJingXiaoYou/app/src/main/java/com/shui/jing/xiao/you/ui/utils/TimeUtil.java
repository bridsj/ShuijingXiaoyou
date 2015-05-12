package com.shui.jing.xiao.you.ui.utils;


import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class TimeUtil {
    public static long getCurrTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.getTimeInMillis();
    }

    public static String convert2String(long time) {
        return convert2String(time, "");
    }

    public static String convert2String(long time, String format) {
        if (time > 0l) {
            if (TextUtils.isEmpty(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sf.format(date);
        }
        return "";
    }
}
