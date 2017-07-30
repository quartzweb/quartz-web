package com.github.quartzweb.service;

import com.github.quartzweb.service.strategy.ServiceStrategy;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public interface ServiceStrategyFactory {

    /**
     * 创建策略
     * @param url
     * @return
     */
    public ServiceStrategy createStrategy(String url);

}
