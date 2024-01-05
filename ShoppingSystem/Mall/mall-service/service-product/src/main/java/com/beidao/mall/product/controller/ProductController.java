package com.beidao.mall.product.controller;


import com.beidao.mall.feign.user.UserFeignClient;
import com.beidao.mall.model.dto.h5.ProductSkuDto;
import com.beidao.mall.model.dto.product.SkuSaleDto;
import com.beidao.mall.model.entity.product.ProductSku;
import com.beidao.mall.model.entity.user.UserBrowseHistory;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.h5.ProductItemVo;
import com.beidao.mall.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProductController {

    @Autowired
    private ProductService productService;


    @Autowired
    private UserFeignClient userFeignClient;

    //商品详情
    @Operation(summary = "商品详情")
    @GetMapping("item/{skuId}")
    public Result item(@PathVariable String skuId){

        ProductItemVo productItemVo;
        if (!"undefined".equals(skuId)) {
            // 如果没有传入有效的skuId，可以返回收藏最多的商品信息
            //远程调用查询收藏最多的商品
            productItemVo = productService.item(skuId);
        }else {
            //远程调用获取浏览量最多的商品
            UserBrowseHistory browseHistory = userFeignClient.getByBrowseHistory();
            Long id = browseHistory.getSkuId();
            String skuidString = String.valueOf(id);
            productItemVo = productService.item(skuidString);
        }

        return Result.build(productItemVo , ResultCodeEnum.SUCCESS);
    }



    @Operation(summary = "分页查询")
    @GetMapping(value = "/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit,
                       ProductSkuDto productSkuDto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(page, limit, productSkuDto);
        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
    }



    //远程调用：根据skuId返回sku信息
    @GetMapping("/getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable Long skuId){
        ProductSku productSku = productService.getBySkuId(skuId);
        return productSku;
    }

    @Operation(summary = "更新商品sku销量")
    @PostMapping("updateSkuSaleNum")
    public Boolean updateSkuSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList) {
        return productService.updateSkuSaleNum(skuSaleDtoList);
    }


}
