/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.log;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志记录类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
final public class LOG {

    static final boolean LOG4J_ENABLED = isLog4jEnabled();
    static final boolean LOG4J2_ENABLED = isLog4j2Enabled();
    static final boolean LOGBACK_ENABLED = isLogbackEnabled();

    private static final QuartzWebLogger QUARTZ_WEB_LOGGER = getQuartzWebLogger();

    private LOG() {
        super();
    }

    /**
     * 判断是否启用Log4j
     * @return true 启用
     */
    private static boolean isLog4jEnabled() {
        try {
            Class.forName("org.apache.log4j.Logger");

            Class.forName("org.apache.log4j.AppenderSkeleton");

            Class.forName("org.apache.log4j.spi.LocationInfo");
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    /**
     * 判断是否启用Log4j2
     * @return true启用
     */
    private static boolean isLog4j2Enabled() {
        try {
            Class.forName("org.apache.logging.log4j.Logger");

            Class.forName("org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder");
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    /**
     * 判断是否启用logback
     * @return true启用
     */
    private static boolean isLogbackEnabled() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            final Class<?> loggerFactoryClass = Class.forName("org.slf4j.LoggerFactory");
            final Method method = loggerFactoryClass.getMethod("getILoggerFactory");
            final Object obj = method.invoke(null);

            return Class.forName("ch.qos.logback.classic.LoggerContext").isAssignableFrom(obj.getClass());

        } catch (final Throwable e) {
            return false;
        }
    }

    /**
     * 记录logger - debug级别
     * @param msg 信息
     */
    public static void debug(String msg) {
        QUARTZ_WEB_LOGGER.debug(msg);
    }

    /**
     * 记录logger - debug级别
     * @param msg 信息
     * @param throwable Throwable
     */
    public static void debug(String msg, Throwable throwable) {
        QUARTZ_WEB_LOGGER.debug(msg, throwable);
    }

    /**
     * 记录logger - info级别
     * @param msg 信息
     */
    public static void info(String msg) {
        QUARTZ_WEB_LOGGER.info(msg);
    }

    /**
     * 记录logger - info级别
     * @param msg 信息
     * @param throwable Throwable
     */
    public static void info(String msg, Throwable throwable) {
        QUARTZ_WEB_LOGGER.info(msg, throwable);
    }

    /**
     * 记录logger - error级别
     * @param msg 信息
     * @param throwable Throwable
     */
    public static void error(String msg, Throwable throwable){
        QUARTZ_WEB_LOGGER.error(msg, throwable);
    }


    /**
     * 记录logger - warn级别
     * @param msg 信息
     * @param throwable Throwable
     */
    public static void warn(String msg, Throwable throwable) {
        try {
            QUARTZ_WEB_LOGGER.warn(msg, throwable);
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    /**
     * 构建request日志信息
     * @param request HttpServletRequest
     * @return 日志信息
     */
    public static String buildLogMessage(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer msg = new StringBuffer();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (msg.length() == 0) {
                msg.append(key).append("=");
            } else {
                msg.append("&").append(key).append("=");
            }

            for (int i = 0; i < values.length; i++) {
                if (i == 0) {
                    msg.append(values[i]);
                } else {
                    msg.append("&").append(key).append("=").append(values[i]);
                }
            }
        }
        return msg.toString();
    }


    /**
     * 获取log实例对象
     * @return log实例
     */
    private static QuartzWebLogger getQuartzWebLogger(){
        if (LOGBACK_ENABLED) {
            return new LogbackLogger();
        } else if (LOG4J2_ENABLED) {
            return new Log4J2Logger();
        } else if (LOG4J_ENABLED) {
            return new Log4JLogger();
        } else {
            return new JavaLogger();
        }
    }

}
