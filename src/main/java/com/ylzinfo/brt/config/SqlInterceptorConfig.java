package com.ylzinfo.brt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "sqlinterceptor")
@Data
@Configuration
public class SqlInterceptorConfig {


    // include=包含 exclude=排除
    private String type;
    private List<String> methods;
}
