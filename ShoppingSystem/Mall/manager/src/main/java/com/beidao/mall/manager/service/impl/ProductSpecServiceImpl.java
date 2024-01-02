package com.beidao.mall.manager.service.impl;

import com.beidao.mall.manager.mapper.ProductSpecMapper;
import com.beidao.mall.manager.service.ProductSpecService;
import com.beidao.mall.model.entity.product.ProductSpec;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecServiceImpl implements ProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;


    //分页查询
    @Override
    public PageInfo<ProductSpec> findByPage(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ProductSpec> list = productSpecMapper.findByPage();
        return new PageInfo<>(list);

    }

    //添加
    @Override
    public void save(ProductSpec productSpec) {
        productSpecMapper.save(productSpec) ;
    }

    //修改
    @Override
    public void updateById(ProductSpec productSpec) {
        productSpecMapper.update(productSpec) ;
    }


    //删除
    @Override
    public void deleteById(Long id) {
        productSpecMapper.delete(id) ;
    }


    //加载商品规格数据
    @Override
    public List<ProductSpec> findAll() {
        return productSpecMapper.findAll();
    }
}
