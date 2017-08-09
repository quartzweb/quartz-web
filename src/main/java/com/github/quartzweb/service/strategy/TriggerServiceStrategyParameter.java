/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.exception.UnsupportedTranslateException;
import com.github.quartzweb.service.HttpParameterNameConstants;
import com.github.quartzweb.utils.DateUtils;
import com.github.quartzweb.utils.RequestUtils;
import com.github.quartzweb.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 曲修成
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class TriggerServiceStrategyParameter implements ServiceStrategyParameter {

    /**
     * scheduler名称
     */
    private String schedulerName;

    /**
     * jobName
     */
    private String jobName;

    /**
     * jobGroup
     */
    private String jobGroup;

    /**
     * triggerName
     */
    private String triggerName;

    /**
     * triggerGroup
     */
    private String triggerGroup;


    /**
     * cron表达式
     */
    private String cronExpression;


    /**
     * 描述
     */
    private String description;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 附件data
     */
    private Map<String, String> jobDataMap;

    /**
     * 获取 scheduler名称
     *
     * @return schedulerName scheduler名称
     */
    public String getSchedulerName() {
        return this.schedulerName;
    }

    /**
     * 设置 scheduler名称
     *
     * @param schedulerName scheduler名称
     */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /**
     * 获取 jobName
     *
     * @return jobName jobName
     */
    public String getJobName() {
        return this.jobName;
    }

    /**
     * 设置 jobName
     *
     * @param jobName jobName
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取 jobGroup
     *
     * @return jobGroup jobGroup
     */
    public String getJobGroup() {
        return this.jobGroup;
    }

    /**
     * 设置 jobGroup
     *
     * @param jobGroup jobGroup
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取 triggerName
     *
     * @return triggerName triggerName
     */
    public String getTriggerName() {
        return this.triggerName;
    }

    /**
     * 设置 triggerName
     *
     * @param triggerName triggerName
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    /**
     * 获取 triggerGroup
     *
     * @return triggerGroup triggerGroup
     */
    public String getTriggerGroup() {
        return this.triggerGroup;
    }

    /**
     * 设置 triggerGroup
     *
     * @param triggerGroup triggerGroup
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
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
    }
    /**
     * 获取 优先级
     * @return priority 优先级
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * 设置 优先级
     * @param priority 优先级
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 获取 开始时间
     * @return startDate 开始时间
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * 设置 开始时间
     * @param startDate 开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取 结束时间
     * @return endDate 结束时间
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * 设置 结束时间
     * @param endDate 结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取 附件data
     * @return jobDataMap 附件data
     */
    public Map<String, String> getJobDataMap() {
        return this.jobDataMap;
    }

    /**
     * 设置 附件data
     * @param jobDataMap 附件data
     */
    public void setJobDataMap(Map<String, String> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    @Override
    public void translate(Object object) throws UnsupportedTranslateException {
        if (object instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) object;
            this.setSchedulerName(request.getParameter(HttpParameterNameConstants.Scheduler.NAME));
            this.setJobGroup(request.getParameter(HttpParameterNameConstants.Job.GROUP));
            this.setJobName(request.getParameter(HttpParameterNameConstants.Job.NAME));
            this.setTriggerName(request.getParameter(HttpParameterNameConstants.Trigger.NAME));
            this.setTriggerGroup(request.getParameter(HttpParameterNameConstants.Trigger.GROUP));

            this.setCronExpression(request.getParameter(HttpParameterNameConstants.Trigger.CRONEXPRESSION));
            this.setDescription(request.getParameter(HttpParameterNameConstants.Trigger.DESCRIPTION));
            this.setPriority(request.getParameter(HttpParameterNameConstants.Trigger.PRIORITY));

            // 获取jobDataMap
            Map<String, String> mapData = RequestUtils.getMapData(request,
                    HttpParameterNameConstants.Job.DATA_MAP_KEY_PREFIX,
                    HttpParameterNameConstants.Job.DATA_MAP_VALUE_PREFIX);

            if (mapData.size() > 0) {
                this.setJobDataMap(mapData);
            }
            //开始时间
            String startDate = request.getParameter(HttpParameterNameConstants.Trigger.START_DATE);
            if (!StringUtils.isEmpty(startDate)) {
                this.setStartDate(DateUtils.parse(startDate));
            }
            String endDate = request.getParameter(HttpParameterNameConstants.Trigger.END_DATE);
            if (!StringUtils.isEmpty(endDate)) {
                //结束时间
                this.setEndDate(DateUtils.parse(endDate));
            }

        } else {
            throw new UnsupportedTranslateException(object.getClass().getName() + " translate exception");
        }
    }



}
