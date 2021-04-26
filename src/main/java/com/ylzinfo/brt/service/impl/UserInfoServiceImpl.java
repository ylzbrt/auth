package com.ylzinfo.brt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.vo.CheckUserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public String getOperator() {

        return getUser().getName();
    }

    private CheckUserVO.UserBean getUser() {
        final CheckUserVO userData = getUserData();
        if (userData == null) {
            return new CheckUserVO.UserBean();
        }
        final CheckUserVO.UserBean user = userData.getUser();
        if (user == null) {
            return new CheckUserVO.UserBean();
        }
        return user;
    }

    @Override
    public String getOperatorAccount() {
        return getUser().getAccount();

    }

    @Override
    public String getClientIp() {
        return getRequest().getHeader(HttpHeaderEnum.CLIENT_IP.getCode());
    }

    @Override
    public String getOperatorId() {
        return getUser().getUserId() + "";

    }

    @Override
    public String getOrganizationId() {
        final CheckUserVO userData = getUserData();
        if (userData == null) {
            return "*";
        }
        final List<CheckUserVO.OrganizationBean> organizations = userData.getOrganizations();
        if (CollectionUtil.isNotEmpty(organizations)) {
            return organizations.get(0).getOrganizationId() + "";
        }
        return "*";

    }


    @Override
    public void saveUserData(CheckUserVO userDataBo) {
        HttpServletRequest request = getRequest();
        request.setAttribute(HttpHeaderEnum.USER_DATA.getCode(), userDataBo);
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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
/*
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
*/
        /**生产环境*/
       HttpServletRequest request = getRequest();
        return (CheckUserVO) request.getAttribute(HttpHeaderEnum.USER_DATA.getCode());
    }
/*
    @Override
    public List<String> getGrantedPoolareaNos() {
        return Optional.ofNullable(getUserData().getPrivileges())
                .orElse(new ArrayList<>())
                .stream()
                .filter(o -> o.getPoolareaNos() != null)
                .flatMap(bizDataPrivilegeBean -> bizDataPrivilegeBean.getPoolareaNos().stream())
                .collect(Collectors.toList());
    }

   @Override
    public String getGrantedPoolareaNosSql(String field) {
        final List<String> grantedPoolareaNos = getGrantedPoolareaNos();
        if (CollectionUtil.isEmpty(grantedPoolareaNos)) {
            return "1=2";
        }
        return String.format("%s in (\"%s\")", field, CollectionUtil.join(grantedPoolareaNos, "\",\""));
    }*/

    @Override
    public String getBizPrivilegeSql(String poolareaNoField, String medicalInstitutionField, String departmentField) {
        CheckUserVO userData= getUserData();
        if (CollectionUtil.isEmpty(userData.getPrivileges())) {
            if(userData.getUser().getUserId()==1){
                return "1=1";
            }
            return "1=2";
        }
        StringJoiner outJoiner = new StringJoiner(" or ", "(", ")").setEmptyValue("1=2");
        for (CheckUserVO.BizDataPrivilegeBean privilege : userData.getPrivileges()) {
            /**认最低级别*/
            //有配置科室
            if (!isEmpty(privilege.getDepartmentIds())) {
                add(outJoiner,departmentField,privilege.getDepartmentIds());
                continue;
            }
            //有配置医疗机构
            if (!isEmpty(privilege.getMedicalInstitutionIds())) {
                add(outJoiner,medicalInstitutionField,privilege.getMedicalInstitutionIds());
                continue;
            }
            //有配置统筹区
            if (!isEmpty(privilege.getPoolareaNos())) {
                add(outJoiner,poolareaNoField,privilege.getPoolareaNos());
                continue;
            }
        }
        return outJoiner.toString();
    }

    /**
     *  是
     * @param arr 统筹区、医疗机构、或科室id
     * @return
     */
    private boolean isEmpty(List<String> arr){
        boolean isEmpty = CollectionUtil.isEmpty(arr);
        if(isEmpty){
            return true;
        }
        for (String s : arr) {
            if(StrUtil.isBlank(s)){
                return true;
            }
        }
        return false;
    }

    private void add(StringJoiner outJoiner, String field, List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        StringJoiner sj = new StringJoiner("\",\"", "(\"", "\")");
        for (String id : ids) {
            sj.add(id);
        }
        outJoiner.add(field + " in " + sj.toString());

    }
}
