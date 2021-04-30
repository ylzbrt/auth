package com.ylzinfo.brt.constant;

import lombok.Getter;

@Getter
public enum HttpHeaderEnum {

    USER_ID("X-USER-ID", "用户"),
    USER_TOKEN("X-USER-TOKEN", "用户token"),
    USER_DATA("X-USER-DATA", "用户信息"),
    USER_SIGN("X-USER-SIGN", "用户信息签名"),
    CLIENT_IP("X-CLIENT-IP", "客户端IP"),
    SERVICE_NAME("X-SERVICE-NAME", "服务名称"),
    SERVICE_SIGN("X-SERVICE-SIGN", "服务签名"),
    TIMESTAMP("X-TIMESTAMP", "时间"),
    SERVER_INFO("X-SERVER-INFO", "后端服务器信息");


    private String code;
    private String name;


    HttpHeaderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
