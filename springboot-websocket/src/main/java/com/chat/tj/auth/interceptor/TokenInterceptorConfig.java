package com.chat.tj.auth.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tangjing
 * @date 2021/01/04 17:12
 */
@Configuration
public class TokenInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 后台
        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/api/user/**", "/file/**")
                // 添加后端不需要验证的接口
                .excludePathPatterns("/auth/token", "/api/user/register", "/api/user/login")
                // swagger地址过滤
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
