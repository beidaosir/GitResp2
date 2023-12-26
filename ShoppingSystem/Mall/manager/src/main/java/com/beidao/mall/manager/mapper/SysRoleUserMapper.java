package com.beidao.mall.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {

    //1、根据userId删除用户之前分配角色数据
    void deleteByUserId(Long userId);

    //2、重新分配资源
    void doAssign(Long userId, Long roleId);

    //根据用户id userId 查询分配过的角色id
    List<Long> selectRoleIdsByUserId(Long userId);



}
