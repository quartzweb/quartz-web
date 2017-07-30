/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service.strategy;

import com.github.quartzweb.manager.web.QuartzWebManager;
import com.github.quartzweb.service.JSONResult;
import com.github.quartzweb.service.QuartzWebURL;


import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class BasicServiceStrategy implements ServiceStrategy<BasicServiceStrategyParameter> {

    public JSONResult service(ServiceStrategyURL serviceStrategyURL, BasicServiceStrategyParameter parameter) {
        // 列出信息
        if (QuartzWebURL.BasicURL.BASIC.getURL().equals(serviceStrategyURL.getURL())) {
            return getInfo();
        }
        return JSONResult.build(JSONResult.RESULT_CODE_ERROR, "404 not found");
    }


    public BasicServiceStrategyParameter newServiceStrategyParameterInstance() {
        return new BasicServiceStrategyParameter();
    }

    public JSONResult getInfo() {
        try {
            Map<String, Object> resultMap = QuartzWebManager.getBasicInfo();
            return JSONResult.build(JSONResult.RESULT_CODE_SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONResult.build(JSONResult.RESULT_CODE_ERROR, e.getMessage());
        }
    }
}
