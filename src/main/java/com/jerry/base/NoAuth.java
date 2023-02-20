package com.jerry.base;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 用于设置忽略鉴权API
 * @author zhangzhilin
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {
}
