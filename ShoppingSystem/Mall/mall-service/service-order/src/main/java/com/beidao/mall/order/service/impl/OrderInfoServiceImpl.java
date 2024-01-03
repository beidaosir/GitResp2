package com.beidao.mall.order.service.impl;

import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.feign.cart.CartFeignClient;
import com.beidao.mall.feign.product.ProductFeignClient;
import com.beidao.mall.feign.user.UserFeignClient;
import com.beidao.mall.model.dto.h5.OrderInfoDto;
import com.beidao.mall.model.entity.h5.CartInfo;
import com.beidao.mall.model.entity.order.OrderInfo;
import com.beidao.mall.model.entity.order.OrderItem;
import com.beidao.mall.model.entity.order.OrderLog;
import com.beidao.mall.model.entity.product.ProductSku;
import com.beidao.mall.model.entity.user.UserAddress;
import com.beidao.mall.model.entity.user.UserInfo;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.h5.TradeVo;
import com.beidao.mall.order.mapper.OrderInfoMapper;
import com.beidao.mall.order.mapper.OrderItemMapper;
import com.beidao.mall.order.mapper.OrderLogMapper;
import com.beidao.mall.order.service.OrderInfoService;
import com.beidao.mall.utils.AuthContextUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {


    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;



    //确认下单
    @Override
    public TradeVo getTrade() {

        //远程调用获取购物车选中商品列表
        List<CartInfo> cartInfoList = cartFeignClient.getAllCkecked();

        //创建list集合用于封装订单项
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo  cartInfo: cartInfoList){
            OrderItem orderItem = new OrderItem();

            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }

        //获取订单支付的总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }


        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);
        return tradeVo;
    }


    //生成订单
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        //1、orderInfoDto 获取所有订单项list集合 List<OrderItem>
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();

        //2、判断为空  List<OrderItem> 抛出异常
        if (CollectionUtils.isEmpty(orderItemList)){
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }

        //3、校验商品库存是否充足
        //遍历 List<OrderItem>集合 得到每个order的OrderItem
        for (OrderItem orderItem : orderItemList) {
            //校验每个orderItem库存量是否充足

            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if (productSku == null) {
                throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
            }

            //远程调用获取商品sku信息-包含库存量
            if (orderItem.getSkuNum().intValue() > productSku.getStockNum().intValue()) {
                throw new BeidaoException(ResultCodeEnum.STOCK_LESS);
            }

        }

        //4、添加数据到orderInfo表
        //封装orderInfo对象
        //远程调用：获取用户收获地址
        OrderInfo orderInfo = new OrderInfo();
        UserInfo userInfo = AuthContextUtil.getUserInfo();

        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());

        //封装用户收货地址信息
        Long userAddressId = orderInfoDto.getUserAddressId();
        // 远程调用：根据收获地址id  获取用户收获地址
        //UserAddress userAddress = null;
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        orderInfoMapper.save(orderInfo);
        //5、添加数据到orderItem表
        //添加List<OrderItem>表里面数据  把集合每个OrderItem添加表
        for (OrderItem orderItem:orderItemList){
            //设置对应订单id
            orderItem.setOrderId(orderInfo.getId());

            orderItemMapper.save(orderItem);
        }

        //6、添加数据到orderLog表
        //记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);


        // 7、把生成订单商品从购物车删除---远程调用
        cartFeignClient.deleteChecked();

        //8、返回订单id
        return orderInfo.getId();
    }


    //获取订单信息
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }


    //立即购买
    @Override
    public TradeVo buy(Long skuId) {
        //封装订单项集合
        List<OrderItem> orderItemList = new ArrayList<>();
        ProductSku productSku = productFeignClient.getBySkuId(skuId);

        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());

        orderItemList.add(orderItem);

        TradeVo tradeVo = new TradeVo();

        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(productSku.getSalePrice());

        return tradeVo;
    }


    //获取订单分页列表
    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {

        PageHelper.startPage(page, limit);

        //查询订单信息
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);


        //订单里面所有订单项
        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItem);
        });

        return new PageInfo<>(orderInfoList);
    }
}
