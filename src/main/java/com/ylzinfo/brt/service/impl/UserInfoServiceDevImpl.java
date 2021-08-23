package com.ylzinfo.brt.service.impl;

import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.vo.CheckUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

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
        privileges.add(privilegesBean);

        //医院
        final CheckUserVO.BizDataPrivilegeBean privilegesBean1 = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean1.setPoolareaNos(Arrays.asList("350200"));
        privilegesBean1.setMedicalInstitutionIds(Arrays.asList("yy22","yy222"));
        privileges.add(privilegesBean1);
        //统筹区
        final CheckUserVO.BizDataPrivilegeBean privilegesBean2 = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean2.setPoolareaNos(Arrays.asList("350300"));
        privileges.add(privilegesBean2);

        //区县
        final CheckUserVO.BizDataPrivilegeBean privilegesBean3 = new CheckUserVO.BizDataPrivilegeBean();
        privilegesBean3.setPoolareaNos(Arrays.asList("350102"));
        privileges.add(privilegesBean3);

        userData.setPrivileges(privileges);
        return userData;

    }
    @Override
    public String getBizPrivilegeSql(String poolareaNoField, String districtField, String departmentField, String medicalInstitutionField) {
       return super.getBizPrivilegeSql(poolareaNoField, districtField, departmentField, medicalInstitutionField);
    }
}
