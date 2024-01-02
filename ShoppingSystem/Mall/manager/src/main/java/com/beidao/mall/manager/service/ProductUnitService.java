package com.beidao.mall.manager.service;

import com.beidao.mall.model.entity.base.ProductUnit;

import java.util.List;

public interface ProductUnitService {

    //加载商品计量单元数据
    List<ProductUnit> findAll();
}
