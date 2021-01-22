package com.ylzinfo.brt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "ylz")
@Data
@Configuration
public class YlzConfig {
    private String serviceSecret;
    private String gatewaySecret;
    private List<String> publicUrls;
    private boolean skipServiceCheck;
    private boolean skipUserCheck;
    private List<String> controllerPackages;
}
