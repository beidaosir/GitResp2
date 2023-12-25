package com.beidao.mall.manager.controller;

import com.beidao.mall.manager.service.SysRoleService;
import com.beidao.mall.model.dto.system.SysRoleDto;
import com.beidao.mall.model.entity.system.SysRole;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    //4.角色删除的方法-逻辑删除
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId){
        sysRoleService.deleteById(roleId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }


    //3.角色修改的方法
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //2.角色添加方法
    @PostMapping(value = "/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //1.角色列表方法，分页查询
    //current 当前页  limit 每页显示记录数  SysRoleDto角色名称
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current") Integer current,
                             @PathVariable("limit") Integer limit,
                             @RequestBody SysRoleDto sysRoleDto){
        //pageHelper插件实现分页  返回的对象为PageInfo
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto,current,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);

    }


}
