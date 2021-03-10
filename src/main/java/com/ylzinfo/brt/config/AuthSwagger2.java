package com.ylzinfo.brt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2

public class AuthSwagger2 {

	@Bean
	public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<>();
		ParameterBuilder tokenPar1 = new ParameterBuilder();
		ParameterBuilder tokenPar2 = new ParameterBuilder();
		tokenPar1.name("X-USER-ID").defaultValue("2").description("用户ID").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		tokenPar2.name("X-USER-TOKEN").defaultValue("4226866376a84c0a90e87a8c71f1fcdd").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar1.build());
        pars.add(tokenPar2.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.globalOperationParameters(pars)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ylzinfo.brt"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("接口文档")
				.build();
	}
}
