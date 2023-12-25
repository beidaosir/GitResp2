package com.beidao.mall.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

//读取配置文件 application-dev.yml 信息
@Data
@ConfigurationProperties(prefix = "mall.auth")
public class UserProperties {

    private List<String> noAuthUrls;
}
