package com.bu.zheng.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenxiaoxiong on 2017/3/15.
 */

public class BuZhengSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "buzheng.db";
    private static final int DB_VERSION = 1;

    public BuZhengSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        doCreateTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void doCreateTables(SQLiteDatabase db){

    }
}
