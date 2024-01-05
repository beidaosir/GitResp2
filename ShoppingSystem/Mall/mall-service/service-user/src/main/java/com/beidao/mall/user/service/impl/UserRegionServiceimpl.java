package com.beidao.mall.user.service.impl;

import com.beidao.mall.model.entity.base.Region;
import com.beidao.mall.user.mapper.UserRegionMapper;
import com.beidao.mall.user.service.UserRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRegionServiceimpl implements UserRegionService {

    @Autowired
    private UserRegionMapper userRegionMapper;
    @Override
    public List<Region> selectByParentCode(Integer code) {
        List<Region> regions = userRegionMapper.selectByParentCode(code);
        return regions;
    }

}