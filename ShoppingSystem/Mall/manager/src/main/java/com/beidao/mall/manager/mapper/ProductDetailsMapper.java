package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {

    // 保存商品详情数据
    void save(ProductDetails productDetails);


    //3、根据id删除商品详情数据
    ProductDetails findProductDetailsById(Long id);


    //修改product_details
    void updateById(ProductDetails productDetails);

    //根据商品id删除product_details表数据
    void deleteByProductId(Long id);
}
