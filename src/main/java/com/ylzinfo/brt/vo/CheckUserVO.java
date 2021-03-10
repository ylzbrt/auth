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
    //所在组织信息
    private List<OrganizationBean> organizations;
    //角色信息
    private List<RolesBean> roles;


    /*以下为权限信息*/
    //管理的机构
    private List<OrganizationBean> manageOrganizations;
    //权限信息（无需包含菜单、按钮、接口、字段信息）
    private List<BizDataPrivilegeBean> privileges;
    //当前接口不可见字段
    private List<String> filterFields;
    //当前接口所属于子系统id
    private Integer subSystemId;


    @NoArgsConstructor
    @Data
    public static class UserBean {
        private Integer userId;
        private String account;
        private String name;
        private String idNumber;
        private String certType;
        private String phone;
        private String mobile;
        private String position;
        private String startDate;
        private String endDate;
        private String userType;//用户性质
        private String drCode;//医师代码
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

    /**权限系统只返回当前接口所对应的子系统的权限*/
    @NoArgsConstructor
    @Data
    public static class BizDataPrivilegeBean {

        private Integer subSystemId;//必需
        private List<String> poolareaNos;//医保
        private List<String> medicalInstitutionIds;//医院、医保
        private List<String> departmentIds;//医院、医保

    }
}
