/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import java.util.Date;
import java.util.HashMap;

/**
 * 基础信息封装类
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class BasicInfo extends HashMap<String, Object> {

    /**
     * quartz-web版本信息
     */
    private String quartzWebVersion;
    /**
     * quartz主版本
     */
    private String versionMajor;
    /**
     * quartz次版本
     */
    private String versionMinor;
    /**
     * quartz增量版本
     */
    private String versionIteration;
    /**
     * quartz整体版本
     */
    private String quartzVersion;
    /**
     * jvm启动时间
     */
    private Date javaVMStartTime;
    /**
     * jvm名称
     */
    private String javaVMName;
    /**
     * jvm版本
     */
    private String javaVersion;
    /**
     * classPath信息
     */
    private String javaClassPath;


    /**
     * 获取 quartz-web版本信息
     * @return quartzWebVersion quartz-web版本信息
     */
    public String getQuartzWebVersion() {
        return this.quartzWebVersion;
    }

    /**
     * 设置 quartz-web版本信息
     * @param quartzWebVersion quartz-web版本信息
     */
    public void setQuartzWebVersion(String quartzWebVersion) {
        this.quartzWebVersion = quartzWebVersion;
        this.put("quartzWebVersion", quartzWebVersion);
    }

    /**
     * 获取 quartz主版本
     * @return versionMajor quartz主版本
     */
    public String getVersionMajor() {
        return this.versionMajor;
    }

    /**
     * 设置 quartz主版本
     * @param versionMajor quartz主版本
     */
    public void setVersionMajor(String versionMajor) {
        this.versionMajor = versionMajor;
        this.put("versionMajor", versionMajor);
    }

    /**
     * 获取 quartz次版本
     * @return versionMinor quartz次版本
     */
    public String getVersionMinor() {
        return this.versionMinor;
    }

    /**
     * 设置 quartz次版本
     * @param versionMinor quartz次版本
     */
    public void setVersionMinor(String versionMinor) {
        this.versionMinor = versionMinor;
        this.put("versionMinor", versionMinor);
    }

    /**
     * 获取 quartz增量版本
     * @return versionIteration quartz增量版本
     */
    public String getVersionIteration() {
        return this.versionIteration;
    }

    /**
     * 设置 quartz增量版本
     * @param versionIteration quartz增量版本
     */
    public void setVersionIteration(String versionIteration) {
        this.versionIteration = versionIteration;
        this.put("versionIteration", versionIteration);
    }

    /**
     * 获取 quartz整体版本
     * @return quartzVersion quartz整体版本
     */
    public String getQuartzVersion() {
        return this.quartzVersion;
    }

    /**
     * 设置 quartz整体版本
     * @param quartzVersion quartz整体版本
     */
    public void setQuartzVersion(String quartzVersion) {
        this.quartzVersion = quartzVersion;
        this.put("quartzVersion", quartzVersion);
    }

    /**
     * 获取 jvm启动时间
     * @return javaVMStartTime jvm启动时间
     */
    public Date getJavaVMStartTime() {
        return this.javaVMStartTime;
    }

    /**
     * 设置 jvm启动时间
     * @param javaVMStartTime jvm启动时间
     */
    public void setJavaVMStartTime(Date javaVMStartTime) {
        this.javaVMStartTime = javaVMStartTime;
        this.put("javaVMStartTime", javaVMStartTime);
    }

    /**
     * 获取 jvm名称
     * @return javaVMName jvm名称
     */
    public String getJavaVMName() {
        return this.javaVMName;
    }

    /**
     * 设置 jvm名称
     * @param javaVMName jvm名称
     */
    public void setJavaVMName(String javaVMName) {
        this.javaVMName = javaVMName;
        this.put("javaVMName", javaVMName);
    }

    /**
     * 获取 jvm版本
     * @return javaVersion jvm版本
     */
    public String getJavaVersion() {
        return this.javaVersion;
    }

    /**
     * 设置 jvm版本
     * @param javaVersion jvm版本
     */
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
        this.put("javaVersion", javaVersion);
    }

    /**
     * 获取 classPath信息
     * @return javaClassPath classPath信息
     */
    public String getJavaClassPath() {
        return this.javaClassPath;
    }

    /**
     * 设置 classPath信息
     * @param javaClassPath classPath信息
     */
    public void setJavaClassPath(String javaClassPath) {
        this.javaClassPath = javaClassPath;
        this.put("javaClassPath", javaClassPath);
    }

}
