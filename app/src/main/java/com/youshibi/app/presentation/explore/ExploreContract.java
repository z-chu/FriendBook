package com.youshibi.app.presentation.explore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.mvp.MvpPresenter;

/**
 * Created by Chu on 2017/8/26.
 */

public interface ExploreContract {

    interface View extends BaseLceView {

        void setTabContent(@NonNull Fragment[] fragments, @NonNull String[] titles);

        void setSelectedTab(String title);

        String getSelectedTab();

    }

    interface Presenter extends MvpPresenter<View> {

        void loadData();

        /**
         * 打开BookType 编辑
         */
        void openBookTypeSelection(Context context);

    }
}
