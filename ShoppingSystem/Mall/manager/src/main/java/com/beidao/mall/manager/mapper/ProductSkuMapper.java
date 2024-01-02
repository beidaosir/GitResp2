package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper {

    // 保存商品sku数据
    void save(ProductSku productSku);

    //2、根据id查询商品sku信息列表
    List<ProductSku> findProductSkuByProductId(Long id);


    //修改product_sku
    void updateById(ProductSku productSku);

    //根据商品id删除product_sku表数据
    void deleteByProductId(Long id);
}
