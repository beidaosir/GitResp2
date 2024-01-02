package com.beidao.mall.manager.service;

import com.beidao.mall.model.dto.order.OrderStatisticsDto;
import com.beidao.mall.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {

    //返回统计数据
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
