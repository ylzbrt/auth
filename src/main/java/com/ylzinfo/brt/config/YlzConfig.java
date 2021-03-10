package com.ylzinfo.brt.config;

import com.ylzinfo.brt.vo.CheckUserVO;
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
     * #已登录用户公共接口权限（已登录用户默认权限，可绕过用户的接口权限，如查询子系统、菜单查询、按地址查询子系统）
     *   loginUserPublicUrls:
     *     - /privilege/sub-system/selectSubSystem
     *     - /privilege/sub-system/getByUrl
     *     - /privilege/menu/query
     */
    private List<String> loginUserPublicUrls;
    /**
     * 跳过服务间调用调用权限
     */
    private boolean skipServiceCheck;
    /**
     * 跳过用户权限验证
     */
    private boolean skipUserCheck;
    /*跳过用户权限时，需配置测试用户数据，为json字符中，结构见CheckUserVO*/
    private CheckUserVO testUserData;
    /**
     * 需要扫描的包
     */
    private List<String> scanPackages;
}
