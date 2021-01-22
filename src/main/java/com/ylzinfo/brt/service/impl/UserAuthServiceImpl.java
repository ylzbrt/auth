package com.ylzinfo.brt.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    YlzConfig ylzConfig;


    @Override
    public boolean check(String userId, String userToken, String userData, String timestamp,String sign) {
        String tpl = String.format("%s_%s_%s_%s_%s", userId, userToken, userData, ylzConfig.getSignSecret(), timestamp);
        return SecureUtil.md5(tpl).equals(sign);
    }
}
