/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.service;

import com.github.quartzweb.utils.json.JSONUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class JSONResult {

    /**
     * 返回结果code
     */
    private int resultCode;

    /**
     * 返回结果
     */
    private Object content;

    /**
     * 返回json结果-成功(1)
     */
    public static final int RESULT_CODE_SUCCESS = 1;

    /**
     * 返回json结果-失败(-1)
     */
    public static final int RESULT_CODE_ERROR = -1;

    public JSONResult(int resultCode, Object content) {
        this.resultCode = resultCode;
        this.content = content;
    }

    /**
     * 创建一个json结果
     * @param resultCode
     * @param content
     * @return
     */
    public static JSONResult build(int resultCode, Object content){
        return new JSONResult(resultCode, content);
    }

    public String json() {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("resultCode", this.resultCode);
        dataMap.put("content", this.content);
        return JSONUtils.toJSONString(dataMap);
    }


    public static String returnJSON(int resultCode, Object content) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("resultCode", resultCode);
        dataMap.put("content", content);
        return JSONUtils.toJSONString(dataMap);
    }
}
