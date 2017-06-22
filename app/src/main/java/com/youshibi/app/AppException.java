package com.youshibi.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zchu.log.Logger;


/**
 * 作者: 赵成柱 on 2016/7/14.
 * 应用程序异常类：用于捕获异常和收集崩溃信息
 */
public class AppException implements Thread.UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    public AppException(Context context) {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mContext = context;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Logger.e(ex, "AppException");
        if (!handleException(ex) && mDefaultHandler != null) {//如果没有处理异常则交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
           // MobclickAgent.onKillProcess(mContext);
            android.os.Process.killProcess(android.os.Process.myPid());// 关闭已奔溃的app进程
        }
    }

    /**
     * 自定义异常处收集错误信息&发送错误报
     *
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        //用友盟发送错误日志
        AppRouter.showCrashActivity(mContext, ex.getMessage(), getCrashReport(mContext,ex));
        return true;
    }

    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private static String getCrashReport(Context context, Throwable ex) {

        StringBuilder exceptionStr = new StringBuilder();
        PackageInfo pinfo;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            exceptionStr
                    .append("Version: ")
                    .append(pinfo.versionName)
                    .append("\nVersionCode: ")
                    .append(pinfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            exceptionStr.append("\nthe application not found \n");
        }
        exceptionStr
                .append("\nAndroid: ")
                .append(android.os.Build.VERSION.RELEASE)
                .append("(")
                .append(android.os.Build.MODEL)
                .append(")\n");
        String stackTraceString = Log.getStackTraceString(ex);
        if (stackTraceString != null && stackTraceString.length() > 0) {
            exceptionStr
                    .append(stackTraceString);
        } else {
            exceptionStr
                    .append("\nException: ")
                    .append(ex.getMessage())
                    .append("\n");
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                exceptionStr
                        .append(element.toString());
            }
        }


        return exceptionStr.toString();
    }

}
