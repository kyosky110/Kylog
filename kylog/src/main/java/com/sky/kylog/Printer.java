package com.sky.kylog;

/**
 * Created by kyosky on 15/11/17.
 */
public interface Printer {

    void d(StackTraceElement element, String message, Object... args);

    void v(StackTraceElement element, String message, Object... args);

    void a(StackTraceElement element, String message, Object... args);

    void i(StackTraceElement element, String message, Object... args);

    void e(StackTraceElement element, String message, Object... args);

    void w(StackTraceElement element, String message, Object... args);

    void json(StackTraceElement element, String message);

    void object(StackTraceElement element, Object object);
}
