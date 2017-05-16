package com.bu.zheng.log;

import android.support.annotation.NonNull;

/**
 * Created by BuZheng on 2017/5/10.
 */

public class Logger {

    private static LogPrinter printer = new LogPrinter();

    /**
     * App Start
     */
    public static void logAppStart() {
        printer.logAppStart();
    }

    /**
     * App Exit
     */
    public static void logAppExit() {
        printer.logAppExit();
    }

    /**
     * debug info
     *
     * @param moduleName
     * @param message
     */
    public static void d(@NonNull String moduleName, String message) {
        printer.d(moduleName, message);
    }

    /**
     * error info
     *
     * @param moduleName
     * @param throwable
     * @param message
     */
    public static void e(@NonNull String moduleName, @NonNull Throwable throwable, String message) {
        printer.e(moduleName, throwable, message);
    }

}
