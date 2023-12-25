package com.beidao.mall.manager.service;

import com.beidao.mall.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService {

    //生成图片验证码
    ValidateCodeVo generateValidateCode();
}
