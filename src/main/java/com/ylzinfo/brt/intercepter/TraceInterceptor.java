package com.ylzinfo.brt.intercepter;


import com.ylzinfo.brt.constant.HttpHeaderEnum;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
@Slf4j
/**
 * 用于将当前服务器（当前应用的信息返回给网关）
 * 用于前端明确知道请求响应自哪个服务
 */
public class TraceInterceptor extends HandlerInterceptorAdapter {

    @Value("${server.port}")
    Integer port;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        final String serverInfo = "http://" + ip + ":" + port;
        response.addHeader(HttpHeaderEnum.SERVER_INFO.getCode(), serverInfo);
        return true;
    }


}
