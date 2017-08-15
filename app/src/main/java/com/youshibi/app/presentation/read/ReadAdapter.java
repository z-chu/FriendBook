package com.youshibi.app.presentation.read;

import android.util.SparseArray;

import com.youshibi.app.data.bean.BookSectionContent;
import com.zchu.reader.StringAdapter;

/**
 * Created by Chu on 2017/8/15.
 */

public class ReadAdapter extends StringAdapter {

    private SparseArray<BookSectionContent> bookArray;

    public ReadAdapter() {
        bookArray = new SparseArray<>();
    }

    @Override
    protected String getPageSource(int section) {
        return bookArray.get(section).getContent();
    }

    @Override
    public int getSectionCount() {
        return bookArray.size();
    }

    @Override
    public String getSectionName(int section) {
        return bookArray.get(section).getSectionName();
    }

    public void addData(int section, BookSectionContent content) {
        bookArray.put(section, content);
    }

    public boolean hasSection(int section){
        return bookArray.get(section)!=null;
    }

}
