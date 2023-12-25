package com.beidao.mall.common.exception;

import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class) //指定异常执行类型
    @ResponseBody //@ResponseBody 是 @RestController(返回json数据格式) 中注解
    public Result error(){
        return Result.build(null , ResultCodeEnum.SYSTEM_ERROR) ; //统一数据格式
    }

    //自定义异常处理
    @ExceptionHandler(BeidaoException.class) //指定异常执行类型
    @ResponseBody //@ResponseBody 是 @RestController(返回json数据格式) 中注解
    public Result error(BeidaoException e){
        return Result.build(null ,e.getResultCodeEnum()) ; //统一数据格式
    }


}