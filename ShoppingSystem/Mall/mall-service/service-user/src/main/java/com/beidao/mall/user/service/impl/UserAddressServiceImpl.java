package com.beidao.mall.user.service.impl;

import com.beidao.mall.model.entity.user.UserAddress;
import com.beidao.mall.user.mapper.UserAddressMapper;
import com.beidao.mall.user.service.UserAddressService;
import com.beidao.mall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserAddressServiceImpl implements UserAddressService {


    @Autowired
    private UserAddressMapper userAddressMapper;


    //获取用户地址列表
    @Override
    public List<UserAddress> findUserAddressList() {

        //获取用户id值
        Long userId = AuthContextUtil.getUserInfo().getId();

        return userAddressMapper.findUserAddressList(userId);

    }



    //根据id获取收获地址信息
    @Override
    public UserAddress getUserAddress(Long id) {
        return userAddressMapper.getUserAddress(id);
    }
}
