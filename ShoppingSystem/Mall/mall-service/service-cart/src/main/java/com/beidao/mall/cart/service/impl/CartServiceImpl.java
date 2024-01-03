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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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


    //购物车列表
    @Override
    public List<CartInfo> getCartList() {

        //1、构建查询的redis里面key值  根据当前userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //2、根据key从redis里面获取hash类型获取所有value值 cartInfo
        List<Object> valueList = redisTemplate.opsForHash().values(cartKey);

        //List<Object>   ---->   List<CartInfo>
        if (!CollectionUtils.isEmpty(valueList)){

            List<CartInfo> cartInfoList = valueList.stream().map(cartInfoObj->
                    JSON.parseObject(cartInfoObj.toString(),CartInfo.class))
                    .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                    .collect(Collectors.toList());

            return cartInfoList;
        }

        //返回空集合
        return new ArrayList<>();
    }


    //删除购物车商品
    @Override
    public void deleteCart(Long skuId) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);


        redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));


    }


    //更新购物车商品选中状态
    @Override
    public void checkCart(Long skuId, Integer isChecked) {

        //1、构建查询的redis里面key值  根据当前userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //2、判断key是否包含field
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey,String.valueOf(skuId));
        if (hasKey) {

            //3、根据key+field把value获取出来
            String cartInfoString =
                    redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)).toString();

            //4、更新value里面的选中状态
            CartInfo cartInfo = JSON.parseObject(cartInfoString, CartInfo.class);
            cartInfo.setIsChecked(isChecked);

            //5、放回到redis的hash类型里面
            redisTemplate.opsForHash().put(cartKey,
                    String.valueOf(skuId),
                    JSON.toJSONString(cartInfo));

        }
    }


    //更新购物车商品全部选中状态
    @Override
    public void allCheckCart(Integer isChecked) {
        //1、构建查询的redis里面key值  根据当前userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //2、根据key获取购物车中所有value值
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        if (!CollectionUtils.isEmpty(objectList)){

            List<CartInfo> cartInfoList = objectList.stream().map(object->
                            JSON.parseObject(object.toString(),CartInfo.class))
                    .collect(Collectors.toList());

            //3、把每个商品的isChecked值进行更新
            cartInfoList.forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                redisTemplate.opsForHash().put(cartKey,String.valueOf(cartInfo.getSkuId()),
                       JSON.toJSONString(cartInfo));
            });

        }

    }



    //清空购物车
    @Override
    public void clearCart() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        //delete
        redisTemplate.delete(cartKey);
    }


    //选中的购物车
    @Override
    public List<CartInfo> getAllCkecked() {
        //获取userId  构建 key
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //根据key获取购物车所有商品
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);


        if (!CollectionUtils.isEmpty(objectList)) {

            List<CartInfo> cartInfoList = objectList.stream().map(object ->
                            JSON.parseObject(object.toString(), CartInfo.class))
                    .filter(cartInfo -> cartInfo.getIsChecked()==1)//选中
                    .collect(Collectors.toList());

            return cartInfoList;
        }

            return new ArrayList<>();
    }



    //远程调用：删除生成订单的购物车商品
    @Override
    public void deleteChecked() {

        //获取userId  构建 key
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //根据key获取redis所有value值
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        //删除选中商品
        objectList.stream().map(object ->
                        JSON.parseObject(object.toString(), CartInfo.class))
                .filter(cartInfo -> cartInfo.getIsChecked()==1)//选中
                .forEach(cartInfo -> redisTemplate.opsForHash().delete(cartKey,
                        String.valueOf(cartInfo.getSkuId())));

    }
}
