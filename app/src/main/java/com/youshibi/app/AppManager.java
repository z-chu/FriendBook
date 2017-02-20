package com.youshibi.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

/**
 * 作者: 赵成柱 on 2016/7/14.
 * app管理器，管理所有activity的生命周期
 */
public class AppManager implements Application.ActivityLifecycleCallbacks {

    public static final boolean isShowLog = true;

    public static final String TAG = "AppManager";

    public static final AppManager INSTANCE = new AppManager();

    private static Stack<Activity> mActivityStack;

    private int mStageActivityCount = 0; //前台activity数

    private Activity mStageActivity;

    private AppManager() {
        mActivityStack = new Stack<>();
    }

    /**
     * 获取指定的Activity
     */
    public static Activity getActivity(Class<?> cls) {
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
    private void addActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    private boolean removeActivity(Activity activity) {
        if (activity != null) {
            return mActivityStack.remove(activity);
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                //finishActivity方法中的activity.isFinishing()方法会导致某些activity无法销毁
                //貌似跳转的时候最后一个activity 是finishing状态，所以没有执行
                //内部实现不是很清楚，但是实测结果如此，使用下面代码则没有问题
                // find by TopJohn
                //finishActivity(activityStack.get(i));

                mActivityStack.get(i).finish();
                //break;
            }
        }
        mActivityStack.clear();
    }

    /**
     * 判断应用是否处于前台
     *
     * @return
     */
    public boolean isStagApp() {
        return mStageActivityCount > 0;
    }

    /**
     * 获取当前获得焦点的activity
     *
     * @return
     */
    public Activity getStageActivity() {
        return mStageActivity;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (isShowLog) {
            Log.i(TAG, "onActivityCreated :" + activity);
        }
        AppManager.INSTANCE.addActivity(activity);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (isShowLog) {
            Log.i(TAG, "onActivityStarted :" + activity);
        }
        mStageActivityCount++;
        mStageActivity = activity;
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
        if (mStageActivity == activity) {
            mStageActivity = null;
        }
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
        AppManager.INSTANCE.removeActivity(activity);

    }


    /**
     * 重启应用程序
     */
    public void resetApp() {
        Intent i = AppContext.context().getPackageManager()
                .getLaunchIntentForPackage(AppContext.context().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppContext.context().startActivity(i);
        AppExit();
    }

    public void AppExit() {
        try {
            finishAllActivity();
            //  TODO 友盟统计，统计异常关闭
            //MobclickAgent.onKillProcess(AppContext.context());
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }


}
