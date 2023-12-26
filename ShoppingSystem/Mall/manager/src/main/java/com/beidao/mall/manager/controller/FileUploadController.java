package com.beidao.mall.manager.controller;


import com.beidao.mall.manager.service.FileUploadService;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        //file这个名字与element-plus中保持一致  ele-p中默认叫file
        //1、得到上传文件
        //2、调用service中的方法上传，  返回minio路径
        String url = fileUploadService.upload(file);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }



}
