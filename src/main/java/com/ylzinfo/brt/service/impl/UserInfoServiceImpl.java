package com.ylzinfo.brt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ylzinfo.brt.constant.HttpHeaderEnum;
import com.ylzinfo.brt.service.UserInfoService;
import com.ylzinfo.brt.vo.CheckUserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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


        HttpServletRequest request = getRequest();
        return (CheckUserVO) request.getAttribute(HttpHeaderEnum.USER_DATA.getCode());
    }


    @Override
    public String getBizPrivilegeSql(String poolareaNoField, String districtField, String departmentField, String medicalInstitutionField) {
        CheckUserVO userData = getUserData();
        if (CollectionUtil.isEmpty(userData.getPrivileges())) {
            if (userData.getUser().getUserId() == 1) {
                return "1=1";
            }
            return "1=2";
        }
        StringJoiner outJoiner = new StringJoiner(" or ", "(", ")\n").setEmptyValue("1=2");
        for (CheckUserVO.BizDataPrivilegeBean privilege : userData.getPrivileges()) {
            /**认最低级别*/
            //有配置科室
            if (StrUtil.isNotBlank(departmentField) && !isEmpty(privilege.getDepartmentIds())) {
                add(outJoiner, departmentField, privilege.getDepartmentIds());
                continue;
            }
            //有配置医疗机构
            if (StrUtil.isNotBlank(medicalInstitutionField) && !isEmpty(privilege.getMedicalInstitutionIds())) {
                add(outJoiner, medicalInstitutionField, privilege.getMedicalInstitutionIds());
                continue;
            }
            //有配置统筹区
            if (StrUtil.isNotBlank(poolareaNoField) && !isEmpty(privilege.getPoolareaNos())) {
                add(outJoiner, poolareaNoField, districtField, privilege.getPoolareaNos());
                continue;
            }
        }
        return outJoiner.toString();
    }


    /**
     * 是
     *
     * @param arr 统筹区、医疗机构、或科室id
     * @return
     */
    private boolean isEmpty(List<String> arr) {
        boolean isEmpty = CollectionUtil.isEmpty(arr);
        if (isEmpty) {
            return true;
        }
        for (String s : arr) {
            if (StrUtil.isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    private void add(StringJoiner outJoiner, String field, List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        StringJoiner sj = new StringJoiner("','", "('", "')\n");
        for (String id : ids) {
            sj.add(id);
        }
        outJoiner.add(field + " in " + sj.toString());

    }

    /***
      (
            (a.poolarea_no = '350000')
            or (a.poolarea_no = '350200')
            or (a.poolarea_no = '350301')
     )
     */
    private void add(StringJoiner outJoiner, String poolareaNoField, String districtField, List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        StringJoiner sj = new StringJoiner("or", "(", ")\n");
        final String tpl = "( {} = {} )";
        for (String id : ids) {
            //省级或市级
            if (id.endsWith("0000") || id.endsWith("00")) {
                sj.add(StrUtil.format(tpl,poolareaNoField,id));
            }
            //区县
            else{
                sj.add(StrUtil.format(tpl,districtField,id));
            }
        }
        outJoiner.add(sj.toString());
    }
}
