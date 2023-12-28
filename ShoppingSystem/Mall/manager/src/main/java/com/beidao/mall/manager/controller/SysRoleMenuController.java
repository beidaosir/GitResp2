package com.beidao.mall.manager.controller;


import com.beidao.mall.manager.service.SysRoleMenuService;
import com.beidao.mall.model.dto.system.AssginMenuDto;
import com.beidao.mall.model.dto.system.AssginRoleDto;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/admin/system/sysRoleMenu")
public class SysRoleMenuController {


    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    //1、查询所有菜单  和 查询角色分配过菜单id列表
    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId(@PathVariable("roleId") Long roleId){
        Map<String, Object> map= sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

    //2、保存角色分配菜单数据
    @PostMapping("/doAssign")
    private Result doAssign(@RequestBody AssginMenuDto assginMenuDto){
        /*//删除角色之前分配过的菜单数据
        sysRoleMenuService.deleteByRoleId(assginMenuDto.getRoleId());

        //保存分配的数据
        List<Map<String,Number>> menuInfo = assginMenuDto.getMenuIdList();

        if (menuInfo != null && menuInfo.size()>0){
            //角色分配了菜单

        }*/

        //代码放在service中实现
        sysRoleMenuService.doAssign(assginMenuDto);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }


}
