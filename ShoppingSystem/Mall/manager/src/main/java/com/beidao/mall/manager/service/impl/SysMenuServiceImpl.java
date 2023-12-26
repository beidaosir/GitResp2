package com.beidao.mall.manager.service.impl;

import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.manager.mapper.SysMenuMapper;
import com.beidao.mall.manager.service.SysMenuService;
import com.beidao.mall.manager.utils.MenuHelper;
import com.beidao.mall.model.entity.system.SysMenu;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    //菜单列表
    @Override
    public List<SysMenu> findNodes() {

        // 1、查询所有菜单 返回list集合
        List<SysMenu> sysMenuList = sysMenuMapper.findAll();

        if (CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }

        //2、调用工具类的方法，把返回list集合封装要求数据格式
        List<SysMenu> treeList = MenuHelper.buildTree(sysMenuList);


        return treeList;
    }

    //菜单添加
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);
    }

    //菜单修改
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    //菜单删除
    @Override
    public void removeById(Long id) {
        //根据当前菜单id查询是否有子菜单
        int count = sysMenuMapper.selectCountById(id);

        //count>0 有子菜单
        if (count>0){
            throw new BeidaoException(ResultCodeEnum.NODE_ERROR);
        }

        //count=0 直接删除
        sysMenuMapper.delete(id);
    }
}
