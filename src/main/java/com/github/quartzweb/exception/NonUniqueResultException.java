/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.exception;

/**
 * @author 曲修成
 * @className NonUniqueResultException
 * @description
 * @date 2017-06-21 17:23:00
 */
public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException() {
    }

    public NonUniqueResultException(String message) {
        super(message);
    }

    public NonUniqueResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonUniqueResultException(Throwable cause) {
        super(cause);
    }
}
