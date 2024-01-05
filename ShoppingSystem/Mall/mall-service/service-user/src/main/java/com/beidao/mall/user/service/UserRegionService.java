package com.beidao.mall.user.service;

import com.beidao.mall.model.entity.base.Region;

import java.util.List;

public interface UserRegionService {

    /**
     * @Description: 收货地址省市区显示
     * @param code
     */
    List<Region> selectByParentCode(Integer code);
}
