package com.beidao.mall.manager.mapper;

import com.beidao.mall.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {

    //2.根据查询前一天日期进行统计数据  统计前一天交易金额
    OrderStatistics selectStatisticsByDate(String createDate);
}
