package com.ylzinfo.brt.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data

public class CheckUserVO {


    //账号未锁定、账号未过期、user_token验证通过、有该接口调用权限时才返回true
    private Boolean checkResult;
    //验证不通过的理由
    private String failReason;
    //用户信息
    private UserBean user;
    //组织信息
    private List<OrganizationBean> organizations;
    //角色信息
    private List<RolesBean> roles;
    //权限信息（无需包含菜单、按钮、接口、字段信息）
    private List<PrivilegesBean> privileges;
    //当前接口不可见字段
    private List<String> filterFields;

    @NoArgsConstructor
    @Data
    public static class UserBean {


        private Integer userId;
        private String userToken;
        private String account;
        private String name;
        private String idNumber;
        private String certType;
        private String phone;
        private String mobile;
        private String position;
        private String startDate;
        private String endDate;
    }

    @NoArgsConstructor
    @Data
    public static class OrganizationBean {


        private int organizationId;
        private String organizationName;
    }

    @NoArgsConstructor
    @Data
    public static class RolesBean {

        private int roleId;
        private String roleName;
    }

    @NoArgsConstructor
    @Data
    public static class PrivilegesBean {
        private String grantType;
        private String resourceId;
        private String resourceCode;
        private String resourceName;
        private String resourceType;
        private String orgGrantType;
        private String organizationId;
        private String admdvs;
        private String medicalInstitutionId;
        private String departmentId;
    }
}
