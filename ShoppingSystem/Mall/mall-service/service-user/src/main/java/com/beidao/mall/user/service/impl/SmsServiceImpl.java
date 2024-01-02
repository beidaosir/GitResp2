package com.beidao.mall.user.service.impl;

import com.beidao.mall.user.service.SmsService;
import com.beidao.mall.utils.HttpUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    //发送短信验证码
    @Override
    public void sendCode(String phone) {

        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasText(code)){

            return;
        }

        //1、生成验证码
        code = RandomStringUtils.randomNumeric(4);

        //2、把生成的验证码放到redis里面  设置过期时间
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        //3、向手机号发送短信验证码
        sendMessage(phone,code);


    }


    //发送短信验证码的方法
    private void sendMessage(String phone, String code) {
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";

        //AppCode
        String appcode = "2f86de54dcc5461cacbfc9dac3330b0b";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();


        bodys.put("content", "code:"+code);
        bodys.put("template_id", "CST_ptdie100");  //该模板为调试接口专用，短信下发有受限制，调试成功后请联系客服报备专属模板
        bodys.put("phone_number", phone);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //以精品服务打造精品API产品。
        //vip优惠券，技术支持。请直接联系客服（vx同号）  15622205140。
        //需要赠送身份证OCR识别，vip优惠券,技术支持。请直接联系客服。
        //商品说明可以在商品介绍里查看

    }

/**
 *重要提示：
 *如您的返回结果中，没有我们接口的返回报文，或者连header的信息都打印出来了。可能是您的代码环境未能适配该请求示例。
 *那么，以下两个命令行，您可以二选一，选择一个适合你环境的加入到请求示例中。即可打印我们接口的返回报文。
 *或者直接联系客服  VX 15622205140
 *
 *System.out.println(EntityUtils.toString(response.getEntity()));
 *
 *System.out.println(response.body().string());
 */

}
