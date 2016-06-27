package com.run.running.app;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.run.runlibrary.BaseApplication;
import com.run.running.constants.Constants;

/**
 * Created by zsr on 2016/5/16.
 */
public class MyAppLication extends BaseApplication {

    @Override
    public void onCreate() {
        if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {//sdk>=9
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());//严苛模式，替代程序奔溃
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    /**
     * 全局初始化设置
     *
     * @param context
     */
    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);//线程池内加载的数量
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//将保存的时候的URI用MD5进行加密
        config.diskCacheSize(50 * 1024 * 1024);//50MB
        config.memoryCacheExtraOptions(480, 800);//保存的缓存文件的最大长宽
        config.discCacheFileCount(100);//缓存文件的数量
//        config.discCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路径
        config.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000));//connectTimeout (5 s), readTimeout (30 s)超时时间
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();//Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
