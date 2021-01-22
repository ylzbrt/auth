package com.ylzinfo.brt.service;

import com.ylzinfo.brt.vo.CheckUserVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    /**
     * 操作人名称
     */
    String getOperator();

    /**
     * 操作人账号
     */
    String getOperatorAccount();

    /**
     * 客户端IP
     */
    String getClientIP();

    void saveUserData(CheckUserVO userDataBo);

    CheckUserVO getUserData();
}
