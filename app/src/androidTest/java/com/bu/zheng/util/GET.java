package com.bu.zheng.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by BuZheng on 2017/8/2.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface GET {
    String value();
}