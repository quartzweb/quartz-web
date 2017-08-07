/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.manager.web.SchedulerInfo;
import com.github.quartzweb.service.JSONResult;
import com.github.quartzweb.service.QuartzWebURL;
import com.github.quartzweb.utils.Assert;
import com.github.quartzweb.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Scheduler 业务处理策略
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class SchedulerServiceStrategy implements ServiceStrategy<SchedulerServiceStrategyParameter> {


    public JSONResult service(ServiceStrategyURL serviceStrategyURL, SchedulerServiceStrategyParameter parameter) {
        String url = serviceStrategyURL.getURL();
        // 列出信息
        if (QuartzWebURL.SchedulerURL.INFO.getURL().equals(url)) {
            return getInfo();
        }
        String schedulerName = parameter.getSchedulerName();
        // 具体操作
        // 启动
        if (QuartzWebURL.SchedulerURL.START.getURL().equals(url)) {
            return start(schedulerName);
        }
        // 延时启动
        if (QuartzWebURL.SchedulerURL.START_DELAYED.getURL().equals(url)) {
            String delayed = parameter.getDelayed();
            return startDelayed(schedulerName, delayed);
        }
        // 关闭
        if (QuartzWebURL.SchedulerURL.SHUTDOWN.getURL().equals(url)) {
            return shutdown(schedulerName);
        }
        // 等待job运行结束后关闭
        if (QuartzWebURL.SchedulerURL.SHUTDOWN_WAIT.getURL().equals(url)) {
            return shutdownWait(schedulerName);
        }

        // 404没有找到url
        return JSONResult.build(JSONResult.RESULT_CODE_ERROR, "404 not found");
    }

    /**
     * 实例化接口参数
     * @return 接口参数
     */
    public SchedulerServiceStrategyParameter newServiceStrategyParameterInstance() {
        return new SchedulerServiceStrategyParameter();
    }

    public JSONResult getInfo(){
        try {
            List<SchedulerInfo> schedulerInfos = QuartzWebManager.getAllSchedulerInfo();
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, schedulerInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult start(String schedulerName){
        try {
            Assert.notEmpty(schedulerName,"schedulerName can not be empty");
            QuartzWebManager.schedulerStart(schedulerName);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult startDelayed(String schedulerName, String delayed) {
        try {
            Assert.notEmpty(schedulerName,"schedulerName can not be empty");
            if (StringUtils.isEmpty(delayed)) {
                delayed = "0";
            }
            Assert.isInteger(delayed,"delayed must be integer");
            QuartzWebManager.schedulerStart(schedulerName, Integer.parseInt(delayed));
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult shutdown(String schedulerName){
        try {
            Assert.notEmpty(schedulerName,"schedulerName can not be empty");
            QuartzWebManager.schedulerShutdown(schedulerName);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

    public JSONResult shutdownWait(String schedulerName){
        try {
            Assert.notEmpty(schedulerName,"schedulerName must not empty");
            QuartzWebManager.schedulerShutdown(schedulerName, true);
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, "ok");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }

}
