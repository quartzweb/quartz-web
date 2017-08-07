/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import java.util.Date;
import java.util.HashMap;

/**
 * 触发器信息封装类
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class TriggerInfo extends HashMap<String, Object> {

    /**
     * 调度器名称
     */
    private String schedulerName;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 作业分组
     */
    private String jobGroup;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 触发器分组
     */
    private String triggerGroup;

    /***
     * 上次触发时间
     */
    private Date previousFireTime;

    /**
     * 下次触发时间
     */
    private Date nextFireTime;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 获取misfire的值，默认为0
     */
    private int misfireInstruction;

    /**
     * 最后触发时间
     */
    private Date finalFireTime;

    /**
     * 某个时间后的触发时间
     */
    private Date fireTimeAfter;

    /**
     * 日历名称
     */
    private String calendarName;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private String triggerState;

    /**
     * cron表达式
     */
    private String cronExpression;


    /**
     * 获取 调度器名称
     * @return schedulerName 调度器名称
     */
    public String getSchedulerName() {
        return this.schedulerName;
    }

    /**
     * 设置 调度器名称
     * @param schedulerName 调度器名称
     */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
        this.put("schedulerName", schedulerName);
    }

    /**
     * 获取 作业名称
     * @return jobName 作业名称
     */
    public String getJobName() {
        return this.jobName;
    }

    /**
     * 设置 作业名称
     * @param jobName 作业名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
        this.put("jobName", jobName);
    }

    /**
     * 获取 作业分组
     * @return jobGroup 作业分组
     */
    public String getJobGroup() {
        return this.jobGroup;
    }

    /**
     * 设置 作业分组
     * @param jobGroup 作业分组
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        this.put("jobGroup", jobGroup);
    }

    /**
     * 获取 触发器名称
     * @return triggerName 触发器名称
     */
    public String getTriggerName() {
        return this.triggerName;
    }

    /**
     * 设置 触发器名称
     * @param triggerName 触发器名称
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        this.put("triggerName", triggerName);
    }

    /**
     * 获取 触发器分组
     * @return triggerGroup 触发器分组
     */
    public String getTriggerGroup() {
        return this.triggerGroup;
    }

    /**
     * 设置 触发器分组
     * @param triggerGroup 触发器分组
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
        this.put("triggerGroup", triggerGroup);
    }

    /**
     * 获取 上次触发时间
     * @return previousFireTime 上次触发时间
     */
    public Date getPreviousFireTime() {
        return this.previousFireTime;
    }

    /**
     * 设置 上次触发时间
     * @param previousFireTime 上次触发时间
     */
    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
        this.put("previousFireTime", previousFireTime);
    }

    /**
     * 获取 下次触发时间
     * @return nextFireTime 下次触发时间
     */
    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    /**
     * 设置 下次触发时间
     * @param nextFireTime 下次触发时间
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
        this.put("nextFireTime", nextFireTime);
    }

    /**
     * 获取 优先级
     * @return priority 优先级
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * 设置 优先级
     * @param priority 优先级
     */
    public void setPriority(int priority) {
        this.priority = priority;
        this.put("priority", priority);
    }

    /**
     * 获取 开始时间
     * @return startTime 开始时间
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * 设置 开始时间
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        this.put("startTime", startTime);
    }

    /**
     * 获取 结束时间
     * @return endTime 结束时间
     */
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * 设置 结束时间
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
        this.put("endTime", endTime);
    }

    /**
     * 获取 获取misfire的值，默认为0
     * @return misfireInstruction 获取misfire的值，默认为0
     */
    public int getMisfireInstruction() {
        return this.misfireInstruction;
    }

    /**
     * 设置 获取misfire的值，默认为0
     * @param misfireInstruction 获取misfire的值，默认为0
     */
    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
        this.put("misfireInstruction", misfireInstruction);
    }

    /**
     * 获取 最后触发时间
     * @return finalFireTime 最后触发时间
     */
    public Date getFinalFireTime() {
        return this.finalFireTime;
    }

    /**
     * 设置 最后触发时间
     * @param finalFireTime 最后触发时间
     */
    public void setFinalFireTime(Date finalFireTime) {
        this.finalFireTime = finalFireTime;
        this.put("finalFireTime", finalFireTime);
    }

    /**
     * 获取 某个时间后的触发时间
     * @return fireTimeAfter 某个时间后的触发时间
     */
    public Date getFireTimeAfter() {
        return this.fireTimeAfter;
    }

    /**
     * 设置 某个时间后的触发时间
     * @param fireTimeAfter 某个时间后的触发时间
     */
    public void setFireTimeAfter(Date fireTimeAfter) {
        this.fireTimeAfter = fireTimeAfter;
        this.put("fireTimeAfter", fireTimeAfter);
    }

    /**
     * 获取 日历名称
     * @return calendarName 日历名称
     */
    public String getCalendarName() {
        return this.calendarName;
    }

    /**
     * 设置 日历名称
     * @param calendarName 日历名称
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        this.put("calendarName", calendarName);
    }

    /**
     * 获取 描述
     * @return description 描述
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置 描述
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
        this.put("description", description);
    }

    /**
     * 获取 状态
     * @return triggerState 状态
     */
    public String getTriggerState() {
        return this.triggerState;
    }

    /**
     * 设置 状态
     * @param triggerState 状态
     */
    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
        this.put("triggerState", triggerState);
    }

    /**
     * 获取 cron表达式
     * @return cronExpression cron表达式
     */
    public String getCronExpression() {
        return this.cronExpression;
    }

    /**
     * 设置 cron表达式
     * @param cronExpression cron表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        this.put("cronExpression", cronExpression);
    }

}
