/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.github.quartzweb.server;


import com.github.quartzweb.log.LOG;
import com.github.quartzweb.manager.quartz.QuartzManager;
import com.github.quartzweb.utils.PropertiesLoaderUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.quartz.Scheduler;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class StartWebServer {


    public void startServer(){
        Thread thread = new Thread(new Runnable() {

            public void run() {
            try {
                Properties properties = PropertiesLoaderUtils.loadProperties("quartzWebServer.properties");
                String port = properties.getProperty("port");
                String resourcePath = properties.getProperty("resourcePath");
                String contextPath = properties.getProperty("contextPath");

                String startWebServerServletClass = properties.getProperty("startWebServer.servlet.class");
                LOG.info("jetty server start.....,port:" + port +
                        ",contextPath:" + contextPath +
                        ",resourcePath:" + resourcePath);
                Class<? extends Servlet> servletClass = (Class<? extends Servlet>) Class.forName(startWebServerServletClass);
                // 新建web服务器
                Server server = new Server(Integer.parseInt(port));
                // 配置web应用上下文
                WebAppContext webAppContext = new WebAppContext();
                webAppContext.setContextPath(contextPath);
                // 设置配置文件扫描路径
                ClassLoader classLoader = StartWebServer.class.getClassLoader();
                URL resource = classLoader.getResource("");

                assert resource != null;
                String path = resource.getPath();
                File resDir = new File(path);

                webAppContext.setResourceBase(resDir.getCanonicalPath() + resourcePath);

                webAppContext.setConfigurationDiscovered(true);
                webAppContext.setParentLoaderPriority(true);
                ServletHolder servletHolder = new ServletHolder(servletClass);
                for (Map.Entry<Object, Object> propEntry : properties.entrySet()) {
                    servletHolder.setInitParameter(propEntry.getKey().toString(), properties.getProperty(propEntry.getKey().toString()));
                }
                webAppContext.addServlet(servletHolder, "/*");


                server.setHandler(webAppContext);
                // 启动web服务器
                server.start();
                // 启动调度器
                String schedulerStart = properties.getProperty("schedulerStart");
                if ("true".equals(schedulerStart)) {
                    QuartzManager quartzManager = QuartzManager.getInstance();
                    List<Scheduler> schedulers = quartzManager.getSchedulers();
                    for (Scheduler scheduler : schedulers) {
                        scheduler.start();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        new StartWebServer().startServer();
    }

}
