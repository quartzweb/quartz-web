/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.http;

import com.github.quartzweb.log.LOG;
import com.github.quartzweb.utils.IOUtils;
import com.github.quartzweb.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责处理资源转发servlet
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
abstract class ResourceServlet extends HttpServlet {


    /**
     * 静态资源路径
     */
    private String resourcePath = "html";

    /**
     * 业务处理路径前缀
     */
    private String servicePathPrefix = "/api/";

    /**
     * 登录处理路径
     */
    private String authcPath = "/submitLogin";

    /**
     * 退出路径
     */
    private String logoutPath = "/submitLogout";

    /**
     * 用户信息
     */
    protected Map<String, String> userInfo = new HashMap<String, String>();


    public void init() throws ServletException {
        super.init();
    }


    /**
     * 复写父类service方法，进行请求转发
     *
     * @param request  请求
     * @param response 应答
     * @throws ServletException 异常
     * @throws IOException      异常
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 项目根路径
        String contextPath = request.getContextPath();
        // servlet访问路径
        String servletPath = request.getServletPath();
        // 访问全路径
        String requestURI = request.getRequestURI();
        // 设置编码
        response.setCharacterEncoding("utf-8");
        // 根目录为空的情况为，root context
        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());
        // request是否合法
        if (!isPermittedRequest(request, response)) {
            return;
        }
        //
        if ("/submitLogin".equals(path)) {
            response.getWriter().print(this.submitLogin(path, request, response));
            return;
        }
        if ("/submitLogout".equals(path)) {
            response.getWriter().print(this.submitLogout(path, request, response));
            return;
        }
        // 没有权限直接返回
        if (!hasPermission(path, request, response)) {
            return;
        }

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect(servletPath + "/index.html");
            } else {
                response.sendRedirect(requestURI + "/index.html");
            }
            return;
        }
        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.startsWith(servicePathPrefix)) {
            LOG.debug("path:" + path);
            response.getWriter().print(this.process(path, request, response));
            return;
        }
        // 其他
        this.returnResourceFile(path, uri, response);
    }


    /**
     * 返回资源文件
     *
     * @param fileName 文件名
     * @param uri      uri
     * @param response 应答
     * @throws ServletException etException 异常
     * @throws IOException      异常
     */
    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException, IOException {
        String filePath = getFilePath(fileName);
        // HTML文件
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        // 获取资源文件
        String text = IOUtils.readFromResource(filePath);
        if (text == null) {
            // 没有资源直接返回到主页，结束以下代码，提高效率
            response.sendRedirect(uri + "/index.html");
            return;
        }
        // css文件
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        }
        // js文件
        if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        // jsp文件
        if (fileName.endsWith(".jpg")) {
            byte[] bytes = IOUtils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }
            return;
        }
        if (fileName.endsWith(".eot") || fileName.endsWith(".svg") || fileName.endsWith(".ttf")
                || fileName.endsWith(".woff") || fileName.endsWith(".woff2")) {
            byte[] bytes = IOUtils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }
            return;
        }

        response.getWriter().write(text);
    }


    /**
     * 获取资源路径
     *
     * @return 资源路径
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * 设置资源路径
     *
     * @param resourcePath 资源路径
     */
    public void setResourcePath(String resourcePath) {
        LOG.debug("resourcePath:" + resourcePath);
        this.resourcePath = resourcePath;
    }

    /**
     * 获取业务处理路径前缀
     *
     * @return 业务处理路径前缀
     */
    public String getServicePathPrefix() {
        return servicePathPrefix;
    }

    /**
     * 设置业务处理路径前缀
     *
     * @param servicePathPrefix 业务处理路径前缀
     */
    public void setServicePathPrefix(String servicePathPrefix) {
        LOG.debug("servicePathPrefix:" + servicePathPrefix);
        this.servicePathPrefix = servicePathPrefix;
    }


    /**
     * 获取 登录处理路径
     * @return authcPath 登录处理路径
     */
    public String getAuthcPath() {
        return this.authcPath;
    }

    /**
     * 设置 登录处理路径
     * @param authcPath 登录处理路径
     */
    public void setAuthcPath(String authcPath) {
        LOG.debug("authcPath:" + authcPath);
        this.authcPath = authcPath;
    }

    /**
     * 获取 退出路径
     * @return logoutPath 退出路径
     */
    public String getLogoutPath() {
        return this.logoutPath;
    }

    /**
     * 设置 退出路径
     * @param logoutPath 退出路径
     */
    public void setLogoutPath(String logoutPath) {
        LOG.debug("logoutPath:" + authcPath);
        this.logoutPath = logoutPath;
    }

    /**
     * 获取某个文件的资源路径
     *
     * @param fileName 文件名
     * @return 资源路径 + 文件名
     */
    protected String getFilePath(String fileName) {
        return getResourcePath() + fileName;
    }


    /**
     * 核查请求是否允许
     * @param request request请求
     * @return true允许,false不允许
     * @throws ServletException 异常
     * @throws IOException 异常
     */
    protected abstract boolean isPermittedRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException;
    /**
     * 登录处理
     * @param path 登录处理路径
     * @param request request对象
     * @param response response对象
     * @return 处理结果
     * @throws ServletException 异常
     * @throws IOException 异常
     */
    protected abstract String submitLogin(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * 注销处理
     * @param path 登录处理路径
     * @param request request对象
     * @param response response对象
     * @return 处理结果
     * @throws ServletException 异常
     * @throws IOException 异常
     */
    protected abstract String submitLogout(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
    /**
     * 是否有权限
     * @param path 路径
     * @param request request对象
     * @param response response对象
     * @return 是否有权限 true有权限,false无权限
     * @throws ServletException 异常
     * @throws IOException 异常
     */
    protected abstract boolean hasPermission(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;


    /**
     * 实际处理业务详情
     * @param path 路径
     * @param request request对象
     * @param response response对象
     * @return 返回详情
     * @throws ServletException 异常
     * @throws IOException 异常
     */
    protected abstract String process(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
