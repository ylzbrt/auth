package com.ylzinfo.brt.service.impl;

import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.UserService;
import com.ylzinfo.brt.vo.CheckUserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getOperator() {
        final CheckUserVO userData = getUserData();
        if(userData==null){
            return "";
        }
        final CheckUserVO.UserBean user = userData.getUser();
        if(user==null){
            return "";
        }
        return user.getName();
    }

    @Override
    public String getOperatorAccount() {
        final CheckUserVO userData = getUserData();
        if(userData==null){
            return "";
        }
        final CheckUserVO.UserBean user = userData.getUser();
        if(user==null){
            return "";
        }
        return user.getAccount();

    }

    @Override
    public String getClientIP() {
        return getRequest().getHeader(HttpHeaderEnum.CLIENT_IP.getCode());
    }


    @Override
    public void saveUserData(CheckUserVO userDataBo) {
        HttpServletRequest request = getRequest();
        request.setAttribute(HttpHeaderEnum.USER_DATA.getCode(), userDataBo);
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @Override
    public CheckUserVO getUserData() {
        HttpServletRequest request = getRequest();
        return (CheckUserVO) request.getAttribute(HttpHeaderEnum.USER_DATA.getCode());
    }
}
