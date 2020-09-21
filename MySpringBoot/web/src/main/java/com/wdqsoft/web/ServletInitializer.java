package com.wdqsoft.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//https://blog.csdn.net/github_38151745/article/details/78707664
//Spring boot 打包 配置所用
public class ServletInitializer extends SpringBootServletInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootServletInitializer.class);
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        LOG.warn("启动-ServletInitializer");
        return application.sources(WebApplication.class);
    }

}