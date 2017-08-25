package com.youshibi.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.data.db.DBRepository;
import com.youshibi.app.pref.C;
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
        initUmeng(this);
        //初始化AppManager
        AppManager.init(this);
        //初始化全局异常捕获
        Thread.setDefaultUncaughtExceptionHandler(new AppException(this));
        //初始化内存泄漏检测器
        LeakCanary.install(this);
        //初始化日志打印器
        Logger.init("FriendBook");
        DBRepository.initDatabase(this);

    }

    private void initUmeng(Context context) {
        MobclickAgent.
                startWithConfigure(
                        new MobclickAgent.UMAnalyticsConfig(context, C.UMENG_APP_KEY, C.UMENG_APP_CHANNEL)
                );
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
    }


    public static AppContext context() {
        return sContext;
    }


}
