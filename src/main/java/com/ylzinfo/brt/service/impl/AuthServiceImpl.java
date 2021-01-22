package com.ylzinfo.brt.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.service.AuthService;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    YlzConfig ylzConfig;
    @Value("${spring.application.name}")
    private String serviceName;


    @Override
    public String getSign() {
        return calSign(this.serviceName);
    }

    public String calSign(String clientServiceName) {
        String nowMinute = DateUtil.format(new Date(), "yyyyMMddHHmm");
        String tpl = String.format("%s_%s_%s", clientServiceName, ylzConfig.getServiceSecret(), nowMinute);
        return SecureUtil.md5(tpl);
    }

    @Override
    public boolean check(String serviceName, String serviceSign) {
        return calSign(serviceName).equals(serviceSign);
    }
}
