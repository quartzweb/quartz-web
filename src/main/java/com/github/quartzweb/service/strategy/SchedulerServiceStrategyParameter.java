/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.exception.UnsupportedTranslateException;
import com.github.quartzweb.service.HttpParameterNameConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class SchedulerServiceStrategyParameter implements ServiceStrategyParameter {


    /**
     * scheduler名称
     */
    private String schedulerName;

    /**
     * 延迟启动秒数
     */
    private String delayed;


    /**
     * 获取 scheduler名称
     * @return schedulerName scheduler名称
     */
    public String getSchedulerName() {
        return this.schedulerName;
    }

    /**
     * 设置 scheduler名称
     * @param schedulerName scheduler名称
     */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /**
     * 获取 延迟启动秒数
     * @return delayed 延迟启动秒数
     */
    public String getDelayed() {
        return this.delayed;
    }

    /**
     * 设置 延迟启动秒数
     * @param delayed 延迟启动秒数
     */
    public void setDelayed(String delayed) {
        this.delayed = delayed;
    }

    /**
     * 转换成SchedulerServiceStrategyParameter
     * @param object 转换bean
     * @return SchedulerServiceStrategyParameter实体类
     */
    public void translate(Object object) throws UnsupportedTranslateException {

        if (object instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) object;
            this.setDelayed(request.getParameter(HttpParameterNameConstants.Scheduler.DELAYED));
            this.setSchedulerName(request.getParameter(HttpParameterNameConstants.Scheduler.NAME));
        } else {
            throw new UnsupportedTranslateException(object.getClass().getName() + " translate exception");
        }

    }

}
