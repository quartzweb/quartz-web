/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

/**
 * 默认策略URL实现类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class DefaultServiceStrategyURL implements ServiceStrategyURL {


    private String URL;

    public DefaultServiceStrategyURL(String URL) {
        this.URL = URL;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
