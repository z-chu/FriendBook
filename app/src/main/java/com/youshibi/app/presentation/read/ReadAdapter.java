package com.youshibi.app.presentation.read;

import android.util.SparseArray;

import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.data.bean.BookChapterContent;
import com.zchu.reader.StringAdapter;

import java.util.List;

/**
 * Created by Chu on 2017/8/15.
 */

public class ReadAdapter extends StringAdapter {

    private SparseArray<BookChapterContent> bookArray;
    private List<BookChapter> mBookSectionItems;

    public ReadAdapter() {
        bookArray = new SparseArray<>();
    }

    public ReadAdapter(List<BookChapter> bookSectionItems) {
        bookArray = new SparseArray<>();
        mBookSectionItems = bookSectionItems;

    }


    @Override
    protected String getPageSource(int section) {
        BookChapterContent sectionContent = bookArray.get(section);
        return sectionContent != null ? bookArray.get(section).getContent() : null;
    }

    @Override
    public int getSectionCount() {
        return bookArray.size();
    }

    @Override
    public String getSectionName(int section) {
        BookChapterContent sectionContent = bookArray.get(section);
        return sectionContent != null ? bookArray.get(section).getChapterName() : null;
    }

    @Override
    public boolean hasNextSection(int currentSection) {
        return bookArray.get(currentSection + 1) != null;
    }

    @Override
    public boolean hasPreviousSection(int currentSection) {
        return bookArray.get(currentSection - 1) != null;
    }

    public void addData(int section, BookChapterContent content) {
        bookArray.put(section, content);
    }

    public boolean hasSection(int section) {
        return bookArray.get(section) != null;
    }

}
