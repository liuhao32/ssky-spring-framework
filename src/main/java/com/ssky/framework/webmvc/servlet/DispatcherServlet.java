package com.ssky.framework.webmvc.servlet;

import com.ssky.framework.annotation.Autowired;
import com.ssky.framework.annotation.PostMapping;
import com.ssky.framework.annotation.RestController;
import com.ssky.framework.annotation.Service;
import com.ssky.framework.utils.BeanUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Copyright (C), 2020
 *
 * @author: liuhao
 * @date: 2020/8/26 0:36
 * @description: servlet 入口类
 */

public class DispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private Set<String> classNames = new HashSet<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        doLoadConfig(config.getInitParameter("contentConfigLocation"));

        doScanner(contextConfig.getProperty("scanPackage"));

        doInstance();

        doAutowired();

        initHandlerMapping();

        System.out.println("init over");

    }

    private void initHandlerMapping() {

        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(RestController.class)) {
                continue;
            }

            String baseUrl = "";

            if (clazz.isAnnotationPresent(PostMapping.class)) {

                PostMapping postMapping = clazz.getAnnotation(PostMapping.class);
                baseUrl = postMapping.value();
            }

            //扫描所有公共方法
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(PostMapping.class)) {
                    continue;
                }
                PostMapping postMapping = method.getAnnotation(PostMapping.class);

                String methodUrl = ("/" + baseUrl + postMapping.value().replaceAll("/+", ""));
                handlerMapping.put(methodUrl, method);

                System.out.println("Mapping :" + methodUrl + ":" + method);
            }
        }

    }

    /**
     * 注入
     */
    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Autowired autowired = field.getAnnotation(Autowired.class);

                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    /**
     * 实例化
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(RestController.class)) {

                    String beanName = BeanUtils.lowerFirstCase(clazz.getName());
                    ioc.put(beanName, clazz.newInstance());

                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();

                    if ("".equals(beanName.trim())) {
                        beanName = BeanUtils.lowerFirstCase(clazz.getName());
                    }
                    Object instance = clazz.newInstance();

                    ioc.put(beanName, instance);

                    for (Class<?> i : clazz.getInterfaces()) {
                        ioc.put(i.getName(), instance);
                    }

                } else {
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 扫描所有类
     *
     * @param scanPackage 扫描包路径
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    /**
     * 加载初始化配置
     *
     * @param config 配置
     */
    private void doLoadConfig(String config) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(config);
        try {
            contextConfig.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getRequestURI();

        String contextPath = req.getContextPath();

        url.replace(contextPath, "").replaceAll("/+", "/");

        if (!handlerMapping.containsKey(url)) {
            resp.getWriter().write("404!");
            return;
        }
        Method method = handlerMapping.get(url);

        System.out.println(method);

    }
}
