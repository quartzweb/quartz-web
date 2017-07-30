/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service;


import com.github.quartzweb.service.strategy.BasicServiceStrategy;
import com.github.quartzweb.service.strategy.JobServiceStrategy;
import com.github.quartzweb.service.strategy.SchedulerServiceStrategy;
import com.github.quartzweb.service.strategy.ServiceStrategy;
import com.github.quartzweb.service.strategy.TriggerServiceStrategy;
import com.github.quartzweb.service.strategy.ValidateServiceStrategy;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class DefaultServiceStrategyFactory implements ServiceStrategyFactory{

    public ServiceStrategy createStrategy(String url) {
        // 基础信息
        if (QuartzWebURL.BasicURL.lookup(url)) {
            return new BasicServiceStrategy();
        }
        // Scheduler操作
        if (QuartzWebURL.SchedulerURL.lookup(url)) {
            return new SchedulerServiceStrategy();
        }
        // Job操作
        if (QuartzWebURL.JobURL.lookup(url)) {
            return new JobServiceStrategy();
        }
        if (QuartzWebURL.TriggerURL.lookup(url)) {
            return new TriggerServiceStrategy();
        }
        // 校验操作
        if (QuartzWebURL.ValidateURL.lookup(url)) {
            return new ValidateServiceStrategy();
        }

        return null;
    }


}
