/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import org.quartz.CronExpression;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public abstract class Assert {


    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(String str, String message) {
        if (!StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInteger(String integer, String message) {
        if (!StringUtils.isInteger(integer)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void equalsAnyOne(String unexpected, String[] actuals, String message) {
        if (unexpected == null) {
            return;
        }
        for (String actual : actuals) {
            if (actual != null) {
                if (actual.equals(unexpected)) {
                    return;
                }
            }
        }
        throw new IllegalArgumentException(message);
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message + " " +
                            "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                            "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }

    /**
     * 校验cron表达式
     * @param unexpected
     * @param message
     */
    public static void isCronExpression(String unexpected, String message) {
        boolean validExpression = CronExpression.isValidExpression(unexpected);
        if (!validExpression) {
            throw new IllegalArgumentException(message);
        }
    }

}

