# 安装到本地仓库  
mvn install:install-file -DgroupId=com.github.ylzbrt -DartifactId=auth -Dversion=master-SNAPSHOT -Dpackaging=jar -Dfile=target/auth-1.0.0-SNAPSHOT.jar


# 使用示例
    原始语句：
    
    select * from organization a
    <where>
      privilege('a.admdvs','','')
    </where>
    
    拦截后语句
    
    select *
     FROM organization a
     WHERE (a.admdvs in ('350100','350200'));


