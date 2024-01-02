package com.beidao.mall.product.mapper;


import com.beidao.mall.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {


    //获取productId  获取商品信息
    Product getById(Long productId);
}
