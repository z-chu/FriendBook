package com.youshibi.app.util;

import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import com.youshibi.app.AppContext;


/**
 * Created by z-chu on 2016/4/21.
 */
public class DisplayUtil {
    private DisplayUtil(){}

    /**
     * 获取导航栏高度，有些没有虚拟导航栏的手机也能获取到，建议先判断是否有虚拟按键
     */
    public static int getNavigationBarHeight() {
        int resourceId = AppContext.context().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ?
                AppContext.context().getResources().getDimensionPixelSize(resourceId) :
                0;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStateBarHeight(){
        int resourceId =AppContext.context().getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ?
               AppContext.context().getResources().getDimensionPixelSize(resourceId) :
                0;
    }

    /**
     * 判断手机是否含有虚拟按键  99%
     * @return
     */
    public static boolean hasVirtualNavigationBar(){
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        if (hasBackKey && hasHomeKey) {
            // 没有虚拟按键
         return false;
        } else {
            // 有虚拟按键：99%可能。
           return true;
        }
    }
}
