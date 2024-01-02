package com.beidao.mall.manager.mapper;

import com.beidao.mall.model.entity.product.ProductSpec;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSpecMapper {

    //分页查询
    List<ProductSpec> findByPage();

    //添加
    void save(ProductSpec productSpec);

    //修改
    void update(ProductSpec productSpec);

    //删除
    void delete(Long id);

    //加载商品规格数据
    List<ProductSpec> findAll();
}
