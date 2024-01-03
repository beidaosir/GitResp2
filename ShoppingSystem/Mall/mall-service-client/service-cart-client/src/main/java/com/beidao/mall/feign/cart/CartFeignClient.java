package com.beidao.mall.feign.cart;


import com.beidao.mall.model.entity.h5.CartInfo;
import com.beidao.mall.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartFeignClient {

    @GetMapping(value = "/api/order/cart/auth/getAllCkecked")
    public List<CartInfo> getAllCkecked();

    //远程调用：删除生成订单的购物车商品
    @GetMapping("/api/order/cart/auth/deleteChecked")
    public Result deleteChecked();
}
