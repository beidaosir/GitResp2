package com.beidao.mall.cart.service;

public interface CartService {


    //添加到购物车
    void addToCart(Long skuId, Integer skuNum);
}
