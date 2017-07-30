/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service;

/**
 * HTTP参数  名称
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public abstract class HttpParameterNameConstants {

    /**
     * 关于Scheduler
     */
    public static class Scheduler {

        /** Scheduler - schedule名称 */
        public static final String NAME = "schedulerName";

        /** Scheduler - 延时秒数 - 单位(秒) */
        public static final String DELAYED = "delayed";

    }


    /**
     * 关于Job操作
     */
    public static class Job {
        /** Job操作 - job名称 */
        public static final String NAME = "jobName";

        /** Job操作 - job分组 */
        public static final String GROUP = "jobGroup";

        /** Job操作 - jobDataMap数据 -key值 */
        public static final String DATA_MAP_KEY_PREFIX = "jobDataMapKey_";

        /** Job操作 - jobDataMap数据 -value值 */
        public static final String DATA_MAP_VALUE_PREFIX = "jobDataMapValue_";

        /** Job操作 - JobClass,*/
        public static final String JOB_CLASS = "jobClass";

        /** Job操作 - Job类型*/
        public static final String JOB_TYPE = "jobType";

        /** Job操作 - job描述 */
        public static final String DESCRIPTION = "description";

        /** Job操作 - JobClass参数名称前缀 */
        public static final String JOB_CLASS_PARAM_TYPE_NAME_PREFIX = "jobClassParamType_";

        /** Job操作 - JobClass参数值前缀 */
        public static final String JOB_CLASS_PARAM_TYPE_VALUE_PREFIX = "jobClassParamTypeValue_";

        /** Job操作 - 执行方法类型 */
        public static final String METHOD_INVOKER_TYPE = "methodInvokerType";

        /** Job操作 - 执行方法名称 */
        public static final String JOB_CLASS_METHOD_NAME = "jobClassMethodName";
        /** Job操作 - 执行method类型前缀*/
        public static final String JOB_CLASS_METHOD_PARAM_TYPE_NAME_PREFIX = "jobClassMethodParamType_";

        /** Job操作 - 执行method参数值前缀 */
        public static final String JOB_CLASS_METHOD_PARAM_TYPE_VALUE_PREFIX = "jobClassMethodParamTypeValue_";

    }


    public static class Trigger {

        /** Trigger操作 - trigger名称 */
        public static final String NAME = "triggerName";


        /** HTTP参数名称 - Trigger操作 - job分组 */
        public static final String GROUP = "triggerGroup";

        /** trigger描述 */
        public static final String DESCRIPTION = "description";

        /** 优先级 */
        public static final String PRIORITY = "priority";

        /** cron表达式 */
        public static final String CRONEXPRESSION = "cronExpression";

        /** trigger 开始时间 */
        public static final String START_DATE = "startDate";

        /** trigger 结束时间 */
        public static final String END_DATE = "endDate";

    }


    public static class Validate{
        /** 对比是否为子类的Class名称 */
        public static final String ASSIGNABLE_CLASS_NAME= "assignableClassName";

        /** Class名称 */
        public static final String CLASS_NAME= "className";

        /** cron表达式 */
        public static final String CRON_EXPRESSION= "cronExpression";
    }


}
