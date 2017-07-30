/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

/**
 * Properties文件加载工具类
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class PropertiesLoaderUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesLoaderUtils.class);

    /**
     * 加载properties文件,只加载文件结尾.properties的文件
     * @param resourcesPaths 文件路径(文件/路径)
     * @return Properties对象实例
     */
    public static Properties loadProperties(String... resourcesPaths){
        Properties props = new Properties();
        for (String location : resourcesPaths) {
            logger.debug("Loading properties file from:" + location);
            InputStream is = null;
            try {
                ClassLoader classLoader = PropertiesLoaderUtils.class.getClassLoader();
                URL resource = classLoader.getResource(location);
                if (resource != null) {
                    String path = resource.getPath();
                    File resourceFile = new File(path);
                    if (resourceFile.isDirectory()) {
                        File[] files = resourceFile.listFiles();
                        if (files != null) {
                            for (File propFile : files) {
                                if(!propFile.getName().endsWith(".properties")){
                                    continue;
                                }
                                Properties propsTemp = new Properties();
                                logger.debug("【加载配置文件】" + propFile.getName());
                                is = classLoader.getResourceAsStream(location+"/"+propFile.getName());
                                propsTemp.load(is);
                                Set<Object> keySet = propsTemp.keySet();
                                for (Object key : keySet) {
                                    Object value = propsTemp.get(key);
                                    props.put(key, value);
                                }
                            }
                        }
                    } else {
                        is = classLoader.getResourceAsStream(location);
                        props.load(is);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Could not load properties from path:" + location + ", " + ex.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ignored) {
                    // ignore
                }
            }

        }
        return props;
    }




}
