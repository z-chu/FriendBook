package com.youshibi.app.presentation.book;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.mvp.MvpPresenter;
import com.youshibi.app.mvp.MvpView;

/**
 * Created by Chu on 2017/5/28.
 */

public interface BookDetailContract {

    interface View extends MvpView {

        void setListAdapter(BaseQuickAdapter adapter);

    }

    interface Presenter extends MvpPresenter<View> {


    }
}
