package com.beidao.mall.manager.service.impl;

import com.beidao.mall.manager.mapper.SysRoleMapper;
import com.beidao.mall.manager.mapper.SysRoleUserMapper;
import com.beidao.mall.manager.service.SysRoleService;
import com.beidao.mall.model.dto.system.SysRoleDto;
import com.beidao.mall.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    //角色列表方法，分页查询
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {

        //设置分页相关参数  加入当前页和 每页展示记录数
        PageHelper.startPage(current,limit);

        //根据条件查询数据库 调用mapper层查询数据
        List<SysRole> list = sysRoleMapper.findByPage(sysRoleDto);

        //封装pageInfo 返回每页数据
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //2.角色添加方法
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    //3.角色修改的方法
    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    //4.角色删除的方法-逻辑删除
    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }

    //查询所有角色
    @Override
    public Map<String, Object> findAll(Long userId) {
        //1、查询所有角色
        List<SysRole> roleList = sysRoleMapper.findAll();
        
        //2、分配过的角色列表
        //根据用户id userId 查询分配过的角色id
        List<Long> roleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);


        Map<String, Object> map = new HashMap<>();
        map.put("allRolesList",roleList);
        map.put("sysUserRoles",roleIds);

        return map;
    }


}
