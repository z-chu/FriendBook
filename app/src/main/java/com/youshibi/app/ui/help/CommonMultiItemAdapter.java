package com.youshibi.app.ui.help;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by zchu on 16-12-1.
 */

public abstract class CommonMultiItemAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T, CommonViewHolder> {

    public CommonMultiItemAdapter(List<T> data) {
        super(data);
    }
    protected CommonViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return this.createBaseViewHolder(this.getItemView(layoutResId, parent));
    }

    protected CommonViewHolder createBaseViewHolder(View view) {
        return new CommonViewHolder(view);
    }


}
