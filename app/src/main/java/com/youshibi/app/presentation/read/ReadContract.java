package com.youshibi.app.presentation.read;

import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.mvp.MvpPresenter;

/**
 * Created by Chu on 2017/5/28.
 */

public interface ReadContract {

    interface View extends BaseLceView {

        void setData(BookSectionContent data);

    }

    interface Presenter extends MvpPresenter<View> {
        void loadData();
    }
}
