package com.ylzinfo.brt.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ylzinfo.brt.config.YlzConfig;
import com.ylzinfo.brt.feign.AuthPrivilegeFeignClient;
import com.ylzinfo.brt.feign.dto.RegisterApiDTO;
import com.ylzinfo.brt.service.ApiService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @Autowired
    YlzConfig ylzConfig;
    private String defaultPackage = "com.ylzinfo.brt.controller";

    @Autowired
    AuthPrivilegeFeignClient privilegeFeignClient;

    @Override
    public void scan() {
        log.info("start scan api");
        List<String> allPackages = new ArrayList<>();
        allPackages.add(defaultPackage);
        allPackages.addAll(ylzConfig.getControllerPackages());

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
                    final RegisterApiDTO.ApiItem apiItem = new RegisterApiDTO.ApiItem(apiCategory, api.getMethod(), fullUrl, name);
                    apis.add(apiItem);
                }

            }
        }
        final RegisterApiDTO dto = new RegisterApiDTO();
        dto.setApis(apis);
        privilegeFeignClient.registerApi(dto);
    }

    private String getApiCategory(String parentPath) {
        return parentPath.replaceAll("/", "");
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
