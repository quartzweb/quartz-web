/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.service.JSONResult;

/**
 * 服务处理策略接口
 * @author quxiucheng [quxiuchengdev@gmail.com]
 * @param <T> 接口参数
 */
public interface ServiceStrategy<T extends ServiceStrategyParameter> {

    /**
     * 业务处理
     * @param serviceStrategyURL 处理URL类
     * @param parameter 参数
     * @return 返回JSON数据
     */
    public JSONResult service(ServiceStrategyURL serviceStrategyURL, T parameter);


    /**
     * 实例化接口参数
     * @return 接口参数
     */
    public T newServiceStrategyParameterInstance();

}
