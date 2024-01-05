package com.beidao.mall.product.service;

import com.beidao.mall.model.dto.h5.ProductSkuDto;
import com.beidao.mall.model.dto.product.SkuSaleDto;
import com.beidao.mall.model.entity.product.ProductSku;
import com.beidao.mall.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    //2、根据销量排序 获取前10条记录
    List<ProductSku> selectProductSkuBySale();

    //分页查询
    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);


    //商品详情
    ProductItemVo item(String skuId);


   // 根据skuId返回sku信息
    ProductSku getBySkuId(Long skuId);


    //更新商品sku销量
    Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
