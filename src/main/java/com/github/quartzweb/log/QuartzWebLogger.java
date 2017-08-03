/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.log;

/**
 * 记录log接口
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public interface QuartzWebLogger {

    String INTERNAL_LOGGER_NAME = "com.github.quartzweb";

    /**
     * 记录logger - debug级别
     * @param msg 信息
     */
    void debug(String msg);

    /**
     * 记录logger - debug级别
     * @param msg 信息
     * @param throwable Throwable
     */
    void debug(String msg, Throwable throwable);

    /**
     * 记录logger - info级别
     * @param msg 信息
     */
    void info(String msg);

    /**
     * 记录logger - info级别
     * @param msg 信息
     * @param throwable Throwable
     */
    void info(String msg, Throwable throwable);

    /**
     * 记录logger - warn级别
     * @param msg 信息
     * @param throwable Throwable
     */
    void warn(String msg, Throwable throwable);

    /**
     * 记录logger - error级别
     * @param msg 信息
     * @param throwable Throwable
     */
    void error(String msg, Throwable throwable);


}
