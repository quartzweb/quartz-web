/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.bean;

import com.github.quartzweb.exception.NoSuchBeanDefinitionException;
import com.github.quartzweb.utils.ReflectionUtils;
import com.github.quartzweb.utils.SortListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzBeanManagerFacade {

    private final static QuartzBeanManagerFacade instance = new QuartzBeanManagerFacade();

    private List<QuartzBeanManager> quartzBeanManagers = new ArrayList<QuartzBeanManager>();

    private QuartzBeanManager defaultQuartzBeanManager = new ConstructorQuartzBeanManager();

    /**
     * 是否使用默认QuartzBeanManager
     */
    private boolean useDefaultQuartzBeanManager = true;

    private QuartzBeanManagerFacade() {
    }

    /**
     * 获取 是否使用默认QuartzBeanManager
     * @return useDefaultQuartzBeanManager 是否使用默认QuartzBeanManager
     */
    public boolean isUseDefaultQuartzBeanManager() {
        return this.useDefaultQuartzBeanManager;
    }

    /**
     * 设置 是否使用默认QuartzBeanManager
     * @param useDefaultQuartzBeanManager 是否使用默认QuartzBeanManager
     */
    public void setUseDefaultQuartzBeanManager(boolean useDefaultQuartzBeanManager) {
        this.useDefaultQuartzBeanManager = useDefaultQuartzBeanManager;
    }

    public static QuartzBeanManagerFacade getInstance() {
        return instance;
    }


    public Object getBean(String name) {
        for (QuartzBeanManager quartzBeanManager : quartzBeanManagers) {
            try {
                Object bean = quartzBeanManager.getBean(name);
                if (bean != null) {
                    return bean;
                }
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }

        }
        if (useDefaultQuartzBeanManager) {
            try {
                Object bean = defaultQuartzBeanManager.getBean(name);
                if (bean != null) {
                    return bean;
                }
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
        }

        throw new NoSuchBeanDefinitionException("No bean named '" + name + "' is defined");
    }

    public Object getBean(String name, Object[] args) {
        for (QuartzBeanManager quartzBeanManager : quartzBeanManagers) {
            try {
                Object bean = quartzBeanManager.getBean(name, args);
                if (bean != null) {
                    return bean;
                }
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }

        }
        if (useDefaultQuartzBeanManager) {
            try {
                Object bean = defaultQuartzBeanManager.getBean(name, args);
                if (bean != null) {
                    return bean;
                }
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
        }

        throw new NoSuchBeanDefinitionException("No bean named '" + name + "' is defined");
    }

    public List<QuartzBeanManager> getQuartzBeanManagers() {
        return quartzBeanManagers;
    }

    public void setQuartzBeanManagers(List<QuartzBeanManager> quartzBeanManagers) {
        this.quartzBeanManagers = quartzBeanManagers;
        SortListUtil.sort(this.quartzBeanManagers, "priority", SortListUtil.DESC);
    }

    public QuartzBeanManager getDefaultQuartzBeanManager() {
        return defaultQuartzBeanManager;
    }

    public void setDefaultQuartzBeanManager(QuartzBeanManager defaultQuartzBeanManager) {
        this.defaultQuartzBeanManager = defaultQuartzBeanManager;
    }

    public List<QuartzBeanManager> getQuartzBeanManagerAll() {
        ArrayList<QuartzBeanManager> all = new ArrayList<QuartzBeanManager>();
        all.addAll(quartzBeanManagers);
        all.add(defaultQuartzBeanManager);
        return all;
    }

}

