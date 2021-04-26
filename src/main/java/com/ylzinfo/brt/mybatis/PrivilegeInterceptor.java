package com.ylzinfo.brt.mybatis;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.ylzinfo.brt.config.SqlInterceptorConfig;
import com.ylzinfo.brt.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;


import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
    private Properties properties;
    @Autowired
    SqlInterceptorConfig sqlInterceptorConfig;

    @Autowired
    UserInfoService userInfoService;
    private AntPathMatcher antPathMatcher=new AntPathMatcher(".");

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

        // 仅拦截 select 查询
        //if (!sqlCommandType.equals(SqlCommandType.SELECT.toString())) {
        //    return invocation.proceed();
        //}

        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String origSql = boundSql.getSql();
        log.info("原始SQL: {}", origSql);


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

        log.info("改写的SQL: {}", newSql);

        return invocation.proceed();
    }

    private String makeNewSql(String id, String origSql) {
        //是否需要拦截取
        if (!isNeedIntercept(id)) {
            return origSql;
        }
        String where = userInfoService.getBizPrivilegeSql("poolarea_no", "medical_institution_id", "department_id");
        if(where.equals("1=1")){
            log.info("超级管理员拥有所有权限，未做sql拦截");
            return origSql;
        }
        String newSql= String.format("select * from (%s) t where %s", origSql, where);
        log.info("改写后的sql={}",newSql);
        return newSql;
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
//            final boolean match = ReUtil.isMatch(method, id);
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
