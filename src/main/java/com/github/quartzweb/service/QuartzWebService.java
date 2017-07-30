/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service;

import com.github.quartzweb.service.strategy.DefaultServiceStrategyURL;
import com.github.quartzweb.service.strategy.QuartzWebServiceContext;
import com.github.quartzweb.service.strategy.ServiceStrategy;
import com.github.quartzweb.service.strategy.ServiceStrategyParameter;
import com.github.quartzweb.service.strategy.ServiceStrategyURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzWebService {

    /**
     * 日志信息
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzWebService.class);

    private final static QuartzWebService instance = new QuartzWebService();

    public static QuartzWebService getInstance() {
        return instance;
    }

    /**
     * 业务策略工厂
     */
    private ServiceStrategyFactory serviceStrategyFactory = new DefaultServiceStrategyFactory();


    public String service(String url, HttpServletRequest request, HttpServletResponse response) {

        try {
            ServiceStrategy serviceStrategy = serviceStrategyFactory.createStrategy(url);
            ServiceStrategyParameter parameter = serviceStrategy.newServiceStrategyParameterInstance();
            parameter.translate(request);
            ServiceStrategyURL serviceStrategyURL = new DefaultServiceStrategyURL(url);
            QuartzWebServiceContext context = new QuartzWebServiceContext(serviceStrategy);
            return context.service(serviceStrategyURL, parameter).json();
        } catch (Exception e) {
            //e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage()).json();
        }

    }


    /**
     * 获取 业务策略工厂
     * @return serviceStrategyFactory 业务策略工厂
     */
    public ServiceStrategyFactory getServiceStrategyFactory() {
        return this.serviceStrategyFactory;
    }

    /**
     * 设置 业务策略工厂
     * @param serviceStrategyFactory 业务策略工厂
     */
    public void setServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
        this.serviceStrategyFactory = serviceStrategyFactory;
    }
}
