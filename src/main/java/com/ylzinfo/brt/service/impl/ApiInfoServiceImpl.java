package com.ylzinfo.brt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.feign.AuthPrivilegeFeignClient;
import com.ylzinfo.brt.feign.dto.RegisterApiDTO;
import com.ylzinfo.brt.service.ApiInfoService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

//@Service
@Slf4j
public class ApiInfoServiceImpl implements ApiInfoService {
    @Autowired
    YlzConfig ylzConfig;
    private String defaultPackage = "com.ylzinfo.brt.controller";

    @Autowired
    AuthPrivilegeFeignClient privilegeFeignClient;

    @Value("${spring.application.name}")
    String serviceName;

    @Override
    public void scan() {
        log.info("start scan api");
        List<String> allPackages = new ArrayList<>();
        allPackages.add(defaultPackage);
        final List<String> others = ylzConfig.getScanPackages();
        if(CollectionUtil.isNotEmpty(others)){
            allPackages.addAll(others);
        }

        List<RegisterApiDTO.ApiItem> apis = new ArrayList<>();
        for (String pkg : allPackages) {
            final Set<Class<?>> controllers = ClassUtil.scanPackage(pkg);
            for (Class<?> controller : controllers) {
                final String parentPath = getControllerUrl(controller);
                final Method[] methods = ReflectUtil.getMethods(controller);
                for (Method method : methods) {
                    if (isInnerMethod(method)) {
                        continue;
                    }
                    Api api = getApi(method);
                    if (StringUtils.isEmpty(api.getMethod())) {
                        continue;
                    }
                    String name = getMethodName(method);
                    final String fullUrl = getFullUrl(parentPath, api);
                    log.info("http_method={},url={},name={}", api.getMethod(), fullUrl, name);

                    String apiCategory = getApiCategory(fullUrl);

                    //VO的字段
                    final Set<RegisterApiDTO.ApiField> voFields = getVOFields(method);
                    final RegisterApiDTO.ApiItem apiItem = new RegisterApiDTO.ApiItem(serviceName,apiCategory, api.getMethod(), fullUrl, name,voFields);
                    apis.add(apiItem);
                }

            }
        }
        final RegisterApiDTO dto = new RegisterApiDTO();
        dto.setApis(apis);
        privilegeFeignClient.registerApi(dto);
    }



    public Set<RegisterApiDTO.ApiField> getVOFields(Method method) {
        Set<RegisterApiDTO.ApiField> apiFields=new HashSet<>();
        for (Class returnClass : getReturnClass(method)) {
            scanFieldItem(apiFields,returnClass);
        }
        return apiFields;
    }
    private List<Class> getReturnClass(Method method){
        List<Class> classList=new ArrayList<>();
        final AnnotatedType annotatedReturnType = method.getAnnotatedReturnType();
        final Type returnType = annotatedReturnType.getType();
        if (returnType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) returnType)
                    .getActualTypeArguments();// 泛型类型列表
            for (Type type : types) {
                try {
                    classList.add(Class.forName(type.getTypeName()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                classList.add(Class.forName(returnType.getTypeName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classList;
    }

    private void scanFieldItem(Set<RegisterApiDTO.ApiField> apiFields, Class aclass) {
        for (Field field : FieldUtils.getAllFields(aclass) ) {
            field.setAccessible(true);
            if(field.getType().isAssignableFrom(List.class)){
                Type gt = field.getGenericType();    //得到泛型类型
                if(gt instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType)gt;
                    Class lll = (Class)pt.getActualTypeArguments()[0];
                    scanFieldItem(apiFields,lll);
                }else if (gt instanceof TypeVariable ){
                    log.error("未知类型={}",aclass);
                }

                continue;
            }
            final ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            final RegisterApiDTO.ApiField apiField = new RegisterApiDTO.ApiField();
            apiField.setEnName(field.getName());

            if (annotation != null) {
                final String value = annotation.value();
                if(!org.springframework.util.StringUtils.isEmpty(value)){
                    apiField.setCnName(value);
                }
            }else{
                apiField.setCnName(field.getName());
            }
            apiFields.add(apiField);
        }


    }

    private String getApiCategory(String parentPath) {
        if(!parentPath.contains("/")){
            return parentPath;
        }
        return parentPath.split("/")[1];
    }

    private boolean isInnerMethod(Method method) {
        List<String> methods = Arrays.asList(
                "finalize",
                "wait",
                "registerNatives",
                "equals",
                "toString",
                "hashCode",
                "getClass",
                "clone",
                "notify",
                "notifyAll");
        return methods.contains(method.getName());
    }

    private String getFullUrl(String parentPath, Api api) {
        return (parentPath + "/" + api.getUrl()).replaceAll("/+", "/");
    }


    private String getMethodName(Method method) {
        if (method.isAnnotationPresent(ApiOperation.class)) {
            final ApiOperation annotation = method.getAnnotation(ApiOperation.class);
            return annotation.value();
        }
        return method.getName();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Api {
        private String method;
        private String url;
    }

    private Api getApi(Method method) {
        List<Class> classes = Arrays.asList(PostMapping.class, GetMapping.class, PutMapping.class, DeleteMapping.class, RequestMapping.class);
        for (Class aClass : classes) {
            if (method.isAnnotationPresent(aClass)) {
                final Object annotation = method.getAnnotation(aClass);
                final Method method1 = ReflectUtil.getMethod(aClass, "value");//url
                try {
                    String url = (String) (((String[]) method1.invoke(annotation))[0]);
                    return new Api(getHttpMethod(annotation), pathVarToStar(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //todo
        return new Api("", method.getName());

    }

    private String getControllerUrl(Class controller) {
        List<Class> classes = Arrays.asList(PostMapping.class, GetMapping.class, PutMapping.class, DeleteMapping.class, RequestMapping.class);
        for (Class aClass : classes) {
            if (controller.isAnnotationPresent(aClass)) {
                final Object annotation = controller.getAnnotation(aClass);
                final Method method1 = ReflectUtil.getMethod(aClass, "value");//url
                try {
                    String url = (String) (((String[]) method1.invoke(annotation))[0]);
                    return url;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";

    }

    private String getHttpMethod(Object annotation) {
        //@org.springframework.web.bind.annotation.PutMapping(path=[], headers=[], name=, produces=[], params=[], value=[], consumes=[])
        final String str = annotation.toString();
        return ReUtil.get("@org\\.springframework\\.web\\.bind\\.annotation\\.(.*?)Mapping\\(", str, 1).toUpperCase();
    }

    private String pathVarToStar(String url) {
        ///{ruleCode}  =>  /*
        return ReUtil.replaceAll(url, "\\{.*?\\}", "*");
    }

    private boolean isApi(Annotation[] declaredAnnotations) {
        for (Annotation declaredAnnotation : declaredAnnotations) {
            log.info("declaredAnnotation={}", declaredAnnotation);
        }
        return false;
    }
}
