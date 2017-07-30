/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.exception;

/**
 * 不支持的转换异常
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class UnsupportedTranslateException extends RuntimeException {

    public UnsupportedTranslateException() {
    }

    public UnsupportedTranslateException(String message) {
        super(message);
    }

    public UnsupportedTranslateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedTranslateException(Throwable cause) {
        super(cause);
    }

}
