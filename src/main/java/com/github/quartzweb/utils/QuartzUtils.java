/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]2017-06-16 9:41:00
 */
public class QuartzUtils {

    /**
     * 将会删除通用
     * @param properties
     * @return
     * @throws SchedulerException
     */
    public static Scheduler createScheduler(Properties properties) throws SchedulerException {
        StdSchedulerFactory stdSchedulerFactory= new StdSchedulerFactory(properties);
        return stdSchedulerFactory.getScheduler();
    }

    /**
     * 删除不够通用
     * @param schedName
     * @return
     */
    public static Scheduler getScheduler(String schedName) {
        return SchedulerRepository.getInstance().lookup(schedName);
    }

    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    private static boolean isQuartz2() {
        try {
            Class.forName("org.quartz.JobKey");
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取仓库中所有的Scheduler集合
     * @return
     */
    public static Collection<Scheduler> getAllScheduler(){
       return SchedulerRepository.getInstance().lookupAll();
    }

    /**
     * 获取Job名称
     * @param jobDetail
     * @return
     */
    public static String getJobName(JobDetail jobDetail) {
        return jobDetail.getKey().getName();
    }

    /**
     * 获取Job分组
     * @param jobDetail
     * @return
     */
    public static String getJobGroup(JobDetail jobDetail) {
        return jobDetail.getKey().getGroup();
    }

    /**
     * 获取scheduler下所有的jobDetail
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static List<JobDetail> getAllJobsOfScheduler(Scheduler scheduler) throws SchedulerException {
        List<JobDetail> result = new ArrayList<JobDetail>();
        List<String> jobGroupNames = scheduler.getJobGroupNames();
        for (String groupName : jobGroupNames) {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupName));
            for (JobKey jobKey : jobKeys) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                result.add(jobDetail);
            }
        }
        return result;
    }

    /**
     * 获取job信息
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public static JobDetail getJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = getJobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        return jobDetail;
    }

    public static JobKey getJobKey(String jobName, String jobGroup) {
        return new JobKey(jobName, jobGroup);
    }

    public static void addJob(JobDetail jobDetail,
                              boolean replace,Scheduler scheduler) throws SchedulerException {
        scheduler.addJob(jobDetail, replace);
    }

    public static void addJob(JobDetail jobDetail,
                              boolean replace, boolean storeNonDurableWhileAwaitingScheduling, Scheduler scheduler) throws SchedulerException {

        scheduler.addJob(jobDetail, replace, storeNonDurableWhileAwaitingScheduling);
    }
    /**
     * 获取Job中所有的Trigger
     * @param jobDetail
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static List<? extends Trigger> getTriggersOfJob(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobDetail.getKey());
    }

    public static List<? extends Trigger> getTriggersOfJob(String jobName,String jobGroup, Scheduler scheduler) throws SchedulerException {
        JobKey jobKey = getJobKey(jobName, jobGroup);
        return scheduler.getTriggersOfJob(jobKey);
    }

    /**
     * 核查Job在scheduler是否存在
     * @param jobDetail
     * @param scheduler
     * @throws SchedulerException
     */
    public static void checkJobExists(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        scheduler.checkExists(jobDetail.getKey());
    }

    /**
     * 暂停Job
     * @param jobDetail
     * @param scheduler
     * @throws SchedulerException
     */
    public static void pauseJob(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        scheduler.pauseJob(jobDetail.getKey());
    }

    public static void pauseJob(String jobName, String jobGroup, Scheduler scheduler) throws SchedulerException {
        scheduler.pauseJob(getJobKey(jobName, jobGroup));
    }

    /**
     * 重启job
     * @param jobDetail
     * @param scheduler
     * @throws SchedulerException
     */
    public static void resumeJob(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        scheduler.resumeJob(jobDetail.getKey());
    }

    public static void resumeJob(String jobName, String jobGroup, Scheduler scheduler) throws SchedulerException {
        scheduler.resumeJob(getJobKey(jobName, jobGroup));
    }

    /**
     * 删除Job和下面所有Trigger
     * @param jobDetail
     * @param scheduler
     * @throws SchedulerException
     */
    public static void removeJob(JobDetail jobDetail, Scheduler scheduler) throws SchedulerException {
        // 暂停
        pauseJob(jobDetail, scheduler);
        // 移除
        scheduler.deleteJob(jobDetail.getKey());
    }

    public static void removeJob(String jobName, String jobGroup, Scheduler scheduler) throws SchedulerException {
        // 暂停
        pauseJob(jobName, jobGroup, scheduler);
        scheduler.deleteJob(getJobKey(jobName, jobGroup));
    }

    /**
     * 获取Trigger的名称
     * @param trigger
     * @return
     */
    public static String getTriggerName(Trigger trigger) {
        return trigger.getKey().getName();
    }

    /**
     * 获取Trigger的分组
     * @param trigger
     * @return
     */
    public static String getTriggerGroup(Trigger trigger) {
        return trigger.getKey().getGroup();
    }

    public static TriggerKey getTriggerKey(String triggerName, String triggerGroup) {
        return new TriggerKey(triggerName, triggerGroup);
    }

    /**
     * 核查Trigger在scheduler是否存在
     *
     * @param trigger
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static boolean checkTriggerExists(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        return scheduler.checkExists(trigger.getKey());
    }

    /**
     * 核查Trigger在scheduler是否存在
     * 存在true
     * 不存在false
     *
     * @param scheduler
     * @param triggerName
     * @param triggerGroup
     * @return
     * @throws SchedulerException
     */
    public static boolean checkTriggerExists(String triggerName, String triggerGroup, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
        return scheduler.checkExists(triggerKey);
    }

    public static void pauseTrigger(String triggerName, String triggerGroup, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
        scheduler.pauseTrigger(triggerKey);
    }
    /**
     * 暂停Trigger
     * @param trigger
     * @param scheduler
     * @throws SchedulerException
     */
    public static void pauseTrigger(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        scheduler.pauseTrigger(trigger.getKey());
    }

    public static void resumeTrigger(String triggerName, String triggerGroup, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
        scheduler.resumeTrigger(triggerKey);
    }
    /**
     * 重启Trigger
     * @param trigger
     * @param scheduler
     * @throws SchedulerException
     */
    public static void resumeTrigger(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        scheduler.resumeTrigger(trigger.getKey());
    }

    public static void removeTrigger(String triggerName, String triggerGroup, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
        scheduler.unscheduleJob(triggerKey);
    }
    /**
     * 删除Trigger
     * @param trigger
     * @param scheduler
     * @throws SchedulerException
     */
    public static void removeTrigger(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        scheduler.unscheduleJob(trigger.getKey());
    }

    /**
     *
     * @param scheduler
     * @param trigger
     * @throws SchedulerException
     */
    public static void addTrigger(Scheduler scheduler, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(trigger);
    }

    public static void addTrigger(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 获取
     * @param trigger
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static TriggerState getTriggerState(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        return triggerState;
    }


    /**
     * 判断Trigger是否暂停
     * @param trigger
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static boolean isTriggerPaused(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        return triggerStateEquals(trigger, TriggerState.PAUSED, scheduler);
    }

    /**
     * 判断Trigger是否错误
     * @param trigger
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static boolean isTriggerError(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        return triggerStateEquals(trigger, TriggerState.ERROR, scheduler);
    }

    /**
     * 判断Triigger是否堵塞
     * @param trigger
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static boolean isTriggerBlocked(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        return triggerStateEquals(trigger, TriggerState.BLOCKED, scheduler);
    }

    /**
     * 对比两个Trigger是否相等
     * @param trigger
     * @param triggerState
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    public static boolean triggerStateEquals(Trigger trigger, TriggerState triggerState, Scheduler scheduler)
            throws SchedulerException {
        TriggerState sourceTriggerState = scheduler.getTriggerState(trigger.getKey());
        return sourceTriggerState.ordinal() == triggerState.ordinal();
    }

    /**
     * 将TriggerState英文翻译成中文
     * @param triggerState
     * @return
     */
    public static String triggerStateEN2CN(TriggerState triggerState){
        if (TriggerState.NORMAL.ordinal() == triggerState.ordinal()) {
            return "正常";
        } else if (TriggerState.PAUSED.ordinal() == triggerState.ordinal()) {
            return "暂停";
        } else if (TriggerState.COMPLETE.ordinal() == triggerState.ordinal()) {
            return "完成";
        } else if (TriggerState.ERROR.ordinal() == triggerState.ordinal()) {
            return "错误";
        } else if (TriggerState.BLOCKED.ordinal() == triggerState.ordinal()) {
            return "阻塞";
        } else {
            return "无";
        }
    }

}
