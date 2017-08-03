/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;


import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class StringUtils {

    /**
     * 判断两个字符串是否相等
     * @param a 字符
     * @param b 字符
     * @return true相等,false不相等
     */
    public static boolean equals(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    /**
     * 忽略大小写判断两个字符串是否相等
     * @param a 字符
     * @param b 字符
     * @return true相等,false不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equalsIgnoreCase(b);
    }

    /**
     * 判断字符串是否为空（NULL或空串""）
     * @param value 字符串
     * @return true为空，false不为空
     */
    public static boolean isEmpty(CharSequence value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为大于某个数字的整数
     * @param str
     * @param integer
     * @return
     */
    public static boolean isIntegerGTNumber(String str,Integer integer) {
        // 是整数
        if (isInteger(str)) {
            int source = Integer.parseInt(str);
            return source > integer;
        } else {
            return false;
        }
    }
}
