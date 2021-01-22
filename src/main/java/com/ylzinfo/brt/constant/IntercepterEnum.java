package com.ylzinfo.brt.constant;

import lombok.Getter;

@Getter
public enum IntercepterEnum {

    IS_PASS("IS_PASS", "用户");


    private String code;
    private String name;


    IntercepterEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
