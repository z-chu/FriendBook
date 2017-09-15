package com.youshibi.app.ui.help;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/9/15
 * desc   :
 */

public abstract class CommonItemDraggableAdapter<T> extends BaseItemDraggableAdapter<T, CommonViewHolder> {

    public CommonItemDraggableAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public CommonItemDraggableAdapter(List<T> data) {
        super(data);
    }

    protected CommonViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return this.createBaseViewHolder(this.getItemView(layoutResId, parent));
    }

    protected CommonViewHolder createBaseViewHolder(View view) {
        return new CommonViewHolder(view);
    }
}
