package com.beidao.mall.manager.service.impl;

import com.beidao.mall.common.log.service.AsyncOperLogService;
import com.beidao.mall.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.beidao.mall.manager.mapper.SysOperLogMapper;


@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {


    @Autowired
    private SysOperLogMapper sysOperLogMapper;


    // 保存日志数据
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}
