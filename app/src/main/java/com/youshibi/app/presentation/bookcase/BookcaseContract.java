package com.youshibi.app.presentation.bookcase;

import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.data.db.table.BookTb;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/9/14
 * desc   :
 */

public interface BookcaseContract {
    interface View extends BaseListContract.View {
        void showEditMode();

        void startDrag(int position);
    }

    interface Presenter extends BaseListContract.Presenter<View> {

        void deleteItems(List<BookTb> bookTbs);

        void dispatchSortSpinnerItemSelected(int position);

        int getDefaultSelectedSortSpinnerItem();

        void finishEdit();

    }
}
