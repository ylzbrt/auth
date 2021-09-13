/**
 *
 */
package com.ylzinfo.brt.intercepter;


import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.IntercepterEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class FirstInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    YlzConfig ylzConfig;
    List<String> defaultPublicUrls = Arrays.asList(
            "/doc.html",
            "/*/v2/api-docs",
            "/static/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("拦截器={},url={}",getClass(),request.getRequestURI());
        final String url = request.getRequestURI();
        //在公共接口中
        if (isPublicUrl(url)) {
            request.setAttribute(IntercepterEnum.IS_PASS.getCode(),true);
        }
        return true;
    }

    /**
     *是否公共url
     */
    private boolean isPublicUrl(String url) {
        List<String> allPublicUrls = new ArrayList<>();
        allPublicUrls.addAll(defaultPublicUrls);
        if (ylzConfig.getPublicUrls() != null) {
            allPublicUrls.addAll(ylzConfig.getPublicUrls());
        }

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String publicUrl : allPublicUrls) {
            if (antPathMatcher.match(publicUrl, url)) {
                return true;
            }
        }

        return false;
    }


}
