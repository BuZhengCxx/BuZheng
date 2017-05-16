package com.bu.zheng.log;

/**
 * Created by BuZheng on 2017/5/14.
 */

public class TransTypeToString {

    public String getTransactionName(int transationType) {
        switch (transationType) {
            case 1:
                return "登录";

            case 2:
                return "获取话题详情";

            default:
                return null;
        }
    }
}
