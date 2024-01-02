package com.beidao.mall.manager;

import com.beidao.mall.common.log.annotation.EnableLogAspect;
import com.beidao.mall.manager.properties.MinioProperties;
import com.beidao.mall.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.beidao.mall"}) //确保所有这个包下的内容能够被扫描到
@EnableConfigurationProperties(value = {UserProperties.class, MinioProperties.class})//确保@ConfigurationProperties能够被扫描到
@EnableScheduling //开启定时任务
@EnableLogAspect //确保扫描到切面类common-log
public class ManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ManagerApplication.class,args);
    }
}
