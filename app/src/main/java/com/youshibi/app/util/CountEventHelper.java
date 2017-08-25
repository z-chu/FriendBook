package com.youshibi.app.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.data.bean.Book;

import java.util.HashMap;

/**
 * author : zchu
 * date   : 2017/8/25
 * desc   :
 */

public class CountEventHelper {

    /**
     * 统计书籍搜索
     */
    public static void countBookSearch(Context context, String keyword) {
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        MobclickAgent.onEvent(context, "book_search", map);
    }

    /**
     * 统计书籍详情打开
     */
    public static void countBookDetailDispay(Context context, Book book) {
        HashMap<String, String> map = new HashMap<>();
        map.put("bookId", book.getId());
        map.put("bookName", book.getName());
        map.put("bookAuthor", book.getAuthor());
        map.put("bookTypeName", book.getBookOneTypeName());
        map.put("bookIsFinished", String.valueOf(book.isFinished()));
        map.put("bookWordNum", String.valueOf(book.getBookWordNum()));
        MobclickAgent.onEvent(context, "book_detail_dispay", map);
    }

    /**
     * 统计书籍阅读
     */
    public static void countBookRead(Context context, String bookId, String bookName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("bookName", bookName);
        MobclickAgent.onEvent(context, "book_read", map);
    }

    /**
     * 统计发现tab选择
     */
    public static void countExploreTab(Context context, String tabName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("tabName", tabName);
        MobclickAgent.onEvent(context, "explore_Tab", map);
    }

}
