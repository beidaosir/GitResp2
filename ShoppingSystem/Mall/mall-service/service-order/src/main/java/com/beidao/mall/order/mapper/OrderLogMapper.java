package com.beidao.mall.order.mapper;

import com.beidao.mall.model.entity.order.OrderLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogMapper {

    //6、添加数据到orderLog表
    void save(OrderLog orderLog);
}
