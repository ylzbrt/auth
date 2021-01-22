package com.ylzinfo.brt.service;

public interface UserAuthService {

    /**
     * 签名验证
     */
    boolean check(String userId, String userToken, String userData, String timestamp, String serviceSign);
}
