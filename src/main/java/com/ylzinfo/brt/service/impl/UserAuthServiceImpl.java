package com.ylzinfo.brt.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    YlzConfig ylzConfig;

    /**
     *
     *  mySign=f1fb754caf81ec8c4c357ea32eb083ce,sign=null,txt=1_1d023ab8-481f-4252-a427-14235034a64d_null_a3d1ed089fd13a0a1db1a9631d02408e_null
     * 2021-03-19 14:44:01.871  INFO 8492 --- [nio-8082-exec-6] c.y.brt.intercepter.UserAuthFilter       : isSkipUserCheck=false,isSignOk=false
     */
    @Override
    public boolean check(String userId, String userToken, String userData, String timestamp,String sign) {
        String txt = String.format("%s_%s_%s_%s_%s", userId, userToken, userData, ylzConfig.getSignSecret(), timestamp);
        final String mySign = SecureUtil.md5(txt);
        final boolean isOk = mySign.equals(sign);
        if (!isOk) {
            log.info("mySign={},sign={},txt={}",mySign,sign,txt);
        }
        return isOk;
    }
}
