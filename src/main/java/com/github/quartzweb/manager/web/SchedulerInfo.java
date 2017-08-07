/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 调度器信息封装类
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class SchedulerInfo extends HashMap<String, Object> {

    /**
     * 调度器名称
     */
    private String schedulerName;

    /**
     * 是否关闭
     */
    private boolean shutdown;

    /**
     * 是否启动
     */
    private boolean started;

    /**
     * 是否处于待机模式
     */
    private boolean inStandbyMode;

    /**
     * job数量
     */
    private int jobCount;

    /**
     * 触发器数量
     */
    private int triggerCount;

    /**
     * 触发器数量
     */
    private int triggerErrorCount;

    /**
     * 阻塞触发器数量
     */
    private int triggerBlockedCount;

    /**
     * 暂停触发器数量
     */
    private int triggerPausedCount;

    /**
     * 调度器
     */
    private List<Map<String, Object>> schedulerContext = new ArrayList<Map<String, Object>>();

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
     * 获取 是否关闭
     * @return isShutdown 是否关闭
     */
    public boolean isShutdown() {
        return this.shutdown;
    }

    /**
     * 设置 是否关闭
     * @param shutdown 是否关闭
     */
    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
        this.put("isShutdown", shutdown);
    }

    /**
     * 获取 是否启动
     * @return isStarted 是否启动
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * 设置 是否启动
     * @param started 是否启动
     */
    public void setStarted(boolean started) {
        this.started = started;
        this.put("isStarted", started);
    }

    /**
     * 获取 是否处于待机模式
     * @return isInStandbyMode 是否处于待机模式
     */
    public boolean isInStandbyMode() {
        return this.inStandbyMode;
    }

    /**
     * 设置 是否处于待机模式
     * @param inStandbyMode 是否处于待机模式
     */
    public void setInStandbyMode(boolean inStandbyMode) {
        this.inStandbyMode = inStandbyMode;
        this.put("isInStandbyMode", inStandbyMode);
    }

    /**
     * 获取 job数量
     * @return jobCount job数量
     */
    public int getJobCount() {
        return this.jobCount;
    }

    /**
     * 设置 job数量
     * @param jobCount job数量
     */
    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
        this.put("jobCount", jobCount);
    }

    /**
     * 获取 触发器数量
     * @return triggerCount 触发器数量
     */
    public int getTriggerCount() {
        return this.triggerCount;
    }

    /**
     * 设置 触发器数量
     * @param triggerCount 触发器数量
     */
    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
        this.put("triggerCount", triggerCount);
    }

    /**
     * 获取 触发器数量
     * @return triggerErrorCount 触发器数量
     */
    public int getTriggerErrorCount() {
        return this.triggerErrorCount;
    }

    /**
     * 设置 触发器数量
     * @param triggerErrorCount 触发器数量
     */
    public void setTriggerErrorCount(int triggerErrorCount) {
        this.triggerErrorCount = triggerErrorCount;
        this.put("triggerErrorCount", triggerErrorCount);
    }

    /**
     * 获取 阻塞触发器数量
     * @return triggerBlockedCount 阻塞触发器数量
     */
    public int getTriggerBlockedCount() {
        return this.triggerBlockedCount;
    }

    /**
     * 设置 阻塞触发器数量
     * @param triggerBlockedCount 阻塞触发器数量
     */
    public void setTriggerBlockedCount(int triggerBlockedCount) {
        this.triggerBlockedCount = triggerBlockedCount;
        this.put("triggerBlockedCount", triggerBlockedCount);
    }

    /**
     * 获取 暂停触发器数量
     * @return triggerPausedCount 暂停触发器数量
     */
    public int getTriggerPausedCount() {
        return this.triggerPausedCount;
    }

    /**
     * 设置 暂停触发器数量
     * @param triggerPausedCount 暂停触发器数量
     */
    public void setTriggerPausedCount(int triggerPausedCount) {
        this.triggerPausedCount = triggerPausedCount;
        this.put("triggerPausedCount", triggerPausedCount);
    }

    /**
     * 获取 调度器
     * @return schedulerContext 调度器
     */
    public List<Map<String, Object>> getSchedulerContext() {
        return this.schedulerContext;
    }

    /**
     * 设置 调度器
     * @param schedulerContext 调度器
     */
    public void setSchedulerContext(List<Map<String, Object>> schedulerContext) {
        this.schedulerContext = schedulerContext;
        this.put("schedulerContext", schedulerContext);
    }


}
