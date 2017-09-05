package com.bu.zheng.log;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bu.zheng.app.BuZhengApp;
import com.bu.zheng.util.PlatformUtil;

import java.util.Calendar;

/**
 * Created by BuZheng on 2017/5/10.
 */

public class LogPrinter {

    private static final String TAG = "COMICLOG";

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;


    private String tag;

    /**
     * Localize single tag and method count for each thread
     */
    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final ThreadLocal<Integer> localMethodCount = new ThreadLocal<>();


    private static final String APP_START = "************************************************************************************************";
    private static final char APP_START_LEFT = '*';

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char VERTICAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_HORIZONTAL_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_HORIZONTAL_DIVIDER = "────────────────────────────────────────────";

    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_HORIZONTAL_DIVIDER + DOUBLE_HORIZONTAL_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_HORIZONTAL_DIVIDER + DOUBLE_HORIZONTAL_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_HORIZONTAL_DIVIDER + SINGLE_HORIZONTAL_DIVIDER;

    private static final int MIN_STACK_OFFSET = 3;

    private static Calendar mDate = Calendar.getInstance();
    private static StringBuffer mBuffer = new StringBuffer();

    /**
     * 组成Log字符串.添加时间信息.
     */
    private static String getLogStr() {

        mDate.setTimeInMillis(System.currentTimeMillis());

        mBuffer.setLength(0);
        mBuffer.append("[");
        mBuffer.append(mDate.get(Calendar.MONTH) + 1);
        mBuffer.append("-");
        mBuffer.append(mDate.get(Calendar.DATE));
        mBuffer.append(" ");
        mBuffer.append(mDate.get(Calendar.HOUR_OF_DAY));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MINUTE));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.SECOND));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MILLISECOND));
        mBuffer.append("]");

        return mBuffer.toString();
    }


    public LogPrinter() {

    }

    public void setTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("log tag cannot be null");
        }
        this.tag = tag;
    }

    /**
     * App Start
     */
    public void logAppStart() {
        logChunk(LogLevel.DEBUG, null, APP_START);
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Time: " + getLogStr());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Account: " + getAccountInfo());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Version: " + "2.7.0");
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Device: " + PlatformUtil.getMobileName());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " NetWork: " + PlatformUtil.getMobileType(BuZhengApp.instance()));
        logChunk(LogLevel.DEBUG, null, APP_START);
    }

    public void logChangeAccount() {
        logChunk(LogLevel.DEBUG, null, "**********************************************");
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Time: " + getLogStr());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Account: " + getAccountInfo());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " 登录方式: " + "微信");
        logChunk(LogLevel.DEBUG, null, "**********************************************");
    }

    public void logDebug() {
        System.out.println("<D> " + getLogStr() + " [ComicDetailActivity]" + " " + "漫画目录信息获取成功");
    }


    /**
     * App Exit
     */
    public void logAppExit() {
        logChunk(LogLevel.DEBUG, null, APP_START);
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " Time: " + getLogStr());
        logChunk(LogLevel.DEBUG, null, APP_START_LEFT + " " + "Exit");
        logChunk(LogLevel.DEBUG, null, APP_START);
    }

    /**
     * DEBUG LOG
     *
     * @param moduleName
     * @param message
     */
    public void d(@NonNull String moduleName, String message) {
        log(LogLevel.DEBUG, moduleName, null, null, message);
    }

    /**
     * ERROR LOG
     *
     * @param throwable
     * @param moduleName
     * @param message
     */
    public void e(@NonNull String moduleName, @NonNull Throwable throwable, String message) {
        log(LogLevel.ERROR, moduleName, throwable, null, message);
    }


    public synchronized void log(int logLevel,
                                 @NonNull String moduleName,
                                 Throwable throwable,
                                 String tag,
                                 String message) {

        if (throwable != null && !TextUtils.isEmpty(message)) {
            //message += " :\n " + Log.getStackTraceString(throwable);
            message += " :\n " + throwable.toString();
        }
        if (throwable != null && TextUtils.isEmpty(message)) {
            message = Log.getStackTraceString(throwable);
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        int methodCount = 3;

        if (TextUtils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        logTopBorder(logLevel, tag);
        logHeaderContent(logLevel, tag, methodCount, moduleName);

        if (methodCount > 0) {
            logDivider(logLevel, tag);
            logMessage(logLevel, tag, message);

        } else {
            logMessage(logLevel, tag, message);

        }
        logBottomBorder(logLevel, tag);
    }

    private void logTopBorder(int logLevel, String tag) {
        logChunk(logLevel, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logLevel, String tag, int methodCount, @NonNull String moduleName) {

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " Time: " + getLogStr());
        logDivider(logLevel, tag);

        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " LogLevel: Error;" + " Thread: " + Thread.currentThread().getName());
        logDivider(logLevel, tag);

        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " Module: " + moduleName);
        logDivider(logLevel, tag);

        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " Transaction Info: [ Type:" + "打赏礼物；" + " ErrorCode: " + -61410 + "； ErrorString: " + "网络错误 ]");
        logDivider(logLevel, tag);

        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " NetWork: " + PlatformUtil.getMobileType(BuZhengApp.instance()));
        logDivider(logLevel, tag);

        String level = "";

        int stackOffset = getStackOffset(trace);

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(VERTICAL_DOUBLE_LINE).append(" ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            logChunk(logLevel, tag, builder.toString());
        }
    }

    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logMessage(int logLevel, String tag, String message) {
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            logContent(logLevel, tag, message);

        } else {
            for (int i = 0; i < length; i += CHUNK_SIZE) {
                int count = Math.min(CHUNK_SIZE, length - i);
                logContent(logLevel, tag, new String(bytes, i, count));
            }
        }
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, tag, VERTICAL_DOUBLE_LINE + " " + line);
        }
    }

    private void logChunk(int logLevel, String tag, String chunk) {
        String finalTag = formatTag(tag);
        switch (logLevel) {
            case LogLevel.DEBUG:
                Log.d(finalTag, chunk);
                break;

            case LogLevel.VERBOSE:
                Log.v(finalTag, chunk);
                break;

            case LogLevel.INFO:
                Log.i(finalTag, chunk);
                break;

            case LogLevel.WARN:
                Log.w(finalTag, chunk);
                break;

            case LogLevel.ERROR:
                Log.e(finalTag, chunk);
                break;

            case LogLevel.ASSERT:
                Log.wtf(finalTag, chunk);
                break;

            default:
                Log.d(finalTag, chunk);
                break;
        }
    }

    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private String formatTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            return TAG + "-" + tag;
        }
        return TAG;
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement stackTraceElement = trace[i];
            String name = stackTraceElement.getClassName();
            if (!TextUtils.equals(LogPrinter.class.getName(), name)
                    && !TextUtils.equals(Logger.class.getName(), name)) {
                return --i;
            }
        }
        return -1;
    }

    private static String getAccountInfo() {
        return "test account";
    }
}
