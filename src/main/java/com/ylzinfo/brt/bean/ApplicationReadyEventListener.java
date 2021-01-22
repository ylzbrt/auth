package com.ylzinfo.brt.bean;

import com.sun.javafx.event.EventUtil;
import com.ylzinfo.brt.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ApiService apiService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            apiService.scan();
        } catch (Exception e) {
            log.error("注册api到权限中心失败，{}", e);
        }
    }
}
