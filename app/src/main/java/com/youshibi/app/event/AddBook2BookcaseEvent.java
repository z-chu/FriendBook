package com.youshibi.app.event;

import com.youshibi.app.data.db.table.BookTb;

/**
 * Created by Chu on 2017/7/18.
 * 添加一本书到书架
 */

public class AddBook2BookcaseEvent {
    public AddBook2BookcaseEvent(BookTb bookTb) {
        this.bookTb = bookTb;
    }

    public BookTb bookTb;
}
