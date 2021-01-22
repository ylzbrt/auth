package com.ylzinfo.brt.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylzinfo.brt.intercepter.AnonymousInterceptor;
import com.ylzinfo.brt.intercepter.ServiceAuthFilter;
import com.ylzinfo.brt.intercepter.UserAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


@Configuration
@EnableWebMvc
public class AuthWebMvcConfigurer implements WebMvcConfigurer {


    @Autowired
    ServiceAuthFilter serviceAuthFilter;

    @Autowired
    UserAuthFilter userAuthFilter;
    @Autowired
    AnonymousInterceptor anonymousInterceptor;
    @Autowired
    YlzConfig ylzConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //以下都是swagger的资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }


    /**
     * 解决配置文件中以下配置失效的问题
     * spring.jackson.time-zone=GMT+8
     * spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();

        // 时间格式化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 设置格式化内容
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //加入顺序即为执行顺序，每个拦截器只在有传各自权限验证参数的情况，才做拦截
        //permissionInterceptor只在有传uid,token生效
        //apiInterceptor只在有传appid,appsecret生效
        //最后未被拦截的请求落入anonymousInterceptor进行验证，这些请求未携带权限验证参数，统一抛出错误
        //服务间调用


        registry.addInterceptor(serviceAuthFilter).addPathPatterns("/**");
        if (!ylzConfig.isSkipUserCheck()) {
            //用户权限
            registry.addInterceptor(userAuthFilter).addPathPatterns("/**");
            //拦截未携带凭证的请求
            registry.addInterceptor(anonymousInterceptor).addPathPatterns("/**");
        }


    }
}

