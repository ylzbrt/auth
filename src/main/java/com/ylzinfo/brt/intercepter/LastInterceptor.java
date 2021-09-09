/**
 *
 */
package com.ylzinfo.brt.intercepter;


import cn.hutool.core.collection.CollectionUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.IntercepterEnum;
import com.ylzinfo.brt.entity.AuthReturnEntity;
import com.ylzinfo.brt.utils.ResponseUtil;
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
public class LastInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    YlzConfig ylzConfig;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("拦截器={},url={}",getClass(),request.getRequestURI());

        final Boolean isPass = (Boolean) request.getAttribute(IntercepterEnum.IS_PASS.getCode());
        if (isPass) {
            return true;
        }
        ResponseUtil.writeDenied(response, AuthReturnEntity.LOGIN_ERR, "权限验证失败,请求头必需包含用户信息或服务信息【auth.AnonymousInterceptor】");
        return false;

    }




}
