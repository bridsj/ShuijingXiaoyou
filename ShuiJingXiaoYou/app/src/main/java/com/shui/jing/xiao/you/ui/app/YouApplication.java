package com.shui.jing.xiao.you.ui.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shui.jing.xiao.you.ui.helper.StorageHelper;

import java.io.File;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class YouApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        initImageLoader(getApplicationContext());
    }

    private static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();

        File cacheDir = new File(StorageHelper.getImgDir(context));
        ImageLoaderConfiguration config;

        config = new ImageLoaderConfiguration.Builder(context)//
                .threadPoolSize(3)//
                .threadPriority(Thread.NORM_PRIORITY - 1)//
                .defaultDisplayImageOptions(options)//
                .denyCacheImageMultipleSizesInMemory()//
                        // .memoryCache(new LruMemoryCache())//
                .discCache(new UnlimitedDiscCache(cacheDir))//
                .tasksProcessingOrder(QueueProcessingType.LIFO)//
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())//
                .threadPoolSize(3).build();//
        ImageLoader.getInstance().init(config);
    }
}
