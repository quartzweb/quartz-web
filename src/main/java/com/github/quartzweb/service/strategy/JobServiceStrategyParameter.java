/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.exception.UnsupportedTranslateException;
import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.service.HttpParameterNameConstants;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.BasicTypeUtils;
import com.github.quartzweb.utils.ClassUtils;
import com.github.quartzweb.utils.RequestUtils;
import com.github.quartzweb.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JobServiceStrategyParameter implements ServiceStrategyParameter {


    /**
     * SchelerName
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
     * jobClass
     */
    private String jobClass;

    /**
     * job data map
     */
    private Map<String, String> jobDataMap;


    /**
     * 构造函数参数
     */
    private Object[] constructorArguments;
    /**
     * 描述
     */
    private String description;

    /**
     * job类型 继承org.quartz.Job和其他类
     */
    private JobType jobType;

    /**
     * 获取 job执行名称
     * @return jobClassMethodName job执行名称
     */
    public String getJobClassMethodName() {
        return this.jobClassMethodName;
    }

    /**
     * 设置 job执行名称
     * @param jobClassMethodName job执行名称
     */
    public void setJobClassMethodName(String jobClassMethodName) {
        this.jobClassMethodName = jobClassMethodName;
    }

    /**
     * 获取 job执行参数
     * @return jobClassMethodArgs job执行参数
     */
    public Object[] getJobClassMethodArgs() {
        return this.jobClassMethodArgs;
    }

    /**
     * 设置 job执行参数
     * @param jobClassMethodArgs job执行参数
     */
    public void setJobClassMethodArgs(Object[] jobClassMethodArgs) {
        this.jobClassMethodArgs = jobClassMethodArgs;
    }

    enum JobType {

        JOB(1, "继承org.quartz.Job"),

        NOJOB(2, "非继承org.quartz.Job"),;


        JobType(int dictValue, String dictLable) {
            this.dictValue = dictValue;
            this.dictLable = dictLable;
        }

        /**
         * 字典值
         */
        private int dictValue;

        /**
         * 字典类型
         */
        private String dictLable;

        public int getDictValue() {
            return dictValue;
        }

        public void setDictValue(int dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictLable() {
            return dictLable;
        }

        public void setDictLable(String dictLable) {
            this.dictLable = dictLable;
        }

        public static JobType getJobType(String value){
            Assert.equalsAnyOne(value, new String[]{"1", "2"}, "jobType must be 1 or 2");
            if ("1".equals(value)) {
                return JobType.JOB;
            } else {
                return JobType.NOJOB;
            }
        }
    }

    /**
     * job执行名称
     */
    private String jobClassMethodName;

    /**
     * job执行参数
     */
    private Object[] jobClassMethodArgs;

    /**
     * 方法执行类型
     */
    private MethodInvokerType methodInvokerType;

    enum MethodInvokerType {
        NORMAL(1,"无状态"),
        STATEFUL(2, "有状态"),;

        MethodInvokerType(int dictValue, String dictLable) {
            this.dictValue = dictValue;
            this.dictLable = dictLable;
        }
        /**
         * 字典值
         */
        private int dictValue;

        /**
         * 字典类型
         */
        private String dictLable;

        public int getDictValue() {
            return dictValue;
        }

        public void setDictValue(int dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictLable() {
            return dictLable;
        }

        public void setDictLable(String dictLable) {
            this.dictLable = dictLable;
        }

        public static MethodInvokerType getMethodInvokerType(String value){
            Assert.equalsAnyOne(value, new String[]{"1", "2"}, "jobType must be 1 or 2");
            if ("1".equals(value)) {
                return MethodInvokerType.NORMAL;
            } else {
                return MethodInvokerType.STATEFUL;
            }
        }
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
     * 获取 SchelerName
     * @return schedulerName SchelerName
     */
    public String getSchedulerName() {
        return this.schedulerName;
    }

    /**
     * 设置 SchelerName
     * @param schedulerName SchelerName
     */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /**
     * 获取 jobName
     * @return jobName jobName
     */
    public String getJobName() {
        return this.jobName;
    }

    /**
     * 设置 jobName
     * @param jobName jobName
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取 jobGroup
     * @return jobGroup jobGroup
     */
    public String getJobGroup() {
        return this.jobGroup;
    }

    /**
     * 设置 jobGroup
     * @param jobGroup jobGroup
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取 jobClass
     * @return jobClass jobClass
     */
    public String getJobClass() {
        return this.jobClass;
    }

    /**
     * 设置 jobClass
     * @param jobClass jobClass
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    /**
     * 获取 jobdatamap
     * @return jobDataMap jobdatamap
     */
    public Map<String, String> getJobDataMap() {
        return this.jobDataMap;
    }

    /**
     * 设置 jobdatamap
     * @param jobDataMap jobdatamap
     */
    public void setJobDataMap(Map<String, String> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }


    /**
     * 获取 构造函数参数
     * @return QquartzBeanParameter 构造函数参数
     */
    public Object[] getConstructorArguments() {
        return constructorArguments;
    }

    /**
     * 设置 构造函数参数
     * @param constructorArguments 构造函数参数
     */
    public void setConstructorArguments(Object[] constructorArguments) {
        this.constructorArguments = constructorArguments;
    }

    /**
     * 获取 job类型
     * @return job类型
     */
    public JobType getJobType() {
        return jobType;
    }

    /**
     * 设置 job类型 继承org.quartz.Job和其他类
     * @param jobType job类型 继承org.quartz.Job和其他类
     */
    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public MethodInvokerType getMethodInvokerType() {
        return methodInvokerType;
    }

    public void setMethodInvokerType(MethodInvokerType methodInvokerType) {
        this.methodInvokerType = methodInvokerType;
    }


    /**
     * 转换实体
     * @param object 将要转换的类
     * @throws UnsupportedTranslateException 转换报错处理
     */
    @Override
    public void translate(Object object) throws UnsupportedTranslateException {
        if (object instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) object;
            String schedulerName = request.getParameter(HttpParameterNameConstants.Scheduler.NAME);
            String jobName = request.getParameter(HttpParameterNameConstants.Job.NAME);
            String jobGroup = request.getParameter(HttpParameterNameConstants.Job.GROUP);
            String jobClass = request.getParameter(HttpParameterNameConstants.Job.JOB_CLASS);
            String description = request.getParameter(HttpParameterNameConstants.Job.DESCRIPTION);
            String jobType = request.getParameter(HttpParameterNameConstants.Job.JOB_TYPE);
            String jobClassMethodName = request.getParameter(HttpParameterNameConstants.Job.JOB_CLASS_METHOD_NAME);
            String methodInvokerType = request.getParameter(HttpParameterNameConstants.Job.METHOD_INVOKER_TYPE);

            // 获取jobDataMap
            Map<String, String> mapData = RequestUtils.getMapData(request,
                    HttpParameterNameConstants.Job.DATA_MAP_KEY_PREFIX,
                    HttpParameterNameConstants.Job.DATA_MAP_VALUE_PREFIX);

            if (mapData.size() > 0) {
                this.setJobDataMap(mapData);
            }

            /********************构造函数设置 start*******************/
            List<Class> classList = new ArrayList<Class>();
            List<Object> argList = new ArrayList<Object>();

            RequestUtils.getClassTypesAndArgs(request, HttpParameterNameConstants.Job.JOB_CLASS_PARAM_TYPE_NAME_PREFIX,
                    HttpParameterNameConstants.Job.JOB_CLASS_PARAM_TYPE_VALUE_PREFIX, classList, argList);


            Class[] classTypes = new Class[classList.size()];
            Object[] args = new Object[argList.size()];
            classList.toArray(classTypes);
            argList.toArray(args);
            /********************构造函数设置 end*******************/


            /********************执行方法设置 start*******************/
            List<Class> methodClassTypeList = new ArrayList<Class>();
            List<Object> methodArgList = new ArrayList<Object>();
            RequestUtils.getClassTypesAndArgs(request, HttpParameterNameConstants.Job.JOB_CLASS_METHOD_PARAM_TYPE_NAME_PREFIX,
                    HttpParameterNameConstants.Job.JOB_CLASS_METHOD_PARAM_TYPE_VALUE_PREFIX, methodClassTypeList, methodArgList);

            Class[] methodClassTypes = new Class[methodClassTypeList.size()];
            Object[] methodArgs = new Object[methodClassTypeList.size()];
            methodClassTypeList.toArray(methodClassTypes);
            methodArgList.toArray(methodArgs);
            /********************执行方法设置 end*******************/

            this.setConstructorArguments(args);
            this.setSchedulerName(schedulerName);
            this.setJobName(jobName);
            this.setJobGroup(jobGroup);
            this.setJobClass(jobClass);
            this.setJobDataMap(jobDataMap);
            this.setDescription(description);
            if (!StringUtils.isEmpty(jobClassMethodName)) {
                if (jobClassMethodName.contains("#")) {
                    jobClassMethodName = jobClassMethodName.substring(0, jobClassMethodName.length() - 1);
                }
            }
            this.setJobClassMethodName(jobClassMethodName);
            this.setJobClassMethodArgs(methodArgs);
            if (!StringUtils.isEmpty(jobType)) {
                this.setJobType(JobType.getJobType(jobType));
                this.setMethodInvokerType(MethodInvokerType.getMethodInvokerType(methodInvokerType));
            }
        } else {
            throw new UnsupportedTranslateException(object.getClass().getName() + " translate exception");
        }

    }


}
