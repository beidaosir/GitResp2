package com.beidao.mall.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.beidao.mall.feign.order.OrderFeignClient;
import com.beidao.mall.feign.product.ProductFeignClient;
import com.beidao.mall.model.dto.product.SkuSaleDto;
import com.beidao.mall.model.entity.order.OrderInfo;
import com.beidao.mall.model.entity.order.OrderItem;
import com.beidao.mall.model.entity.pay.PaymentInfo;
import com.beidao.mall.pay.mapper.PaymentInfoMapper;
import com.beidao.mall.pay.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



/*@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper ;

    @Autowired
    private OrderFeignClient orderFeignClient ;

    @Autowired
    private ProductFeignClient productFeignClient ;*/

   /* @Override
    public PaymentInfo savePaymentInfo(String orderNo) {

        // 根据订单号查询支付信息数据，如果已经已经存在了就不用进行保存(一个订单支付失败以后可以继续支付)
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);
        //判断支付记录是否存在
        if(null == paymentInfo) {
            //远程调用获取订单信息
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo).getData();
            //把orderinfo获取数据封装到paymentInfo里面
            paymentInfo = new PaymentInfo();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            String content = "";
            for(OrderItem item : orderInfo.getOrderItemList()) {
                content += item.getSkuName() + " ";
            }
            paymentInfo.setContent(content);
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);
            //添加
            paymentInfoMapper.save(paymentInfo);
        }
        return paymentInfo;
    }*/

/*
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        // 根据订单号查询支付信息数据，如果已经已经存在了就不用进行保存(一个订单支付失败以后可以继续支付)
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);

        // 判断支付记录是否存在
        if (paymentInfo == null) {
            // 远程调用获取订单信息
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo).getData();

            // 检查 orderInfo 是否为 null
            if (orderInfo == null) {
                // 添加日志
                System.out.println("Failed to retrieve order information for orderNo: " + orderNo);
                throw new RuntimeException("Failed to retrieve order information for orderNo: " + orderNo);
            }

            // 把orderinfo获取数据封装到paymentInfo里面
            paymentInfo = new PaymentInfo();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());

            // 判断订单项列表是否为 null
            if (orderInfo.getOrderItemList() != null) {
                String content = "";
                for (OrderItem item : orderInfo.getOrderItemList()) {
                    content += item.getSkuName() + " ";
                }
                paymentInfo.setContent(content);
            }

            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);

            // 判断 paymentInfoMapper 是否为 null
            if (paymentInfoMapper != null) {
                // 添加
                paymentInfoMapper.save(paymentInfo);
            } else {
                // 添加日志
                System.out.println("PaymentInfoMapper is not initialized");
                throw new RuntimeException("PaymentInfoMapper is not initialized");
            }
        }

        return paymentInfo;
    }



    @Override
    public void updatePaymentStatus(Map<String, String> map ,Integer payType) {
        // 查询PaymentInfo
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(map.get("out_trade_no"));
        if (paymentInfo.getPaymentStatus() == 1) {
            return;
        }

        //更新支付信息
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(map.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(map));
        paymentInfoMapper.updateById(paymentInfo);

        // 3、更新订单的支付状态
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo() , payType) ;

        // 4、更新商品销量
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo()).getData();
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).collect(Collectors.toList());
        System.out.println(skuSaleDtoList);

        productFeignClient.updateSkuSaleNum(skuSaleDtoList) ;
    }


}*/

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {


    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;


    //保存支付记录
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {

        //1、根据orderNo查询支付记录  支付记录是否存在
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);

        //2、支付记录不存在  远程调用：根据订单编号获取订单信息---》完成添加
        //在service-order模块创建接口：根据订单编号返回订单信息
        if (paymentInfo==null){
            //远程调用：根据订单编号获取订单信息---》完成添加
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
            //把orderInfo获取数据封装到paymentInfo里面
            paymentInfo = new PaymentInfo();

            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            String content = "";
            for(OrderItem item : orderInfo.getOrderItemList()) {
                content += item.getSkuName() + " ";
            }
            paymentInfo.setContent(content);
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);

            //添加
            paymentInfoMapper.save(paymentInfo);

        }

        return paymentInfo;
    }


    // 正常的支付成功，我们应该更新交易记录状态
    @Override
    public void updatePaymentStatus(Map<String, String> map,Integer payType) {

        // 根据订单编号查询PaymentInfo
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(map.get("out_trade_no"));

        //如果支付记录已经完成，不需要更新
        if (paymentInfo.getPaymentStatus() == 1) {
            return;
        }

        //更新支付信息
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(map.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(map));
        paymentInfoMapper.updateById(paymentInfo);

        //更新订单状态
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo(),payType);


        //更新sku
        // 4、更新商品销量
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo());
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).collect(Collectors.toList());
        productFeignClient.updateSkuSaleNum(skuSaleDtoList) ;

    }
}
