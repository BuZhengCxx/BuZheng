package com.bu.zheng.log;

/**
 * Created by BuZheng on 2017/5/14.
 */

public class TransResultCodeToString {

    public static String getTransactionResultString(int errorCode) {
        switch (errorCode) {
            case 1:
                return "网络异常";

            case 2:
                return "话题被删除";

            default:
                return "未知异常";
        }
    }
}
