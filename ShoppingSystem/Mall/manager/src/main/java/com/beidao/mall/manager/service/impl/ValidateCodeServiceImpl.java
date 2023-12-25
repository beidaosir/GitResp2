package com.beidao.mall.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.beidao.mall.manager.service.ValidateCodeService;
import com.beidao.mall.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    //注入redis    验证码存入redis
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //生成图片验证码
    //在common中引入生成图片验证的依赖  hutool-all
    @Override
    public ValidateCodeVo generateValidateCode() {

        //1、生成图片验证码  hutool-all
        //参数：宽  高  验证码位数 干扰线数量
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 2);
        String codeValue = circleCaptcha.getCode();//生成的验证码的值
        //返回图片 图片通过base64进行了编码 图片中包含验证码信息
        String imageBase64 = circleCaptcha.getImageBase64();

        //2、把验证码存储到redis中 设置redis的key ：uuid 与 redis的value：验证码
        //设置数据过期时间
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:validate"+key,codeValue,5, TimeUnit.MINUTES);


        //3、返回ValidateCodeVo对象 封装

        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64); //设置图片格式，直接展示图片验证码

        return validateCodeVo;
    }
}
