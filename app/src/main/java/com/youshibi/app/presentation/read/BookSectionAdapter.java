package com.youshibi.app.presentation.read;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.youshibi.app.R;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/11/3
 * desc   :
 */

public class BookSectionAdapter extends CommonAdapter<BookChapter> {


    private int textColor = -1;

    public BookSectionAdapter(List<BookChapter> data) {
        super(R.layout.list_item_book_section, data);
    }

    @Override
    protected void convert(CommonViewHolder helper, BookChapter item) {
        TextView textView=helper.getView(R.id.tv_section_name);
        textView.setText(item.getChapterName());
        if(textColor==-1){
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.textPrimary));
        }else{
            textView.setTextColor(textColor);
        }
    }

    public void setTextColor(int color) {
        textColor=color;
        notifyDataSetChanged();
    }
}
