package com.ylzinfo.brt.service;

import com.ylzinfo.brt.vo.CheckUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserInfoService {
    /**
     * 操作人名称
     */
    String getOperator();

    /**
     * 操作人账号
     */
    String getOperatorAccount();

    /**
     * 客户端IP
     */
    String getClientIp();

    /**
    操作人id/经办人id
     */
    String getOperatorId();
    /**
     * 经办机构编码
     * */
    String getOrganizationId();

    void saveUserData(CheckUserVO userDataBo);

    CheckUserVO getUserData();


    /**
     * 已授权的统筹区、医疗机构编码、科室编码
     */

    String getBizPrivilegeSql(String poolareaNoField, String medicalInstitutionField, String departmentField);
}
