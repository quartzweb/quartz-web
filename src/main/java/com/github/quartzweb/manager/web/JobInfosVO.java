/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.web;

import java.util.HashMap;
import java.util.List;

/**
 * 作业信息封装类 VO
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JobInfosVO extends HashMap<String, List<JobInfo>> {


    /**
     * 获取 jobInfo实体类
     *
     * @return jobInfos jobInfo实体类
     */
    public List<JobInfo> getJobInfos(String schedulerName) {
        return this.get(schedulerName);
    }

    /**
     * 设置 jobInfo实体类
     *
     * @param jobInfos jobInfo实体类
     */
    public void addJobInfos(String schedulerName, List<JobInfo> jobInfos) {
        this.put(schedulerName, jobInfos);
    }
}
