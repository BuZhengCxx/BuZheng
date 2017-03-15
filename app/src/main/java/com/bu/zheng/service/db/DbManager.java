package com.bu.zheng.service.db;

/**
 * Created by BuZheng on 2017/3/15.
 */

public class DbManager {

    private static DbManager mInstance;

    public static DbManager getInstance() {
        if (mInstance == null) {
            mInstance = new DbManager();
        }
        return mInstance;
    }

    private DbManager() {

    }
}
