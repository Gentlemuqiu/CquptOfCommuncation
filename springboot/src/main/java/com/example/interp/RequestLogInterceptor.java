package com.example.interp;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * 自定义请求日志拦截器：打印每次请求的详细信息
 */
@Slf4j // 日志注解（需确保项目引入lombok，你的pom已包含）
@Component // 交给Spring管理
public class RequestLogInterceptor implements HandlerInterceptor {

    // 存储请求开始时间的key
    private static final String START_TIME = "requestStartTime";

    /**
     * 请求进入控制器前执行：记录请求基本信息 + 开始时间
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 记录请求开始时间（用于计算耗时）
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        // 2. 收集请求核心信息
        String url = request.getRequestURL().toString(); // 完整URL
        String method = request.getMethod(); // 请求方式（GET/POST/PUT等）
        String ip = request.getRemoteAddr(); // 客户端IP
        String userAgent = request.getHeader("User-Agent"); // 客户端设备/浏览器

        // 3. 收集请求参数（兼容GET/POST表单参数）
        StringBuilder params = new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.append(paramName).append("=").append(paramValue).append("&");
        }
        String paramStr = params.length() > 0 ? params.substring(0, params.length() - 1) : "无参数";

        // 4. 打印请求开始日志
        log.info("=========================================");
        log.info("【请求开始】");
        log.info("请求URL: {}", url);
        log.info("请求方式: {}", method);
        log.info("客户端IP: {}", ip);
        log.info("客户端信息: {}", userAgent);
        log.info("请求参数: {}", paramStr);
        log.info("=========================================");

        // 返回true：放行请求（必须加，否则请求会被拦截）
        return true;
    }

    /**
     * 请求处理完成后执行（记录响应状态 + 耗时）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 1. 计算请求耗时
        long startTime = (long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 2. 获取响应状态码
        int statusCode = response.getStatus();

        // 3. 打印请求结束日志
        log.info("=========================================");
        log.info("【请求结束】");
        log.info("响应状态码: {}", statusCode);
        log.info("请求耗时: {} 毫秒", costTime);
        if (ex != null) {
            log.error("请求异常: {}", ex.getMessage(), ex); // 有异常时打印异常信息
        }
        log.info("=========================================\n");
    }

    // 可选：控制器处理完成但视图渲染前执行（一般用不到，留空即可）
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 无需实现
    }
}