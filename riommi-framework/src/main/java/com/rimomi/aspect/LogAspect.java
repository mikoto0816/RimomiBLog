package com.rimomi.aspect;

import com.alibaba.fastjson.JSON;
import com.rimomi.anotation.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
/**日志切面类
 * @author rimomi
 */
public class LogAspect {
    @Pointcut("@annotation(com.rimomi.anotation.SysLog)")
    public void pt(){
    }

    @Around("pt()")
    public Object pointLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //目标方法的调用
        Object proceed;
        try {
            beforeLog(joinPoint);
            proceed = joinPoint.proceed();
            afterLog(proceed);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }

        //返回增强后的目标方法返回值
        return proceed;
    }

    private void afterLog(Object proceed) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(proceed));
    }

    private void beforeLog(ProceedingJoinPoint joinPoint) {
        log.info("=======Start=======");
        // 打印请求 URL
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =(HttpServletRequest)requestAttributes;
        log.info("URL            : {}", request.getRequestURI());
        // 打印描述信息
        SysLog sysLog = getSyslog(joinPoint);

        log.info("BusinessName   : {}", sysLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
        // 打印出参
    }

    private SysLog getSyslog(ProceedingJoinPoint joinPoint) {
        //获取joinPoint的签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SysLog annotation = methodSignature.getMethod().getAnnotation(SysLog.class);
        return annotation;
    }


}
