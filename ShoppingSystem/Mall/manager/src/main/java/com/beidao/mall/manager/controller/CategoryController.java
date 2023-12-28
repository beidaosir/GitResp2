package com.beidao.mall.manager.controller;

import com.beidao.mall.manager.service.CategoryService;
import com.beidao.mall.model.entity.product.Category;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value="/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    //导入功能
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        //获取上传文件   注意命名file
        categoryService.importData(file);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }



    //导出功能
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }


    //分类列表  每次查询一层数据---懒加载
    //select * from category where parent_id = id
    @GetMapping("/findCategoryList/{id}")
    public Result findCategory(@PathVariable Long id){
        List<Category> list = categoryService.findCategoryList(id);
        return Result.build(list, ResultCodeEnum.SUCCESS);

    }





}
