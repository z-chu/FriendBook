package com.youshibi.app.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

/**
 * Created by Chu on 2016/12/4.
 */

public class ExpandTextView extends android.widget.TextView {
    public ExpandTextView(Context context) {
        super(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMaxLines(getMeasuredHeight()/getLineHeight());//根据高度设置最大行数
    }


}
