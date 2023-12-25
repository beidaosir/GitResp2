package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {


    //根据用户名username查询数据库表sys_user表
    SysUser selectUserInfoByUserName(String userName);


}
