//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ylzinfo.brt.entity;



import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthReturnEntity<T>   {
    public static String SUCCESS ="0"; //成功
    public static String AUTH_ERR="-4";//权限校验异常
    public static String SERVICE_AUTH_ERR="-8";//非服务间正常调用
    public static String ILLEGAL_USER_ERR="-9";//非法用户信息

    @ApiModelProperty("响应代码")
    private String code ="0";
    @ApiModelProperty("错误信息")
    private String message="操作成功";
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("流水号")
    private String traceid;

    public AuthReturnEntity() {
        this.code =SUCCESS;
        this.message="操作成功";
    }
    public AuthReturnEntity(T data) {
        this();
        this.data=data;
    }
    public AuthReturnEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> AuthReturnEntity success(T data) {
        return new AuthReturnEntity(data);
    }
    public static AuthReturnEntity success(){
        return new AuthReturnEntity();
    }
}
