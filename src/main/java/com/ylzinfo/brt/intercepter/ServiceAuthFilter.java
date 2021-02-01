package com.ylzinfo.brt.intercepter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.constant.IntercepterEnum;
import com.ylzinfo.brt.entity.AuthReturnEntity;

import com.ylzinfo.brt.service.ServiceAuthService;
import com.ylzinfo.brt.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class ServiceAuthFilter extends HandlerInterceptorAdapter {

   @Autowired
   ServiceAuthService authService;

   @Autowired
    YlzConfig ylzConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器={}",getClass());

        String serviceName = request.getHeader(HttpHeaderEnum.SERVICE_NAME.getCode());
        String serviceSign = request.getHeader(HttpHeaderEnum.SERVICE_SIGN.getCode());
        if(StringUtils.isEmpty(serviceName) && StringUtils.isEmpty(serviceSign)){
            //丢给下一个过滤器
            return true;
        }
        boolean isOk=ylzConfig.isSkipServiceCheck() || authService.check(serviceName,serviceSign);
        if(!isOk){
            ResponseUtil.writeDenied(response, AuthReturnEntity.SERVICE_AUTH_ERR,"服务间调用异常");
            return false;
        }
        request.setAttribute(IntercepterEnum.IS_PASS.getCode(),true);
        return true;
    }

}
