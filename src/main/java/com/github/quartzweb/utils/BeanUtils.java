/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.Enumeration;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class BeanUtils {

    public static <T> T newInstance(Class<T> clazz){
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return newInstance(constructor);
        } catch (NoSuchMethodException e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return null;
    }

    public static <T> T newInstance(Class<T> clazz, Parameter argsAndTypeParam) {
        if (clazz.isInterface()) {
            throw new IllegalArgumentException("class is an interface");
        }
        boolean checkResult = checkArgsAndTypeParam(argsAndTypeParam);
        if (!checkResult) {
            throw new IllegalArgumentException("args mismatch types");
        }
        try {
            Constructor<T> constructor = clazz.getConstructor(argsAndTypeParam.getClassTypes());
            return newInstance(constructor, argsAndTypeParam.getArgs());
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return null;
    }

    public static <T> T newInstance(Constructor<T> ctor, Object... args) {
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(args);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return null;
    }

    public static boolean checkArgsAndTypeParam(Parameter parameter) {
        Object[] args = parameter.getArgs();
        Class[] types = parameter.getClassTypes();
        // 都为空则正确
        if (args.length == 0 && types.length == 0) {
            return true;
        }
        // 构造类型和长度不相等的话返回失败
        if (args.length != types.length) {
            return false;
        }
        // 校验参数类型是否匹配
        for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            Object arg = args[i];
            // 是否为基础类型
            if (BasicTypeUtils.checkBasicType(type)) {
                // 基础类型不相等,系统系统无法拆箱装箱
                if (!BasicTypeUtils.compareBasicType(type, arg.getClass())) {
                    return false;
                }
            }else {
                // 判断类型是否相同
                if (!type.isAssignableFrom(arg.getClass())) {
                    return false;
                }
            }

        }
        return true;
    }

    public interface Parameter{
        /**
         * 获取类型列表
         * @return
         */
        public Class[] getClassTypes();

        /**
         * 获取参数列表
         * @return
         */
        public Object[] getArgs();

    }

    /**
     * 获取所有的构造器
     * @param clazz
     * @return
     */
    public static Constructor[] getConstructors(Class clazz) {
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        return declaredConstructors;
    }

    public static <T> T request2Bean(HttpServletRequest request, Class<T> beanClass) {
        try {
            T bean = beanClass.newInstance();   //实例化随意类型
            Enumeration en = request.getParameterNames();   //获得参数的一个列举
            while (en.hasMoreElements()) {         //遍历列举来获取所有的参数
                String name = (String) en.nextElement();
                String value = request.getParameter(name);
                ReflectionUtils.setField(bean, name, value);   //注意这里导入的是
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);  //如果错误 则向上抛运行时异常
        }
    }


}
