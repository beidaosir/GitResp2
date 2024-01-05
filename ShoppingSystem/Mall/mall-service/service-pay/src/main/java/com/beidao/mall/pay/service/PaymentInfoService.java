package com.beidao.mall.pay.service;

import com.beidao.mall.model.entity.pay.PaymentInfo;

import java.util.Map;

public interface PaymentInfoService {

    //保存支付记录
    PaymentInfo savePaymentInfo(String orderNo);

    // 正常的支付成功，我们应该更新交易记录状态
    void updatePaymentStatus(Map<String, String> paramMap,Integer payType);
}
