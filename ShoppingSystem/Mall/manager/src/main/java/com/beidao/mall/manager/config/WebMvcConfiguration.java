package com.beidao.mall.manager.config;

import com.beidao.mall.manager.interceptor.LoginAuthInterceptor;
import com.beidao.mall.manager.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor;

    @Autowired
    private UserProperties userProperties;


   //拦截器注册
    @Override
    public void addInterceptors(InterceptorRegistry registry){

        //登录和验证码不需要拦截
        registry.addInterceptor(loginAuthInterceptor)
                /*.excludePathPatterns("/admin/system/index/login" ,
                        "/admin/system/index/generateValidateCode")  优化之前*/
                .excludePathPatterns(userProperties.getNoAuthUrls())
                .addPathPatterns("/**");
    }

    //CORS解决跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
            	.allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }
    
}