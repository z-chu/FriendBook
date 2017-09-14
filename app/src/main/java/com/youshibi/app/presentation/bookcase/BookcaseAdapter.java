package com.youshibi.app.presentation.bookcase;

import android.widget.ImageView;

import com.youshibi.app.R;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.util.GlideApp;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/9/14
 * desc   :
 */

public class BookcaseAdapter extends CommonAdapter<BookTb> {

    private boolean isEditing = false;

    public BookcaseAdapter(List<BookTb> data) {
        super(R.layout.grid_item_bookcase_book, data);
    }

    @Override
    protected void convert(CommonViewHolder helper, BookTb item) {
        GlideApp
                .with(mContext)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.ic_book_cover_default)
                .into((ImageView) helper.getView(R.id.iv_cover));
        helper.setText(R.id.tv_title, item.getName());
        helper.addOnLongClickListener(R.id.iv_cover);
        helper.setGone(R.id.iv_selected, isEditing);
    }

    public boolean cancelEdit() {
        if (isEditing) {
            changeEditState(false);
            return true;
        }
        return false;
    }

    public boolean startEdit() {
        if (!isEditing) {
            changeEditState(true);
            return true;
        }
        return false;
    }

    /**
     * 开启编辑模式
     */
    private void changeEditState(boolean state) {
        isEditing = state;
        notifyDataSetChanged();
    }
}
