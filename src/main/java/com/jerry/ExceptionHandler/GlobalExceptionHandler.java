package com.jerry.ExceptionHandler;

import com.jerry.base.Result;
import org.apache.shiro.authc.AuthenticationException;
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
    public Result<?> Unauthor(){
        return new Result<String>().error("接口无权限");
    }
}
