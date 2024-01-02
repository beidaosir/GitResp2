package com.beidao.mall.manager.service;

import com.beidao.mall.model.entity.product.ProductSpec;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductSpecService {


    //分页查询
    PageInfo<ProductSpec> findByPage(Integer page, Integer limit);

    //添加
    void save(ProductSpec productSpec);


    //修改
    void updateById(ProductSpec productSpec);

    //删除
    void deleteById(Long id);

    //加载商品规格数据
    List<ProductSpec> findAll();
}
