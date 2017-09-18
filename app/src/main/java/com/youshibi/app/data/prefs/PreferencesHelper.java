package com.youshibi.app.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youshibi.app.AppContext;
import com.youshibi.app.data.bean.Channel;
import com.youshibi.app.presentation.bookcase.BookcaseSort;
import com.zchu.log.Logger;

import java.util.List;

import static com.youshibi.app.presentation.bookcase.BookcaseSort.LATEST_READ_TIME;

/**
 * Created by Chu on 2017/8/26.
 */

public class PreferencesHelper {
    private static final String K_SELECTED_ARTICLE_LABELS = "selected_book_labels";
    private static final String K_FIRST_START_APP = "first_start_app";
    private static final String K_BOOKCASE_SORT = "bookcase_sort";


    private static PreferencesHelper sPreferencesHelper;
    private SharedPreferences mPreferences;
    private Gson mGson;

    private PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences("app-prefs", Context.MODE_PRIVATE);
        mGson = new Gson();

    }

    public static PreferencesHelper getInstance() {
        if (sPreferencesHelper == null) {
            sPreferencesHelper = new PreferencesHelper(AppContext.context());
        }
        return sPreferencesHelper;
    }


    public void setSelectedChannels(List<Channel> labels) {
        if (labels != null && labels.size() > 0) {
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i) == null) {
                    labels.remove(i);
                }
            }
            if (labels.size() > 0) {
                mPreferences
                        .edit()
                        .putString(K_SELECTED_ARTICLE_LABELS, mGson.toJson(labels))
                        .apply();
                return;
            }
        }
        mPreferences
                .edit()
                .remove(K_SELECTED_ARTICLE_LABELS)
                .apply();

    }

    public List<Channel> getSelectedChannels() {
        String string = mPreferences.getString(K_SELECTED_ARTICLE_LABELS, null);
        if (string != null) {
            try {
                return mGson.fromJson(string, new TypeToken<List<Channel>>() {
                }.getType());
            } catch (Exception e) {
                Logger.e(e);
            }
        }
        return null;
    }

    public boolean isFirstStartApp() {
        return mPreferences.getBoolean(K_FIRST_START_APP, true);
    }

    public void setFirstStartApp(boolean b) {
        mPreferences
                .edit()
                .putBoolean(K_FIRST_START_APP, b)
                .apply();
    }

    public void setBookcaseSort(@BookcaseSort int bookcaseSort) {
        mPreferences
                .edit()
                .putInt(K_BOOKCASE_SORT, bookcaseSort)
                .apply();
    }

    @BookcaseSort
    public int getBookcaseSort() {
        @BookcaseSort
        int bookcaseSort = mPreferences.getInt(K_BOOKCASE_SORT, LATEST_READ_TIME);
        return bookcaseSort;
    }
}
