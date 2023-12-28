package com.beidao.mall.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.manager.properties.MinioProperties;
import com.beidao.mall.manager.service.FileUploadService;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {

        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            //创建minioClient对象
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey())
                            .build();

            // Make 'asiatrip' bucket if not exist.
            //创建bucket
            //bucket 名 go-mall
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                // 如果不存在，那么此时就创建一个新的桶
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {
                // 如果存在打印信息
                System.out.println("Bucket 'go-mall' already exists.");
            }


            //获取上传的文件名称
            //1、让每个上传文件名称唯一  uuid生成拼接
            //2、根据当前日期对上传的文件进行分组处理

            //20231225/jiah54ghi3481342photo.jpg
            String dateDir = DateUtil.format(new Date(),"yyyyMMdd");

            String uuid = UUID.randomUUID().toString().replaceAll("-","");

            String filename =dateDir +"/" + uuid + file.getOriginalFilename();


            //得到文件输入流  file.getInputStream();
            //得到文件大小  file.getSize();

            //文件上传
            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(filename)
                    .stream(file.getInputStream(),file.getSize(),-1)
                    .build());


            //获取上传文件在minio中的路径
            //http://127.0.0.1:9000/go-mall/photo.jpg
            String url = minioProperties.getEndpointUrl()+"/" +minioProperties.getBucketName()+"/"+ filename;
            return url;


        } catch (Exception e) {
            e.printStackTrace();
            throw new BeidaoException(ResultCodeEnum.SYSTEM_ERROR);
        }

    }
}
