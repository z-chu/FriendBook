package com.youshibi.app.presentation.book;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.mvp.MvpPresenter;

/**
 * Created by Chu on 2017/5/28.
 */

public interface BookDetailContract {

    interface View extends BaseLceView {

        void setListAdapter(BaseQuickAdapter adapter);

        void showRead(String bookId, int sectionIndex);

    }

    interface Presenter extends MvpPresenter<View> {
        void loadData();
    }
}
