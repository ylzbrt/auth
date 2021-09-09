package com.ylzinfo.brt.intercepter;

import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.IntercepterEnum;
import com.ylzinfo.brt.entity.AuthReturnEntity;
import com.ylzinfo.brt.service.UserAuthService;
import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.utils.ResponseUtil;
import com.ylzinfo.brt.vo.CheckUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class TestUserAuthFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserInfoService userService;

    @Autowired
    YlzConfig ylzConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(ylzConfig.isSkipUserCheck()){
            CheckUserVO userDataBo = ylzConfig.getTestUserData();
            if(userDataBo==null){
                ResponseUtil.writeDenied(response, AuthReturnEntity.SYS_ERR, "测试用户配置错误，请检查配置ylz.testUserData");
                return false;
            }
            userService.saveUserData(userDataBo);
            request.setAttribute(IntercepterEnum.IS_PASS.getCode(),true);
        }
        return true;
    }


}
