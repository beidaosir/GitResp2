package com.beidao.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.beidao.mall.cart.service.CartService;
import com.beidao.mall.feign.product.ProductFeignClient;
import com.beidao.mall.model.entity.h5.CartInfo;
import com.beidao.mall.model.entity.product.ProductSku;
import com.beidao.mall.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;



    //生成key的方法
    private String getCartKey(Long userId) {
        //定义key user:cart:userId
        return "user:cart:" + userId;
    }


    //添加到购物车
    @Override
    public void addToCart(Long skuId, Integer skuNum) {

        //1、必须时登录状态  获取当前登录用户id （作为redis的hash类型的key值）
        //从threadLocal中获取用户信息
        Long userId = AuthContextUtil.getUserInfo().getId();
        //构建hash类型key名称
        String cartKey = this.getCartKey(userId);


        //2、因为购物车放在redis中存储
        //hash类型  key：userId  field：skuId  value：sku信息cartInfo
        //从redis中获取购物车数据  根据key值+skuid获取(hash类型key+field)
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));

        //3、如果商品在购物车中存在 ---把商品数量添加
        CartInfo cartInfo = null;
        if (cartInfoObj != null){
            //添加到购物车商品已经存在---数量增加
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);

            //数量相加  取出来相加 再放回去
           cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);

           //设置属性,购物车商品选中状态
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());

        }else {

            //4、如果添加的商品在购物车中不存在 ---直接把商品添加到购物车中---添加到redis里面
            //远程调用实现：通过nacos+openFeign实现 ---》根据skuid获取商品sku信息

            cartInfo = new CartInfo();

            //远程调用实现：根据skuid获取商品sku信息
            ProductSku productSku = productFeignClient.getBySkuId(skuId);
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());

        }

        //添加到redis里面
        redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId), JSON.toJSONString(cartInfo));





    }
}
