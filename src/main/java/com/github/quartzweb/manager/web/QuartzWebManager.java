/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import com.github.quartzweb.VERSION;
import com.github.quartzweb.job.MethodInvoker;
import com.github.quartzweb.manager.bean.QuartzBeanManagerFacade;
import com.github.quartzweb.manager.quartz.QuartzManager;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.ClassUtils;
import com.github.quartzweb.utils.DateUtils;
import com.github.quartzweb.utils.QuartzUtils;
import com.github.quartzweb.utils.ReflectionUtils;
import com.github.quartzweb.utils.json.JSONWriter;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.core.QuartzScheduler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzWebManager {

    private final static QuartzWebManager instance = new QuartzWebManager();

    private QuartzWebManager() {
    }

    public static QuartzWebManager getInstance() {
        return instance;
    }

    private static QuartzManager quartzManager = QuartzManager.getInstance();

    private static QuartzBeanManagerFacade quartzBeanManagerFacade = QuartzBeanManagerFacade.getInstance();

    public static Map<String, Object> getBasicInfo() {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        String versionIteration = QuartzScheduler.getVersionIteration();
        String versionMajor = QuartzScheduler.getVersionMajor();
        String versionMinor = QuartzScheduler.getVersionMinor();

        dataMap.put("quartzWebVersion", VERSION.getVersionNumber());
        dataMap.put("versionMajor", versionMajor);
        dataMap.put("versionMinor", versionMinor);
        dataMap.put("versionIteration", versionIteration);

        dataMap.put("quartzVersion", versionMajor + "." + versionMinor + "." + versionIteration);

        dataMap.put("javaVMStartTime", DateUtils.getVMStartTime());
        dataMap.put("javaVMName", System.getProperty("java.vm.name"));
        dataMap.put("javaVersion", System.getProperty("java.version"));
        dataMap.put("javaClassPath", System.getProperty("java.class.path"));
        return dataMap;
    }

    /**
     * 获取Scheduler基本信息
     *
     * @return 返回基本信息的Map
     * @throws SchedulerException 异常
     */
    public static List<Map<String, Object>> getAllSchedulerInfo() throws SchedulerException {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Collection<Scheduler> allSchedulers = quartzManager.getSchedulers();

        for (Scheduler scheduler : allSchedulers) {
            // 获取Job数量
            List<JobDetail> allJobsOfScheduler = QuartzUtils.getAllJobsOfScheduler(scheduler);

            // 错误Trigger数量
            int triggerErrorCount = 0;
            // 堵塞Trigger数量
            int triggerBlockedCount = 0;
            // 暂停Trigger数量
            int triggerPausedCount = 0;
            for (JobDetail jobDetail : allJobsOfScheduler) {
                List<? extends Trigger> triggersOfJob = QuartzUtils.getTriggersOfJob(jobDetail, scheduler);
                for (Trigger trigger : triggersOfJob) {
                    boolean isError = QuartzUtils.isTriggerError(trigger, scheduler);
                    if (isError) {
                        triggerErrorCount++;
                    }
                    boolean isBlocked = QuartzUtils.isTriggerBlocked(trigger, scheduler);
                    if (isBlocked) {
                        triggerBlockedCount++;
                    }
                    boolean isPaused = QuartzUtils.isTriggerPaused(trigger, scheduler);
                    if (isPaused) {
                        triggerPausedCount++;
                    }
                }

            }

            // schedulerConext中参数Map
            List<Map<String, Object>> schedulerConextMapList = new ArrayList<Map<String, Object>>();
            SchedulerContext context = scheduler.getContext();
            Set<String> contextKeySet = context.keySet();
            for (String contextKey : contextKeySet) {
                Map<String, Object> contextMap = new LinkedHashMap<String, Object>();
                Object contextKeyObj = context.get(contextKey);
                // 是否支持json转换
                boolean support = JSONWriter.support(contextKeyObj);
                if (support) {
                    contextMap.put("key", contextKey);
                    contextMap.put("value", contextKeyObj);
                } else {
                    contextMap.put("key", contextKey);
                    contextMap.put("value", contextKeyObj.toString());
                }
                schedulerConextMapList.add(contextMap);
            }

            Map<String, Object> schedulerInfoMap = new LinkedHashMap<String, Object>();
          /*  schedulerInfoMap.put("schedulerInstanceId", scheduler.getSchedulerInstanceId());*/
            schedulerInfoMap.put("schedulerName", scheduler.getSchedulerName());
            schedulerInfoMap.put("isShutdown", scheduler.isShutdown());
            schedulerInfoMap.put("isStarted", scheduler.isStarted());
            schedulerInfoMap.put("isInStandbyMode", scheduler.isInStandbyMode());
            // 设置job数量
            schedulerInfoMap.put("jobCount", allJobsOfScheduler.size());
            // 设置数量
            schedulerInfoMap.put("triggerErrorCount", triggerErrorCount);
            schedulerInfoMap.put("triggerBlockedCount", triggerBlockedCount);
            schedulerInfoMap.put("triggerPausedCount", triggerPausedCount);
            // 设置schedulerConext
            schedulerInfoMap.put("schedulerConext", schedulerConextMapList);


            dataList.add(schedulerInfoMap);
        }
        return dataList;
    }


    /**
     * 启动
     *
     * @param schedulerName schedler名称
     * @throws SchedulerException 异常
     */
    public static void schedulerStart(String schedulerName) throws SchedulerException {
        quartzManager.schedulerStart(schedulerName);
    }

    /**
     * 延时启动
     *
     * @param schedulerName schedler名称
     * @param delayed       延时启动秒数
     * @throws SchedulerException 异常信息
     */
    public static void schedulerStart(String schedulerName, int delayed) throws SchedulerException {
        quartzManager.schedulerStart(schedulerName, delayed);
    }

    public static void schedulerShutdown(String schedulerName) throws SchedulerException {
        quartzManager.schedulerShutdown(schedulerName);
    }

    public static void schedulerShutdown(String schedulerName, boolean waitForJobsToComplete) throws SchedulerException {
        quartzManager.schedulerShutdown(schedulerName, waitForJobsToComplete);
    }

    /**
     * 获取Job的信息
     *
     * @return
     * @throws SchedulerException
     */
    public static Map<String, Object> getAllJobInfo() throws SchedulerException {

        Collection<Scheduler> allSchedulers = quartzManager.getSchedulers();
        Map<String, Object> jobInfo = new LinkedHashMap<String, Object>();
        for (Scheduler scheduler : allSchedulers) {
            List<JobDetail> allJobs = QuartzUtils.getAllJobsOfScheduler(scheduler);
            List<Map<String, Object>> jobMapList = new ArrayList<Map<String, Object>>();
            for (JobDetail jobDetail : allJobs) {
                Map<String, Object> jobMap = new LinkedHashMap<String, Object>();
                jobMap.put("jobName", QuartzUtils.getJobName(jobDetail));
                jobMap.put("jobGroup", QuartzUtils.getJobGroup(jobDetail));
                jobMap.put("jobClass", jobDetail.getJobClass().getName());

                // 是否并发运行
                jobMap.put("isConcurrentExectionDisallowed", jobDetail.isConcurrentExectionDisallowed());
                // 是否实例化
                jobMap.put("isDurable", jobDetail.isDurable());
                // 时候执行后保存JobData
                jobMap.put("isPersistJobDataAfterExecution", jobDetail.isPersistJobDataAfterExecution());
                jobMap.put("jobDataMap", jobDetail.getJobDataMap());
                jobMap.put("description", jobDetail.getDescription());
                jobMapList.add(jobMap);
            }

            jobInfo.put(scheduler.getSchedulerName(), jobMapList);
        }
        return jobInfo;
    }

    public static List<Map<String, Object>> getAllJobInfo(String schedulerName) throws SchedulerException {

        List<JobDetail> allJobs = quartzManager.getAllJobsOfScheduler(schedulerName);
        List<Map<String, Object>> jobMapList = new ArrayList<Map<String, Object>>();
        for (JobDetail jobDetail : allJobs) {
            Map<String, Object> jobMap = new LinkedHashMap<String, Object>();
            jobMap.put("jobName", QuartzUtils.getJobName(jobDetail));
            jobMap.put("jobGroup", QuartzUtils.getJobGroup(jobDetail));
            jobMap.put("jobClass", jobDetail.getJobClass().getName());

            // 是否并发运行
            jobMap.put("isConcurrentExectionDisallowed", jobDetail.isConcurrentExectionDisallowed());
            // 是否实例化
            jobMap.put("isDurable", jobDetail.isDurable());
            // 时候执行后保存JobData
            jobMap.put("isPersistJobDataAfterExecution", jobDetail.isPersistJobDataAfterExecution());
            jobMap.put("jobDataMap", jobDetail.getJobDataMap());
            jobMap.put("description", jobDetail.getDescription());
            jobMapList.add(jobMap);
        }
        return jobMapList;
    }

    /**
     * 校验job是否存在
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @return true存在
     * @throws SchedulerException
     */
    public static boolean checkJobExist(String schedulerName,String jobName,String jobGroup) throws SchedulerException {
        return quartzManager.existJob(schedulerName, jobName, jobGroup);
    }

    public static Trigger getTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        return quartzManager.getTrigger(schedulerName, triggerName, triggerGroup);
    }

    public static void runTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        quartzManager.runTrigger(schedulerName, triggerName, triggerGroup);
    }
    /**
     * 获取触发器集合
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public static List<Trigger> getTriggers(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        List<? extends Trigger> triggersOfJob = quartzManager.getTriggersOfJob(schedulerName, jobName, jobGroup);
        List<Trigger> triggers = new ArrayList<Trigger>();
        triggers.addAll(triggersOfJob);
        return triggers;
    }

    public static List<Map<String, Object>> getTriggerInfo(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        // 最终返回数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<? extends Trigger> triggersOfJob = getTriggers(schedulerName, jobName, jobGroup);
        for (Trigger trigger : triggersOfJob) {
            Map<String, Object> triggerMap = new LinkedHashMap<String, Object>();
            triggerMap.put("schedulerName", schedulerName);
            triggerMap.put("jobName", jobName);
            triggerMap.put("jobGroup", jobGroup);
            triggerMap.put("triggerName", QuartzUtils.getTriggerName(trigger));
            triggerMap.put("triggerGroup", QuartzUtils.getTriggerGroup(trigger));
            // 上次触发时间
            triggerMap.put("previousFireTime", trigger.getPreviousFireTime());
            // 下次触发时间
            triggerMap.put("nextFireTime", trigger.getNextFireTime());
            // 优先级
            triggerMap.put("priority", trigger.getPriority());
            triggerMap.put("startTime", trigger.getStartTime());
            triggerMap.put("endTime", trigger.getEndTime());
            //获取misfire的值，默认为0
            triggerMap.put("misfireInstruction", trigger.getMisfireInstruction());
            // 最后触发时间
            triggerMap.put("finalFireTime", trigger.getFinalFireTime());
            // 某个时间后的触发时间
            triggerMap.put("fireTimeAfter", trigger.getFireTimeAfter(new Date()));
            // 日历名称
            triggerMap.put("calendarName", trigger.getCalendarName());
            // 描述
            triggerMap.put("description", trigger.getDescription());
            triggerMap.put("triggerState", quartzManager.getTriggerState(schedulerName, trigger).name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                triggerMap.put("cronExpression", cronTrigger.getCronExpression());
            } else {
                triggerMap.put("cronExpression", "");
            }
            dataList.add(triggerMap);
        }

        return dataList;
    }

    /**
     * 核查名称是否正确
     * @param className
     */
    public static boolean checkClass(String className) {
        Assert.notEmpty(className, "className can not be empty");
        Object bean = null;
        try {
            bean = quartzBeanManagerFacade.getBean(className);
        } catch (Exception ignored) {
        }
        if (bean != null) {
            return true;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }

    }

    public static Class getClass(String className) {
        Assert.notEmpty(className, "className can not be empty");
        Object bean = null;
        try {
            bean = quartzBeanManagerFacade.getBean(className);
        } catch (Exception ignored) {
        }
        if (bean != null) {
            return bean.getClass();
        }
        try {
            Class<?> clazz = ClassUtils.forName(className,ClassUtils.getDefaultClassLoader());
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }

    }
    /**
     * 根据class名称获取bean
     * @param className
     * @return
     */
    public static Object getBean(String className) {
        Assert.notEmpty(className, "className can not be empty");
        Object bean = quartzBeanManagerFacade.getBean(className);
        return bean;
    }

    /**
     * 根据参数类型和class名称获取bean
     * @param className
     * @param args 参数
     * @return
     */
    public static Object getBean(String className, Object[] args) {
        Assert.notEmpty(className, "className can not be empty");
        Object bean = quartzBeanManagerFacade.getBean(className, args);
        return bean;
    }

    public static Method[] getAllDeclaredMethods(Class<?> clazz){
        Assert.notNull(clazz, "class can not be null");
        List<Method> methodList = new ArrayList<Method>();
        // 获取全部方法，包括父类和接口
        ReflectionUtils.getAllDeclaredMethods(clazz, methodList);

        // 转换成数组
        Method[] methods = methodList.toArray(new Method[methodList.size()]);
        return methods;
    }

    public static Method[] getMethods(Class<?> clazz){
        Assert.notNull(clazz, "class can not be null");
        // 获取全部方法，包括父类和接口
        return clazz.getDeclaredMethods();
    }
    /**
     * 获取构造函数信息
     * [
     *  [参数类型1，参数类型2],
     *  [参数类型1]
     * ]
     * @param clazz
     * @return
     */
    public static List<List<String>> getConstructorInfo(Class<?> clazz){
        Assert.notNull(clazz, "object can not be null");
        Constructor[] constructors = clazz.getConstructors();
        /**
         * [
         *  [参数类型1，参数类型2],
         *  [参数类型1]
         * ]
         */
        List<List<String>> dataList = new ArrayList<List<String>>();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            ArrayList<String> parameterTypeNames = new ArrayList<String>();
            for (Class parameterType : parameterTypes) {
                String parameterTypeName = parameterType.getName();
                parameterTypeNames.add(parameterTypeName);
            }
            dataList.add(parameterTypeNames);
        }
        return dataList;
    }

    /**
     * 获取方法信息,名称后面添加了一个#,方便前台解析
     * [
     *  methodName1#=[参数类型1，参数类型2]，
     *  methodName2#=[参数类型1]
     * ]
     * @param clazz
     * @return
     */
    public static List<Map<String,List<String>>> getMethodInfo(Class<?> clazz){
        Assert.notNull(clazz, "class can not be null");
        Method[] methods = getMethods(clazz);
        /**
         * [
         *  methodName1=[参数类型1，参数类型2]，
         *  methodName2=[参数类型1]
         * ]
         */
        List<Map<String,List<String>>> dataList = new ArrayList<Map<String,List<String>>>();
        for (Method method : methods) {
            Class[] parameterTypes = method.getParameterTypes();
            Map<String, List<String>> nameType = new LinkedHashMap<String, List<String>>();
            String name = method.getName();
            List<String> parameterTypeNames = new ArrayList<String>();
            for (Class parameterType : parameterTypes) {
                String parameterTypeName = parameterType.getName();
                parameterTypeNames.add(parameterTypeName);
            }
            nameType.put(name+"#",parameterTypeNames);
            dataList.add(nameType);
        }
        return dataList;
    }

    public static JobDetail getJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        return quartzManager.getJob(schedulerName, jobName, jobGroup);
    }

    public static JobDetail addJob(String schedulerName, JobDetail jobDetail) throws SchedulerException {
        quartzManager.addJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public static JobDetail addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String description,
                                          MethodInvoker methodInvoker) throws SchedulerException {
        return quartzManager.addMethodInovkeJob(schedulerName, jobName, jobGroup, description, methodInvoker);
    }

    public static JobDetail addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                          Object[] constructorArguments, String jobClassMethodName,
                                          Object[] jobClassMethodArgs, String description) throws SchedulerException {
        return quartzManager.addMethodInovkeJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                jobClassMethodName,jobClassMethodArgs,description);
    }

    public static JobDetail addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String description,
                                          MethodInvoker methodInvoker) throws SchedulerException {
        return quartzManager.addStatefulMethodJob(schedulerName, jobName, jobGroup, description, methodInvoker);
    }

    public static JobDetail addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                          Object[] constructorArguments, String jobClassMethodName,
                                          Object[] jobClassMethodArgs, String description) throws SchedulerException {
        return quartzManager.addStatefulMethodJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                jobClassMethodName,jobClassMethodArgs,description);
    }

    public static JobDetail updateJob(String schedulerName, JobDetail jobDetail) throws SchedulerException {
        quartzManager.updateJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public static JobDetail updateMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String description,
                                               MethodInvoker methodInvoker) throws SchedulerException {
        return quartzManager.updateMethodInovkeJob(schedulerName, jobName, jobGroup, description, methodInvoker);
    }

    public static JobDetail updateMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                               Object[] constructorArguments, String jobClassMethodName,
                                               Object[] jobClassMethodArgs, String description) throws SchedulerException {
        return quartzManager.updateMethodInovkeJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                jobClassMethodName,jobClassMethodArgs,description);
    }

    public static JobDetail updateStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String description,
                                                 MethodInvoker methodInvoker) throws SchedulerException {
        return quartzManager.updateStatefulMethodJob(schedulerName, jobName, jobGroup, description, methodInvoker);
    }

    public static JobDetail updateStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                                 Object[] constructorArguments, String jobClassMethodName,
                                                 Object[] jobClassMethodArgs, String description) throws SchedulerException {
        return quartzManager.updateStatefulMethodJob(schedulerName, jobName, jobGroup, jobClass, constructorArguments,
                jobClassMethodName,jobClassMethodArgs,description);
    }

    public static void addTriggerForJob(String schedulerName, String jobName, String jobGroup, Trigger trigger) throws SchedulerException {
        quartzManager.addTriggerForJob(schedulerName, jobName, jobGroup, trigger);
    }

    public static void addTriggerForJob(String schedulerName, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        quartzManager.addTriggerForJob(schedulerName, jobDetail, trigger);
    }

    public static void addTriggerForJob(String schedulerName, Trigger trigger) throws SchedulerException {
        quartzManager.addTriggerForJob(schedulerName, trigger);
    }

    public static void updateTriggerForJob(String schedulerName, Trigger trigger) throws SchedulerException {
        quartzManager.updateTriggerForJob(schedulerName, trigger);
    }

    public static void pauseJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        quartzManager.pauseJob(schedulerName, jobName, jobGroup);

    }

    public static void resumeJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        quartzManager.resumeJob(schedulerName, jobName, jobGroup);
    }

    public static void removeJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        quartzManager.removeJob(schedulerName, jobName, jobGroup);
    }

    public static void runJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        quartzManager.runJob(schedulerName, jobName, jobGroup);
    }

    /**
     * 核查触发器是否存在
     * @param schedulerName
     * @param triggerName
     * @param triggerGroup
     * @return
     */
    public static boolean checkTriggerExists(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        return quartzManager.existTrigger(schedulerName, triggerName, triggerGroup);
    }

    public static void pauseTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        quartzManager.pauseTrigger(schedulerName, triggerName, triggerGroup);

    }

    public static void resumeTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        quartzManager.resumeTrigger(schedulerName, triggerName, triggerGroup);
    }

    public static void removeTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        quartzManager.removeTrigger(schedulerName, triggerName, triggerGroup);
    }

}