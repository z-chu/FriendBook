package com.youshibi.app.util;


import android.annotation.TargetApi;
import android.content.SharedPreferences;


/**
 * Preferences帮助类
 * 作者：z-chu on 2016/4/14 15:07
 * 邮箱：zchu8073@gmail.com
 */
public class PreferencesHelper {
    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(SharedPreferences sp,String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        apply(editor);
    }
    public static void remove(SharedPreferences sp,String key1,String key2) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key1).remove(key2);
        apply(editor);
    }

    public static void remove(SharedPreferences sp,String key1,String key2,String key3) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key1).remove(key2).remove(key3);
        apply(editor);
    }

    public static void remove(SharedPreferences sp,String key1,String key2,String key3,String key4) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key1).remove(key2).remove(key3).remove(key4);
        apply(editor);
    }

    public static void remove(SharedPreferences sp,String... key) {
        SharedPreferences.Editor editor = sp.edit();
        for (String k : key) {
            editor.remove(k);
        }
        apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(SharedPreferences sp) {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        apply(editor);
    }

    @TargetApi(9)
    public static void apply(SharedPreferences.Editor var0) {
        if(null != var0) {
            var0.apply();
        }
    }

}