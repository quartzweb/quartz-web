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
import com.github.quartzweb.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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
            Map<String, String> jobDataMap = null;
            // 获取jobDataMap
            Map<String, String[]> dataMap = request.getParameterMap();

            for (Map.Entry<String, String[]> dataEntry : dataMap.entrySet()) {
                String key = dataEntry.getKey();
                // 是否为构造函数参数
                if (key.contains(HttpParameterNameConstants.Job.DATA_MAP_KEY_PREFIX)) {

                    String[] dataMapKeyInfo = key.split("_");

                    //参数名称是否正确
                    if (dataMapKeyInfo.length != 2) {
                        throw new UnsupportedTranslateException(" jobDataMap format exception");
                    }
                    //序号
                    String index = dataMapKeyInfo[1];

                    if (jobDataMap == null) {
                        jobDataMap = new LinkedHashMap<String, String>();
                    }
                    jobDataMap.put(request.getParameter(HttpParameterNameConstants.Job.DATA_MAP_KEY_PREFIX  + index),
                            request.getParameter(HttpParameterNameConstants.Job.DATA_MAP_VALUE_PREFIX + index));
                }
            }

            this.setJobDataMap(jobDataMap);

            /********************构造函数设置 start*******************/
            // 类型
            Map<String, String> jobClassParamType = new LinkedHashMap<String, String>();
            Map<String, String[]> jobParameterMap = request.getParameterMap();
            // 生成构造函数类型
            for (Map.Entry<String, String[]> parameterEntry : jobParameterMap.entrySet()) {
                String key = parameterEntry.getKey();
                // 是否为构造函数参数
                if (key.contains(HttpParameterNameConstants.Job.JOB_CLASS_PARAM_TYPE_NAME_PREFIX)) {
                    jobClassParamType.put(key, request.getParameter(key));
                }
            }
            Class[] classTypes = new Class[jobClassParamType.size()];
            Object[] args = new Object[jobClassParamType.size()];
            for (Map.Entry<String, String> jobClassParamTypeEntry : jobClassParamType.entrySet()) {
                // 获取参数名称_index
                String key = jobClassParamTypeEntry.getKey();
                String paramClassName = jobClassParamTypeEntry.getValue();
                String[] jobClassParamTypeInfo = key.split("_");

                //参数名称是否正确
                if (jobClassParamTypeInfo.length != 2) {
                    throw new UnsupportedTranslateException(" jobClass format exception");
                }
                //序号
                String paramIndex = jobClassParamTypeInfo[1];
                // 校验序号
                if (!StringUtils.isIntegerGTNumber(paramIndex, -1)) {
                    throw new UnsupportedTranslateException(" jobClass format exception");
                }
                // 校验class是否合法
                // 不是基础类型
                if (!BasicTypeUtils.checkBasicType(paramClassName)) {
                    // 类型不存在
                    if (!QuartzWebManager.checkClass(paramClassName)) {
                        throw new UnsupportedTranslateException("jobClass no class found [" + paramClassName + "] exception");
                    }
                }

                //获取class的实例对象值
                String paramClassValue = request.getParameter(HttpParameterNameConstants.Job.JOB_CLASS_PARAM_TYPE_VALUE_PREFIX + paramIndex);
                try {
                    Integer index = Integer.parseInt(paramIndex);
                    if (BasicTypeUtils.checkBasicType(paramClassName)||BasicTypeUtils.checkBasicTypeObj(paramClassName)) {
                        classTypes[index] = BasicTypeUtils.getClass(paramClassName);
                    } else {
                        classTypes[index] = QuartzWebManager.getBean(paramClassName).getClass();
                    }
                    if (classTypes[index].isAssignableFrom(Integer.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Integer.valueOf(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(int.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Integer.parseInt(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(Double.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Double.valueOf(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(double.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Double.parseDouble(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(Float.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Float.valueOf(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(float.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        args[index] = Float.parseFloat(paramClassValue);
                    } else if (classTypes[index].isAssignableFrom(Date.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            args[index] = null;
                        }else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = sdf.parse(paramClassValue);
                            args[index] = date;
                        }

                    } else if (classTypes[index].isAssignableFrom(String.class)) {
                        args[index] = paramClassValue;
                    } else {
                        if (!StringUtils.isEmpty(paramClassValue)) {
                            boolean sourceCheck = QuartzWebManager.checkClass(paramClassName);
                            boolean targetCheck = QuartzWebManager.checkClass(paramClassValue);
                            if (sourceCheck && targetCheck) {
                                Object source = QuartzWebManager.getBean(paramClassName);
                                Object target = QuartzWebManager.getBean(paramClassValue);
                                boolean assignableFrom = ClassUtils.isAssignableFrom(source, target);
                                if (assignableFrom) {
                                    args[index] = QuartzWebManager.getBean(paramClassValue);
                                }
                            }
                            throw new UnsupportedTranslateException("jobClass class not found or class cast exception");

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UnsupportedTranslateException("jobClass class not found or class cast exception");
                }
            }
            /********************构造函数设置 end*******************/

            /********************执行方法设置 start*******************/
            // 类型
            Map<String, String> jobClassMethodParamType = new LinkedHashMap<String, String>();
            Map<String, String[]> jobMethodParameterMap = request.getParameterMap();
            // 生成构造函数类型
            for (Map.Entry<String, String[]> parameterEntry : jobMethodParameterMap.entrySet()) {
                String key = parameterEntry.getKey();
                // 是否为构造函数参数
                if (key.contains(HttpParameterNameConstants.Job.JOB_CLASS_METHOD_PARAM_TYPE_NAME_PREFIX)) {
                    jobClassMethodParamType.put(key, request.getParameter(key));
                }
            }
            Class[] methodClassTypes = new Class[jobClassMethodParamType.size()];
            Object[] methodArgs = new Object[jobClassMethodParamType.size()];
            for (Map.Entry<String, String> jobClassMethodParamTypeEntry : jobClassMethodParamType.entrySet()) {
                // 获取参数名称_index
                String key = jobClassMethodParamTypeEntry.getKey();
                String paramClassName = jobClassMethodParamTypeEntry.getValue();
                String[] jobClassMethodParamTypeInfo = key.split("_");

                //参数名称是否正确
                if (jobClassMethodParamTypeInfo.length != 2) {
                    throw new UnsupportedTranslateException(" jobClass method format exception");
                }
                //序号
                String paramIndex = jobClassMethodParamTypeInfo[1];
                // 校验序号
                if (!StringUtils.isIntegerGTNumber(paramIndex, -1)) {
                    throw new UnsupportedTranslateException(" jobClass method format exception");
                }
                // 校验class是否合法
                // 不是基础类型
                if (!BasicTypeUtils.checkBasicType(paramClassName)) {
                    // 类型不存在
                    if (!QuartzWebManager.checkClass(paramClassName)) {
                        throw new UnsupportedTranslateException("jobClass method no class found [" + paramClassName + "] exception");
                    }
                }

                //获取class的实例对象值
                String paramClassValue = request.getParameter(HttpParameterNameConstants.Job.JOB_CLASS_METHOD_PARAM_TYPE_VALUE_PREFIX + paramIndex);
                try {
                    Integer index = Integer.parseInt(paramIndex);
                    if (BasicTypeUtils.checkBasicType(paramClassName)||BasicTypeUtils.checkBasicTypeObj(paramClassName)) {
                        methodClassTypes[index] = BasicTypeUtils.getClass(paramClassName);
                    } else {
                        methodClassTypes[index] = QuartzWebManager.getBean(paramClassName).getClass();
                    }
                    if (methodClassTypes[index].isAssignableFrom(Integer.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Integer.valueOf(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(int.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Integer.parseInt(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(Double.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Double.valueOf(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(double.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Double.parseDouble(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(Float.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Float.valueOf(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(float.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            paramClassValue = "0";
                        }
                        methodArgs[index] = Float.parseFloat(paramClassValue);
                    } else if (methodClassTypes[index].isAssignableFrom(Date.class)) {
                        if (StringUtils.isEmpty(paramClassValue)) {
                            methodArgs[index] = null;

                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = sdf.parse(paramClassValue);
                            methodArgs[index] = date;
                        }

                    } else if (methodClassTypes[index].isAssignableFrom(String.class)) {
                        methodArgs[index] = paramClassValue;
                    } else {
                        if (!StringUtils.isEmpty(paramClassValue)) {
                            boolean sourceCheck = QuartzWebManager.checkClass(paramClassName);
                            boolean targetCheck = QuartzWebManager.checkClass(paramClassValue);
                            if (sourceCheck && targetCheck) {
                                Object source = QuartzWebManager.getBean(paramClassName);
                                Object target = QuartzWebManager.getBean(paramClassValue);
                                boolean assignableFrom = ClassUtils.isAssignableFrom(source, target);
                                if (assignableFrom) {
                                    methodArgs[index] = QuartzWebManager.getBean(paramClassValue);
                                }
                            }
                            throw new UnsupportedTranslateException("jobClass method class not found or class cast exception");

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UnsupportedTranslateException("jobClass class not found or class cast exception");
                }
            }
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
