package com.youshibi.app.ui.help;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者: 赵成柱 on 2016/9/23 0023.
 *  监听RecyclerView滑动到底部
 */

public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        boolean triggerCondition = visibleItemCount > 0 && newState == 0 && this.canTriggerLoadMore(recyclerView);
        if (triggerCondition) {
            this.onLoadMore(recyclerView);
        }

    }

    private boolean canTriggerLoadMore(RecyclerView recyclerView) {
        View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int position = recyclerView.getChildLayoutPosition(lastChild);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount - 1 == position;
    }

    public abstract void onLoadMore(RecyclerView var1);
}
