package com.youshibi.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.youshibi.app.data.db.table.BookTbDao;
import com.youshibi.app.data.db.table.DaoMaster;


/**
 * Created by zchu on 17-2-24.
 */

 class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(
                db,
                BookTbDao.class
        );
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(
                db,
                BookTbDao.class
        );
    }
}
