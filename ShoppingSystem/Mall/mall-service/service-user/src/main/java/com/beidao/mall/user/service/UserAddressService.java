package com.beidao.mall.user.service;

import com.beidao.mall.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {

    //获取用户地址列表
    List<UserAddress> findUserAddressList();



    //根据id获取收获地址信息
    UserAddress getUserAddress(Long id);
}
