package com.beidao.mall.manager.service;

import com.beidao.mall.model.entity.system.SysMenu;

import java.util.List;

public interface SysMenuService {

    //菜单列表
    List<SysMenu> findNodes();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void update(SysMenu sysMenu);

    //菜单删除
    void removeById(Long id);
}
