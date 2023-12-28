package com.beidao.mall.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.manager.listener.ExcelListener;
import com.beidao.mall.manager.mapper.CategoryMapper;
import com.beidao.mall.manager.service.CategoryService;
import com.beidao.mall.model.entity.product.Category;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    //分类列表  每次查询一层数据
    @Override
    public List<Category> findCategoryList(Long id) {

        // 1、根据id条件进行查询
        //select * from category where parent_id = id
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);

        //2、遍历返回list集合   为了让前端能够显示出分层结构
        // 判断每个分类是否有下一层分类  如果有---》设置hasChildren = true
        if (!CollectionUtils.isEmpty(categoryList)) {
            categoryList.forEach(category -> {
                // 判断每个分类是否有下一层分类
                //select count(*) from category where parent_id=1
                int count = categoryMapper.selectCountByParentId(category.getId());

                if (count > 0) {
                    //下一层分类
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }

            });
        }
        return categoryList;
    }


    //导出功能
    @Override
    public void exportData(HttpServletResponse response) {
        try {

            //1、设置响应头信息和其他信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");

            //设置响应头信息     Content-disposition设置文件以下载的方式打开
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");


            //2、调用mapper方法查询所有分类 返回list集合
            List<Category> categoryList = categoryMapper.findAll();

            //最终数据的list集合
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();

            //  List<Category> 转换成 List<CategoryExcelVo>
            //遍历得到每一个Category  然后放入vo中
            for (Category category:categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();

                //把category中的值获取出来  设置到categoryExcelVo

                //一个一个取出来再放进去太麻烦(get,set方法)  ---> 借助工具类BeanUtils.copyProperties实现
                BeanUtils.copyProperties(category,categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);


            }
            
            //3、调用EasyExcel的write方法完成写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }


    }

    //导入功能
    @Override
    public void importData(MultipartFile file) {

        //监听器     每次读取都创建一个新的对象 防止并发问题
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener)
                    .sheet().doRead();

        }catch (IOException e){
            e.printStackTrace();
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
