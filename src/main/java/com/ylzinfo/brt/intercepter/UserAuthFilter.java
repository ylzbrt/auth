package com.ylzinfo.brt.intercepter;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.constant.IntercepterEnum;
import com.ylzinfo.brt.entity.AuthReturnEntity;
import com.ylzinfo.brt.service.UserAuthService;
import com.ylzinfo.brt.service.UserService;
import com.ylzinfo.brt.utils.ResponseUtil;
import com.ylzinfo.brt.vo.CheckUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
public class UserAuthFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserService userService;

    @Autowired
    YlzConfig ylzConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器={}",getClass());
        String userId = request.getHeader(HttpHeaderEnum.USER_ID.getCode());
        String userToken = request.getHeader(HttpHeaderEnum.USER_TOKEN.getCode());
        String userData = request.getHeader(HttpHeaderEnum.USER_DATA.getCode());
        String userSign = request.getHeader(HttpHeaderEnum.USER_SIGN.getCode());
        String timestamp = request.getHeader(HttpHeaderEnum.TIMESTAMP.getCode());
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(userToken) && StringUtils.isEmpty(userData)) {
            //丢给下一个过滤器
            return true;
        }
        boolean isOk =ylzConfig.isSkipUserCheck() ||  userAuthService.check(userId, userToken, userData, timestamp,userSign);
        if (!isOk) {
            ResponseUtil.writeDenied(response, AuthReturnEntity.ILLEGAL_USER_ERR, "非法用户请求");
            return false;
        }
        //反序列化用户信息，放在上下文
        CheckUserVO userDataBo = null;
        try {
            userDataBo = JSONUtil.toBean(URLUtil.decode(userData), CheckUserVO.class);
        } catch (Exception e) {
            log.error("反序列化用户信息失败,userData={}", userData);
            ResponseUtil.writeDenied(response, AuthReturnEntity.ILLEGAL_USER_ERR, "非法用户请求,用户数据异常");
            return false;
        }
        userService.saveUserData(userDataBo);
        request.setAttribute(IntercepterEnum.IS_PASS.getCode(),true);
        return true;
    }


}