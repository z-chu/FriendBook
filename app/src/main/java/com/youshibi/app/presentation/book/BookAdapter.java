package com.youshibi.app.presentation.book;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;

/**
 * Created by Chu on 2017/5/19.
 */

public class BookAdapter extends CommonAdapter<Book> {
    private Drawable placeDrawable;

    public BookAdapter(List<Book> data) {
        super(R.layout.list_item_book,data);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonViewHolder commonViewHolder = super.onCreateViewHolder(parent, viewType);
        placeDrawable=new ColorDrawable(ContextCompat.getColor(mContext,R.color.colorPictureDefault));
        return commonViewHolder;
    }

    @Override
    protected void convert(CommonViewHolder holder, Book bookItem) {
        ImageView ivCover = holder.getView(R.id.iv_cover);
        Glide
                .with(mContext)
                .load(bookItem.getCoverUrl())
                .apply(RequestOptions.placeholderOf(placeDrawable))
                .into(ivCover);
        holder
                .setText(R.id.tv_author, bookItem.getAuthor())
                .setText(R.id.tv_describe, bookItem.getDescribe())
                .setText(R.id.tv_title, bookItem.getTitle());
    }
}
