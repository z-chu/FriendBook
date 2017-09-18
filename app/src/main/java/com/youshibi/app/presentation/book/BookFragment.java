package com.youshibi.app.presentation.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.help.RecyclerViewItemDecoration;
import com.youshibi.app.ui.widget.LoadErrorView;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookFragment extends BaseListFragment<BaseListContract.Presenter> {

    private static final String BUNDLE_CHANNEL_TYPE = "channel_type";
    private static final String BUNDLE_CHANNEL_ID = "channel_id";


    public static BookFragment newInstance(@ChannelType String channelType, long channelId) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_CHANNEL_TYPE, channelType);
        args.putLong(BUNDLE_CHANNEL_ID, channelId);
        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadErrorView.setErrorViewCreatedListener(new LoadErrorView.OnViewCreatedListener() {
            @Override
            public void onViewCreated(@NonNull View view) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.topMargin = -getResources().getDimensionPixelSize(R.dimen.bottom_navigation_height);
                view.requestLayout();
            }
        });
    }


    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        super.setRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(getActivity())
                .color(ContextCompat.getColor(getActivity(), R.color.colorDivider))
                .thickness(1)
                .create());
    }

    @NonNull
    @Override
    public BaseListContract.Presenter createPresenter() {
        Bundle arguments = getArguments();
        long channelId = arguments.getLong(BUNDLE_CHANNEL_ID);
        String channelType = arguments.getString(BUNDLE_CHANNEL_TYPE, ChannelType.BOOKS);
        switch (channelType) {
            case ChannelType.BOOK_RANKING:
                return new BookRankingPresenter(channelId);
            case ChannelType.BOOKS:
            default:
                return new BookPresenter(channelId);

        }

    }


}
