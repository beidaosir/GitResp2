package com.beidao.mall.common.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//CORS解决跨域

@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
            	.allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }
    
}