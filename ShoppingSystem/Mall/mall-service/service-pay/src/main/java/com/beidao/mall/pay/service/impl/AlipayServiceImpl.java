package com.beidao.mall.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.model.entity.pay.PaymentInfo;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.pay.service.AlipayService;
import com.beidao.mall.pay.service.PaymentInfoService;
import com.beidao.mall.pay.utils.AlipayProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties;

    @SneakyThrows  // lombok的注解，对外声明异常
    @Override
    public String submitAlipay(String orderNo) {

        //保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 同步回调
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // 异步回调
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", paymentInfo.getOrderNo());
        map.put("product_code", "QUICK_WAP_WAY");
        map.put("total_amount", paymentInfo.getAmount());
        //map.put("total_amount",new BigDecimal("0.01"));
        map.put("subject", paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // 发送请求
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);

            if (response.isSuccess()){

                String form = response.getBody();
                return form;
            }else {
                throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
            }

        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

}
      /*  AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
        if(response.isSuccess()){
            log.info("调用成功");
            return response.getBody();
        } else {
            log.info("调用失败");
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }*/







/*
@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;


    //"支付宝下单"
    @Override
    public String submitAlipay(String orderNo) {

        //1、保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);


        //2、调用支付宝服务接口
        //构建需要参数 进行调用   //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 同步回调
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // 异步回调
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        map.put("total_amount",paymentInfo.getAmount());
        //map.put("total_amount",new BigDecimal("0.01"));
        map.put("subject",paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));


        //调用支付宝服务的接口

        // 发送请求
        AlipayTradeWapPayResponse response = null;
        try {
            response = alipayClient.pageExecute(alipayRequest);

            if(response.isSuccess()){
                log.info("调用成功");
                return response.getBody();
            } else {
                log.info("调用失败");
                throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
            }

        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

     */
/*   try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);

            if (response.isSuccess()){
                String form = response.getBody();
                return form;
            }else {
                throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }*//*


    }
}
*/
