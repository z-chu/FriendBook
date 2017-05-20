package com.youshibi.app.ui.help;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.bumptech.glide.Glide;

/**
 * Created by Chu on 2017/5/19.
 * gilde 专用，列表滑动是不加载
 */

public class GlideScrollPauseHelper {

    public static void with(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Glide.with(recyclerView.getContext()).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Glide.with(recyclerView.getContext()).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:

                        break;
                }
            }
        });
    }

    public static void with(AbsListView listView){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_FLING://用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动
                        Glide.with(view.getContext()).pauseRequests();
                        break;
                    case SCROLL_STATE_IDLE://视图已经停止滑动
                    case SCROLL_STATE_TOUCH_SCROLL://手指没有离开屏幕，视图正在滑动
                        Glide.with(view.getContext()).resumeRequests();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });

    }

}
