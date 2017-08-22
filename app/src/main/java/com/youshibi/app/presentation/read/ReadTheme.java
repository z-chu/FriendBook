package com.youshibi.app.presentation.read;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.youshibi.app.AppContext;
import com.youshibi.app.R;

/**
 * author : zchu
 * date   : 2017/8/22
 * desc   :
 */

public enum ReadTheme {

    WHITE(AppContext.context(), R.color.read_theme_white_page_background, R.color.read_theme_white_text),
    AMBER(AppContext.context(), R.color.read_theme_amber_page_background, R.color.read_theme_amber_text),
    GREEN(AppContext.context(), R.color.read_theme_green_page_background, R.color.read_theme_green_text),
    BROWN(AppContext.context(), R.color.read_theme_brown_page_background, R.color.read_theme_brown_text),
    BLACK(AppContext.context(), R.color.read_theme_black_page_background, R.color.read_theme_black_text),
    NIGHT(AppContext.context(), R.color.read_theme_night_page_background, R.color.read_theme_night_text),
    DEFAULT(AppContext.context(), R.color.read_theme_default_page_background, R.color.read_theme_default_text);


    ReadTheme(Context context, @ColorRes int pageBackgroundRes, @ColorRes int textColorRes) {
        this.pageBackground = ContextCompat.getColor(context, pageBackgroundRes);
        this.textColor = ContextCompat.getColor(context, textColorRes);
    }

    @Nullable
    public static ReadTheme getReadTheme(int pageBackground, int textColor) {
        for (ReadTheme readTheme : ReadTheme.values()) {
            if (readTheme.pageBackground == pageBackground && readTheme.textColor == textColor) {
                return readTheme;
            }
        }
        return null;
    }

    private int pageBackground;
    private int textColor;

    public int getPageBackground() {
        return pageBackground;
    }

    public void setPageBackground(int pageBackground) {
        this.pageBackground = pageBackground;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
