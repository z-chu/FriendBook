package com.youshibi.app.presentation.bookcase;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.youshibi.app.R;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.ui.help.CommonItemDraggableAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.util.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zchu
 * date   : 2017/9/14
 * desc   :
 */

public class BookcaseAdapter extends CommonItemDraggableAdapter<BookTb> {

    private boolean isEditing = false;
    private ArrayList<BookTb> selectedBookTbs = new ArrayList<>();
    private OnItemSelectedListener mListener;

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
        helper.setSelected(R.id.iv_cover, selectedBookTbs.contains(item));
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
        if(state){
            selectedBookTbs.clear();
        }
        RecyclerView recyclerView = getRecyclerView();
        int visibleChildCount = recyclerView.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            CommonViewHolder childViewHolder = (CommonViewHolder) getRecyclerView()
                    .getChildViewHolder(recyclerView.getChildAt(i));
            childViewHolder.setGone(R.id.iv_selected, isEditing);
        }
    }

    public void selectedItem(int position) {
        RecyclerView recyclerView = getRecyclerView();
        CommonViewHolder viewHolder = (CommonViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        BookTb bookTb = mData.get(position);
        if (selectedBookTbs.contains(bookTb)) {
            selectedBookTbs.remove(bookTb);
            viewHolder.setSelected(R.id.iv_selected, false);
        } else {
            selectedBookTbs.add(bookTb);
            viewHolder.setSelected(R.id.iv_selected, true);
        }
        if (mListener != null) {
            mListener.onSelectedItemsChange(selectedBookTbs);
        }
    }

    public boolean isEditing() {
        return isEditing;
    }

    public interface OnItemSelectedListener {
        void onSelectedItemsChange(ArrayList<BookTb> items);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mListener = listener;
        mListener.onSelectedItemsChange(selectedBookTbs);
    }

    public ArrayList<BookTb> getSelectedBookTbs() {
        return selectedBookTbs;
    }
}
