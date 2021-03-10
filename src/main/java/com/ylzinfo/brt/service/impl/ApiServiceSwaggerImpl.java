package com.ylzinfo.brt.service.impl;

import cn.hutool.core.util.ReUtil;
import com.google.common.base.Strings;
import com.ylzinfo.brt.feign.AuthPrivilegeFeignClient;
import com.ylzinfo.brt.feign.dto.RegisterApiDTO;
import com.ylzinfo.brt.service.ApiInfoService;
import io.swagger.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import springfox.documentation.service.Documentation;
import springfox.documentation.swagger.common.HostNameProvider;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class ApiServiceSwaggerImpl implements ApiInfoService {
    @Autowired
    springfox.documentation.spring.web.DocumentationCache documentationCache;
    @Autowired
    ServiceModelToSwagger2Mapper mapper;

    @Value("${spring.application.name}")
    String serviceName;
    @Autowired
    AuthPrivilegeFeignClient privilegeFeignClient;

    @Override
    public void scan() {
        String groupName = "default";
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            log.warn("Unable to find specification for group {}", groupName);
            return;
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
//        final HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

//        UriComponents uriComponents = HostNameProvider.componentsFrom(servletRequest, swagger.getBasePath());
//        swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
        swagger.basePath("/");
//        final String json = JSONUtil.toJsonStr(swagger);
//        log.info("json={}", json);
        List<RegisterApiDTO.ApiItem> apis = new ArrayList<>();

        swagger.getPaths().forEach((path, v) -> {
            each(swagger, apis, "GET", path, v.getGet());
            each(swagger, apis, "POST", path, v.getPost());
            each(swagger, apis, "PUT", path, v.getPut());
            each(swagger, apis, "DELETE", path, v.getDelete());
        });
        final RegisterApiDTO dto = new RegisterApiDTO();
        dto.setApis(apis);
        privilegeFeignClient.registerApi(dto);

    }

    /***
     * 处理每个api
     */

    private void each(Swagger swagger, List<RegisterApiDTO.ApiItem> apis, String httpMethod, String url, Operation operation) {
        if (operation == null) {
            return;
        }
        final RegisterApiDTO.ApiItem apiItem = new RegisterApiDTO.ApiItem();
        apiItem.setServiceName(serviceName);
        apiItem.setHttpMethod(httpMethod);
        apiItem.setUrl(pathVarToStar(url));
        apiItem.setApiCategory(operation.getTags().get(0));
        apiItem.setApiName(operation.getSummary() != null ? operation.getSummary() : url);
        handlerApiFields(swagger, operation, apiItem);
        apis.add(apiItem);
    }

    /**
     * 处理api的字段
     * 针对没有返回值的情况  则不处理
     */
    private void handlerApiFields(Swagger swagger, Operation operation, RegisterApiDTO.ApiItem apiItem) {
        final Model responseSchema = operation.getResponses().get("200").getResponseSchema();
        if (responseSchema == null || !(responseSchema instanceof RefModel)) {
            return;
        }

        final String simpleRef = ((RefModel) responseSchema).getSimpleRef();
        if (simpleRef == null) {
            return;
        }

        apiItem.setApiFields(getApiFields(swagger, simpleRef));

    }

    public String getSimplteObject(String t) {
        final String s = ReUtil.get("«(.*)»", t, 1);
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        if (s.contains("«")) {
            return getSimplteObject(s);
        }
        return s;
    }

    private String pathVarToStar(String url) {
        ///{ruleCode}  =>  /*
        return ReUtil.replaceAll(url, "\\{.*?\\}", "*");
    }

    private Set<RegisterApiDTO.ApiField> getApiFields(Swagger swagger, String simpleRef) {
        Set<RegisterApiDTO.ApiField> fields = new HashSet<>();

        //RepoProjectMap对象
        fields.addAll(getSimpleApiFields(swagger, getSimplteObject(simpleRef)));
        return fields;

    }

    private Set<RegisterApiDTO.ApiField> getSimpleApiFields(Swagger swagger, String defName) {
        Set<RegisterApiDTO.ApiField> fields = new HashSet<>();


        //RepoProjectMap对象
        final Model model = swagger.getDefinitions().get(defName);
        if (model == null || model.getProperties() == null) {
            return fields;
        }
        model.getProperties().forEach((k, v) -> {
            RegisterApiDTO.ApiField apiField = new RegisterApiDTO.ApiField(k, v.getDescription() != null ? v.getDescription() : k);
            fields.add(apiField);
        });
        return fields;
    }


}
