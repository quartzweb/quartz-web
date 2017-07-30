/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.service.JSONResult;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzWebServiceContext {

    private ServiceStrategy serviceStrategy;

    public QuartzWebServiceContext(ServiceStrategy serviceStrategy) {
        this.serviceStrategy = serviceStrategy;
    }

    public JSONResult service(ServiceStrategyURL serviceStrategyURL, ServiceStrategyParameter parameter){
        return this.serviceStrategy.service(serviceStrategyURL, parameter);
    }


}
