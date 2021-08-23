package com.ylzinfo.brt.mybatis;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.ylzinfo.brt.config.SqlInterceptorConfig;
import com.ylzinfo.brt.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/***
 用于对查询数据的权限进行拦截，自动修改sql语句，拼接上统筹区、医疗机构、科室的语句
 使用方式：
 在需要替换的sql位置，增加占位符，占位符必须符合以下正则表达式格式：
 privilege\('(.*?)'\s*?,\s*?'(.*?)'\s*?,\s*?'(.*?)\)
 可到https://tool.oschina.net/regex/ 进行验证



 示例1: 按统筹区过滤数据
 原始语句： select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','','')
 修改后语句：select * from xxx a ,yyy b where a.field1=b.field1 and a.poolarea_no in ('350100')
 示例2: 按统筹区、医疗机构过滤数据
 原始语句： select * from xxx a ,yyy b where a.field1=b.field1 and privilege('a.poolarea_no','a.medical_institution_id','')
 修改后语句：select * from xxx a ,yyy b where a.field1=b.field1 and  a.poolarea_no in ('350100') and a.medical_institution_id in ('H000001')
 示例3: 按医疗机构过滤数据
 原始语句： select * from xxx a ,yyy b where a.field1=b.field1 and privilege('','a.medical_institution_id','')
 修改后语句：select * from xxx a ,yyy b where a.field1=b.field1 and a.medical_institution_id in ('H000001')
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class,
        method = "query",
        args = {MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class})
})
@Component
public class PrivilegeInterceptor implements Interceptor {
    public static final String FUN_NAME = "privilege";
    private Properties properties;
    @Autowired
    SqlInterceptorConfig sqlInterceptorConfig;

    @Autowired
    UserInfoService userInfoService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher(".");


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        // id为执行的mapper方法的全路径名，如com.mapper.UserMapper
        //com.ylzinfo.brt.mapper.TestMapper.test
        String id = ms.getId();
        // sql语句类型 select、delete、insert、update
        String sqlCommandType = ms.getSqlCommandType().toString();
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String origSql = boundSql.getSql();
        log.debug("原始SQL: {}", origSql);
        // 组装新的 sql
        String newSql = makeNewSql(id, origSql);
        // 重新new一个查询语句对象
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
        // 把新的查询放到statement里
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        Object[] queryArgs = invocation.getArgs();
        queryArgs[0] = newMs;
        log.debug("改写的SQL: {}", newSql);
        return invocation.proceed();
    }

    /***
     * 构造出一个新的sql语句
     * @param id mapper中的id
     * @param origSql 原始sql
     * @return
     */
    public String makeNewSql(String id, String origSql) throws Throwable {
        //是否需要拦截取
        if (!isNeedIntercept(id)) {
            return origSql;
        }
        final List<ConditionField> conditionFields = parse(origSql);
        if (CollectionUtil.isEmpty(conditionFields)) {
            //没有占位符
            return origSql;
        }
        //开始进行替换
        String newSql = origSql;
        for (ConditionField conditionField : conditionFields) {
            String where = userInfoService.getBizPrivilegeSql(conditionField.getPoolareaNoField(), conditionField.getDistrictField(), conditionField.getDepartmentField(), conditionField.getMedicalInstitutionField());
            String beforeReplace = newSql;
            newSql = newSql.replace(conditionField.getPlaceholder(), where);
            log.debug("conditionField={},where={},beforeReplace={},afterReplace={}", conditionField, where, beforeReplace, newSql);
        }
        log.debug("改写后的sql={}", newSql);
        return newSql;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConditionField {
        private String placeholder;
        private String poolareaNoField;
        private String districtField;
        private String medicalInstitutionField;
        private String departmentField;
    }

    public List<ConditionField> parse(String origSql) throws Exception {

        final String regex = "privilege\\('(.*?)'\\s*?,\\s*?'(.*?)'\\s*?,\\s*?'(.*?)'\\s*?,\\s*?'(.*?)'\\)";
        final List<String> field0 = ReUtil.findAll(regex, origSql, 0);
        if (CollectionUtil.isEmpty(field0)) {
            log.debug("未发现占用符");
        }
        final List<String> field1 = ReUtil.findAll(regex, origSql, 1);
        final List<String> field2 = ReUtil.findAll(regex, origSql, 2);
        final List<String> field3 = ReUtil.findAll(regex, origSql, 3);
        final List<String> field4 = ReUtil.findAll(regex, origSql, 4);
        log.debug("field0={}", field0);
        log.debug("field1={}", field1);
        log.debug("field2={}", field2);
        log.debug("field3={}", field3);
        log.debug("field4={}", field4);

        List<ConditionField> conditionFields = new ArrayList<>();
        for (int i = 0; i < field1.size(); i++) {
            final ConditionField conditionField = new ConditionField(field0.get(i), field1.get(i), field2.get(i),field3.get(i),field4.get(i));
            /*
             以下为错误配置
             and privilege('a.poolarea_no','a.akb020') and privilege('a.aaa027','a.akb020','a.ksid')
             field0= privilege('a.poolarea_no','a.akb020') and privilege('a.aaa027','a.akb020','a.ksid')
             field1= a.poolarea_no
             field2= a.akb020') and privilege('a.aaa027
             field3= a.akb020','a.ksid
            */
            if(
                conditionField.getPoolareaNoField().contains(FUN_NAME)||
                conditionField.getDistrictField().contains(FUN_NAME)||
                conditionField.getMedicalInstitutionField().contains(FUN_NAME)||
                conditionField.getDepartmentField().contains(FUN_NAME)
            ){
                throw new Exception("sql语句占位符格式错误，错误片段=" + conditionField.getPlaceholder());
            }

            conditionFields.add(conditionField);
        }

        return conditionFields;
    }


    private boolean isNeedIntercept(String id) {
        if (sqlInterceptorConfig == null) {
            log.warn("未配置sqlinterceptor,不进行sql拦截");
            return false;
        }
        if (StrUtil.isBlank(sqlInterceptorConfig.getType())) {
            log.warn("未配置sqlinterceptor.type,不进行sql拦截");
            return false;
        }
        if (!CollectionUtil.contains(Arrays.asList("include", "exclude"), sqlInterceptorConfig.getType())) {
            log.error("sqlinterceptor.type配置错误,可选值为include、exclude");
            return false;
        }

        final boolean empty = CollectionUtil.isEmpty(sqlInterceptorConfig.getMethods());
        if ("include".equals(sqlInterceptorConfig.getType())) {
            if (empty) {
                return false;
            }
            return isMatch(sqlInterceptorConfig.getMethods(), id);
        } else if ("exclude".equals(sqlInterceptorConfig.getType())) {
            if (empty) {
                return true;
            }
            return !isMatch(sqlInterceptorConfig.getMethods(), id);
        }
        return false;

    }

    private boolean isMatch(List<String> methods, String id) {
        for (String method : methods) {
            final boolean match = antPathMatcher.match(method, id);
            if (match) {
                return true;
            }
        }
        return false;
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties0) {
        this.properties = properties0;
    }


}
