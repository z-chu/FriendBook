package com.youshibi.app.ui.widget;

import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.youshibi.app.R;

/**
 * Create Time:2017/7/7 15:49
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SuperConfig {
    private static final float DEFAULT_CORNER_RADIUS = 0f;
    private static final float DEFAULT_STROKE_WIDTH = 0f;
    private static final int DEFAULT_STROKE_COLOR = 0;
    private static final int DEFAULT_SOLID_COLOR = 0;

    private static final float DEFAULT_DASHWIDTH = 0f;
    private static final float DEFAULT_DASHGAP = 0f;

    private static final float DEFAULT_TOP_LEFT_RADIUS = 0f;
    private static final float DEFAULT_TOP_RIGHT_RADIUS = 0f;
    private static final float DEFAULT_BOTTOM_LEFT_RADIUS = 0f;
    private static final float DEFAULT_BOTTOM_IGHT_RADIUS = 0f;
    private static final String TAG = "SuperConfig";
    private GradientDrawable mGradientDrawable;

    public  void beSuperView(AttributeSet attrs, View view) {
        TypedArray typedArray = view.getContext().obtainStyledAttributes(attrs, R.styleable.SuperShapeView);
        float mCornerRadius = typedArray.getDimension(R.styleable.SuperShapeView_super_cornerRadius,
                DEFAULT_CORNER_RADIUS);
        int mStrokeColor = typedArray.getColor(R.styleable.SuperShapeView_super_strokeColor, DEFAULT_STROKE_COLOR);
        int mSolidColor = typedArray.getColor(R.styleable.SuperShapeView_super_solidColor, DEFAULT_SOLID_COLOR);
        int mStrokeWidth = (int) typedArray.getDimension(R.styleable.SuperShapeView_super_strokeWidth,
                DEFAULT_STROKE_WIDTH);

        float mDashWidth = typedArray.getDimension(R.styleable.SuperShapeView_super_dashWidth, DEFAULT_DASHWIDTH);
        float mDashGap = typedArray.getDimension(R.styleable.SuperShapeView_super_dashGap, DEFAULT_DASHGAP);

        float mTopLeftRadius = typedArray.getDimension(R.styleable.SuperShapeView_super_topLeftRadius,
                DEFAULT_TOP_LEFT_RADIUS);
        float mTopRightRadius = typedArray.getDimension(R.styleable.SuperShapeView_super_topRightRadius,
                DEFAULT_TOP_RIGHT_RADIUS);
        float mBottomLeftRadius = typedArray.getDimension(R.styleable.SuperShapeView_super_bottomLeftRadius,
                DEFAULT_BOTTOM_LEFT_RADIUS);
        float mBottomRightRadius = typedArray.getDimension(R.styleable.SuperShapeView_super_bottomRightRadius,
                DEFAULT_BOTTOM_IGHT_RADIUS);

        typedArray.recycle();

        setupGradientDrawable(view, mCornerRadius, mStrokeColor, mSolidColor, mStrokeWidth, mDashWidth, mDashGap,
                mTopLeftRadius, mTopRightRadius, mBottomLeftRadius, mBottomRightRadius);

    }

    private  void setupGradientDrawable(View view, float mCornerRadius, int mStrokeColor, int mSolidColor, int
            mStrokeWidth, float
            mDashWidth, float mDashGap, float mTopLeftRadius, float mTopRightRadius, float mBottomLeftRadius, float
                                                      mBottomRightRadius) {
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(mSolidColor);
        mGradientDrawable.setStroke(mStrokeWidth, mStrokeColor, mDashWidth, mDashGap);
        float[] radius = {mTopLeftRadius, mTopLeftRadius, mTopRightRadius, mTopRightRadius, mBottomRightRadius,
                mBottomRightRadius, mBottomLeftRadius, mBottomLeftRadius};

        if (mCornerRadius == DEFAULT_CORNER_RADIUS) {
            mGradientDrawable.setCornerRadii(radius);
        } else {
            mGradientDrawable.setCornerRadius(mCornerRadius);
        }
        view.setBackgroundDrawable(mGradientDrawable);
    }
}
