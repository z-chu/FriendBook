package com.youshibi.app.presentation.read;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.youshibi.app.R;
import com.youshibi.app.ui.widget.ShapeView;
import com.zchu.reader.PageView;

/**
 * Created by Chu on 2017/8/20.
 */

public class ReaderSettingDialog extends BottomSheetDialog implements View.OnClickListener {

    private PageView mTargetPageView;

    private ImageView readIvLightnessMinus;
    private SeekBar readSbLightnessProgress;
    private ImageView readIvLightnessPlus;
    private TextView readTvLightnessSystem;
    private TextView readTvFontSizeMinus;
    private TextView readTvFontSize;
    private TextView readTvFontSizePlus;
    private TextView readTvFontSizeDefault;
    private TextView readTvFontSetting;
    private TextView readTvFlipOverCover;
    private TextView readTvFlipOverSimulation;
    private TextView readTvFlipOverSlide;
    private TextView readTvFlipOverNone;
    private ImageView readTvColorSetting;

    private ShapeView readThemeWhite;
    private ShapeView readThemeAmber;
    private ShapeView readThemeGreen;
    private ShapeView readThemeBrown;
    private ShapeView readThemeBlack;


    public ReaderSettingDialog(@NonNull Context context, @NonNull PageView pageView) {
        super(context, R.style.Read_Setting_Dialog);
        super.setContentView(R.layout.bottom_sheet_read_setting);
        this.mTargetPageView = pageView;
        initView();
        initListener();
        initDisplay();
    }

    private void initView() {
        readIvLightnessMinus = (ImageView) findViewById(R.id.read_iv_lightness_minus);
        readSbLightnessProgress = (SeekBar) findViewById(R.id.read_sb_lightness_progress);
        readIvLightnessPlus = (ImageView) findViewById(R.id.read_iv_lightness_plus);
        readTvLightnessSystem = (TextView) findViewById(R.id.read_tv_lightness_system);
        readTvFontSizeMinus = (TextView) findViewById(R.id.read_tv_font_size_minus);
        readTvFontSize = (TextView) findViewById(R.id.read_tv_font_size);
        readTvFontSizePlus = (TextView) findViewById(R.id.read_tv_font_size_plus);
        readTvFontSizeDefault = (TextView) findViewById(R.id.read_tv_font_size_default);
        readTvFontSetting = (TextView) findViewById(R.id.read_tv_font_setting);
        readTvFlipOverCover = (TextView) findViewById(R.id.read_tv_flip_over_cover);
        readTvFlipOverSimulation = (TextView) findViewById(R.id.read_tv_flip_over_simulation);
        readTvFlipOverSlide = (TextView) findViewById(R.id.read_tv_flip_over_slide);
        readTvFlipOverNone = (TextView) findViewById(R.id.read_tv_flip_over_none);
        readTvColorSetting = (ImageView) findViewById(R.id.read_tv_color_setting);

        readThemeWhite = (ShapeView) findViewById(R.id.read_theme_white);
        readThemeAmber = (ShapeView) findViewById(R.id.read_theme_amber);
        readThemeGreen = (ShapeView) findViewById(R.id.read_theme_green);
        readThemeBrown = (ShapeView) findViewById(R.id.read_theme_brown);
        readThemeBlack = (ShapeView) findViewById(R.id.read_theme_black);
    }

    private void initListener() {
        readIvLightnessMinus.setOnClickListener(this);
        readIvLightnessPlus.setOnClickListener(this);
        readTvLightnessSystem.setOnClickListener(this);
        readTvFontSizeMinus.setOnClickListener(this);
        readTvFontSizePlus.setOnClickListener(this);
        readTvFontSizeDefault.setOnClickListener(this);
        readTvFontSetting.setOnClickListener(this);
        readTvFlipOverCover.setOnClickListener(this);
        readTvFlipOverSimulation.setOnClickListener(this);
        readTvFlipOverSlide.setOnClickListener(this);
        readTvFlipOverNone.setOnClickListener(this);
        readTvColorSetting.setOnClickListener(this);

        readThemeWhite.setOnClickListener(this);
        readThemeAmber.setOnClickListener(this);
        readThemeGreen.setOnClickListener(this);
        readThemeBrown.setOnClickListener(this);
        readThemeBlack.setOnClickListener(this);
    }

    private void initDisplay() {
        readTvFontSize.setText(String.valueOf(mTargetPageView.getTextSize()));
        setPageMode(mTargetPageView.getPageMode());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_iv_lightness_minus://亮度减

                break;
            case R.id.read_iv_lightness_plus://亮度加

                break;
            case R.id.read_tv_lightness_system://亮度跟随系统
                readTvLightnessSystem.setSelected(true);
                break;
            case R.id.read_tv_font_size_minus://字体大小减
                setTextSize(mTargetPageView.getTextSize() - 1);
                break;
            case R.id.read_tv_font_size_plus://字体大小加
                setTextSize(mTargetPageView.getTextSize() + 1);
                break;
            case R.id.read_tv_font_size_default://字体大小 默认
                setTextSize(40);
                break;
            case R.id.read_tv_font_setting://字体设置

                break;
            case R.id.read_tv_flip_over_cover://翻页模式-仿真
                setPageMode(PageView.PAGE_MODE_COVER);
                break;
            case R.id.read_tv_flip_over_simulation://翻页模式-覆盖
                setPageMode(PageView.PAGE_MODE_SIMULATION);
                break;
            case R.id.read_tv_flip_over_slide://翻页模式-滑动
                setPageMode(PageView.PAGE_MODE_SLIDE);
                break;
            case R.id.read_tv_flip_over_none://翻页模式-无
                setPageMode(PageView.PAGE_MODE_NONE);
                break;
            case R.id.read_theme_white:
                mTargetPageView.setPageBackground(Color.parseColor("#F5F4F0"));
                mTargetPageView.setTextColor(Color.parseColor("#3D3D3D"));
                mTargetPageView.refreshPage();
                selectedThemeView(readThemeWhite);
                break;
            case R.id.read_theme_amber:
                mTargetPageView.setPageBackground(Color.parseColor("#C5B284"));
                mTargetPageView.setTextColor(Color.parseColor("#3A342B"));
                mTargetPageView.refreshPage();
                selectedThemeView(readThemeAmber);
                break;
            case R.id.read_theme_green:
                mTargetPageView.setPageBackground(Color.parseColor("#CCE8CF"));
                mTargetPageView.setTextColor(Color.parseColor("#333333"));
                mTargetPageView.refreshPage();
                selectedThemeView(readThemeGreen);
                break;
            case R.id.read_theme_brown:
                mTargetPageView.setPageBackground(Color.parseColor("#3A3131"));
                mTargetPageView.setTextColor(Color.parseColor("#95938F"));
                mTargetPageView.refreshPage();
                selectedThemeView(readThemeBrown);
                break;
            case R.id.read_theme_black:
                mTargetPageView.setPageBackground(Color.parseColor("#001C29"));
                mTargetPageView.setTextColor(Color.parseColor("#637079"));
                mTargetPageView.refreshPage();
                break;
            case R.id.read_tv_color_setting://更多主题设置

                break;
        }

    }


    private void selectedThemeView(@NonNull View selectedView) {
        selectedView.setSelected(true);
        View[] views = {
                readThemeWhite,
                readThemeAmber,
                readThemeGreen,
                readThemeBrown,
                readThemeBlack
        };
        for (View view : views) {
            if (view != selectedView && view.isSelected()) {
                view.setSelected(false);
            }
        }

    }

    private void setTextSize(int size) {
        mTargetPageView.setTextSize(size);
        readTvFontSize.setText(String.valueOf(size));
    }

    private void setPageMode(int pageMode) {
        View selectedView = null;
        switch (pageMode) {
            case PageView.PAGE_MODE_COVER:
                readTvFlipOverCover.setSelected(true);
                selectedView = readTvFlipOverCover;
                break;
            case PageView.PAGE_MODE_SIMULATION:
                readTvFlipOverSimulation.setSelected(true);
                selectedView = readTvFlipOverSimulation;
                break;
            case PageView.PAGE_MODE_SLIDE:
                readTvFlipOverSlide.setSelected(true);
                selectedView = readTvFlipOverSlide;
                break;
            case PageView.PAGE_MODE_NONE:
                readTvFlipOverNone.setSelected(true);
                selectedView = readTvFlipOverNone;
                break;
        }
        View[] views = {
                readTvFlipOverCover,
                readTvFlipOverSimulation,
                readTvFlipOverSlide,
                readTvFlipOverNone
        };
        for (View view : views) {
            if (view != selectedView && view.isSelected()) {
                view.setSelected(false);
            }
        }
        if (pageMode != mTargetPageView.getPageMode()) {
            mTargetPageView.setPageAnimMode(pageMode);
            ReaderSettingManager
                    .getInstance()
                    .setPageMode(pageMode);
        }

    }
}
