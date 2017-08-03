/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class ExceptionUtils {

    /**
     * 将异常转化为信息
     * @param ex 异常
     * @return
     */
    public static String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));
        return buf.toString();
    }

}
