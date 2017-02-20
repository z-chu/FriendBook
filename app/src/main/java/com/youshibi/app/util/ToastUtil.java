package com.youshibi.app.util;

import android.widget.Toast;

import com.youshibi.app.AppContext;


/**
 * Created by Chu on 2016/3/14.
 */
public class ToastUtil {
    private static Toast sToast = null;
    public static boolean isShow = true;


    public static void showToast( String msg) {
        if (!isShow) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(AppContext.context(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void showToast(int resId) {
        if (!isShow) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(AppContext.context(), resId, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(resId);
        }
        sToast.show();
    }

    public static void showToastLong( String msg) {
        if (!isShow) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(AppContext.context(), msg, Toast.LENGTH_LONG);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void showToastLong(int resId) {
        if (!isShow) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(AppContext.context(), resId, Toast.LENGTH_LONG);
        } else {
            sToast.setText(resId);
        }
        sToast.show();
    }

    // 主要针对需要在某个时候，取消提示
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}
