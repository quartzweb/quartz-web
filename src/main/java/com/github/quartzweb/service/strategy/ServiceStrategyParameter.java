/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.exception.UnsupportedTranslateException;

/**
 * 处理规则参数
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public interface ServiceStrategyParameter {

    /**
     * 参数换成Bean
     */
    public void translate(Object object) throws UnsupportedTranslateException;


}
