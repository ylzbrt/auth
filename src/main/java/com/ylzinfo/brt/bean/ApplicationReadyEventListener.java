package com.ylzinfo.brt.bean;

import cn.hutool.core.thread.ThreadUtil;
import com.ylzinfo.brt.service.ApiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ApiInfoService apiService;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            log.info("开始扫描接口");
            apiService.scan();
        } catch (Exception e) {
            log.error("注册api到权限中心失败，{}", e);
        }
    }
}
