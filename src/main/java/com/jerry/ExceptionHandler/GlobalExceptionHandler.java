package com.jerry.ExceptionHandler;

import com.jerry.base.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author zhangzhilin
 */
@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public Result<?> unAuthor(){
        return new Result<String>().error("接口无权限");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> methodNotSupported(){
        return new Result<>().error(405,"接口Method异常，请检查类型");
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> messageNotRead(){
        return new Result<>().error(400,"消息体数据异常，请检查格式");
    }
}

