package com.beidao.mall.user.service.impl;

import com.beidao.mall.model.entity.user.UserAddress;
import com.beidao.mall.user.mapper.UserAddressMapper;
import com.beidao.mall.user.mapper.UserRegionMapper;
import com.beidao.mall.user.service.UserAddressService;
import com.beidao.mall.user.service.UserRegionService;
import com.beidao.mall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserAddressServiceImpl implements UserAddressService {


    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserRegionMapper userRegionMapper;


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




    //用户收货地址修改
    @Override
    public void updateByid(UserAddress userAddress) {
        String provinceCode = userRegionMapper.selectByCode(userAddress.getProvinceCode());
        String cityCode = userRegionMapper.selectByCode(userAddress.getCityCode());
        String districtCode = userRegionMapper.selectByCode(userAddress.getDistrictCode());

        StringBuilder fullAddressBuilder = new StringBuilder();
        fullAddressBuilder.append(provinceCode)
                .append(cityCode)
                .append(districtCode);

        String fullAddress = fullAddressBuilder.toString();
        userAddress.setFullAddress(fullAddress);
        userAddressMapper.updateByid(userAddress);
    }

    //用户收货地址新增
    @Override
    public void save(UserAddress userAddress) {
        //获取用户userID
        Long userId = AuthContextUtil.getUserInfo().getId();
        userAddress.setUserId(userId);
        //拼接全部地址
        String provinceCode = userRegionMapper.selectByCode(userAddress.getProvinceCode());
        String cityCode = userRegionMapper.selectByCode(userAddress.getCityCode());
        String districtCode = userRegionMapper.selectByCode(userAddress.getDistrictCode());

        StringBuilder fullAddressBuilder = new StringBuilder();
        fullAddressBuilder.append(provinceCode)
                .append(cityCode)
                .append(districtCode);

        String fullAddress = fullAddressBuilder.toString();
        userAddress.setFullAddress(fullAddress);
        userAddressMapper.save(userAddress);
    }

    //用户收货地址刪除
    @Override
    public void removeById(Long id) {
        userAddressMapper.removeById(id);
    }

}
