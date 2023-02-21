package com.jerry.base;

import java.util.HashSet;
import java.util.Set;

/**
 * 常量类
 * @author zhangzhilin
 */
public final class Contact {
    /**忽略鉴权方法**/
    public static Set<String> SET=new HashSet<>();

    /**验证码类型：注册,重置密码**/
    public static final String[] TYPE={"REGISTER","RESET"};


}
