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
import java.util.List;


@Component
@Slf4j
public class AnonymousInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    YlzConfig ylzConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("拦截器={},ylzconfig={}", getClass(),ylzConfig);
        final Boolean isPass = (Boolean) request.getAttribute(IntercepterEnum.IS_PASS.getCode());
        if (ylzConfig.isSkipUserCheck() || (isPass != null && isPass)) {
            return super.preHandle(request, response, handler);
        }

        final String url = request.getRequestURI();
        //不在公共接口中
        if (!isPublicUrl(url, ylzConfig.getPublicUrls())) {
            log.error("当前url非公共url，url={}");
            return ResponseUtil.writeDenied(response, AuthReturnEntity.AUTH_ERR, "权限验证失败");
        }
        return super.preHandle(request, response, handler);
    }

    /**
     *是否公共url
     */
    private boolean isPublicUrl(String url, List<String> publicUrls) {
        if (CollectionUtil.isNotEmpty(publicUrls)) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (String publicUrl : publicUrls) {
                if (antPathMatcher.match(publicUrl, url)) {
                    return true;
                }
            }
        }
        return false;
    }


}
