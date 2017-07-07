package com.youshibi.app.presentation.search;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.mvp.MvpPresenter;

/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchContract {

    interface View extends BaseLceView {

        void setListAdapter(BaseQuickAdapter adapter);
    }

    interface Presenter extends MvpPresenter<View> {

        void search(String keyword);

    }
}
