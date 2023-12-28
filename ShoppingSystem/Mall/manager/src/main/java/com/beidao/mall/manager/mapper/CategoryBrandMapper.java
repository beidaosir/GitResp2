package com.beidao.mall.manager.mapper;

import com.beidao.mall.model.dto.product.CategoryBrandDto;
import com.beidao.mall.model.entity.product.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {

    //分类品牌条件分页查询
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);


    //添加
    void save(CategoryBrand categoryBrand);

    // 修改
    void updateById(CategoryBrand categoryBrand);


    // 删除
    void deleteById(Long id);
}
