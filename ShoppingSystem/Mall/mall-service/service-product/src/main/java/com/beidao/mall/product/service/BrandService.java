package com.beidao.mall.product.service;

import com.beidao.mall.model.entity.product.Brand;

import java.util.List;

public interface BrandService {

    //获取全部品牌
    List<Brand> findAll();
}
