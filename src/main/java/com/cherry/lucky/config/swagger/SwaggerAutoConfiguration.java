package com.cherry.lucky.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author : ganxiongwen
 * Date: 2022/4/21 15:40
 * Description: swagger 配置管理类
 * ClassName: SwaggerAutoConfiguration
 * Package: com.cherry.flow.common.config.swagger
 * Copyright (c) 2022,All Rights Reserved.
 */
@Configuration
@EnableOpenApi
@EnableConfigurationProperties(SwaggerProperties.class)
// This SuppressWarnings is used to suppress "application context that are not configured for this file" warnings
@SuppressWarnings(value = "all")
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Autowired
    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .apis(RequestHandlerSelectors.basePackage("com.cherry.lucky.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息的简介
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact(swaggerProperties.getName(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();
    }

}
