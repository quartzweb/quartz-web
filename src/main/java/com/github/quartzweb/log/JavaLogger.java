/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.log;

import java.util.logging.Level;

/**
 * java log记录
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JavaLogger implements QuartzWebLogger {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(INTERNAL_LOGGER_NAME);

    /** {@inheritDoc} */
    @Override
    public void debug(String msg) {
        LOGGER.log(Level.FINE, msg);
    }

    /** {@inheritDoc} */
    @Override
    public void debug(String msg, Throwable throwable) {
        LOGGER.log(Level.FINE, msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void info(String msg) {
        LOGGER.log(Level.INFO, msg);
    }

    /** {@inheritDoc} */
    @Override
    public void info(String msg, Throwable throwable) {
        LOGGER.log(Level.INFO, msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void warn(String msg, Throwable throwable) {
        LOGGER.log(Level.WARNING, msg, throwable);
    }

    /** {@inheritDoc} */
    @Override
    public void error(String msg, Throwable throwable) {
        LOGGER.log(Level.SEVERE, msg, throwable);
    }
}
