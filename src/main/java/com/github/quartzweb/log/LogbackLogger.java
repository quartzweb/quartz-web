/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.log;

import org.slf4j.Logger;

/**
 * logback 日志记录
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class LogbackLogger implements QuartzWebLogger {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(INTERNAL_LOGGER_NAME);

    /** {@inheritDoc} */
    @Override
    public void debug(String msg) {
        LOGGER.debug(msg);
    }

    /** {@inheritDoc} */
    @Override
    public void debug(String msg, Throwable throwable) {
        LOGGER.debug(msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void info(String msg) {
        LOGGER.info(msg);
    }

    /** {@inheritDoc} */
    @Override
    public void info(String msg, Throwable throwable) {
        LOGGER.info(msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void warn(String msg, Throwable throwable) {
        LOGGER.warn(msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void error(String msg, Throwable throwable) {
        LOGGER.error(msg, throwable);
    }
}
