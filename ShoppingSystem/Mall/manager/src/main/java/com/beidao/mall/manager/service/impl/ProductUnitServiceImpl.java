package com.beidao.mall.manager.service.impl;

import com.beidao.mall.manager.mapper.ProductUnitMapper;
import com.beidao.mall.manager.service.ProductUnitService;
import com.beidao.mall.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper;

    //加载商品计量单元数据
    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll() ;
    }
}
