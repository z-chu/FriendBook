package com.youshibi.app.ui.help;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by zchu on 16-12-1.
 *
 */

public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, CommonViewHolder> {


    public CommonAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public CommonAdapter(List<T> data) {
        super(data);
    }

    protected CommonViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return this.createBaseViewHolder(this.getItemView(layoutResId, parent));
    }

    protected CommonViewHolder createBaseViewHolder(View view) {
        return new CommonViewHolder(view);
    }
}
