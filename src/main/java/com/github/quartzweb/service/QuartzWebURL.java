/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service;

import com.github.quartzweb.service.strategy.ServiceStrategyURL;
import com.github.quartzweb.utils.StringUtils;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzWebURL {

    public enum BasicURL implements ServiceStrategyURL {
        BASIC("/api/basic.json");

        private String url;

        BasicURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

        public static boolean lookup(String url) {
            if(StringUtils.isEmpty(url)){
                return false;
            }
            BasicURL[] basicURLs = BasicURL.values();
            for (BasicURL basicURL : basicURLs) {
                if (url.startsWith(basicURL.getURL())) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum SchedulerURL implements ServiceStrategyURL {

        INFO("/api/schedulerInfo.json"),
        START("/api/schedulerStart.json"),
        START_DELAYED("/api/schedulerStartDelayed.json"),
        SHUTDOWN("/api/schedulerShutdown.json"),
        SHUTDOWN_WAIT("/api/schedulerShutdownWait.json");

        private String url;

        SchedulerURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

        public static boolean lookup(String url) {
            if(StringUtils.isEmpty(url)){
                return false;
            }
            SchedulerURL[] schedulerURLs = SchedulerURL.values();
            for (SchedulerURL schedulerURL : schedulerURLs) {
                if (url.startsWith(schedulerURL.getURL())) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum JobURL implements ServiceStrategyURL {
        INFO("/api/jobInfo.json"),
        ADD("/api/jobAdd.json"),
        RESUME("/api/jobResume.json"),
        REMOVE("/api/jobRemove.json"),
        PAUSE("/api/jobPause.json"),
        RUN("/api/jobRun.json");

        private String url;

        JobURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

        public static boolean lookup(String url) {
            if(StringUtils.isEmpty(url)){
                return false;
            }
            JobURL[] jobURLs = JobURL.values();
            for (JobURL jobURL : jobURLs) {
                if (url.startsWith(jobURL.getURL())) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum TriggerURL implements ServiceStrategyURL {
        INFO("/api/triggerInfo.json"),
        ADD("/api/triggerAdd.json"),
        RESUME("/api/triggerResume.json"),
        REMOVE("/api/triggerRemove.json"),
        PAUSE("/api/triggerPause.json"),
        RUN("/api/triggerRun.json");

        private String url;

        TriggerURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

        public static boolean lookup(String url) {
            if(StringUtils.isEmpty(url)){
                return false;
            }
            TriggerURL[] triggerURLs = TriggerURL.values();
            for (TriggerURL triggerURL : triggerURLs) {
                if (url.startsWith(triggerURL.getURL())) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum ValidateURL implements ServiceStrategyURL {
        VALIDATE_CLASS("/api/validateClass.json"),
        VALIDATE_CLASSINFO("/api/classInfo.json"),
        VALIDATE_ASSIGNABLE("/api/assignable.json"),
        VALIDATE_JOB("/api/validateJob.json"),
        VALIDATE_TRIGGER("/api/validateTrigger.json"),
        VALIDATE_CRONEXPRESSION("/api/validateCronExpression.json");
        private String url;

        ValidateURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }

        public static boolean lookup(String url) {
            if(StringUtils.isEmpty(url)){
                return false;
            }
            ValidateURL[] validateURLs = ValidateURL.values();
            for (ValidateURL validateURL : validateURLs) {
                if (url.startsWith(validateURL.getURL())) {
                    return true;
                }
            }
            return false;
        }

    }

}
