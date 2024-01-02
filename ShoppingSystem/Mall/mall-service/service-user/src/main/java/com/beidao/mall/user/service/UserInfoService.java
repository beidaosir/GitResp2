package com.beidao.mall.user.service;

import com.beidao.mall.model.dto.h5.UserLoginDto;
import com.beidao.mall.model.dto.h5.UserRegisterDto;
import com.beidao.mall.model.vo.h5.UserInfoVo;

public interface UserInfoService {


    //注册
    void register(UserRegisterDto userRegisterDto);


    //用户登录
    Object login(UserLoginDto userLoginDto);


    //获取当前登录的用户信息
    UserInfoVo getCurrentUserInfo(String token);
}
