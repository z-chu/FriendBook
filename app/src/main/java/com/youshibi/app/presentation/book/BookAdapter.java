package com.youshibi.app.presentation.book;

import android.widget.ImageView;

import com.youshibi.app.R;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.util.GlideApp;

import java.util.List;

/**
 * Created by Chu on 2017/5/19.
 */

public class BookAdapter extends CommonAdapter<Book> {

    public BookAdapter(List<Book> data) {
        super(R.layout.list_item_book, data);
    }


    @Override
    protected void convert(CommonViewHolder holder, Book bookItem) {
        ImageView ivCover = holder.getView(R.id.iv_cover);
        GlideApp
                .with(mContext)
                .load(bookItem.getCoverUrl())
                .placeholder(R.drawable.ic_book_cover_default)
                .into(ivCover);
        holder
                .setText(R.id.tv_author, bookItem.getAuthor())
                .setText(R.id.tv_describe, bookItem.getDescribe())
                .setText(R.id.tv_title, bookItem.getName());
    }
}
