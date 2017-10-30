package com.youshibi.app.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youshibi.app.R;

/**
 * author : zchu
 * date   : 2017/8/23
 * desc   :
 */

public class MineRowView extends RelativeLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private View divider;
    private View arrow;

    public MineRowView(Context context) {
        this(context, null);
    }

    public MineRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttributeSet(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_mine_row, this, true);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        divider = findViewById(R.id.divider);
        arrow=findViewById(R.id.arrow);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineRowView);
        int iconResId = typedArray.getResourceId(R.styleable.MineRowView_mine_icon, 0);
        if (iconResId != 0) {
            Drawable icon = VectorDrawableCompat.create(typedArray.getResources(),iconResId, null);
            ivIcon.setImageDrawable(icon);
        }

        String title = typedArray.getString(R.styleable.MineRowView_mine_title);
        tvTitle.setText(title);
        boolean dividerVisibility = typedArray.getBoolean(R.styleable.MineRowView_mine_divider_visibility, true);
        divider.setVisibility(dividerVisibility ? View.VISIBLE : View.GONE);
        boolean arrowVisibility = typedArray.getBoolean(R.styleable.MineRowView_mine_arrow_visibility, true);
        arrow.setVisibility(arrowVisibility ? View.VISIBLE : View.GONE);
        typedArray.recycle();


    }
}
