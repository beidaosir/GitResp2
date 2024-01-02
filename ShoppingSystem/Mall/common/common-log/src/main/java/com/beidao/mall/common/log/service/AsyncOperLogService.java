package com.beidao.mall.common.log.service;

import com.beidao.mall.model.entity.system.SysOperLog;

public interface AsyncOperLogService {

    // 保存日志数据
   public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
