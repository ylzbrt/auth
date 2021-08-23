package com.ylzinfo.brt.service;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.ylzinfo.brt.vo.CheckUserVO.UserBean;

import com.ylzinfo.brt.service.impl.UserInfoServiceImpl;
import com.ylzinfo.brt.vo.CheckUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.ListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@Slf4j
public class UserInfoServiceTest {

    @Test
    public void getBizPrivilegeSql() {
        UserInfoService userInfoService = new UserInfoServiceImpl();
        final String sql = userInfoService.getBizPrivilegeSql("poolarea_no","aab301", "medical_institution_id", "department_id");
        log.info("sql={}", sql);
    }

    @Test
    public void reglReplace() {
        String tpl = "select * from xxx where a.xxx=1 " +
                " and privilege('a.poolarea_no','b.mdedical_id','b.dep_id')" +
                " and privilege('a.统筹区','b.机构编码','b.科室')" +
                " and privilege('a.aaa027','b.akb020','b.kkbh')";
        final String regex = "privilege\\('(.*?)'\\s*?,\\s*?'(.*?)'\\s*?,\\s*?'(.*?)'\\)";
        final List<String> field0 = ReUtil.findAll(regex, tpl, 0);
        final List<String> field1 = ReUtil.findAll(regex, tpl, 1);
        final List<String> field2 = ReUtil.findAll(regex, tpl, 2);
        final List<String> field3 = ReUtil.findAll(regex, tpl, 3);
        log.info("{}{},{},{}", field1, field2, field3);
        for (int i = 0; i < field1.size(); i++) {
            tpl=tpl.replace(field0.get(i),getSql(field1.get(i), field2.get(i), field3.get(i)));
        }
        log.info("tpl={}", tpl);
        Assert.assertEquals(tpl,"select * from xxx where a.xxx=1  and (a.poolarea_no='350000' and b.mdedical_id='H0001' and b.dep_id='xnk') and (a.统筹区='350000' and b.机构编码='H0001' and b.科室='xnk') and (a.aaa027='350000' and b.akb020='H0001' and b.kkbh='xnk')");
    }
    @Test
    public void reglReplace2() {
        String tpl = "select * from xxx where a.xxx=1 " +
                " and privilege('a.poolarea_no','b.mdedical_id')" +
                " and privilege('a.统筹区','b.机构编码','b.科室')" +
                " and privilege('a.aaa027','b.akb020','b.kkbh')";
        final String regex = "privilege\\('(.*?)'\\s*?,\\s*?'(.*?)'\\s*?,\\s*?'(.*?)'\\)";
        final List<String> field0 = ReUtil.findAll(regex, tpl, 0);
        final List<String> field1 = ReUtil.findAll(regex, tpl, 1);
        final List<String> field2 = ReUtil.findAll(regex, tpl, 2);
        final List<String> field3 = ReUtil.findAll(regex, tpl, 3);
        log.info("{}{},{},{}", field1, field2, field3);
        for (int i = 0; i < field1.size(); i++) {
            tpl=tpl.replace(field0.get(i),getSql(field1.get(i), field2.get(i), field3.get(i)));
        }
        log.info("tpl={}", tpl);
        Assert.assertEquals(tpl,"select * from xxx where a.xxx=1  and (a.poolarea_no='350000' and b.mdedical_id='H0001' and b.dep_id='xnk') and (a.统筹区='350000' and b.机构编码='H0001' and b.科室='xnk') and (a.aaa027='350000' and b.akb020='H0001' and b.kkbh='xnk')");
    }


    public String getSql(String aaa027F, String akb020F, String ksF) {
        return StrUtil.format("({}='350000' and {}='H0001' and {}='xnk')", aaa027F, akb020F, ksF);
    }
}
