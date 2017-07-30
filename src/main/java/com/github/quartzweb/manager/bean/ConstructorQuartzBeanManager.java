/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.bean;


import com.github.quartzweb.job.ConstructorInstantiator;
import com.github.quartzweb.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 构造函数获取实体类bean
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class ConstructorQuartzBeanManager extends AbstractQuartzBeanManager {



    public Object getBean(String name) throws ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = ClassUtils.forName(name, ClassUtils.getDefaultClassLoader());
        // 判断是否有无参数构造函数
        if (ClassUtils.hasNonParameterConstructor(clazz)) {
            ConstructorInstantiator instantiator = new ConstructorInstantiator();
            instantiator.setClassName(name);
            return instantiator.prepareNewInstance();
        }
        return null;
    }


    public Object getBean(String name, Object[] args) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ConstructorInstantiator instantiator = new ConstructorInstantiator();
        instantiator.setClassName(name);
        instantiator.setArguments(args);
        return instantiator.prepareNewInstance();
    }


}
