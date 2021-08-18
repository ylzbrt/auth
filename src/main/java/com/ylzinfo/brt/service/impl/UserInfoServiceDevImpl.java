package com.ylzinfo.brt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.vo.CheckUserVO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/***
 * 测试时使用
 */
public class UserInfoServiceDevImpl extends UserInfoServiceImpl implements UserInfoService {
    @Override
    public String getOperator() {
            return "";
     }

    private CheckUserVO.UserBean getUser() {
        return new CheckUserVO.UserBean();
    }

    @Override
    public String getOperatorAccount() {
        return getUser().getAccount();

    }

    @Override
    public String getClientIp() {
        return "127.0.0.1";
    }

    @Override
    public String getOperatorId() {
        return getUser().getUserId() + "";

    }

    @Override
    public String getOrganizationId() {
        return "*";
    }


    @Override
    public void saveUserData(CheckUserVO userDataBo) {

    }

    private HttpServletRequest getRequest() {
        return null;
    }

    @Override
    public CheckUserVO getUserData() {

        /**测试用
          (
             medical_institution_id in ("yy22","yy222")
                or poolarea_no in ("350300")
                or department_id in ("d1","d11")
         )
         */

        final CheckUserVO userData = new CheckUserVO();

        final ArrayList<CheckUserVO.BizDataPrivilegeBean> privileges = new ArrayList<>();
        //科室
        final CheckUserVO.BizDataPrivilegeBean privilegesBean = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean.setPoolareaNos(Arrays.asList("350100"));
        privilegesBean.setMedicalInstitutionIds(Arrays.asList("yy11","yy111"));
        privilegesBean.setDepartmentIds(Arrays.asList("d1","d11"));
        //医院
        final CheckUserVO.BizDataPrivilegeBean privilegesBean1 = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean1.setPoolareaNos(Arrays.asList("350200"));
        privilegesBean1.setMedicalInstitutionIds(Arrays.asList("yy22","yy222"));
        privileges.add(privilegesBean1);
        //统筹区
        final CheckUserVO.BizDataPrivilegeBean privilegesBean2 = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean2.setPoolareaNos(Arrays.asList("350300"));
        privileges.add(privilegesBean2);
        privileges.add(privilegesBean);

        userData.setPrivileges(privileges);
        return userData;

    }
    @Override
    public String getBizPrivilegeSql(String poolareaNoField, String medicalInstitutionField, String departmentField) {
       return super.getBizPrivilegeSql(poolareaNoField,medicalInstitutionField,departmentField);
    }
}
