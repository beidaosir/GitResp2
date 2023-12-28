package com.beidao.mall.manager.service;

import com.beidao.mall.model.dto.system.AssginMenuDto;

import java.util.Map;

public interface SysRoleMenuService {

    //1、查询所有菜单  和 查询角色分配过菜单id列表
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);


    //为角色分配菜单
    void doAssign(AssginMenuDto assginMenuDto);
}
