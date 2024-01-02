package com.beidao.mall.manager.service.impl;

import com.beidao.mall.manager.mapper.ProductDetailsMapper;
import com.beidao.mall.manager.mapper.ProductMapper;
import com.beidao.mall.manager.mapper.ProductSkuMapper;
import com.beidao.mall.manager.service.ProductService;
import com.beidao.mall.model.dto.product.ProductDto;
import com.beidao.mall.model.entity.product.Product;
import com.beidao.mall.model.entity.product.ProductDetails;
import com.beidao.mall.model.entity.product.ProductSku;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;


    //条件分页查询
    @Override
    public PageInfo<Product> findByPage(Integer page,
                                        Integer limit,
                                        ProductDto productDto) {

        PageHelper.startPage(page, limit);
        List<Product> list = productMapper.findByPage(productDto);
        return new PageInfo<>(list);

    }


    //添加
    @Override
    public void save(Product product) {
        //1、保存商品基本信息  到product表
        // 保存商品数据
        product.setStatus(0);              // 设置上架状态为0
        product.setAuditStatus(0);         // 设置审核状态为0
        productMapper.save(product);


        //2、获取商品sku列表集合  保存sku信息 product_sku表
        // 保存商品sku数据
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (int i = 0; i < productSkuList.size(); i++) {

            // 获取ProductSku对象
            ProductSku productSku = productSkuList.get(i);

            // 构建skuCode
            productSku.setSkuCode(product.getId() + "_" + i);
            // 设置商品id
            productSku.setProductId(product.getId());
            //设置skuName
            productSku.setSkuName(product.getName() + productSku.getSkuSpec());
            // 设置销量
            productSku.setSaleNum(0);
            //设置状态
            productSku.setStatus(0);
            // 保存数据
            productSkuMapper.save(productSku);

        }


        //3、保存商品详情数据  product_details表
        // 保存商品详情数据
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);

    }

    //根据商品id查询商品信息
    @Override
    public Product getById(Long id) {

        //1、根据id查询商品基本信息 product
        Product product = productMapper.findProductById(id);

        //2、根据id查询商品sku信息列表  product_sku
        List<ProductSku> productSkuList = productSkuMapper.findProductSkuByProductId(id);
        //封装到product中
        product.setProductSkuList(productSkuList);


        //3、根据id删除商品详情数据  product_details
        ProductDetails productDetails = productDetailsMapper.findProductDetailsById(id);
        String imageUrls = productDetails.getImageUrls();
        //封装到product中
        product.setDetailsImageUrls(imageUrls);

        return product;
    }


    //保存修改的数据
    @Override
    public void update(Product product) {

        //修改product
        productMapper.updateById(product);

        //修改product_sku
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuList.forEach(productSku -> {
            productSkuMapper.updateById(productSku);
        });

        //修改product_details
        String detailsImageUrls = product.getDetailsImageUrls();
        ProductDetails productDetails = productDetailsMapper.findProductDetailsById(product.getId());
        productDetails.setImageUrls(detailsImageUrls);
        productDetailsMapper.updateById(productDetails);
    }


    //删除
    @Override
    public void deleteById(Long id) {
        //根据商品id删除product表数据
        productMapper.deleteById(id);

        //根据商品id删除product_sku表数据
        productSkuMapper.deleteByProductId(id);

        //根据商品id删除product_details表数据
        productDetailsMapper.deleteByProductId(id);

    }

    //商品审核
    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        //设置商品id
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);
    }


    //商品上下架
    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }


}
