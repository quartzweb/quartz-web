/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.http;

import com.github.quartzweb.utils.IOUtils;
import com.github.quartzweb.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    /**
     * 静态资源路径
     */
    private String resourcePath = "html";

    /**
     * 业务处理路径前缀
     */
    private String servicePathPrefix = "/api/";

    public static final String SESSION_USER_KEY = "quartz-web-user";
    public static final String INIT_NAME_USER = "quartWebUser";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static final String PARAM_NAME_ALLOW = "allow";
    public static final String PARAM_NAME_DENY = "deny";
    public static final String PARAM_REMOTE_ADDR = "remoteAddress";

    /**
     * 用户信息
     */
    protected Map<String, String> userInfo = new HashMap<String, String>();


    public void init() throws ServletException {
        super.init();
        initAuth();
    }

    /**
     * 初始化权限信息
     */
    private void initAuth() {
        // 获取用户配置信息
        String quartWebUser = getInitParameter(INIT_NAME_USER);
        if (!StringUtils.isEmpty(quartWebUser)) {
            String[] quartWebUsers = quartWebUser.split(";");
            if (quartWebUsers.length > 0) {
                for (String webUser : quartWebUsers) {
                    String[] webUserInfo = webUser.split(":");
                    if (webUserInfo.length != 2) {
                        throw new IllegalArgumentException("quartWebUser format exception.form username:password;username:password");
                    }
                    userInfo.put(webUserInfo[0], webUserInfo[1]);
                }
            }
        }
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

        if ("/submitLogin".equals(path)) {
            String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
            String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
            if (this.userInfo.containsKey(usernameParam) && this.userInfo.get(usernameParam).equals(passwordParam)) {
                request.getSession().setAttribute(SESSION_USER_KEY, usernameParam);
                response.getWriter().print("success");
            } else {
                response.getWriter().print("error");
            }
            return;
        }

        if (isRequireAuth() //
                && !containsUser(request)//
                && !checkLoginParam(request)//
                && !("/login.html".equals(path) //
                || path.startsWith("/css")//
                || path.startsWith("/js") //
                || path.startsWith("/img"))) {

            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect(servletPath + "/login.html");
                return;
            } else {
                if (uri.endsWith("/")) {
                    response.sendRedirect(uri + "login.html");
                } else {
                    response.sendRedirect(uri + "/login.html");
                }
                return;
            }

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
            //String fullUrl = path;
            /*if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }*/
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

    public boolean containsUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_USER_KEY) != null;
    }

    public boolean checkLoginParam(HttpServletRequest request) {
        String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
        if(null == this.userInfo.get(usernameParam)){
            return false;
        } else if (this.userInfo.containsKey(usernameParam) && this.userInfo.get(usernameParam).equals(passwordParam)) {
            return true;
        }
        return false;
    }

    public boolean isRequireAuth() {
        return this.userInfo.size() != 0;
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
        this.servicePathPrefix = servicePathPrefix;
    }

    /**
     * 实际处理业务详情
     */
    protected abstract String process(String fullUrl, HttpServletRequest request, HttpServletResponse response);

}
