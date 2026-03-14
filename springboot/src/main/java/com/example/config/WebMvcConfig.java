package com.example.config;

import com.example.interp.RequestLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类：注册拦截器
 */
@Configuration // 标记为配置类
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLogInterceptor requestLogInterceptor; // 注入自定义拦截器

    /**
     * 注册拦截器，指定拦截所有请求
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/static/**", "/favicon.ico"); // 排除静态资源（可选）
    }
}
