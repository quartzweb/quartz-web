/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.http;

import com.github.quartzweb.service.QuartzWebService;
import com.github.quartzweb.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 负责处理业务servlet
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class QuartzWebServlet extends ResourceServlet {

    private static final Logger logger = LoggerFactory.getLogger(QuartzWebServlet.class);

    public static final String PARAM_RESOURCE_PATH = "resourcePath";


    private QuartzWebService quartzWebService = QuartzWebService.getInstance();

    /**
     * 设置资源路径
     * @throws ServletException
     */
    public void init() throws ServletException {
        super.init();
        // 用户是否有配置新的资源路径
        String paraResourcePath = getInitParameter(PARAM_RESOURCE_PATH);
        if (!StringUtils.isEmpty(paraResourcePath)) {
            setResourcePath(paraResourcePath);
        }
    }

    protected String process(String url, HttpServletRequest request, HttpServletResponse response) {
        return quartzWebService.service(url, request, response);
    }

}
