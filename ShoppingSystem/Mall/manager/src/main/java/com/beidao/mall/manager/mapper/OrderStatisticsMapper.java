package com.beidao.mall.manager.mapper;


import com.beidao.mall.model.dto.order.OrderStatisticsDto;
import com.beidao.mall.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatisticsMapper {

    //3.把统计之后的数据，添加进统计结果表
    void insert(OrderStatistics orderStatistics);


    //根据dto条件查询统计结果数据  返回list集合
    List<OrderStatistics> selectList(OrderStatisticsDto orderStatisticsDto);
}
