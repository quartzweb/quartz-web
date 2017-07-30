/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.manager.bean;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com].
 */
public abstract class AbstractQuartzBeanManager implements QuartzBeanManager {

    private int priority = 1;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
