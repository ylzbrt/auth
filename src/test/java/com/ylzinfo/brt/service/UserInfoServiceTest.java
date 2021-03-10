package com.ylzinfo.brt.service;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.ylzinfo.brt.vo.CheckUserVO.UserBean;

import com.ylzinfo.brt.service.impl.UserInfoServiceImpl;
import com.ylzinfo.brt.vo.CheckUserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.ListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@Slf4j
public class UserInfoServiceTest {

    @Test
    public void getBizPrivilegeSql() {


        UserInfoService userInfoService =new UserInfoServiceImpl();


        final String sql =userInfoService.getBizPrivilegeSql("poolarea_no", "medical_institution_id", "department_id");

        log.info("sql={}",sql);
    }
}