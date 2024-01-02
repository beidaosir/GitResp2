package com.beidao.mall.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.beidao.mall.manager.mapper.OrderStatisticsMapper;
import com.beidao.mall.manager.service.OrderInfoService;
import com.beidao.mall.model.dto.order.OrderStatisticsDto;
import com.beidao.mall.model.entity.order.OrderStatistics;
import com.beidao.mall.model.vo.order.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;


    //返回统计数据
    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {

        //根据dto条件查询统计结果数据  返回list集合
        List<OrderStatistics> orderStatisticsList =
                orderStatisticsMapper.selectList(orderStatisticsDto) ;

        //遍历list集合 得到所有日期  把所有日期封装list集合里面
        //把日期变成字符串 封装到list中

        List<String> dateList = orderStatisticsList.stream()
                .map(orderStatistics ->
                        DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd"))
                .collect(Collectors.toList());

        //遍历list集合 得到所有日期对应总金额  把所有日期对应总金额封装list集合里面
        List<BigDecimal> decimalList = orderStatisticsList.stream()
                .map(OrderStatistics::getTotalAmount)
                .collect(Collectors.toList());

        //把两个list集合封装到vo中 返回vo
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo() ;
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(decimalList);

        return orderStatisticsVo;
    }
}
