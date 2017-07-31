/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.job;

import com.github.quartzweb.manager.bean.QuartzBeanManagerFacade;
import com.github.quartzweb.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class MethodInvokeJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MethodInvokeJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date startDate = new Date();
        logger.debug("methodInvokeJob start : " + DateUtils.formart(startDate));
        long startTime = startDate.getTime();

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        //Object methodInvokerObj = jobDataMap.get("methodInvoker");
        Object jobClassObj = jobDataMap.get("jobClass");
        Object constructorArgumentsObj = jobDataMap.get("constructorArguments");
        Object jobClassMethodNameObj = jobDataMap.get("jobClassMethodName");
        Object jobClassMethodArgsObj = jobDataMap.get("jobClassMethodArgs");
        try {
            String jobClass = (String) jobClassObj;
            Object[] constructorArguments = (Object[]) constructorArgumentsObj;
            String jobClassMethodName = (String) jobClassMethodNameObj;
            Object[] jobClassMethodArgs = (Object[]) jobClassMethodArgsObj;
            Object jobBean;
            QuartzBeanManagerFacade quartzBeanManagerFacade = QuartzBeanManagerFacade.getInstance();
            if (constructorArguments != null && constructorArguments.length > 0) {
                jobBean = quartzBeanManagerFacade.getBean(jobClass, constructorArguments);
            } else {
                jobBean = quartzBeanManagerFacade.getBean(jobClass);
            }

            MethodInvoker methodInvoker = new MethodInvoker();
            methodInvoker.setTargetMethod(jobClassMethodName);
            methodInvoker.setArguments(jobClassMethodArgs);

            methodInvoker.setTargetObject(jobBean);

            boolean prepared = methodInvoker.isPrepared();
            if (!prepared) {
                methodInvoker.prepare();
            }
            Object result = methodInvoker.invoke();
            context.setResult(result);
            Date endDate = new Date();
            long endTime = endDate.getTime();
            logger.debug("methodInvokeJob end : " + DateUtils.formart(endDate) + "," + (endTime - startTime));

        } catch (Exception e) {
            logger.error("MethodInvokeJob exception message:" + e.getMessage());
            e.printStackTrace();
            throw new JobExecutionException(e);
        }
    }
}
