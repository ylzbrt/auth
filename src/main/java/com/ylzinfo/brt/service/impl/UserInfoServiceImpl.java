package com.ylzinfo.brt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.vo.CheckUserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public String getOperator() {

        return getUser().getName();
    }

    private CheckUserVO.UserBean getUser() {
        final CheckUserVO userData = getUserData();
        if (userData == null) {
            return new CheckUserVO.UserBean();
        }
        final CheckUserVO.UserBean user = userData.getUser();
        if (user == null) {
            return new CheckUserVO.UserBean();
        }
        return user;
    }

    @Override
    public String getOperatorAccount() {
        return getUser().getAccount();

    }

    @Override
    public String getClientIp() {
        return getRequest().getHeader(HttpHeaderEnum.CLIENT_IP.getCode());
    }

    @Override
    public String getOperatorId() {
        return getUser().getUserId() + "";

    }

    @Override
    public String getOrganizationId() {
        final CheckUserVO userData = getUserData();
        if (userData == null) {
            return "";
        }
        final List<CheckUserVO.OrganizationBean> organizations = userData.getOrganizations();
        if (CollectionUtil.isNotEmpty(organizations)) {
            return organizations.get(0).getOrganizationId() + "";
        }
        return "";

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
