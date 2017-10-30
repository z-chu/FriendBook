package com.youshibi.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
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

    private Application mApplication;

    @SuppressLint("StaticFieldLeak")
    private static AppManager sInstance;

    private Stack<Activity> mActivityStack;

    private int mStageActivityCount = 0; //前台activity数

    private AppManager(Application application) {
        mActivityStack = new Stack<>();
        mApplication = application;
        application.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }


    public static AppManager init(Application application) {
        if (application == null) {
            throw new NullPointerException("You cannot start a init on a null Application");
        }
        if (sInstance == null) {
            sInstance = new AppManager(application);
        }
        return sInstance;
    }

    public static AppManager getInstance() {
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
        Activity activity = null;
        if (!mActivityStack.empty()) {
            activity = mActivityStack.lastElement();
        }
        return activity;
    }

    public Activity beforeActivity() {
        Activity activity = null;
        if (mActivityStack.size() > 1) {
            activity = mActivityStack.get(mActivityStack.size() - 2);
        }
        return activity;
    }

    public Activity beforeActivity(Activity activity) {
        Activity beforeActivity = null;
        int indexOf = mActivityStack.indexOf(activity);
        if (indexOf >= 1) {
            beforeActivity = mActivityStack.get(indexOf - 1);
        }
        return beforeActivity;
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

    public Object[] getActivityArray(){
        return  mActivityStack.toArray();
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
        Intent i = mApplication.getPackageManager()
                .getLaunchIntentForPackage(mApplication.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mApplication.startActivity(i);
        exit();
    }

    public void exit() {
        try {
            finishAllActivity();
            // 友盟统计，统计关闭
            MobclickAgent.onKillProcess(mApplication);
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
