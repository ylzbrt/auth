package com.ylzinfo.brt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "ylz")
@Data
@Configuration
public class YlzConfig {
    /**
    1、用于服务间调用的验签密钥
    2、网关在请求头注入用户信息，子系统验证是否被篡改
     */
    private String signSecret;
    /**
     * 无需要进行权限验证的公共接口
     */
    private List<String> publicUrls;
    /**
     * 跳过服务间调用调用权限
     */
    private boolean skipServiceCheck;
    /**
     * 跳过用户权限验证
     */
    private boolean skipUserCheck;
    /**
     * 需要扫描的包
     */
    private List<String> scanPackages;
}
