package com.ssky.framework.webmvc.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Copyright (C), 2020
 * FileName: KYDispatcherServlet
 * Author:   liuhao
 * Date:     2020/8/26 0:36
 * Description: servlet 入口类
 */

public class DispatcherServlet extends HttpServlet {

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
        doLoadConfig(config);


    }

    private void doLoadConfig(ServletConfig config) {
        this.getClass().getClassLoader().getResourceAsStream("");
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp){

    }
}
