/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.http;

import com.github.quartzweb.http.util.IPAddress;
import com.github.quartzweb.http.util.IPRange;
import com.github.quartzweb.log.LOG;
import com.github.quartzweb.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责处理权限servlet
 *
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public abstract class SecurityServlet extends ResourceServlet {


    public static final String SESSION_USER_KEY = "quartz-web-user";
    public static final String INIT_NAME_USER = "quartWebUser";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static final String PARAM_NAME_ALLOW = "allow";
    public static final String PARAM_NAME_DENY = "deny";
    public static final String PARAM_REMOTE_ADDR = "remoteAddress";

    protected List<IPRange> allowList = new ArrayList<IPRange>();
    protected List<IPRange> denyList = new ArrayList<IPRange>();

    protected String remoteAddressHeader = null;

    @Override
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

        String paramRemoteAddressHeader = getInitParameter(PARAM_REMOTE_ADDR);
        if (!StringUtils.isEmpty(paramRemoteAddressHeader)) {
            this.remoteAddressHeader = paramRemoteAddressHeader;
        }

        try {
            String param = getInitParameter(PARAM_NAME_ALLOW);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                String[] items = param.split(",");

                for (String item : items) {
                    if (item == null || item.length() == 0) {
                        continue;
                    }

                    IPRange ipRange = new IPRange(item);
                    allowList.add(ipRange);
                }
            }
        } catch (Exception e) {
            String msg = "initParameter config error, allow : " + getInitParameter(PARAM_NAME_ALLOW);
            LOG.error(msg, e);
        }

        try {
            String param = getInitParameter(PARAM_NAME_DENY);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                String[] items = param.split(",");

                for (String item : items) {
                    if (item == null || item.length() == 0) {
                        continue;
                    }

                    IPRange ipRange = new IPRange(item);
                    denyList.add(ipRange);
                }
            }
        } catch (Exception e) {
            String msg = "initParameter config error, deny : " + getInitParameter(PARAM_NAME_DENY);
            LOG.error(msg, e);
        }
    }

    protected boolean containsUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_USER_KEY) != null;
    }

    protected boolean checkLoginParam(HttpServletRequest request) {
        String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
        if(null == this.userInfo.get(usernameParam)){
            return false;
        } else if (this.userInfo.containsKey(usernameParam) && this.userInfo.get(usernameParam).equals(passwordParam)) {
            return true;
        }
        return false;
    }

    /**
     * 是否权限校验
     * @return true是,false否
     */
    protected boolean isRequireAuth() {
        return this.userInfo.size() != 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String submitLogin(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
        if (this.userInfo.containsKey(usernameParam) && this.userInfo.get(usernameParam).equals(passwordParam)) {
            request.getSession().setAttribute(SESSION_USER_KEY, usernameParam);
            return "success";
        } else {
            return "error";
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String submitLogout(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object attribute = request.getSession(false).getAttribute(SESSION_USER_KEY);
        if (attribute == null) {
            return "error";
        } else {
            request.getSession(false).removeAttribute(SESSION_USER_KEY);
            return "success";
        }
    }

    /**
     * {@inheritDoc}
     */
    protected boolean hasPermission(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 项目根路径
        String contextPath = request.getContextPath();
        // servlet访问路径
        String servletPath = request.getServletPath();
        // 设置编码
        response.setCharacterEncoding("utf-8");
        // 根目录为空的情况为，root context
        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        if (isRequireAuth() //
                && !containsUser(request)//
                && !checkLoginParam(request)//
                && !("/login.html".equals(path) //
                || path.startsWith("/css")//
                || path.startsWith("/js") //
                || path.startsWith("/img"))) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect(servletPath + "/login.html");
            } else {
                if (uri.endsWith("/")) {
                    response.sendRedirect(uri + "login.html");
                } else {
                    response.sendRedirect(uri + "/login.html");
                }
            }
            return false;

        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isPermittedRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String remoteAddress = getRemoteAddress(request);
        boolean permittedRequest = isPermittedRequest(remoteAddress);
        if (!permittedRequest) {
            response.getWriter().print("request is not permitted ");
            return false;
        }
        return true;
    }

    protected boolean isPermittedRequest(String remoteAddress) {
        boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(':') != -1;

        if (ipV6) {
            return "0:0:0:0:0:0:0:1".equals(remoteAddress) || (denyList.size() == 0 && allowList.size() == 0);
        }

        IPAddress ipAddress = new IPAddress(remoteAddress);

        for (IPRange range : denyList) {
            if (range.isIPAddressInRange(ipAddress)) {
                return false;
            }
        }

        if (allowList.size() > 0) {
            for (IPRange range : allowList) {
                if (range.isIPAddressInRange(ipAddress)) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    protected String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress = null;

        if (remoteAddressHeader != null) {
            remoteAddress = request.getHeader(remoteAddressHeader);
        }

        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }

        return remoteAddress;
    }

}
