package com.beidao.mall.manager.utils;

import com.beidao.mall.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;


//封装树形菜单数据
public class MenuHelper {

    //递归实现封装
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList){

        //完成封装过程

        //sysMenuList所有菜单集合
        //创建List集合 用于封装最终数据
        List<SysMenu> trees = new ArrayList<>();
        
        //遍历
        for (SysMenu sysMenu: sysMenuList) {
            //找到递归操作的入口  第一层菜单  parent_id=0
            if (sysMenu.getParentId().longValue()==0){
                //根据第一层 找下层数据 使用递归完成
                //方法实现递归  第一个参数是当前第一层菜单  第二个参数是所有菜单集合
                trees.add(findChildren(sysMenu,sysMenuList));
            }

        }
        return trees;
    }

    //递归查找下层菜单
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {

        //SysMenu有属性 private List<SysMenu> children; 封装下一层数据
        //初始化
        sysMenu.setChildren(new ArrayList<>());
        
        //递归查询
        //sysMenu 的id值  和  sysMenuList中parentId相同 
        for (SysMenu sysMenu1:
             sysMenuList) {
            //判断id 和 parentId是否相同
            if (sysMenu.getId().longValue() == sysMenu1.getParentId().longValue()){
                //sysMenu1  下层数据 进行封装
                sysMenu.getChildren().add(findChildren(sysMenu1,sysMenuList));
            }
        }
        return sysMenu;
    }
}
