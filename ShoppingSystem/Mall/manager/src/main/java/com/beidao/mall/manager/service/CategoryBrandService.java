package com.beidao.mall.manager.service;

import com.beidao.mall.model.dto.product.CategoryBrandDto;
import com.beidao.mall.model.entity.product.Brand;
import com.beidao.mall.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService {

    //分类品牌条件分页查询
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);


    //添加
    void save(CategoryBrand categoryBrand);

    // 修改
    void updateById(CategoryBrand categoryBrand);


    // 删除
    void deleteById(Long id);

    //根据分类id查询对应品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);
}
