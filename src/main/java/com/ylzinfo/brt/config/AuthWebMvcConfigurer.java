package com.ylzinfo.brt.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylzinfo.brt.intercepter.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthWebMvcConfigurer implements WebMvcConfigurer {


    @Autowired
    ServiceAuthFilter serviceAuthFilter;

    @Autowired
    UserAuthFilter userAuthFilter;
    @Autowired
    TestUserAuthFilter testUserAuthFilter;
    @Autowired
    LastInterceptor lastInterceptor;
    @Autowired
    FirstInterceptor firstInterceptor;

    @Autowired
    TraceInterceptor traceInterceptor;
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
        /**
        此处不能新增一个MappingJackson2HttpMessageConverter并添加在converters的最前面，否则引起xxl-job-executor注册失败
         http://127.0.0.1:8093/job-admin/api/registry
         header: 'Content-Type: application/json'
         {"registryGroup":"EXECUTOR","registryKey":"job-executor","registryValue":"http://127.0.0.1:8095/"}
         */
        for (HttpMessageConverter<?> converter : converters) {
            if(converter instanceof MappingJackson2HttpMessageConverter){
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            }
        }

    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**");
        registry.addInterceptor(serviceAuthFilter).addPathPatterns("/**");
        registry.addInterceptor(testUserAuthFilter).addPathPatterns("/**");
        registry.addInterceptor(userAuthFilter).addPathPatterns("/**");
        registry.addInterceptor(traceInterceptor).addPathPatterns("/**");

    }
}

