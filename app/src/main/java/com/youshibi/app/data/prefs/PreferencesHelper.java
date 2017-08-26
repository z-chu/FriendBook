package com.youshibi.app.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youshibi.app.AppContext;
import com.youshibi.app.data.bean.BookType;
import com.zchu.log.Logger;

import java.util.List;

/**
 * Created by Chu on 2017/8/26.
 */

public class PreferencesHelper {
    private static final String K_SELECTED_ARTICLE_LABELS = "selected_book_labels";

    private static PreferencesHelper sPreferencesHelper;
    private SharedPreferences mPreferences;
    private Gson mGson;

    public PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences("app-prefs", Context.MODE_PRIVATE);
        mGson = new Gson();

    }
    public static PreferencesHelper getInstance() {
        if (sPreferencesHelper == null) {
            sPreferencesHelper = new PreferencesHelper(AppContext.context());
        }
        return sPreferencesHelper;
    }


    public void setSelectedBookLabels(List<BookType> labels) {
        if (labels != null && labels.size() > 0) {
            mPreferences
                    .edit()
                    .putString(K_SELECTED_ARTICLE_LABELS, mGson.toJson(labels))
                    .apply();
        } else {
            mPreferences
                    .edit()
                    .remove(K_SELECTED_ARTICLE_LABELS)
                    .apply();
        }
    }
    public List<BookType> getSelectedBookLabels() {
        String string = mPreferences.getString(K_SELECTED_ARTICLE_LABELS, null);
        if (string != null) {
            try {
                return mGson.fromJson(string, new TypeToken<List<BookType>>() {
                }.getType());
            } catch (Exception e) {
                Logger.e(e);
            }
        }
        return null;
    }

}
