package com.beidao.mall.manager.service.impl;

import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.manager.mapper.SysMenuMapper;
import com.beidao.mall.manager.mapper.SysRoleMenuMapper;
import com.beidao.mall.manager.service.SysMenuService;
import com.beidao.mall.manager.utils.MenuHelper;
import com.beidao.mall.model.entity.system.SysMenu;
import com.beidao.mall.model.entity.system.SysUser;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.system.SysMenuVo;
import com.beidao.mall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

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

        //当新添加一个子菜单  把父菜单isHalf改成半开状态
        updateSysRoleMenu(sysMenu);

    }

    //当新添加一个子菜单  把父菜单isHalf改成半开状态
    private void updateSysRoleMenu(SysMenu sysMenu) {

        //获取当前添加菜单的父菜单
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        //查询是否有父节点
        if (parentMenu != null){

            // 将该id的菜单设置为半开
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId()) ;

            // 递归调用
            updateSysRoleMenu(parentMenu) ;
        }

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


    //查询用户可以操作的菜单
    @Override
    public List<SysMenuVo> findMenusByUserId() {
        //获取当前登录用户id
        SysUser sysUser = AuthContextUtil.get();//AuthContextUtil类对ThreadLocal进行封装,从中取到当前登录用户id
        Long userId = sysUser.getId();

        //根据userId查询可以操作的菜单
        //封装要求数据格式，返回
        List<SysMenu> sysMenuList = MenuHelper.buildTree(sysMenuMapper.findMenusByUserId(userId));

        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenuList);

        return sysMenuVos;
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
