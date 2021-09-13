package com.ylzinfo.brt.intercepter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
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
public class UserAuthFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserInfoService userService;

    @Autowired
    YlzConfig ylzConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器={},url={}",getClass(),request.getRequestURI());
        String userId = request.getHeader(HttpHeaderEnum.USER_ID.getCode());
        String userToken = request.getHeader(HttpHeaderEnum.USER_TOKEN.getCode());
        String userData = request.getHeader(HttpHeaderEnum.USER_DATA.getCode());
        String userSign = request.getHeader(HttpHeaderEnum.USER_SIGN.getCode());
        String timestamp = request.getHeader(HttpHeaderEnum.TIMESTAMP.getCode());
        if (StrUtil.isBlank(userId) && StrUtil.isBlank(userToken) && StrUtil.isBlank(userData)) {
            //丢给下一个过滤器
            return true;
        }
        final boolean isSignOk = userAuthService.check(userId, userToken, userData, timestamp, userSign);
        boolean isOk =ylzConfig.isSkipUserCheck() || isSignOk;
        if (!isOk) {
            log.info("isSkipUserCheck={},isSignOk={}",ylzConfig.isSkipUserCheck(),isSignOk);
            ResponseUtil.writeDenied(response, AuthReturnEntity.LOGIN_ERR, "非法用户请求【auth.UserAuthFilter】");
            return false;
        }
        //反序列化用户信息，放在上下文
        CheckUserVO userDataBo = null;
        try {
            userDataBo = JSONUtil.toBean(URLUtil.decode(userData), CheckUserVO.class);
        } catch (Exception e) {
            log.error("反序列化用户信息失败,userData={}", userData);
            ResponseUtil.writeDenied(response, AuthReturnEntity.SYS_ERR, "非法用户请求,用户数据异常【auth.UserAuthFilter】");
            return false;
        }
        userService.saveUserData(userDataBo);
        request.setAttribute(IntercepterEnum.IS_PASS.getCode(),true);
        return true;
    }


}
