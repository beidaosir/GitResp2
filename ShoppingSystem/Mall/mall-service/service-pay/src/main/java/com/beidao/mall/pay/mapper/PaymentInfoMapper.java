package com.beidao.mall.pay.mapper;


import com.beidao.mall.model.entity.pay.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInfoMapper {

    //1、根据orderNo查询支付记录  支付记录是否存在
    PaymentInfo getByOrderNo(String orderNo);


    //添加
    void save(PaymentInfo paymentInfo);


    //更新支付信息
    void updateById(PaymentInfo paymentInfo);
}
