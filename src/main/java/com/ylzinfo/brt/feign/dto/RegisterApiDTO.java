package com.ylzinfo.brt.feign.dto;

import com.ylzinfo.brt.service.impl.ApiServiceImpl;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterApiDTO {

    private List<ApiItem> apis;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiItem{
        private String serviceName;
        private String apiCategory;
        private String httpMethod;
        private String url;
        private String apiName;
        private Set<ApiField> apiFields;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiField{
        private String enName;
        private String cnName;
    }
}
