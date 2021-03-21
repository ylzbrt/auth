package com.ylzinfo.brt.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.service.ServiceAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServiceAuthServiceImpl implements ServiceAuthService {
    @Autowired
    YlzConfig ylzConfig;
    @Value("${spring.application.name}")
    private String serviceName;


    @Override
    public String getSign(String timestamp) {
        return calSign(this.serviceName,timestamp);
    }

    public String calSign(String clientServiceName, String timestamp ) {
        String tpl = String.format("%s_%s_%s", clientServiceName, ylzConfig.getSignSecret(), timestamp);
        return SecureUtil.md5(tpl);
    }

    @Override
    public boolean check(String serviceName, String serviceSign, String timestamp) {
        return calSign(serviceName,timestamp).equals(serviceSign);
    }
}
