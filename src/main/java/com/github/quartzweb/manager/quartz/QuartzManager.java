package com.github.quartzweb.manager.quartz;

import com.github.quartzweb.exception.NonUniqueResultException;
import com.github.quartzweb.job.MethodInvokeJob;
import com.github.quartzweb.job.MethodInvoker;
import com.github.quartzweb.job.StatefulMethodInvokeJob;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.QuartzUtils;
import com.github.quartzweb.utils.StringUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 操作quartz的scheduler,job,triger核心管理类
 *
 * @author quxiucheng [quxiucheng@gmail.com]
 */
public class QuartzManager {

    private final static QuartzManager instance = new QuartzManager();

    private QuartzManager() {

    }

    public static QuartzManager getInstance() {
        return instance;
    }

    /**
     * 是否查找Scheduler仓库,默认true
     */
    private boolean lookupSchedulerRepository = true;

    /**
     * 是否启用默认Scheduler,默认true
     * 默认Scheduler是从defualtSchedulerFactory获取
     */
    private boolean useDefaultScheduler = true;

    /**
     * 默认Scheduler工厂
     */
    private SchedulerFactory defualtSchedulerFactory = new StdSchedulerFactory();

    /**
     * 管理的Scheduler集合
     */
    private List<Scheduler> schedulers = new ArrayList<Scheduler>();

    /**
     * schedulerMap方便查找
     */
    private Map<String, Scheduler> schedulerMap = new HashMap<String, Scheduler>();

    private boolean init = false;


    /**
     * 获取 是否查找Scheduler仓库,默认true
     *
     * @return lookupSchedulerRepository 是否查找Scheduler仓库,默认true
     */
    public boolean isLookupSchedulerRepository() {
        return this.lookupSchedulerRepository;
    }

    /**
     * 设置 是否查找Scheduler仓库,默认true
     *
     * @param lookupSchedulerRepository 是否查找Scheduler仓库,默认true
     */
    public void setLookupSchedulerRepository(boolean lookupSchedulerRepository) throws SchedulerException {
        this.lookupSchedulerRepository = lookupSchedulerRepository;
    }

    /**
     * 获取 是否启用默认Scheduler,默认true默认Scheduler是从defualtSchedulerFactory获取
     *
     * @return useDefaultScheduler 是否启用默认Scheduler,默认true默认Scheduler是从defualtSchedulerFactory获取
     */
    public boolean isUseDefaultScheduler() {
        return this.useDefaultScheduler;
    }

    /**
     * 设置 是否启用默认Scheduler,默认true默认Scheduler是从defualtSchedulerFactory获取
     *
     * @param useDefaultScheduler 是否启用默认Scheduler,默认true默认Scheduler是从defualtSchedulerFactory获取
     */
    public void setUseDefaultScheduler(boolean useDefaultScheduler) throws SchedulerException {
        this.useDefaultScheduler = useDefaultScheduler;
    }

    /**
     * 获取 默认Scheduler工厂
     *
     * @return defualtSchedulerFactory 默认Scheduler工厂
     */
    public SchedulerFactory getDefualtSchedulerFactory() {
        return this.defualtSchedulerFactory;
    }

    /**
     * 设置 默认Scheduler工厂
     *
     * @param defualtSchedulerFactory 默认Scheduler工厂
     */
    public void setDefualtSchedulerFactory(SchedulerFactory defualtSchedulerFactory) throws SchedulerException {
        this.defualtSchedulerFactory = defualtSchedulerFactory;
    }

    /**
     * 设置 管理的Scheduler集合
     *
     * @param schedulers 管理的Scheduler集合
     */
    public void setSchedulers(List<Scheduler> schedulers) throws SchedulerException {
        this.schedulers = schedulers;
    }

    /**
     * 注册获取schedulers
     * 所有设置后调用该方法
     *
     * @throws SchedulerException 异常
     */
    private void registerSchedulers() throws SchedulerException {
        // 未初始化,执行初始化注册代码
        if (!init) {
            if (schedulers == null) {
                schedulers = new ArrayList<Scheduler>();
            }

            // 将名称转换成map方便查找
            Map<String, String> schedulerNameMap = new HashMap<String, String>();
            for (Scheduler scheduler : this.schedulers) {
                schedulerNameMap.put(scheduler.getSchedulerName(), scheduler.getSchedulerName());
            }

            //是否查找仓库
            if (this.lookupSchedulerRepository) {
                //仓库中所有的Scheduler集合
                Collection<Scheduler> allRepositoryScheduler = QuartzUtils.getAllScheduler();
                for (Scheduler repositoryScheduler : allRepositoryScheduler) {
                    //在scheduler中不存在添加
                    if (StringUtils.isEmpty(schedulerNameMap.get(repositoryScheduler.getSchedulerName()))) {
                        this.schedulers.add(repositoryScheduler);
                    }
                }
            }
            //是否启动默认scheduler
            if (this.useDefaultScheduler) {
                Scheduler defualtScheduler = defualtSchedulerFactory.getScheduler();
                if (StringUtils.isEmpty(schedulerNameMap.get(defualtScheduler.getSchedulerName()))) {
                    this.schedulers.add(defualtScheduler);
                }
            }
            // 初始化schedulerMap信息
            this.schedulerMap = new HashMap<String, Scheduler>();
            for (Scheduler scheduler : this.schedulers) {
                this.schedulerMap.put(scheduler.getSchedulerName(), scheduler);
            }
            List<Scheduler> schedulersTemp = new ArrayList<Scheduler>();
            for (Map.Entry<String, Scheduler> schedulerEntry : this.schedulerMap.entrySet()) {
                Scheduler value = schedulerEntry.getValue();
                schedulersTemp.add(value);
            }
            this.schedulers = schedulersTemp;
            init = true;
        }

    }


    /**
     * 当scheduler唯一时获取scheduler
     *
     * @return scheduler对象
     * @throws SchedulerException 异常
     */
    public Scheduler getScheduler() throws SchedulerException {
        registerSchedulers();

        if (this.schedulers.size() > 1) {
            throw new NonUniqueResultException("[org.quartz.Scheduler] did not return a unique result");
        } else {
            return schedulers.get(0);
        }

    }

    /**
     * 根据scheduler名称获取
     *
     * @param schedulerName 名称
     * @return scheduler对象
     * @throws SchedulerException 异常
     */
    public Scheduler getScheduler(String schedulerName) throws SchedulerException {
        registerSchedulers();
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        return schedulerMap.get(schedulerName);
    }

    /**
     * 根据名称获取scheduler,如果没有则报错
     * @param schedulerName
     * @return
     * @throws IllegalArgumentException 出问题时报错
     */
    public Scheduler getAssertScheduler(String schedulerName) throws SchedulerException {
        registerSchedulers();
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Scheduler scheduler = schedulerMap.get(schedulerName);
        if (scheduler == null) {
            throw new IllegalArgumentException(schedulerName + " is not exists");
        }
        return scheduler;
    }

    /**
     * 获取 管理的Scheduler集合
     *
     * @return schedulers 管理的Scheduler集合
     */
    public List<Scheduler> getSchedulers() throws SchedulerException {
        registerSchedulers();
        // 不可改变
        return java.util.Collections.unmodifiableList(this.schedulers);
    }


    /**
     * 核查scheduler是否存在
     * @param schedulerName
     * @return
     */
    public boolean existsScheduler(String schedulerName) throws SchedulerException {
        Scheduler scheduler = this.getScheduler(schedulerName);
        if (scheduler == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取job
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public JobDetail getJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        JobDetail jobDetail = QuartzUtils.getJob(scheduler, jobName, jobGroup);
        return jobDetail;
    }

    /**
     * 获取某个scheduler的job详情集合
     * @param schedulerName
     * @return
     * @throws SchedulerException
     */
    public List<JobDetail> getAllJobsOfScheduler(String schedulerName) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        List<JobDetail> allJobsOfScheduler = QuartzUtils.getAllJobsOfScheduler(scheduler);
        return allJobsOfScheduler;
    }

    /**
     * 获取所有的job详情集合
     * @return
     * @throws SchedulerException
     */
    public List<JobDetail> getAllJobs() throws SchedulerException {
        List<JobDetail> allJobs = new ArrayList<JobDetail>();
        List<Scheduler> schedulers = this.getSchedulers();
        for (Scheduler scheduler : schedulers) {
            List<JobDetail> allJobsOfScheduler = QuartzUtils.getAllJobsOfScheduler(scheduler);
            allJobs.addAll(allJobsOfScheduler);
        }
        return allJobs;
    }

    /**
     * 获取job的trigger信息
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public List<? extends Trigger> getTriggersOfJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        List<? extends Trigger> triggersOfJob = QuartzUtils.getTriggersOfJob(jobName, jobGroup, scheduler);
        return triggersOfJob;
    }

    /**
     * 获取所有的Trigger
     * @param schedulerName
     * @return
     * @throws SchedulerException
     */
    public List<? extends Trigger> getAllTriggersOfScheduler(String schedulerName) throws SchedulerException {
        List<Trigger> triggersOfScheduler = new ArrayList<Trigger>();
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        List<JobDetail> jobDetails = getAllJobsOfScheduler(schedulerName);
        for (JobDetail jobDetail : jobDetails) {
            List<? extends Trigger> triggersOfJob = QuartzUtils.getTriggersOfJob(jobDetail, scheduler);
            triggersOfScheduler.addAll(triggersOfJob);
        }

        return triggersOfScheduler;
    }

    /**
     * 获取全部trigger
     * @return
     * @throws SchedulerException
     */
    public List<? extends Trigger> getAllTriggers() throws SchedulerException {
        List<Trigger> triggers = new ArrayList<Trigger>();
        List<Scheduler> schedulers = this.getSchedulers();
        for (Scheduler scheduler : schedulers) {
            List<JobDetail> jobDetails = getAllJobsOfScheduler(scheduler.getSchedulerName());
            List<Trigger> triggersOfScheduler = new ArrayList<Trigger>();
            for (JobDetail jobDetail : jobDetails) {
                List<? extends Trigger> triggersOfJob = QuartzUtils.getTriggersOfJob(jobDetail, scheduler);
                triggersOfScheduler.addAll(triggersOfJob);
            }
            triggers.addAll(triggersOfScheduler);
        }
        return triggers;
    }

    public Trigger getTrigger(String schedulerName,String triggerName,String triggerGroup) throws SchedulerException {
        Assert.notEmpty(triggerName, "triggerName can not be empty");
        Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        TriggerKey triggerKey = QuartzUtils.getTriggerKey(triggerName, triggerGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        return trigger;
    }

    public void runTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        Trigger trigger = getTrigger(schedulerName, triggerName, triggerGroup);
        Assert.notNull(trigger, "trigger is not exist");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        scheduler.triggerJob(trigger.getJobKey(),trigger.getJobDataMap());
    }

    /**
     * 启动全部
     * @throws SchedulerException
     */
    public void schedulerStart() throws SchedulerException {
        List<Scheduler> schedulers = this.getSchedulers();
        for (Scheduler scheduler : schedulers) {
            scheduler.start();
        }
    }
    /**
     * 启动
     * @param schedulerName
     * @throws SchedulerException
     */
    public void schedulerStart(String schedulerName) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
    }

    /**
     * 延时启动
     * @param schedulerName
     * @param delayed 延迟秒数
     * @throws SchedulerException
     */
    public void schedulerStart(String schedulerName, int delayed) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!scheduler.isStarted()) {
            if (delayed <= 0) {
                scheduler.start();
            } else {
                scheduler.startDelayed(delayed);
            }
        }
    }

    public void schedulerShutdown(String schedulerName) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void schedulerShutdown(String schedulerName, boolean waitForJobsToComplete) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!scheduler.isShutdown()) {
            List<JobDetail> allJobsOfScheduler = QuartzUtils.getAllJobsOfScheduler(scheduler);
            for (JobDetail jobDetail : allJobsOfScheduler) {
                QuartzUtils.pauseJob(jobDetail, scheduler);
            }
            if (waitForJobsToComplete) {
                scheduler.shutdown(waitForJobsToComplete);
            } else {
                scheduler.shutdown();
            }
        }
    }

    /**
     * 核查job是否存在
     * 存在 true
     * 存在 false
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public boolean existJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        JobDetail jobDetail = QuartzUtils.getJob(scheduler, jobName, jobGroup);
        if (jobDetail == null) {
            return false;
        }
        return true;
    }


    /**
     * 核查触发器是否存在
     * @param schedulerName
     * @param triggerName
     * @param triggerGroup
     * @return
     */
    public boolean existTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        Assert.notEmpty(triggerName, "triggerName can not be empty");
        Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        return QuartzUtils.checkTriggerExists(triggerName, triggerGroup, scheduler);
    }


    /**
     * 添加job
     *
     * @param schedulerName
     * @param jobDetail
     * @return
     * @throws SchedulerException
     */
    public void addJob(String schedulerName, JobDetail jobDetail, boolean replace) throws SchedulerException {
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notNull(jobDetail, "jobDetail can not be null");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        scheduler.addJob(jobDetail, replace);
    }

    /**
     * 添加job
     * @param schedulerName
     * @param jobDetail
     * @return
     * @throws SchedulerException
     */
    public void addJob(String schedulerName, JobDetail jobDetail) throws SchedulerException {
         this.addJob(schedulerName, jobDetail, false);
    }

    public void updateJob(String schedulerName, JobDetail jobDetail) throws SchedulerException {
        if (existJob(schedulerName, jobDetail.getKey().getName(), jobDetail.getKey().getGroup())) {
            //替换
            this.addJob(schedulerName, jobDetail, true);
        }else{
            throw new IllegalArgumentException("job [" + jobDetail.getKey().getName() +
                    ":" + jobDetail.getKey().getGroup() + "] not exist");
        }
    }


    public JobDetail addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String description,
                                               MethodInvoker methodInvoker) throws SchedulerException {
        Assert.notNull(methodInvoker, "methodInvoker can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("methodInvoker", methodInvoker);
        JobDetail jobDetail = JobBuilder.newJob(MethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        addJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail updateMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String description,
                                      MethodInvoker methodInvoker) throws SchedulerException {
        Assert.notNull(methodInvoker, "methodInvoker can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("methodInvoker", methodInvoker);
        JobDetail jobDetail = JobBuilder.newJob(MethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        updateJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail addMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                   Object[] constructorArguments, String jobClassMethodName,
                                   Object[] jobClassMethodArgs, String description) throws SchedulerException {
        Assert.notNull(jobClass, "jobClass can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Assert.notEmpty(jobClassMethodName, "jobClassMethodName can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobClass", jobClass);
        jobDataMap.put("constructorArguments", constructorArguments);
        jobDataMap.put("jobClassMethodName", jobClassMethodName);
        jobDataMap.put("jobClassMethodArgs", jobClassMethodArgs);
        JobDetail jobDetail = JobBuilder.newJob(MethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        addJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail updateMethodInovkeJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                        Object[] constructorArguments, String jobClassMethodName,
                                        Object[] jobClassMethodArgs, String description) throws SchedulerException {
        Assert.notNull(jobClass, "jobClass can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Assert.notEmpty(jobClassMethodName, "jobClassMethodName can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobClass", jobClass);
        jobDataMap.put("constructorArguments", constructorArguments);
        jobDataMap.put("jobClassMethodName", jobClassMethodName);
        jobDataMap.put("jobClassMethodArgs", jobClassMethodArgs);
        JobDetail jobDetail = JobBuilder.newJob(MethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        updateJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String description,
                                                 MethodInvoker methodInvoker) throws SchedulerException {
        Assert.notNull(methodInvoker, "methodInvoker can not be null");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("methodInvoker", methodInvoker);
        JobDetail jobDetail = JobBuilder.newJob(StatefulMethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        addJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail updateStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String description,
                                          MethodInvoker methodInvoker) throws SchedulerException {
        Assert.notNull(methodInvoker, "methodInvoker can not be null");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("methodInvoker", methodInvoker);
        JobDetail jobDetail = JobBuilder.newJob(StatefulMethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        updateJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail addStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                        Object[] constructorArguments, String jobClassMethodName,
                                        Object[] jobClassMethodArgs, String description) throws SchedulerException {
        Assert.notNull(jobClass, "jobClass can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Assert.notEmpty(jobClassMethodName, "jobClassMethodName can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobClass", jobClass);
        jobDataMap.put("constructorArguments", constructorArguments);
        jobDataMap.put("jobClassMethodName", jobClassMethodName);
        jobDataMap.put("jobClassMethodArgs", jobClassMethodArgs);
        JobDetail jobDetail = JobBuilder.newJob(StatefulMethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        addJob(schedulerName, jobDetail);
        return jobDetail;
    }

    public JobDetail updateStatefulMethodJob(String schedulerName, String jobName, String jobGroup, String jobClass,
                                          Object[] constructorArguments, String jobClassMethodName,
                                          Object[] jobClassMethodArgs, String description) throws SchedulerException {
        Assert.notNull(jobClass, "jobClass can not be null");
        Assert.notEmpty(schedulerName, "schedulerName can not be empty");
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Assert.notEmpty(jobClassMethodName, "jobClassMethodName can not be empty");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobClass", jobClass);
        jobDataMap.put("constructorArguments", constructorArguments);
        jobDataMap.put("jobClassMethodName", jobClassMethodName);
        jobDataMap.put("jobClassMethodArgs", jobClassMethodArgs);
        JobDetail jobDetail = JobBuilder.newJob(StatefulMethodInvokeJob.class).withIdentity(jobName, jobGroup)
                .withDescription(description).setJobData(jobDataMap).storeDurably().build();
        updateJob(schedulerName, jobDetail);
        return jobDetail;
    }


    public void addTriggerForJob(String schedulerName, String jobName, String jobGroup, Trigger trigger) throws SchedulerException {
        JobDetail jobDetail = this.getJob(schedulerName, jobName, jobGroup);
        addTriggerForJob(schedulerName, jobDetail, trigger);
    }

    public void addTriggerForJob(String schedulerName, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        scheduler.scheduleJob(jobDetail, trigger);
    }


    public void addTriggerForJob(String schedulerName, Trigger trigger) throws SchedulerException {
        Assert.notNull(trigger, "trigger can not be null");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        scheduler.scheduleJob(trigger);
    }

    /**
     * 更新Trigger
     * @param schedulerName
     * @param trigger
     * @throws SchedulerException
     */
    public void updateTriggerForJob(String schedulerName, Trigger trigger) throws SchedulerException {
        Assert.notNull(trigger, "trigger can not be null");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (existTrigger(schedulerName, QuartzUtils.getTriggerName(trigger), QuartzUtils.getTriggerGroup(trigger))) {
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } else {
            throw new IllegalArgumentException("trigger [" + trigger.getKey().getName() +
                    ":" + trigger.getKey().getGroup() + "] not exist");
        }
    }

    public void pauseJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!existJob(schedulerName, jobName, jobGroup)) {
            throw new IllegalArgumentException("job [" + jobName + ":" + jobGroup + "] not exist");
        }
        QuartzUtils.pauseJob(jobName, jobGroup, scheduler);

    }

    public void resumeJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!existJob(schedulerName, jobName, jobGroup)) {
            throw new IllegalArgumentException("job [" + jobName + ":" + jobGroup + "] not exist");
        }
        QuartzUtils.resumeJob(jobName, jobGroup, scheduler);
    }

    public void removeJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!existJob(schedulerName, jobName, jobGroup)) {
            throw new IllegalArgumentException("job [" + jobName + ":" + jobGroup + "] not exist");
        }
        // 暂停
        QuartzUtils.pauseJob(jobName, jobGroup, scheduler);
        // 移除
        QuartzUtils.removeJob(jobName, jobGroup, scheduler);
    }

    /**
     * 运行job
     * @param schedulerName
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void runJob(String schedulerName, String jobName, String jobGroup) throws SchedulerException {
        Assert.notEmpty(jobName, "jobName can not be empty");
        Assert.notEmpty(jobGroup, "jobGroup can not be empty");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        if (!existJob(schedulerName, jobName, jobGroup)) {
            throw new IllegalArgumentException("job [" + jobName + ":" + jobGroup + "] not exist");
        }
        JobDetail jobDetail = this.getJob(schedulerName, jobName, jobGroup);
        List<? extends Trigger> triggersOfJob = this.getTriggersOfJob(schedulerName, jobName, jobGroup);
        Set<Trigger> triggersSet = new HashSet<Trigger>();
        for (Trigger trigger : triggersOfJob) {
            triggersSet.add(trigger);
        }
        scheduler.scheduleJob(jobDetail, triggersSet, true);
    }


    public Trigger.TriggerState getTriggerState(String schedulerName, Trigger trigger) throws SchedulerException {
        Assert.notNull(trigger, "trigger can not be null");
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        return QuartzUtils.getTriggerState(trigger, scheduler);
    }

    public void pauseTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        Assert.notEmpty(triggerName, "triggerName can not be empty");
        Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
        if (!existTrigger(schedulerName, triggerName, triggerGroup)) {
            throw new IllegalArgumentException("trigger [" + triggerName + ":" + triggerGroup + "] not exist");
        }
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        QuartzUtils.pauseTrigger(triggerName, triggerGroup, scheduler);

    }

    public void resumeTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        Assert.notEmpty(triggerName, "triggerName can not be empty");
        Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
        if (!existTrigger(schedulerName, triggerName, triggerGroup)) {
            throw new IllegalArgumentException("trigger [" + triggerName + ":" + triggerGroup + "] not exist");
        }
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        QuartzUtils.resumeTrigger(triggerName, triggerGroup, scheduler);
    }

    public void removeTrigger(String schedulerName, String triggerName, String triggerGroup) throws SchedulerException {
        Assert.notEmpty(triggerName, "triggerName can not be empty");
        Assert.notEmpty(triggerGroup, "triggerGroup can not be empty");
        if (!existTrigger(schedulerName, triggerName, triggerGroup)) {
            throw new IllegalArgumentException("trigger [" + triggerName + ":" + triggerGroup + "] not exist");
        }
        Scheduler scheduler = this.getAssertScheduler(schedulerName);
        QuartzUtils.removeTrigger(triggerName, triggerGroup, scheduler);
    }

}
