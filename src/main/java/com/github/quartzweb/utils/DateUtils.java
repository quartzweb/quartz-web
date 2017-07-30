/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]0
 */
public class DateUtils {



    private static Date startTime;

    /**
     * 获取jvm启动时间
     * @return 启动时间
     */
    public final static Date getVMStartTime() {
        if (startTime == null) {
            startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        }
        return startTime;
    }

    /**
     * 格式化时间
     * @param date
     * @return yyyy-MM-dd HH:mm:ss 的时间
     */
    public static String formart(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Date parse(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            //e.printStackTrace();
            return null;
        }
    }

}
