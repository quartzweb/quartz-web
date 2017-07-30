/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.exception;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class UnsupportedClassException extends RuntimeException {
    public UnsupportedClassException() {
    }

    public UnsupportedClassException(String message) {
        super(message);
    }

    public UnsupportedClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedClassException(Throwable cause) {
        super(cause);
    }
}
