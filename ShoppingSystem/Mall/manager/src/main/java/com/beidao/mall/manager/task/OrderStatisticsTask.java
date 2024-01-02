package com.beidao.mall.manager.task;


import cn.hutool.core.date.DateUtil;
import com.beidao.mall.manager.mapper.OrderInfoMapper;
import com.beidao.mall.manager.mapper.OrderStatisticsMapper;
import com.beidao.mall.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component //交给spring管理
public class OrderStatisticsTask {


    //注解@Scheduled + cron 表达式
    //cron表达式设置执行规则
    //测试定时任务
    //每隔五秒  方法执行一次
    /*@Scheduled(cron = "0/5 * * * * ? ")
    public void testHello(){
        System.out.println(new Date().toInstant());

    }*/

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;


    //在每天的凌晨两点  查询前一天统计数据  把统计之后数据添加到统计结果表里面
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderTotalAmountStatistics(){

        //1.获取前一天日期
        String createDate = DateUtil.offsetDay(new Date(),-1).toString("yyyy-MM-dd");

        //2.根据查询前一天日期进行统计数据---分组操作
        //统计前一天交易金额
        OrderStatistics orderStatistics = orderInfoMapper.selectStatisticsByDate(createDate);

        //3.把统计之后的数据，添加进统计结果表
        if (orderStatistics != null){
            orderStatisticsMapper.insert(orderStatistics);
        }




       // 把统计之后数据添加到统计结果表里面


    }

}
