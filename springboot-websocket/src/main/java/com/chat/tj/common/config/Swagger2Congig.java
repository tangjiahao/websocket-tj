package com.chat.tj.common.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjing
 * @date 2020/12/23 09:30
 */
@Configuration
@EnableSwagger2
public class Swagger2Congig {
    @Bean
    public Docket createRestApi(){


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {

        List<ApiKey> list = new ArrayList<>();
        list.add(new ApiKey("token", "token", "header"));
        return list;
    }

    private List<SecurityContext> securityContexts() {

        List<SecurityContext> list = new ArrayList<>();
        list.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build());
        return list;
    }

    private List<SecurityReference> defaultAuth() {

        List<SecurityReference> list = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("", "");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        list.add(new SecurityReference("token", authorizationScopes));

        return list;
    }

    //基本信息的配置，信息会在api文档上显示
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("唐镜聊天室测试的接口文档")
                .description("tj微服务相关接口的文档")
                .termsOfServiceUrl("http://localhost:8080/tj")
                .version("1.0")
                .build();
    }
}
