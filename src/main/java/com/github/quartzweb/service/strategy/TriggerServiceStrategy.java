/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.manager.web.TriggerInfo;
import com.github.quartzweb.service.JSONResult;
import com.github.quartzweb.service.QuartzWebURL;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class TriggerServiceStrategy implements ServiceStrategy<TriggerServiceStrategyParameter> {
    @Override
    public JSONResult service(ServiceStrategyURL serviceStrategyURL, TriggerServiceStrategyParameter parameter) {
        // 列出信息
        String url = serviceStrategyURL.getURL();
        if (QuartzWebURL.TriggerURL.INFO.getURL().equals(url)) {
            return getInfo(parameter.getSchedulerName(), parameter.getJobName(), parameter.getJobGroup());
        }

        String schedulerName = parameter.getSchedulerName();
        String triggerName = parameter.getTriggerName();
        String triggerGroup = parameter.getTriggerGroup();
        // 具体操作

        // 添加
        if (QuartzWebURL.TriggerURL.ADD.getURL().equals(url)) {
            Map<String, String> jobDataMap = parameter.getJobDataMap();
            Map<String, Object> jobDataMapObj = new LinkedHashMap<String, Object>();
            if (jobDataMap != null) {
                jobDataMapObj.putAll(jobDataMap);

            }
            return addTrigger(schedulerName, parameter.getJobName(), parameter.getJobGroup(),
                    triggerName, triggerGroup, parameter.getCronExpression(),
                    parameter.getDescription(), parameter.getPriority(),
                    parameter.getStartDate(), parameter.getEndDate(), jobDataMapObj);
        }
        //重启
        if (QuartzWebURL.TriggerURL.RESUME.getURL().equals(url)) {
            return resumeTrigger(schedulerName, triggerName, triggerGroup);
        }
        //移除
        if (QuartzWebURL.TriggerURL.REMOVE.getURL().equals(url)) {
            return removeTrigger(schedulerName, triggerName, triggerGroup);
        }
        //暂停
        if (QuartzWebURL.TriggerURL.PAUSE.getURL().equals(url)) {
            return pauseTrigger(schedulerName, triggerName, triggerGroup);
        }
        //执行一次
        if (QuartzWebURL.TriggerURL.RUN.getURL().equals(url)) {
            return runTrigge(schedulerName, triggerName, triggerGroup);
        }

        // 404没有找到url
        return JSONResult.build(JSONResult.RESULT_CODE_ERROR, "404 not found");
    }

    @Override
    public TriggerServiceStrategyParameter newServiceStrategyParameterInstance() {
        return new TriggerServiceStrategyParameter();
    }

    public JSONResult getInfo(String schedulerName, String jobName, String jobGroup) {
        try {
            List<TriggerInfo> triggerInfos = QuartzWebManager.getTriggerInfo(schedulerName, jobName, jobGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, triggerInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult addTrigger(String schedulerName, String jobName, String jobGroup,
                                 String triggerName, String triggerGroup, String cronExpression, String description,
                                 String priority, Date startDate, Date endDate, Map<String, Object> dataMap) {

        try {
            Assert.notEmpty(schedulerName, "schedulerName can not be empty");
            Assert.notEmpty(jobName, "jobName can not be empty");
            Assert.notEmpty(jobGroup, "jobGroup can not be empty");
            Assert.notEmpty(triggerName, "triggerName can not be empty");
            Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
            Assert.isCronExpression(cronExpression, "cronExpression is error");
            TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger();
            triggerTriggerBuilder = triggerTriggerBuilder.withIdentity(triggerName, triggerGroup);
            if (!StringUtils.isEmpty(description)) {
                triggerTriggerBuilder = triggerTriggerBuilder.withDescription(description);
            }
            if (!StringUtils.isEmpty(priority) && StringUtils.isInteger(priority)) {
                Assert.isInteger(priority, "priority must be int");
                triggerTriggerBuilder.withPriority(Integer.parseInt(priority));
            }
            if (startDate != null) {
                triggerTriggerBuilder.startAt(startDate);
            }
            if (endDate != null) {
                triggerTriggerBuilder.endAt(endDate);
            }
            //jobDataMap
            if (dataMap != null && dataMap.size() > 0) {
                JobDataMap jobDataMap = new JobDataMap();
                for (Map.Entry<String, Object> dataKey : dataMap.entrySet()) {
                    if (!StringUtils.isEmpty(dataKey.getKey())) {
                        jobDataMap.put(dataKey.getKey(), dataKey.getValue());
                    }
                }
                triggerTriggerBuilder.usingJobData(jobDataMap);
            }
            triggerTriggerBuilder.forJob(jobName, jobGroup);
            CronTrigger trigger = triggerTriggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
            //JobDetail jobDetail = QuartzWebManager.getJob(schedulerName, jobName, jobGroup);
            // 存在更新不存在,添加
            if (!QuartzWebManager.checkTriggerExists(schedulerName, triggerName, triggerGroup)) {
                QuartzWebManager.addTriggerForJob(schedulerName, trigger);
            } else {
                QuartzWebManager.updateTriggerForJob(schedulerName, trigger);
            }

            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult pauseTrigger(String schedulerName, String triggerName, String triggerGroup) {
        try {
            QuartzWebManager.pauseTrigger(schedulerName, triggerName,triggerGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public static JSONResult resumeTrigger(String schedulerName, String triggerName, String triggerGroup) {
        try {
            QuartzWebManager.resumeTrigger(schedulerName,triggerName, triggerGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public static JSONResult removeTrigger(String schedulerName, String triggerName, String triggerGroup) {
        try {
            QuartzWebManager.removeTrigger(schedulerName,triggerName, triggerGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public static JSONResult runTrigge(String schedulerName, String triggerName, String triggerGroup) {
        try {
            QuartzWebManager.runTrigger(schedulerName, triggerName, triggerGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

}
