package com.youshibi.app.presentation.book;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author : zchu
 * date   : 2017/9/18
 * desc   :
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({ChannelType.BOOKS, ChannelType.BOOK_RANKING})
public @interface ChannelType {
    String BOOKS = "books";
    String BOOK_RANKING = "book-ranking";
}
