/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.job;

import com.github.quartzweb.utils.ClassUtils;
import com.github.quartzweb.utils.ReflectionUtils;
import com.github.quartzweb.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class ConstructorInstantiator {

    private String className;

    private Class<?> targetClass;

    private Object[] arguments = new Object[0];

    private Constructor<?> constructorObject;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) throws ClassNotFoundException {
        this.className = className;
        if (!StringUtils.isEmpty(className)) {
            this.targetClass = resolveClassName(className);
        }
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = (arguments != null ? arguments : new Object[0]);
    }


    public void prepare() throws ClassNotFoundException, NoSuchMethodException {


        Class<?> targetClass = getTargetClass();

        if (targetClass == null) {
            throw new IllegalArgumentException("Either 'targetClass' or 'targetObject' is required");
        }

        Object[] arguments = getArguments();
        Class<?>[] argTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            argTypes[i] = (arguments[i] != null ? arguments[i].getClass() : Object.class);
        }


        // Try to get the exact method first.

        try {
            this.constructorObject = targetClass.getConstructor(argTypes);
        } catch (NoSuchMethodException ex) {
            // Just rethrow exception if we can't get any match.
            this.constructorObject = findMatchingConstructor();
            if (this.constructorObject == null) {
                throw ex;
            }
        }



    }

    protected Constructor findMatchingConstructor() {

        Object[] arguments = getArguments();
        int argCount = arguments.length;
        Constructor<?>[] candidates = targetClass.getConstructors();
        int minTypeDiffWeight = Integer.MAX_VALUE;
        // 获取匹配的类型
        Constructor matchingConstructor = null;
        for (Constructor candidate : candidates) {

            Class<?>[] paramTypes = candidate.getParameterTypes();
            if (paramTypes.length == argCount) {
                int typeDiffWeight = ClassUtils.getTypeDifferenceWeight(paramTypes, arguments);
                if (typeDiffWeight < minTypeDiffWeight) {
                    minTypeDiffWeight = typeDiffWeight;
                    matchingConstructor = candidate;
                }
            }

        }
        return matchingConstructor;
    }

    protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
        return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
    }

    public Constructor getPreparedConstructor() throws IllegalStateException {
        if (this.constructorObject == null) {
            throw new IllegalStateException("prepare() must be called prior to newInstance() on ConstructorInstantiator");
        }
        return this.constructorObject;
    }

    public Object newInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor preparedConstructor= getPreparedConstructor();
        ReflectionUtils.makeAccessible(preparedConstructor);
        return preparedConstructor.newInstance(getArguments());
    }

    public Object prepareNewInstance() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        prepare();
        return newInstance();
    }

}
