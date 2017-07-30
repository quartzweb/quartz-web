/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class SpringQuartzBeanManager extends AbstractQuartzBeanManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public Object getBean(String name) throws Exception {
        return applicationContext.getBean(name);
    }

    public Object getBean(String name, Object[] args) throws Exception {
        return applicationContext.getBean(name, args);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
