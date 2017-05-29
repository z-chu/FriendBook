package com.youshibi.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.youshibi.app.data.db.DBRepository;
import com.zchu.log.Logger;


/**
 * 作者: zchu on 2016/11/16.
 */
public class AppContext extends Application {

    private static AppContext sContext = null;

    public static long sStartTime = System.currentTimeMillis();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化AppManager
        registerActivityLifecycleCallbacks(AppManager.INSTANCE);
        //初始化全局异常捕获
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        //初始化内存泄漏检测器
        LeakCanary.install(this);
        //初始化日志打印器
        Logger.init("FriendBook");
        DBRepository.initDatabase(this);

    }


    public static AppContext context() {
        return sContext;
    }


}
