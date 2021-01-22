package com.ylzinfo.brt.feign;

import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.AuthService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class AuthMyFeignInterceptor implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    AuthService authService;

    public void apply(RequestTemplate template) {
        template.header(HttpHeaderEnum.SERVICE_NAME.getCode(), serviceName);
        final String sign = authService.getSign();
        template.header(HttpHeaderEnum.SERVICE_SIGN.getCode(), sign);
        log.debug("feign注入,SERVICE_NAME={},SERVICE_SIGN={}", serviceName, sign);
    }

}
