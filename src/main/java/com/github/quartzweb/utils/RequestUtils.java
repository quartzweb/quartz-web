/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import com.github.quartzweb.exception.UnsupportedTranslateException;
import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.service.HttpParameterNameConstants;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * request操作工具类
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class RequestUtils {

    /**
     * 获取reuqest中的map数据
     * 数据结构 mapKeyPrefix_index,value
     *
     * @param request      request请求
     * @param mapKeyPrefix key前缀
     * @return map数据
     */
    public static Map<String, String> getMapData(HttpServletRequest request, String mapKeyPrefix,String mapValuePrefix) {

        Map<String,String> mapData = new LinkedHashMap<String, String>();
        Map<String, String[]> requestParameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> dataEntry : requestParameterMap.entrySet()) {
            String key = dataEntry.getKey();
            // 是否为构造函数参数
            if (key.startsWith(mapKeyPrefix)) {

                String[] dataMapKeyInfo = key.split("_");

                //参数名称是否正确
                if (dataMapKeyInfo.length != 2) {
                    throw new UnsupportedTranslateException("resolve request map data format exception");
                }
                //序号
                String index = dataMapKeyInfo[1];
                if (!StringUtils.isIntegerGTNumber(index, -1)) {
                    throw new UnsupportedTranslateException("resolve request map data format exception");
                }

                mapData.put(request.getParameter(mapKeyPrefix + index), request.getParameter(mapValuePrefix + index));
            }
        }
        return mapData;
    }

    /**
     * 将request转换成所需要的map类型
     *
     * @param request request实体类
     * @param classTypePrefix 类型前缀
     * @param argsPrefix 参数前缀
     * @param classList class类型集合
     * @param argList 参数集合
     */
    public static void getClassTypesAndArgs(HttpServletRequest request, String classTypePrefix, String argsPrefix,
                                            List<Class> classList,List<Object> argList) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, String> jobClassParamType = new LinkedHashMap<String, String>();
        // 获取class类型的Map
        for (Map.Entry<String, String[]> parameterEntry : requestParameterMap.entrySet()) {
            String key = parameterEntry.getKey();
            if (key.startsWith(classTypePrefix)) {
                jobClassParamType.put(key, request.getParameter(key));
            }
        }

        Class[] classTypes = new Class[jobClassParamType.size()];
        Object[] args = new Object[jobClassParamType.size()];
        for (Map.Entry<String, String> jobClassParamTypeEntry : jobClassParamType.entrySet()) {
            // 获取参数名称_index
            String key = jobClassParamTypeEntry.getKey();
            String paramClassName = jobClassParamTypeEntry.getValue();
            String[] jobClassParamTypeInfo = key.split("_");

            //参数名称是否正确
            if (jobClassParamTypeInfo.length != 2) {
                throw new UnsupportedTranslateException("resolve request class format exception");
            }
            //序号
            String paramIndex = jobClassParamTypeInfo[1];
            // 校验序号
            if (!StringUtils.isIntegerGTNumber(paramIndex, -1)) {
                throw new UnsupportedTranslateException("resolve request class format exception");
            }
            // 校验class是否合法
            // 不是基础类型
            if (!BasicTypeUtils.checkBasicType(paramClassName)) {
                // 类型不存在
                if (!QuartzWebManager.checkClass(paramClassName)) {
                    throw new UnsupportedTranslateException("class no class found [" + paramClassName + "] exception");
                }
            }

            //获取class的实例对象值
            String paramClassValue = request.getParameter(argsPrefix + paramIndex);
            try {
                Integer index = Integer.parseInt(paramIndex);
                if (index > jobClassParamType.size()) {
                    throw new UnsupportedTranslateException("resolve request class format exception");
                }
                if (BasicTypeUtils.checkBasicType(paramClassName)||BasicTypeUtils.checkBasicTypeObj(paramClassName)) {
                    classTypes[index] = BasicTypeUtils.getClass(paramClassName);
                } else {
                    classTypes[index] = QuartzWebManager.getBean(paramClassName).getClass();
                }
                if (classTypes[index].isAssignableFrom(Integer.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Integer.valueOf(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(int.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Integer.parseInt(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(Double.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Double.valueOf(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(double.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Double.parseDouble(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(Float.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Float.valueOf(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(float.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        paramClassValue = "0";
                    }
                    args[index] = Float.parseFloat(paramClassValue);
                } else if (classTypes[index].isAssignableFrom(Date.class)) {
                    if (StringUtils.isEmpty(paramClassValue)) {
                        args[index] = null;
                    }else{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(paramClassValue);
                        args[index] = date;
                    }

                } else if (classTypes[index].isAssignableFrom(String.class)) {
                    args[index] = paramClassValue;
                } else {
                    if (!StringUtils.isEmpty(paramClassValue)) {
                        boolean sourceCheck = QuartzWebManager.checkClass(paramClassName);
                        boolean targetCheck = QuartzWebManager.checkClass(paramClassValue);
                        if (sourceCheck && targetCheck) {
                            Object source = QuartzWebManager.getBean(paramClassName);
                            Object target = QuartzWebManager.getBean(paramClassValue);
                            boolean assignableFrom = ClassUtils.isAssignableFrom(source, target);
                            if (assignableFrom) {
                                args[index] = QuartzWebManager.getBean(paramClassValue);
                            }
                        }
                        throw new UnsupportedTranslateException("class not found or class cast exception");

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new UnsupportedTranslateException("class not found or class cast exception");
            }
        }
        Collections.addAll(classList, classTypes);
        Collections.addAll(argList, args);
    }



}
