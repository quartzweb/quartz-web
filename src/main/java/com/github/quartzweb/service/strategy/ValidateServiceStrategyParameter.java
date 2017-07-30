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
public class ValidateServiceStrategyParameter implements ServiceStrategyParameter {

    /**
     * class名称
     */
    private String className;

    /**
     * class子类名称
     */
    private String assignableClassName;

    /**
     * job名称
     */
    private String jobName;

    /**
     * job分组
     */
    private String jobGroup;

    /**
     * scheduler名称
     */
    private String schedulerName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * trigger名称
     */
    private String triggerName;

    /**
     * trigger分组
     */
    private String triggerGroup;


    /**
     * 获取 class名称
     * @return className class名称
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * 设置 class名称
     * @param className class名称
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取 class子类名称
     * @return assignableClassName class子类名称
     */
    public String getAssignableClassName() {
        return this.assignableClassName;
    }

    /**
     * 设置 class子类名称
     * @param assignableClassName class子类名称
     */
    public void setAssignableClassName(String assignableClassName) {
        this.assignableClassName = assignableClassName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    @Override
    public void translate(Object object) throws UnsupportedTranslateException {

        if (object instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) object;
            this.setClassName(request.getParameter(HttpParameterNameConstants.Validate.CLASS_NAME));
            this.setAssignableClassName(request.getParameter(HttpParameterNameConstants.Validate.ASSIGNABLE_CLASS_NAME));
            this.setCronExpression(request.getParameter(HttpParameterNameConstants.Validate.CRON_EXPRESSION));
            this.setJobName(request.getParameter(HttpParameterNameConstants.Job.NAME));
            this.setJobGroup(request.getParameter(HttpParameterNameConstants.Job.GROUP));
            this.setSchedulerName(request.getParameter(HttpParameterNameConstants.Scheduler.NAME));
            this.setTriggerName(request.getParameter(HttpParameterNameConstants.Trigger.NAME));
            this.setTriggerGroup(request.getParameter(HttpParameterNameConstants.Trigger.GROUP));
        } else {
            throw new UnsupportedTranslateException(object.getClass().getName() + " translate exception");
        }

    }

}
