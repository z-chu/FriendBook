package com.youshibi.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.data.db.DBRepository;
import com.youshibi.app.pref.AppConfig;
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //初始化全局异常捕获
        Thread.setDefaultUncaughtExceptionHandler(new AppException(this));
        //初始化内存泄漏检测器
        LeakCanary.install(this);
        //初始化日志打印器
        Logger.init("FriendBook");
        //初始化AppManager
        AppManager.init(this);
        DBRepository.initDatabase(this);
        initUmeng(this);
        initTheme();

    }

    private void initUmeng(Context context) {
        MobclickAgent.
                startWithConfigure(
                        new MobclickAgent.UMAnalyticsConfig(context, C.UMENG_APP_KEY, C.UMENG_APP_CHANNEL)
                );
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void initTheme(){
        //设置该app的主题根据时间不同显示
        if(AppConfig.isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


    public static AppContext context() {
        return sContext;
    }


}
