package com.youshibi.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zchu.log.Logger;

import java.util.Iterator;
import java.util.Stack;

/**
 * 作者: 赵成柱 on 2016/7/14.
 * app管理器，管理所有activity的生命周期
 */
public class AppManager {

    private static final boolean isShowLog = BuildConfig.DEBUG;

    private static final String TAG = "AppManager";

    private static Application sApplication;

    private static volatile AppManager sInstance;

    private Stack<Activity> mActivityStack;

    private int mStageActivityCount = 0; //前台activity数

    private AppManager() {
        mActivityStack = new Stack<>();
        if (sApplication == null) {
            throw new NullPointerException("AppManager is not initialized ,please call the init method first");
        }
        sApplication.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }


    public static void init(Application application) {
        if (application == null) {
            throw new NullPointerException("You cannot start a init on a null Application");
        }
        if (sInstance == null) {
            sApplication = application;
        }
    }

    public static AppManager getInstance() {
        if (sInstance == null) {
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (mActivityStack != null)
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }


    /**
     * 添加Activity到堆栈
     */
    private boolean addActivity(Activity activity) {
        return activity != null && mActivityStack.add(activity);
    }

    private boolean removeActivity(Activity activity) {
        return activity != null && mActivityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity=null;
        if(!mActivityStack.empty()){
            activity=mActivityStack.lastElement();
        }
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (!mActivityStack.empty()) {
            finishActivity(mActivityStack.pop());
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的所有Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束指定非类名的所有Activity
     */
    public void finishOtherActivity(Class<?> cls) {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            iterator.remove();
            activity.finish();
        }
    }

    /**
     * 判断应用是否处于前台
     */
    public boolean isStagApp() {
        return mStageActivityCount > 0;
    }


    /**
     * 重启应用程序
     */
    public void resetApp() {
        Intent i = sApplication.getPackageManager()
                .getLaunchIntentForPackage(sApplication.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sApplication.startActivity(i);
        exit();
    }

    public void exit() {
        try {
            finishAllActivity();
            // 友盟统计，统计关闭
            //MobclickAgent.onKillProcess(sApplication);
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    private class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            if (isShowLog) {
                Log.i(TAG, "onActivityCreated :" + activity);
            }
            AppManager.sInstance.addActivity(activity);

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (isShowLog) {
                Log.i(TAG, "onActivityStarted :" + activity);
            }
            mStageActivityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (isShowLog) {
                Log.i(TAG, "onActivityResumed :" + activity);
            }

        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (isShowLog) {
                Log.i(TAG, "onActivityPaused :" + activity);
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (isShowLog) {
                Log.i(TAG, "onActivityStopped :" + activity);
            }
            mStageActivityCount--;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            if (isShowLog) {
                Log.i(TAG, "onActivitySaveInstanceState :" + activity);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (isShowLog) {
                Log.i(TAG, "onActivityDestroyed :" + activity);
            }
            AppManager.sInstance.removeActivity(activity);

        }
    }


}
