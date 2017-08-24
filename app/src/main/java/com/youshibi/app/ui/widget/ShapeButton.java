package com.youshibi.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Chu on 2017/8/24.
 */

public class ShapeButton extends android.support.v7.widget.AppCompatButton {
    public ShapeButton(Context context) {
        super(context);
    }

    public ShapeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSuperShapeView(attrs);
    }

    public ShapeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSuperShapeView(attrs);
    }

    private void initSuperShapeView(AttributeSet attrs) {
        new SuperConfig().beSuperView(attrs, this);
    }
}
