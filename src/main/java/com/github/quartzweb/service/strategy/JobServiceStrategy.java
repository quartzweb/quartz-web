/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.exception.UnsupportedClassException;
import com.github.quartzweb.job.MethodInvoker;
import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.service.JSONResult;
import com.github.quartzweb.service.QuartzWebURL;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.StringUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JobServiceStrategy implements ServiceStrategy<JobServiceStrategyParameter> {

    @Override
    public JSONResult service(ServiceStrategyURL serviceStrategyURL, JobServiceStrategyParameter parameter) {
        String url = serviceStrategyURL.getURL();
        // 列出信息
        if (QuartzWebURL.JobURL.INFO.getURL().equals(url)) {
            return getInfo();
        }

        String schedulerName = parameter.getSchedulerName();
        String jobName = parameter.getJobName();
        String jobGroup = parameter.getJobGroup();
        String jobClass = parameter.getJobClass();
        Map<String, String> jobDataMap = parameter.getJobDataMap();
        String description = parameter.getDescription();
        // 转换类型
        Map<String, Object> jobDataMapObject = new LinkedHashMap<String, Object>();
        if (jobDataMap != null) {
            jobDataMapObject.putAll(jobDataMap);
        }

        //添加
        if (QuartzWebURL.JobURL.ADD.getURL().equals(url)) {

            JobServiceStrategyParameter.JobType jobType = parameter.getJobType();
            if (jobType == null) {
                throw new IllegalArgumentException("jobType can not be null");
            }
            // 继承org.quartz.Job添加
            if (jobType.getDictValue() == JobServiceStrategyParameter.JobType.JOB.getDictValue()) {
                return addQuartzJob(schedulerName, jobName, jobGroup, jobClass, jobDataMapObject, description);
            } else {
                String jobClassMethodName = parameter.getJobClassMethodName();
                Assert.notEmpty(jobClassMethodName, "jobClassMethodName can not be null");
                Object[] jobClassMethodArgs = parameter.getJobClassMethodArgs();
                Object[] constructorArguments = parameter.getConstructorArguments();

                // 非继承org.quartz.Job
                JobServiceStrategyParameter.MethodInvokerType methodInvokerType = parameter.getMethodInvokerType();

                // 无状态添加JOB
                if (methodInvokerType.getDictValue() == JobServiceStrategyParameter.MethodInvokerType.NORMAL.getDictValue()) {
                    //return addMethodInovkeJob(schedulerName, jobName, jobGroup, description,methodInvoker);
                    return addMethodInovkeJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                            jobClassMethodName, jobClassMethodArgs, description);
                } else {
                    //有状态添加
                    //return addStatefulMethodJob(schedulerName, jobName, jobGroup, description,methodInvoker);
                    return addStatefulMethodJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                            jobClassMethodName, jobClassMethodArgs, description);
                }
                // 普通执行mehtond方法添加
                //return addQuartzJob(schedulerName, jobName, jobGroup, jobClass, jobDataMapObject, description);
            }

        }
        //重启
        if (QuartzWebURL.JobURL.RESUME.getURL().equals(url)) {
            return resumeJob(schedulerName, jobName, jobGroup);
        }
        //移除
        if (QuartzWebURL.JobURL.REMOVE.getURL().equals(url)) {
            return removeJob(schedulerName, jobName, jobGroup);
        }
        //暂停
        if (QuartzWebURL.JobURL.PAUSE.getURL().equals(url)) {
            return pauseJob(schedulerName, jobName, jobGroup);
        }
        //执行一次
        if (QuartzWebURL.JobURL.RUN.getURL().equals(url)) {
            return runJob(schedulerName, jobName, jobGroup);
        }
        return JSONResult.build(JSONResult.RESULT_CODE_ERROR, "404 not found");
    }



    @Override
    public JobServiceStrategyParameter newServiceStrategyParameterInstance() {
        return new JobServiceStrategyParameter();
    }

    /**
     * 获取job信息
     *
     * @return
     */
    public JSONResult getInfo() {
        try {
            Map<String, Object> result = QuartzWebManager.getAllJobInfo();
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }


    /**
     * 添加继承自 org.quartz.Job的类的方法
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @param jobClass
     * @param jobDataMap
     * @param description
     * @return
     */
    private JSONResult addQuartzJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                    Map<String, Object> jobDataMap, String description) {
        try {
            Assert.notEmpty(schedulerName, "schedulerName can not be empty");
            Assert.notEmpty(jobName, "jobName can not be empty");
            Assert.notEmpty(jobGroup, "jobGroup can not be empty");
            if (!QuartzWebManager.checkClass(jobClass)) {
                throw new IllegalArgumentException("jobClass no class found [" + jobClass + "] exception");
            }
            Class beanClass = QuartzWebManager.getClass(jobClass);

            if (org.quartz.Job.class.isAssignableFrom(beanClass)) {
                // 开始添加job
                JobDataMap jobMap = new JobDataMap();
                if (jobDataMap != null) {
                    for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
                        if (!StringUtils.isEmpty(entry.getKey())) {
                            jobMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                JobDetail jobDetail = JobBuilder.newJob(beanClass).withIdentity(jobName, jobGroup)
                        .withDescription(description).setJobData(jobMap).storeDurably().build();
                if (!QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup)) {
                    QuartzWebManager.addJob(schedulerName, jobDetail);
                } else {
                    QuartzWebManager.updateJob(schedulerName, jobDetail);
                }
                return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
            } else {
                throw new UnsupportedClassException(jobClass + " must extends org.quartz.Job ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    /**
     * 类方法执行job添加
     *
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @param description
     * @return
     */
    private JSONResult addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String description,
                                          MethodInvoker methodInvoker) {

        try {
            Assert.notNull(methodInvoker, "methodInvoker can not be null");
            if (QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup)) {
                QuartzWebManager.addMethodInovkeJob(schedulerName, jobName, jobGroup, description, methodInvoker);
            } else {
                QuartzWebManager.updateMethodInovkeJob(schedulerName, jobName, jobGroup, description, methodInvoker);
            }
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }

    }

    private JSONResult addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                          Object[] constructorArguments, String jobClassMethodName,
                                          Object[] jobClassMethodArgs, String description) {

        try {
            if (!QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup)) {
                QuartzWebManager.addMethodInovkeJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                        jobClassMethodName, jobClassMethodArgs, description);
            } else {
                QuartzWebManager.updateMethodInovkeJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                        jobClassMethodName,jobClassMethodArgs,description);
            }

            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }

    }

    /**
     * 有状态类方法执行job添加
     *
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @param description
     * @return
     */
    private JSONResult addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String description,
                                            MethodInvoker methodInvoker) {
        try {
            Assert.notNull(methodInvoker, "methodInvoker can not be null");
            if (!QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup)) {
                QuartzWebManager.addStatefulMethodJob(schedulerName, jobName, jobGroup, description, methodInvoker);
            } else {
                QuartzWebManager.updateStatefulMethodJob(schedulerName, jobName, jobGroup, description, methodInvoker);
            }
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }

    }

    private JSONResult addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                          Object[] constructorArguments, String jobClassMethodName,
                                          Object[] jobClassMethodArgs, String description) {

        try {
            if (!QuartzWebManager.checkJobExist(schedulerName, jobName, jobGroup)) {
                QuartzWebManager.addStatefulMethodJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                        jobClassMethodName, jobClassMethodArgs, description);
            } else {
                QuartzWebManager.updateStatefulMethodJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                        jobClassMethodName, jobClassMethodArgs, description);
            }
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }

    }

    public JSONResult pauseJob(String schedulerName, String jobName, String jobGroup) {
        try {
            QuartzWebManager.pauseJob(schedulerName, jobName, jobGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult resumeJob(String schedulerName, String jobName, String jobGroup) {
        try {
            QuartzWebManager.resumeJob(schedulerName, jobName, jobGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult removeJob(String schedulerName, String jobName, String jobGroup) {
        try {
            QuartzWebManager.removeJob(schedulerName, jobName, jobGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }


    private JSONResult runJob(String schedulerName, String jobName, String jobGroup) {
        try {
            QuartzWebManager.runJob(schedulerName, jobName, jobGroup);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

}
