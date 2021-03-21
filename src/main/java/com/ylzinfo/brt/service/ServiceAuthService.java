package com.ylzinfo.brt.service;

public interface ServiceAuthService {
    /**
     * feign请求调用时，计算出签名
     */
    String getSign(String timestamp);

    /**
     * 签名验证
     */
    boolean check(String serviceName, String serviceSign, String timestamp );
}
