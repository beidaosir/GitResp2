package com.beidao.mall.common.exception;

import com.beidao.mall.model.vo.common.ResultCodeEnum;
import lombok.Data;

//自定义异常  ---  需要手动抛出
@Data
public class BeidaoException extends RuntimeException {

    private Integer code ;          // 错误状态码
    private String message ;        // 错误消息

    private ResultCodeEnum resultCodeEnum ;     // 封装错误状态码和错误消息

    //构造
    public BeidaoException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum ;
        this.code = resultCodeEnum.getCode() ;
        this.message = resultCodeEnum.getMessage();
    }

    public BeidaoException(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }

}