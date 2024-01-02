package com.beidao.mall.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.beidao.mall.model.entity.user.UserInfo;
import com.beidao.mall.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;


/*
拦截前端所有以api开头的接口，
只是把当前用户直接放到ThreadLocal中即可
 */
public class UserLoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 如果token不为空，那么此时验证token的合法性
        String token = request.getHeader("token");
        String userJSON = redisTemplate.opsForValue().get("user:mall"+token);

        //放到threadLocal里面
        AuthContextUtil.setUserInfo(JSON.parseObject(userJSON , UserInfo.class));
        return true ;

    }

}