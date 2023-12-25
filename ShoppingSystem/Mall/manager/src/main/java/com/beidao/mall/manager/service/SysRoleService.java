package com.beidao.mall.manager.service;

import com.beidao.mall.model.dto.system.SysRoleDto;
import com.beidao.mall.model.entity.system.SysRole;
import com.github.pagehelper.PageInfo;

public interface SysRoleService {

    //角色列表方法，分页查询
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit);


    //2.角色添加方法
    void saveSysRole(SysRole sysRole);

    //3.角色修改的方法
    void updateSysRole(SysRole sysRole);

    //4.角色删除的方法-逻辑删除
    void deleteById(Long roleId);
}
