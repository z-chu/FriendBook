package com.zchu.reader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by newbiechen on 17-5-17.
 * 阅读器的配置管理
 */

public class ReadSettingManager {
    /*************实在想不出什么好记的命名方式。。******************/
    public static final int READ_BG_DEFAULT = 0;
    public static final int READ_BG_1 = 1;
    public static final int READ_BG_2 = 2;
    public static final int READ_BG_3 = 3;
    public static final int READ_BG_4 = 4;
    public static final int NIGHT_MODE = 5;

    public static final String SHARED_READ_BG = "shared_read_bg";
    public static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    public static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";
    public static final String SHARED_READ_TEXT_SIZE = "shared_read_text_size";
    public static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default";
    public static final String SHARED_READ_PAGE_MODE = "shared_read_mode";
    public static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";
    public static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    public static final String SHARED_READ_FULL_SCREEN = "shared_read_full_screen";

    private static volatile ReadSettingManager sInstance;
    private static Context sContext;

    private SharedPreferences mPreferences;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static ReadSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReadSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReadSettingManager();
                }
            }
        }
        return sInstance;
    }

    private ReadSettingManager() {
        mPreferences = sContext.getSharedPreferences("read-setting", Context.MODE_PRIVATE);
    }

    public void setReadBackground(int theme) {
        mPreferences
                .edit()
                .putInt(SHARED_READ_BG, theme)
                .apply();
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

    public int getTextSize() {
        return mPreferences.getInt(SHARED_READ_TEXT_SIZE, 40);
    }

    public boolean isDefaultTextSize() {
        return mPreferences.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false);
    }

    public int getPageMode() {
        return mPreferences.getInt(SHARED_READ_PAGE_MODE, PageView.PAGE_MODE_SIMULATION);
    }

    public int getReadBgTheme() {
        return mPreferences.getInt(SHARED_READ_BG, READ_BG_DEFAULT);
    }

    public boolean isNightMode() {
        return mPreferences.getBoolean(SHARED_READ_NIGHT_MODE, false);
    }

    public boolean isVolumeTurnPage() {
        return mPreferences.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false);
    }


    public boolean isFullScreen() {
        return mPreferences.getBoolean(SHARED_READ_FULL_SCREEN, false);
    }
}
