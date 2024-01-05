package com.beidao.mall.user.service;

import com.beidao.mall.model.dto.h5.UserLoginDto;
import com.beidao.mall.model.dto.h5.UserRegisterDto;
import com.beidao.mall.model.entity.user.UserBrowseHistory;
import com.beidao.mall.model.entity.user.UserCollect;
import com.beidao.mall.model.vo.h5.UserInfoVo;
import com.github.pagehelper.PageInfo;

public interface UserInfoService {


    //注册
    void register(UserRegisterDto userRegisterDto);


    //用户登录
    Object login(UserLoginDto userLoginDto);


    //获取当前登录的用户信息
    UserInfoVo getCurrentUserInfo(String token);



    void saveUserCollect(Long id);

    //浏览商品分页展示
    PageInfo<UserCollect> findUserBrowseHistoryPage(Integer page, Integer limit);

    //收藏商品分页展示
    PageInfo<UserBrowseHistory> findUserCollectPage(Integer page, Integer limit);

    //取消收藏
    void updatecancelCollect(Long skuId);

    //商品收藏
    void savecollect(Long skuId);

    //查看用户是否收藏
    Boolean findUserisCollect(Long skuId);

    //    远程调用：获取浏览量最多的商品
    UserBrowseHistory getMostFrequentSkuId();
}
