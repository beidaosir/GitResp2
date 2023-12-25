package com.beidao.mall.utils;

import com.beidao.mall.model.entity.system.SysUser;

//创建一个AuthContextUtil类对ThreadLocal进行封装
//操作threadLocal对象
public class AuthContextUtil {

    // 创建一个ThreadLocal对象
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>() ;

    // 定义添加数据的静态方法
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    // 定义获取数据的方法
    public static SysUser get() {
        return threadLocal.get() ;
    }

    // 删除数据的方法
    public static void remove() {
        threadLocal.remove();
    }

}