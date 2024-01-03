package com.beidao.mall.order.mapper;


import com.beidao.mall.model.entity.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    //5、添加数据到orderItem表
    void save(OrderItem orderItem);


    //订单里面所有订单项
    List<OrderItem> findByOrderId(Long id);
}
