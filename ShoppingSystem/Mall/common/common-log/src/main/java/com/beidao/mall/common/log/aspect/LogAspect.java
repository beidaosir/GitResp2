package com.beidao.mall.common.log.aspect;

import com.beidao.mall.common.log.annotation.Log;
import com.beidao.mall.common.log.service.AsyncOperLogService;
import com.beidao.mall.common.log.utils.LogUtil;
import com.beidao.mall.model.entity.system.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private AsyncOperLogService asyncOperLogService ;

    // 环绕通知切面类定义
    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint , Log sysLog) {

//        String title = sysLog.title();
//        int businessType = sysLog.businessType();
//        System.out.println("title: "+title+"::businessType: "+businessType);

        //log.info("LogAspect...doAroundAdvice方法执行了"+title);
        //System.out.println("LogAspect...doAroundAdvice方法执行了"+title);


        //业务方法调用之前  封装数据
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);


        //业务方法
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();              // 执行业务方法

            //System.out.println("在业务方法之后执行...");

            //调用业务方法之后封装数据
            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,0,null);


        } catch (Throwable e) {
            // 代码执行进入到catch中，业务方法执行产生异常

            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,1,e.getMessage());

            //抛出运行时异常 ---让事务感知异常，避免事务失效    （事务切面类优先级高于自定义切面类）
            throw new RuntimeException();
        }

        //调用service中方法把日志信息添加到数据库中
        // 保存日志数据
        asyncOperLogService.saveSysOperLog(sysOperLog);

        return proceed ;                                // 返回执行结果
    }
}