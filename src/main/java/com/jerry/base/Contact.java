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

    /** 直接交换队列**/
    public static final String DIRECT_QUERY="DIRECT";

    /** 全局交换**/
    public static final String FANOUT_QUERY="FANOUT";

    /** 键匹配队列**/
    public static final String TOPIC_QUERY_ALL="TOPIC_ALL";
    public static final String TOPIC_QUERY_ONE="TOPIC_ONE";

    /** 直接交换队列键**/
    public static final String DIRECT_QUERY_KEY="DIRECT_KET";


    /** 键匹配队列**/
    public static final String TOPIC_QUERY_ALL_KEY="TOPIC_KEY.#";

    /** 键匹配队列**/
    public static final String TOPIC_QUERY_ONE_KEY="TOPIC_KEY.*";
}
