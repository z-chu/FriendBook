package com.youshibi.app.presentation.bookcase;

import com.youshibi.app.base.BaseListContract;

/**
 * author : zchu
 * date   : 2017/9/14
 * desc   :
 */

public interface BookcaseContract {
    interface View extends BaseListContract.View {
        void showEditMode();
    }

    interface Presenter extends BaseListContract.Presenter<View> {

    }
}
