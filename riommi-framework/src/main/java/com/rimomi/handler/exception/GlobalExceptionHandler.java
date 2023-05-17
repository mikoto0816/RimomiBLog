package com.rimomi.handler.exception;

import com.rimomi.domain.ResponseResult;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseResult systemExceptionHandler(SystemException ex){
        log.error("出现了异常--->{}",ex);
        return ResponseResult.errorResult(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler
    public ResponseResult ExceptionHandler(Exception ex){
        log.error("出现了异常--->{}",ex);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, ex.getMessage());
    }
}
