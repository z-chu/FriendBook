package com.youshibi.app.presentation.bookcase;

import android.widget.ImageView;

import com.youshibi.app.R;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.ui.help.CommonItemDraggableAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.ui.widget.MaskableImageView;
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
    protected void convert(final CommonViewHolder helper, BookTb item) {
        GlideApp
                .with(mContext)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.ic_book_cover_default)
                .into((ImageView) helper.getView(R.id.iv_cover));
        helper.setText(R.id.tv_title, item.getName());
        helper.addOnLongClickListener(R.id.iv_cover);
        helper.setSelected(R.id.iv_selected, selectedBookTbs.contains(item));
        helper.setGone(R.id.iv_selected, isEditing);
        helper.setGone(R.id.tv_updated,item.getHasUpdate());
        ((MaskableImageView) helper.getView(R.id.iv_cover)).setEnabledMaskable(!isEditing);
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
        selectedBookTbs.clear();
        notifyDataSetChanged();
        if(mListener!=null){
            mListener.onSelectedItemsChange(selectedBookTbs);
        }
    }

    public void selectedItem(int position) {

        BookTb bookTb = mData.get(position);
        if (selectedBookTbs.contains(bookTb)) {
            selectedBookTbs.remove(bookTb);
        } else {
            selectedBookTbs.add(bookTb);
        }
        notifyItemChanged(position);
        if (mListener != null) {
            mListener.onSelectedItemsChange(selectedBookTbs);
        }
    }

    public void selectedAllItem() {
        selectedBookTbs = new ArrayList<>(mData);
        notifyDataSetChanged();
        if (mListener != null) {
            mListener.onSelectedItemsChange(selectedBookTbs);
        }
    }

    public void clearSelectedAllItem() {
        selectedBookTbs.clear();
        notifyDataSetChanged();
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
