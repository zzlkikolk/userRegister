package com.jerry.base;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 用于设置消息
 * @author zhangzhilin
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitMessage {

    /** 路由键**/
    String routerKey();


    /** 交换机类型**/
    Class<?>  exchangeType();

    String queryName() default "query";

}
