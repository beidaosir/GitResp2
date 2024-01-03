package com.beidao.mall.user.controller;


import com.beidao.mall.model.entity.user.UserAddress;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户地址接口")
@RestController
@RequestMapping(value="/api/user/userAddress")
public class UserAddressController {


    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "获取用户地址列表")
    @GetMapping("auth/findUserAddressList")
    public Result findUserAddressList() {
        List<UserAddress> list = userAddressService.findUserAddressList();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }

    //根据id获取收获地址信息
    @GetMapping("getUserAddress/{id}")
    public UserAddress getUserAddress(@PathVariable Long id){
        return userAddressService.getUserAddress(id);
    }

}
