package com.youshibi.app.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.youshibi.app.AppContext;
import com.youshibi.app.util.FileUtil;

import java.io.File;

/**
 * 作者: 赵成柱 on 2016/7/13.
 * app设置管理类
 */
public class AppConfig {

    public final static String APP_CONFIG = "appConfig";
    // 默认存放图片的路径
    public static final String IMAGE_CACHE = AppContext.context().getCacheDir().getPath() + File.separator + "image-cache";

    // 默认存放数据缓存的路径
    public static final String DATA_CACHE = AppContext.context().getCacheDir().getPath() + File.separator + "data-cache";

    //图片缓存的最大大小
    // public final static int IMAGE_CACHE_MAXSIZE = 20 * 1024 * 1024;

    // 默认存放WebView缓存的路径
    public final static String WEB_CACHE_PATH = AppContext.context().getCacheDir().getPath() + File.separator + "webCache";

    private final static String C_SOFT_INPUT_HEIGHT = "soft_input_height";

    private static SharedPreferences sPref;

    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache() {
        FileUtil.deleteFile(WEB_CACHE_PATH);
    }

    public static void setSoftInputHeight(int height) {
        getPreferences()
                .edit()
                .putInt(C_SOFT_INPUT_HEIGHT, height)
                .apply();
    }

    /**
     * 不存在返回-1
     */
    public static int getSoftInputHeight(){
        return getPreferences().getInt(C_SOFT_INPUT_HEIGHT,-1);

    }
    private static SharedPreferences getPreferences() {
        if (sPref == null) {
            sPref = AppContext.context().getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        }
        return sPref;
    }
}
