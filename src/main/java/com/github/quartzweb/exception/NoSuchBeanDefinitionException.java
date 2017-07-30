/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.exception;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com].
 */
public class NoSuchBeanDefinitionException extends RuntimeException {

    public NoSuchBeanDefinitionException() {
    }

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

    public NoSuchBeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchBeanDefinitionException(Throwable cause) {
        super(cause);
    }
}
