package com.beidao.mall.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//整合Knife4j(Knife4j是一种基于Swagger构建的增强工具)
@Configuration
public class Knife4jConfig {

    //在启动器中加入 @ComponentScan(basePackages = {"com.beidao.mall"}) //确保所有这个包下的内容能够被扫描到
    //保证配置生效
    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("admin-api")         // 为接口分组  分组名称
                .pathsToMatch("/admin/**")  // 接口请求路径规则  在管理员系统中都用admin开头
                .build();
    }

    /***
     * @description 自定义接口信息
     */
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                 .info(new Info()
                 .title("Go享生活API接口文档")
                 .version("1.0")
                 .description("Go享生活API接口文档")
                 .contact(new Contact().name("beidao"))); // 设定作者
    }

}