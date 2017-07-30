/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));
        return buf.toString();
    }

}
