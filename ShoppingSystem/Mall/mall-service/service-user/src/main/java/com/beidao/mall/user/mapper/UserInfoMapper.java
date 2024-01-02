package com.beidao.mall.user.mapper;


import com.beidao.mall.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

    //3、检验用户名不能重复
    UserInfo selectByUserName(String username);


    //4、封装添加数据  调用方法添加数据库
    void save(UserInfo userInfo);
}
