package com.youshibi.app.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by Chu on 2016/12/4.
 */

public class ExpandTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean isSetMaxLines = false;

    public ExpandTextView(Context context) {
        super(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!isSetMaxLines) {
            setMaxLines(getMeasuredHeight() / getLineHeight());
            isSetMaxLines = true;
        }
        super.onDraw(canvas);
    }
}
