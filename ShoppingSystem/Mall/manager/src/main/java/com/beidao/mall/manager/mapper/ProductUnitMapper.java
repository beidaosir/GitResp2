package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.entity.base.ProductUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductUnitMapper {

    //加载商品计量单元数据
    List<ProductUnit> findAll();
}
