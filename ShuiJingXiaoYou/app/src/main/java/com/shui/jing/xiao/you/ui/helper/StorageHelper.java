package com.shui.jing.xiao.you.ui.helper;

import android.content.Context;

import com.shui.jing.xiao.you.ui.constant.Constants;
import com.shui.jing.xiao.you.ui.utils.LibIOUtil;

import java.io.File;

/**
 * @author deng.shengjin
 * @version create_time:2014-3-15_下午3:08:00
 * @Description 文件存储
 */
public class StorageHelper {

    // public static final boolean DEVELOPER_MODE = false;

    // 存储显示图片
    private static final String IMAGE_CACHE_DIR_NAME = File.separator + Constants.APP_BASE_DIR_NAME + File.separator + "images" + File.separator;


    public static String getImgDir(Context context) {
        String baseCacheLocation = LibIOUtil.getBaseLocalLocation(context);
        String images = baseCacheLocation + IMAGE_CACHE_DIR_NAME;
        return createFileDir(images);
    }

    private static String createFileDir(String path) {
        if (!LibIOUtil.isDirExist(path)) {
            boolean isMakeSucc = LibIOUtil.makeDirs(path);
            if (!isMakeSucc) {
                return path;
            }
        }
        return path;
    }

}