/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.bean;

/**
 * @author quxiucheng [quxiucheng@gmail.com]
 */
public interface QuartzBeanManager {

    /**
     * 根据名称获取实体类
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception;

    /**
     * 根据名称和参数获取实体类
     * @param name
     * @param args
     * @return
     * @throws Exception
     */
    public Object getBean(String name, Object[] args) throws Exception;

    /**
     * 设置优先级
     * @return
     */
    public int getPriority();


}
