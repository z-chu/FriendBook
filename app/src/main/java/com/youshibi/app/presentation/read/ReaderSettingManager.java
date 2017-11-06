package com.youshibi.app.presentation.read;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.youshibi.app.AppContext;
import com.youshibi.app.R;
import com.youshibi.app.util.DensityUtil;
import com.zchu.reader.PageView;

/**
 * Created by Chu on 2017/8/19.
 */

public class ReaderSettingManager {

    private static final String SHARED_READ_BG = "shared_read_bg";
    private static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    private static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";
    private static final String SHARED_READ_TEXT_SIZE = "shared_read_text_size";
    private static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default";
    private static final String SHARED_READ_PAGE_MODE = "shared_read_mode";
    private static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";
    private static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    private static final String SHARED_READ_FULL_SCREEN = "shared_read_full_screen";
    private static final String SHARED_READ_PAGE_BACKGROUND = "shared_read_page_background";
    private static final String SHARED_READ_TEXT_COLOR = "shared_read_text_color";


    private static volatile ReaderSettingManager sInstance;
    private static Context sContext;

    private SharedPreferences mPreferences;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static ReaderSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReaderSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReaderSettingManager();
                }
            }
        }
        return sInstance;
    }

    private ReaderSettingManager() {
        mPreferences = sContext.getSharedPreferences("read-setting", Context.MODE_PRIVATE);
    }


    public void setBrightness(int progress) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_BRIGHTNESS, progress)
                .apply();
    }

    public void setAutoBrightness(boolean isAuto) {
        mPreferences
                .edit()
                .putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto)
                .apply();
    }

    public void setDefaultTextSize(boolean isDefault) {
        mPreferences
                .edit()
                .putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault)
                .apply();
    }

    public void setTextSize(int textSize) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_TEXT_SIZE, textSize)
                .apply();
    }

    public int getTextSize() {
        return mPreferences.getInt(SHARED_READ_TEXT_SIZE, DensityUtil.sp2px(AppContext.context(), 18));
    }


    public void setPageMode(int mode) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_PAGE_MODE, mode)
                .apply();
    }

    public void setNightMode(boolean isNight) {
        mPreferences
                .edit()
                .putBoolean(SHARED_READ_NIGHT_MODE, isNight)
                .apply();
    }

    public void setVolumeTurnPage(boolean isTurn) {
        mPreferences
                .edit()
                .putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn)
                .apply();
    }

    public void setFullScreen(boolean isFullScreen) {
        mPreferences
                .edit()
                .putBoolean(SHARED_READ_FULL_SCREEN, isFullScreen)
                .apply();
    }

    public int getBrightness() {
        return mPreferences
                .getInt(SHARED_READ_BRIGHTNESS, 40);
    }

    public boolean isBrightnessAuto() {
        return mPreferences.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, false);
    }


    public boolean isDefaultTextSize() {
        return mPreferences.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false);
    }

    public int getPageMode() {
        return mPreferences.getInt(SHARED_READ_PAGE_MODE, PageView.PAGE_MODE_COVER);
    }


    public boolean isVolumeTurnPage() {
        return mPreferences.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false);
    }


    public boolean isFullScreen() {
        return mPreferences.getBoolean(SHARED_READ_FULL_SCREEN, false);
    }

    public int getPageBackground() {
        return mPreferences
                .getInt(SHARED_READ_PAGE_BACKGROUND, ContextCompat.getColor(sContext, R.color.read_theme_default_page_background));
    }

    public void setPageBackground(int color) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_PAGE_BACKGROUND, color)
                .apply();
    }


    public int getTextColor() {
        return mPreferences.getInt(SHARED_READ_TEXT_COLOR, ContextCompat.getColor(sContext, R.color.read_theme_default_text));
    }

    public void setTextColor(int color) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_TEXT_COLOR, color)
                .apply();
    }
}
