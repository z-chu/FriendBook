package com.youshibi.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zchu.log.Logger;


/**
 * 作者: 赵成柱 on 2016/7/14.
 * 应用程序异常类：用于捕获异常和收集崩溃信息
 *
 */
public class AppException implements Thread.UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException() {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }


    /**
     * 获取APP异常崩溃处理对象
     */
    public static AppException getAppExceptionHandler() {
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Logger.e(ex);
        if (!handleException(ex) && mDefaultHandler != null) {//如果没有处理异常则交给系统处理

            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义异常处收集错误信息&发送错误报
     *
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        final Context context = AppManager.getInstance().currentActivity();
        if (context == null) {//如果所有activity已经关闭
            return false;
        }

        final String crashReport = getCrashReport(context, ex);

        new Thread() {

            public void run() {
                // TODO: 2016/7/14 处理崩溃信息



                AppManager.getInstance().exit();
            }

        }.start();
        return true;
    }

    /**
     * 获取APP崩溃异常报告
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {

        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            exceptionStr.append("the application not found \n");
        }
        exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
        exceptionStr.append("Exception: "+ex.getMessage()+"\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString()+"\n");
        }
        return exceptionStr.toString();
    }

}
