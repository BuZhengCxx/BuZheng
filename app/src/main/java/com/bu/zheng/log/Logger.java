package com.bu.zheng.log;

/**
 * Created by BuZheng on 2017/5/10.
 */

public class Logger {

    private static LogPrinter printer = new LogPrinter();

    public static void d(String msg, Object... args) {
        printer.d(msg, args);
    }

}
