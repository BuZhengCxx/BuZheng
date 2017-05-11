package com.bu.zheng.log;

import android.text.TextUtils;
import android.util.Log;

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

    public LogPrinter() {

    }

    public void setTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("log tag cannot be null");
        }
        this.tag = tag;
    }

    /**
     * DEBUG LOG
     *
     * @param msg
     * @param args
     */
    public void d(String msg, Object... args) {
        String message = createMessage(msg, args);
        log(LogLevel.DEBUG, null, null, message);
    }

    public void d(String msg) {
        log(LogLevel.DEBUG, null, null, msg);
    }

    public synchronized void log(int logLevel, Throwable throwable, String tag, String message) {

        if (throwable != null && !TextUtils.isEmpty(message)) {
            message += " : " + Log.getStackTraceString(throwable);
        }
        if (throwable != null && TextUtils.isEmpty(message)) {
            message = Log.getStackTraceString(throwable);
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        int methodCount = 0;

        if (TextUtils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        logTopBorder(logLevel, tag);
        logHeaderContent(logLevel, tag, methodCount);

        if (methodCount > 0) {
            logDivider(logLevel, tag);
            logMessage(logLevel, tag, message);

        } else {
            logMessage(logLevel,tag,message);

        }
        logBottomBorder(logLevel, tag);
    }

    private void logTopBorder(int logLevel, String tag) {
        logChunk(logLevel, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logLevel, String tag, int methodCount) {

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        logChunk(logLevel, tag, VERTICAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().getName());
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
            builder.append("║ ")
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

    /**
     * @param message
     * @param args
     * @return
     */
    private String createMessage(String message, Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
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
}
