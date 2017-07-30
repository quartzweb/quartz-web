/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.service.JSONResult;
import com.github.quartzweb.service.QuartzWebURL;
import com.github.quartzweb.utils.Assert;
import org.quartz.CronExpression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class ValidateServiceStrategy implements ServiceStrategy<ValidateServiceStrategyParameter> {


    /**
     * 业务处理
     * @param serviceStrategyURL 处理URL类
     * @param parameter 参数
     * @return 返回JSON数据
     */
    public JSONResult service(ServiceStrategyURL serviceStrategyURL, ValidateServiceStrategyParameter parameter) {
        // 核查class名称
        if (QuartzWebURL.ValidateURL.VALIDATE_CLASS.getURL().equals(serviceStrategyURL.getURL())) {
            return validateClass(parameter.getClassName());
        }
        // 核查class并且获取classInfo
        if (QuartzWebURL.ValidateURL.VALIDATE_CLASSINFO.getURL().equals(serviceStrategyURL.getURL())) {
            return validateClassInfo(parameter.getClassName());
        }
        // 核查class时候为另一个子类型
        if (QuartzWebURL.ValidateURL.VALIDATE_ASSIGNABLE.getURL().equals(serviceStrategyURL.getURL())) {
            return validateAssignable(parameter.getClassName(), parameter.getAssignableClassName());
        }
        //校验job是否存在
        if (QuartzWebURL.ValidateURL.VALIDATE_JOB.getURL().equals(serviceStrategyURL.getURL())) {
            return validateJob(parameter.getSchedulerName(), parameter.getJobName(), parameter.getJobGroup());
        }
        //校验trigger是否存在
        if (QuartzWebURL.ValidateURL.VALIDATE_TRIGGER.getURL().equals(serviceStrategyURL.getURL())) {
            return validateTrigger(parameter.getSchedulerName(), parameter.getTriggerName(), parameter.getTriggerGroup());
        }
        //校验cron表达式是否正确
        if (QuartzWebURL.ValidateURL.VALIDATE_CRONEXPRESSION.getURL().equals(serviceStrategyURL.getURL())) {
            return validateCronExpression(parameter.getCronExpression());
        }
        return JSONResult.build(JSONResult.RESULT_CODE_ERROR, "404 not found");
    }


    public ValidateServiceStrategyParameter newServiceStrategyParameterInstance() {
        return new ValidateServiceStrategyParameter();
    }

    /**
     * 核查className是否合法
     * @param className class名称
     * @return 返回JSON数据
     */
    public JSONResult validateClass(String className){
        try {
            Assert.notEmpty(className, "className can not be empty");
            boolean check = QuartzWebManager.checkClass(className);
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            result.put("result", check);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    /**
     * 核查className是否合法,并获取名称
     * @param className class名
     * @return JSON数据
     */
    public JSONResult validateClassInfo(String className){
        try {
            Assert.notEmpty(className, "className can not be empty");
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            Class clazz = QuartzWebManager.getClass(className);
            boolean classResult;
            if (clazz != null) {
                // 是否继承 job类
                if (org.quartz.Job.class.isAssignableFrom(clazz)) {
                    result.put("jobType", JobServiceStrategyParameter.JobType.JOB.getDictValue());
                } else {
                    result.put("jobType", JobServiceStrategyParameter.JobType.NOJOB.getDictValue());

                    List<List<String>> constructorInfo = QuartzWebManager.getConstructorInfo(clazz);
                    // 方法信息
                    List<Map<String,List<String>>> methodInfo = QuartzWebManager.getMethodInfo(clazz);
                    result.put("constructorInfo", constructorInfo);
                    result.put("methodInfo", methodInfo);
                }

                classResult = true;
            } else {
                classResult = false;
            }
            result.put("result", classResult);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    /**
     * 核查class时候为另一个子类型
     * @param className class名称
     * @param assignableClassName class子类名称
     * @return JSON数据
     */
    public JSONResult validateAssignable(String className, String assignableClassName) {
        try {
            Assert.notEmpty(className, "className can not be empty");
            Assert.notEmpty(assignableClassName, "assignableClassName can not be empty");
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            boolean sourceCheck = QuartzWebManager.checkClass(className);
            boolean targetCheck = QuartzWebManager.checkClass(assignableClassName);
            if (sourceCheck && targetCheck) {
                Class clazz = QuartzWebManager.getClass(className);
                Class assignableClazz = QuartzWebManager.getClass(assignableClassName);
                boolean assignableFrom = assignableClazz.isAssignableFrom(clazz);
                result.put("result", assignableFrom);
            } else {
                result.put("result", false);
            }
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult validateJob(String schedulerName, String jobName, String jobGroup) {
        try {
            Assert.notEmpty(schedulerName, "schedulerName can not be empty");
            Assert.notEmpty(jobName, "jobName can not be empty");
            Assert.notEmpty(jobGroup, "jobGroup can not be empty");
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            boolean exist = QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup);
            result.put("result", exist);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    /**
     * 核查trigger是否存在
     * @param schedulerName
     * @param triggerName
     * @param triggerGroup
     * @return
     */
    public JSONResult validateTrigger(String schedulerName, String triggerName, String triggerGroup) {
        try {
            Assert.notEmpty(schedulerName, "schedulerName can not be empty");
            Assert.notEmpty(triggerName, "triggerName can not be empty");
            Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            boolean exist = QuartzWebManager.checkTriggerExists(schedulerName, triggerName, triggerGroup);
            result.put("result", exist);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    private JSONResult validateCronExpression(String cronExpression) {
        try {
            Assert.notEmpty(cronExpression, "cronExpression can not be empty");
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            result.put("result", CronExpression.isValidExpression(cronExpression));
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

}
