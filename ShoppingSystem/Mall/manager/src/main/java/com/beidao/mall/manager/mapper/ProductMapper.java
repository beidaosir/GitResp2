package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.dto.product.ProductDto;
import com.beidao.mall.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    //条件分页查询
    List<Product> findByPage(ProductDto productDto);


    // 保存商品数据
    void save(Product product);


    //1、根据id查询商品基本信息 product
    Product findProductById(Long id);

    //修改product
    void updateById(Product product);


    //根据商品id删除product表数据
    void deleteById(Long id);
}
