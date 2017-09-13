package com.youshibi.app.presentation.book;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.util.GlideApp;

import java.text.DecimalFormat;
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
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_book_cover_default)
                .into(ivCover);
        holder
                .setText(R.id.tv_author, bookItem.getAuthor())
                .setText(R.id.tv_describe, bookItem.getDescribe())
                .setText(R.id.tv_is_finish,bookItem.isFinished()?
                        mContext.getString(R.string.book_finished):mContext.getString(R.string.book_unfinished))
                .setText(R.id.tv_word_count,formatDownloads(bookItem.getBookWordNum())+"字")
                .setText(R.id.tv_title, bookItem.getName());
    }
    public static String formatDownloads(long number) {
        if (number >= 100000000) {
            return new DecimalFormat("0.0").format(number / 100000000.0) + "亿";
        } else if (number >= 1000000) {
            return number / 10000 + "万";
        } else if (number >= 10000) {
            return new DecimalFormat("0.0").format(number / 10000.0) + "万";
        } else {
            return number + "";
        }
    }
}
