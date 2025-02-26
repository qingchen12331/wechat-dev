package com.qingchen.api.aspect;

import com.alibaba.nacos.shaded.com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    @Around("execution(* com.qingchen.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopwatch=new StopWatch();
        String pointName=joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName();
        stopwatch.start(pointName+":业务执行开始");
        //需要统计每一个service实现的执行时间,如果执行时间太久,则进行error级别的日志输出
//        long begin =System.currentTimeMillis();
        Object proceed=joinPoint.proceed();
        stopwatch.stop();
        log.info(stopwatch.prettyPrint());
        log.info(stopwatch.shortSummary());
        log.info("任务总数"+stopwatch.getTaskCount());
        log.info("执行总时间: "+stopwatch.getTotalTimeMillis());


//        long end=System.currentTimeMillis();
        long takeTimes=stopwatch.getTotalTimeMillis();
        if(takeTimes>3000){
            log.error("执行位置{},耗费了{}毫秒",pointName,takeTimes);
        }else if(takeTimes>2000){
            log.warn("执行位置{},执行时间稍微有点长,耗费了{}毫秒",pointName,takeTimes);
        }else{
            log.info("执行位置{},执行时间正常,耗费了{}毫秒",pointName,takeTimes);
        }

        return proceed;
    }
}
