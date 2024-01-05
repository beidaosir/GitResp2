package com.beidao.mall.user.mapper;

import com.beidao.mall.model.entity.base.Region;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRegionMapper {

    List<Region> selectByParentCode(Integer code);

    String selectByCode(String Code);
}