package com.youshibi.app.data.db;

import android.content.Context;

import com.youshibi.app.data.db.table.DaoMaster;
import com.youshibi.app.data.db.table.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

/**
 * Created by Chu on 2017/5/29.
 */

public class DBRepository {
    public static final String DB_NAME = "data-db";//数据库名称

    private static DaoSession mDaoSession;

    /**
     * 初始化greenDao，这个操作建议在Application初始化的时候添加；
     */
    public static void initDatabase(Context context) {
        if (mDaoSession != null) {
            return;
        }
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        DatabaseOpenHelper mHelper;
        mHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DB_NAME);
       /* if (BuildConfig.DEBUG) {
            mHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DB_NAME);
        } else {
            mHelper = new MySQLiteOpenHelper(context.getApplicationContext(), DB_NAME);
        }*/
        Database db = mHelper.getWritableDb();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
