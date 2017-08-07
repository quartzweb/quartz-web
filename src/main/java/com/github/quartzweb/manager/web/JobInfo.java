/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import org.quartz.JobDataMap;

import java.util.HashMap;

/**
 * 作业信息封装类
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JobInfo extends HashMap<String, Object> {

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
     * 作业class
     */
    private String jobClass;

    /**
     * 作业是否允许并发执行
     */
    private boolean concurrentExectionDisallowed;

    /**
     * 是否实例化
     */
    private boolean durable;

    /**
     * 执行后是否保存
     */
    private boolean persistJobDataAfterExecution;

    /**
     * 描述
     */
    private String description;

    /**
     * jobData
     */
    private JobDataMap jobDataMap;

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
        this.put("schedulerName", schedulerName);
        this.schedulerName = schedulerName;
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
        this.put("jobName", jobName);
        this.jobName = jobName;
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
        this.put("jobGroup", jobGroup);
        this.jobGroup = jobGroup;
    }

    /**
     * 获取 作业class
     * @return jobClass 作业class
     */
    public String getJobClass() {
        return this.jobClass;
    }

    /**
     * 设置 作业class
     *
     * @param jobClass 作业class
     */
    public void setJobClass(String jobClass) {
        this.put("jobClass", jobClass);
        this.jobClass = jobClass;
    }

    /**
     * 获取 作业是否允许并发执行
     * @return concurrentExectionDisallowed 作业是否允许并发执行
     */
    public boolean isConcurrentExectionDisallowed() {
        return this.concurrentExectionDisallowed;
    }

    /**
     * 设置 作业是否允许并发执行
     *
     * @param concurrentExectionDisallowed 作业是否允许并发执行
     */
    public void setConcurrentExectionDisallowed(boolean concurrentExectionDisallowed) {
        this.put("isConcurrentExectionDisallowed", concurrentExectionDisallowed);
        this.concurrentExectionDisallowed = concurrentExectionDisallowed;
    }

    /**
     * 获取 是否实例化
     * @return durable 是否实例化
     */
    public boolean isDurable() {
        return this.durable;
    }

    /**
     * 设置 是否实例化
     *
     * @param durable 是否实例化
     */
    public void setDurable(boolean durable) {
        this.put("isDurable", durable);
        this.durable = durable;
    }

    /**
     * 获取 执行后是否保存
     * @return persistJobDataAfterExecution 执行后是否保存
     */
    public boolean isPersistJobDataAfterExecution() {
        return this.persistJobDataAfterExecution;
    }

    /**
     * 设置 执行后是否保存
     *
     * @param persistJobDataAfterExecution 执行后是否保存
     */
    public void setPersistJobDataAfterExecution(boolean persistJobDataAfterExecution) {
        this.put("isPersistJobDataAfterExecution", persistJobDataAfterExecution);
        this.persistJobDataAfterExecution = persistJobDataAfterExecution;
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
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.put("description", description);
        this.description = description;
    }

    /**
     * 获取 jobData
     * @return jobDataMap jobData
     */
    public JobDataMap getJobDataMap() {
        return this.jobDataMap;
    }

    /**
     * 设置 jobData
     *
     * @param jobDataMap jobData
     */
    public void setJobDataMap(JobDataMap jobDataMap) {
        this.put("jobDataMap", jobDataMap);
        this.jobDataMap = jobDataMap;
    }

}
