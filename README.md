# 安装到本地仓库  
mvn install:install-file -DgroupId=com.github.ylzbrt -DartifactId=auth -Dversion=master-SNAPSHOT -Dpackaging=jar -Dfile=target/auth-1.0.0-SNAPSHOT.jar


# 使用示例
    原始语句：
    
    select * from organization a
    <where>
      privilege('a.aaa027','a.aab301','','')
    </where>
    
    
    拦截后语句
    
    select *
     FROM organization a
     WHERE (a.aaa027 in ('350100','350200'));


    select *
     FROM organization a
     WHERE (a.aab301 in ('350101','350203'));
     
# 发布到jitpack

git push ylz master

