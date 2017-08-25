package com.zchu.labelselection;


import android.support.v7.widget.RecyclerView;

/**
 * Created by zchu on 2017/1/5 0005.
 */

public interface OnItemDragListener extends OnItemMoveListener {
    void onStarDrag(RecyclerView.ViewHolder viewHolder);

}
