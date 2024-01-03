package com.beidao.mall.order.service;

import com.beidao.mall.model.dto.h5.OrderInfoDto;
import com.beidao.mall.model.entity.order.OrderInfo;
import com.beidao.mall.model.vo.h5.TradeVo;
import com.github.pagehelper.PageInfo;

public interface OrderInfoService {


    //确认下单
    TradeVo getTrade();


    //生成订单
    Long submitOrder(OrderInfoDto orderInfoDto);


    //获取订单信息
    OrderInfo getOrderInfo(Long orderId);


    //立即购买
    TradeVo buy(Long skuId);


    //    获取订单分页列表
    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);
}
