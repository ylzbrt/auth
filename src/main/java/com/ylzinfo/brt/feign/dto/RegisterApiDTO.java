package com.ylzinfo.brt.feign.dto;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterApiDTO {

    private List<ApiItem> apis;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiItem{
        private String apiCategory;
        private String httpMethod;
        private String url;
        private String apiName;
    }
}
