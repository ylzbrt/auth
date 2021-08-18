package com.ylzinfo.brt.mybatis;

import com.ylzinfo.brt.config.SqlInterceptorConfig;
import com.ylzinfo.brt.service.impl.UserInfoServiceDevImpl;
import com.ylzinfo.brt.service.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class PrivilegeInterceptorTest {

    PrivilegeInterceptor privilegeInterceptor = new PrivilegeInterceptor();

    private String sql1 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','','')";
    private String sql2 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.akb020','')";
    private String sql3 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.akb020','a.ks')";
    //错误示例
    private String sql4 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.akb020')";
    private String sql5 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.akb020','a.ksid') and privilege('a.aaa027','a.akb020','a.ksid')";
    //错误示例
    private String sql6 = "select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.akb020') and privilege('a.aaa027','a.akb020','a.ksid')";

    @Before
    public void init() {
        final SqlInterceptorConfig sqlInterceptorConfig = new SqlInterceptorConfig();
        sqlInterceptorConfig.setType("exclude");
        privilegeInterceptor.sqlInterceptorConfig= sqlInterceptorConfig;
        privilegeInterceptor.userInfoService= new UserInfoServiceDevImpl();
    }

    @Test
    public void testMakeNewSql1() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql1);
        log.info("sql={}", xxx);
    }
    @Test
    public void testMakeNewSql2() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql2);
        log.info("sql={}", xxx);
    }
    @Test
    public void testMakeNewSql3() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql3);
        log.info("sql={}", xxx);
    }
    @Test
    public void testMakeNewSql4() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql4);
        log.info("sql={}", xxx);
    }

    @Test
    public void testMakeNewSql5() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql5);
        log.info("sql={}", xxx);
    }
    @Test
    public void testMakeNewSql6() throws Throwable {
        final String xxx = privilegeInterceptor.makeNewSql("xxx", sql6);
        log.info("sql={}", xxx);
    }
    @Test
    public void parse1() throws Exception {
        List<PrivilegeInterceptor.ConditionField> arr = privilegeInterceptor.parse(sql1);
        log.info("arr={}", arr);
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).getPoolareaNoField(), "a.poolarea_no");
        Assert.assertEquals(arr.get(0).getMedicalInstitutionField(), "");
        Assert.assertEquals(arr.get(0).getDepartmentField(), "");
    }

    @Test
    public void parse2() throws Exception {
        List<PrivilegeInterceptor.ConditionField> arr = privilegeInterceptor.parse(sql2);
        log.info("arr={}", arr);
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).getPoolareaNoField(), "a.poolarea_no");
        Assert.assertEquals(arr.get(0).getMedicalInstitutionField(), "a.akb020");
        Assert.assertEquals(arr.get(0).getDepartmentField(), "");
    }

    @Test
    public void parse3() throws Exception {
        List<PrivilegeInterceptor.ConditionField> arr = privilegeInterceptor.parse(sql3);
        log.info("arr={}", arr);
        Assert.assertEquals(arr.size(), 1);
        Assert.assertEquals(arr.get(0).getPoolareaNoField(), "a.poolarea_no");
        Assert.assertEquals(arr.get(0).getMedicalInstitutionField(), "a.akb020");
        Assert.assertEquals(arr.get(0).getDepartmentField(), "a.ks");
    }


    @Test
    public void parse4() throws Exception {
        List<PrivilegeInterceptor.ConditionField> arr = privilegeInterceptor.parse(sql4);
        log.info("arr={}", arr);
        Assert.assertEquals(arr.size(), 0);

    }

    @Test
    public void parse5() throws Exception {
        List<PrivilegeInterceptor.ConditionField> arr = privilegeInterceptor.parse(sql5);
        Assert.assertEquals(arr.size(), 2);
        Assert.assertEquals(arr.get(0).getPoolareaNoField(), "a.poolarea_no");
        Assert.assertEquals(arr.get(0).getMedicalInstitutionField(), "a.akb020");
        Assert.assertEquals(arr.get(0).getDepartmentField(), "a.ksid");

        Assert.assertEquals(arr.get(1).getPoolareaNoField(), "a.aaa027");
        Assert.assertEquals(arr.get(1).getMedicalInstitutionField(), "a.akb020");
        Assert.assertEquals(arr.get(1).getDepartmentField(), "a.ksid");

    }
}